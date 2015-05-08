package de.iss.mv2.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import de.iss.mv2.messaging.EncryptedMessage;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageParser;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.processors.EncryptedMessagePreProcessor;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.security.SymetricKeyGenerator;

/**
 * A thread to perform the communication with a client.
 * @author Marcel Singer
 *
 */
public class ClientThread extends Thread implements CommunicationPartner{

	/**
	 * Holds the connection to the client.
	 */
	private final Socket connection;
	/**
	 * Indicates if the communication is about to cancel.
	 */
	private boolean isCanceled = false;
	/**
	 * Holds the parser to be used for incoming messages.
	 */
	private final MessageParser parser;
	/**
	 * Holds a list with all registered listeners.
	 */
	private final List<ClientListener> listeners = new ArrayList<ClientListener>();
	/**
	 * Holds a list with all registered {@link MessagePreProcessor}s.
	 */
	private final List<MessagePreProcessor> preProcessors = new ArrayList<MessagePreProcessor>();
	
	/**
	 * Creates a new instance of {@link ClientThread}.
	 * @param connection The connection to the client.
	 * @param settings The encryption settings to be used.
	 * @param key The private key of this instance.
	 * @throws IOException if an I/O error occurs.
	 */
	public ClientThread(Socket connection, MessageCryptorSettings settings,  KeyPair key) throws IOException {
		this.connection = connection;
		this.connection.setSoTimeout(1000);
		this.parser = new MessageParser(connection.getInputStream());
		parser.setEncryptionSetting(settings);
		parser.setKey(key);
		register(new EncryptedMessagePreProcessor(this));
	}
	
	/**
	 * Registers a {@link MessagePreProcessor}.
	 * @param p The {@link MessagePreProcessor} to register.
	 */
	public void register(MessagePreProcessor p){
		if(!preProcessors.contains(p)) preProcessors.add(p);
	}
	
	/**
	 * Marks the current communication to be about to cancel.
	 */
	public void cancel(){
		isCanceled = true;
	}
	
	/**
	 * Adds a listener.
	 * @param listener The listener to add.
	 */
	public void addClientListener(ClientListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	/**
	 * Reports the disconnected client to all listeners.
	 */
	private void reportDisconnect(){
		for(ClientListener l : listeners) l.clientDisconnected(this);
	}
	
	/**
	 * Reports a received message to all listeners.
	 * @param m
	 */
	private void reportMessage(MV2Message m){
		for(ClientListener l : listeners) l.messageReceived(this, m);
	}
	
	/**
	 * Returns the connection to the client.
	 * @return The connection to the client.
	 */
	public Socket getConnection(){ return connection; }
	
	@Override
	public void send(MV2Message m) throws IOException{
		OutputStream out = connection.getOutputStream();
		if(parser.getCryptorSettings().getKeyGenerator().hasFixedKeyAndIV()){
			EncryptedMessage enc = EncryptedMessage.encrypt(m, parser.getCryptorSettings(), null, true);
			enc.serialize(out);
			out.flush();
			if (!parser.getCryptorSettings().getKeyGenerator().hasFixedKeyAndIV()) {
				SymetricKeyGenerator keyGen = parser.getCryptorSettings().getKeyGenerator();
				byte[] keyS = enc.getUsedSymmetricKey();
				byte[] ivS = enc.getUsedSymmetricIV();
				keyGen.setFixedKey(keyS);
				keyGen.setFixedIV(ivS);
			}
			return;
		}
		m.serialize(out);
	}
	
	/**
	 * Returns the current parser.
	 * @return The current parser.
	 */
	public MessageParser getParser(){
		return parser;
	}
	
	@Override
	public void run() {
		super.run();
		MV2Message received;
		while(!isCanceled){
			try {
				received = parser.readNext();
				for(MessagePreProcessor pp : preProcessors){
					received = pp.prepare(received);
				}
				reportMessage(received);
			} catch(SocketTimeoutException ex){
				
			} catch(EOFException e){
				cancel();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("Client disconnected!");
		reportDisconnect();
		try{
			connection.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	@Override
	public InetAddress getLocalAddress() {
		return connection.getLocalAddress();
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return connection.getRemoteSocketAddress();
	}
	

}
