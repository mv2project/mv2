package de.iss.mv2.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.UnableToProcessMessage;
import de.iss.mv2.processors.CertProcessor;
import de.iss.mv2.security.MessageCryptorSettings;

/**
 * A MV2 server to serve client requests.
 * @author Marcel Singer
 *
 */
public class MV2Server {

	/**
	 * Hold the port of this instance.
	 */
	private final int port;
	/**
	 * Holds the socket of this instance.
	 */
	private ServerSocket socket;
	/**
	 * Holds the thread used by this server.
	 */
	private Thread serverThread;
	/**
	 * Indicates if the execution of the server thread is about to cancel.
	 */
	private boolean isCanceled = false;
	/**
	 * Holds a list with all currently connected clients.
	 */
	private final List<ClientThread> clients = new ArrayList<ClientThread>();
	/**
	 * Holds a list with all registered {@link MessageProcessor}s.
	 */
	private final List<MessageProcessor> processors = new ArrayList<MessageProcessor>();
	/**
	 * Holds a list with all registeres {@link MessagePreProcessor}s.
	 */
	private final List<MessagePreProcessor> preProcessors = new ArrayList<MessagePreProcessor>();

	/**
	 * Holds the server certificate.
	 */
	private final X509Certificate cert;
	
	/**
	 * Holds the encryption settings.
	 */
	private final MessageCryptorSettings settings;
	
	/**
	 * Holds the private key of this server.
	 */
	private final KeyPair key;
	
	/**
	 * Creates a new server with the given settings.
	 * @param cert The certificate of the server.
	 * @param settings The encryption settings to use.
	 * @param key The public and private Key of this server.
	 * @param port The port to listen.
	 */
	public MV2Server(X509Certificate cert, MessageCryptorSettings settings, KeyPair key,  int port) {
		this.port = port;
		this.cert = cert;
		this.key = key;
		this.settings = settings;
		registerProcessor(new CertProcessor(this));
		
	}

	/**
	 * Starts the execution of this server.
	 * @throws IOException if an I/O error occurs.
	 */
	public void start() throws IOException {
		socket = new ServerSocket(port);
		serverThread = new ServerThread();
		serverThread.start();
		socket.setSoTimeout(1000);
	}

	/**
	 * Stops this server.
	 */
	public void stopServer() {
		isCanceled = true;
	}

	/**
	 * Registers a new {@link MessageProcessor}.
	 * @param processor The processor to register.
	 */
	public void registerProcessor(MessageProcessor processor) {
		this.processors.add(processor);
	}

	/**
	 * Registers a new {@link MessagePreProcessor}.
	 * @param preProcessor The preprocessor to register.
	 */
	public void registerPreProcessor(MessagePreProcessor preProcessor) {
		this.preProcessors.add(preProcessor);
	}

	/**
	 * Handles a client message.
	 * @param client The client that sent the message to handle.
	 * @param message The received message.
	 */
	private void handleMessage(ClientThread client, MV2Message message) {
		for (MessagePreProcessor preP : preProcessors) {
			message = preP.prepare(message);
		}
		for (MessageProcessor p : processors) {
			try {
				if (p.process(client, message))
					return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		UnableToProcessMessage utp = new UnableToProcessMessage();
		utp.setCause("The identifier of the message was not recognized.");
		try {
			client.send(utp);
		} catch (IOException e) {
			client.cancel();
		}
	}

	/**
	 * Returns the servers certificate.
	 * @return The servers certificate.
	 */
	public X509Certificate getCertificate() {
		return cert;
	}

	/**
	 * Represents the communication thread of a server.
	 * @author Marcel Singer
	 *
	 */
	private class ServerThread extends Thread implements ClientListener {

		@Override
		public void run() {
			super.run();
			Socket client;
			ClientThread clientThread;
			while (!isCanceled) {
				try {
					client = socket.accept();
					System.out.println("New client: "
							+ client.getRemoteSocketAddress());
					clientThread = new ClientThread(client, settings.doClone(), key);
					clientThread.addClientListener(this);
					clientThread.start();
					clients.add(clientThread);
				} catch (SocketTimeoutException ex) {

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			shutdown();
		}

		/**
		 * Attemts to stop the server thread.
		 */
		private void shutdown() {
			System.out.println("Server is shutting down...");
			for (ClientThread c : clients) {
				c.cancel();
			}
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

		@Override
		public void clientDisconnected(ClientThread client) {
			clients.remove(client);
			System.out.println("Connected: " + clients.size());
		}

		@Override
		public void messageReceived(ClientThread client, MV2Message m) {
			System.out.println("Message from "
					+ client.getConnection().getRemoteSocketAddress() + ":");
			System.out.println(m);
			handleMessage(client, m);
		}

	}

}
