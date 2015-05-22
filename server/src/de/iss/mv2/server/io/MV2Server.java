package de.iss.mv2.server.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import de.iss.mv2.data.CertificateManager;
import de.iss.mv2.logging.LogEntryLevel;
import de.iss.mv2.logging.LoggerManager;
import de.iss.mv2.messaging.DomainNamesResponse;
import de.iss.mv2.messaging.MV2Message;
import de.iss.mv2.messaging.MessageParser;
import de.iss.mv2.messaging.MessagePreProcessor;
import de.iss.mv2.messaging.MessageProcessor;
import de.iss.mv2.messaging.UnableToProcessMessage;
import de.iss.mv2.security.MessageCryptorSettings;
import de.iss.mv2.server.ServerBindings;
import de.iss.mv2.server.data.CertificateManagerImpl;
import de.iss.mv2.server.data.DatabaseContext;
import de.iss.mv2.server.data.SessionManagerImpl;
import de.iss.mv2.server.data.WebSpaceManager;
import de.iss.mv2.server.data.WebSpaceManagerImpl;
import de.iss.mv2.server.processors.CertProcessor;
import de.iss.mv2.server.processors.ClientCertificateRequestProcessor;
import de.iss.mv2.server.processors.ClientLoginDataProcessor;
import de.iss.mv2.server.processors.ClientLoginRequestProcessor;
import de.iss.mv2.server.processors.DomainNamesProcessor;
import de.iss.mv2.server.processors.HelloMessageProcessor;
import de.iss.mv2.server.processors.KeyPutRequestProcessor;
import de.iss.mv2.server.processors.KeyRequestProcessor;
import de.iss.mv2.server.processors.MessageDeliveryRequestProcessor;
import de.iss.mv2.server.processors.MessageFetchProcessor;
import de.iss.mv2.server.processors.MessageQueryProcessor;
import de.iss.mv2.server.processors.SpaceCreationProcessor;

/**
 * A MV2 server to serve client requests.
 * 
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
	 * Holds a list with all registers {@link MessagePreProcessor}s.
	 */
	private final List<MessagePreProcessor> preProcessors = new ArrayList<MessagePreProcessor>();

	/**
	 * Holds the bindings of this server.
	 */
	private final ServerBindings bindings;

	/**
	 * Holds the encryption settings.
	 */
	private final MessageCryptorSettings settings;

	/**
	 * Holds the current certificate manager.
	 */
	private final CertificateManager certificateManager;

	/**
	 * Creates a new server with the given settings.
	 * 
	 * @param bindings
	 *            The bindings for this server.
	 * @param settings
	 *            The encryption settings to use.
	 * @param port
	 *            The port to listen.
	 */
	public MV2Server(ServerBindings bindings, MessageCryptorSettings settings,
			int port) {
		this.certificateManager = new CertificateManagerImpl(
				DatabaseContext.getContext());
		this.port = port;
		this.settings = settings;
		this.bindings = bindings;
		registerDefaultProcessors();

	}

	/**
	 * Registers all default and or needed processors.
	 */
	public void registerDefaultProcessors() {
		WebSpaceManager spaceManager = new WebSpaceManagerImpl(
				DatabaseContext.getContext(), certificateManager);
		registerProcessor(new CertProcessor(this));
		DomainNamesProcessor dnp = new DomainNamesProcessor(
				bindings.getAvailableAddressesArray());
		registerProcessor(dnp);
		registerPreProcessor(dnp);
		SpaceCreationProcessor scp = new SpaceCreationProcessor(this,
				spaceManager);
		registerProcessor(scp);
		registerPreProcessor(scp);
		SessionManagerImpl smi = new SessionManagerImpl();
		ClientLoginDataProcessor cldp = new ClientLoginDataProcessor(smi);
		registerProcessor(cldp);
		registerPreProcessor(cldp);
		try {
			ClientLoginRequestProcessor clrp = new ClientLoginRequestProcessor(
					smi, spaceManager);
			registerProcessor(clrp);
			registerPreProcessor(clrp);
		} catch (NoSuchAlgorithmException e) {
			LoggerManager.getCurrentLogger().push(e);
		}
		ClientCertificateRequestProcessor ccrp = new ClientCertificateRequestProcessor(
				spaceManager);
		registerProcessor(ccrp);
		registerPreProcessor(ccrp);
		MessageDeliveryRequestProcessor mdrp = new MessageDeliveryRequestProcessor(
				spaceManager);
		registerProcessor(mdrp);
		registerPreProcessor(mdrp);
		MessageFetchProcessor mfp = new MessageFetchProcessor(smi, spaceManager);
		registerPreProcessor(mfp);
		registerProcessor(mfp);
		MessageQueryProcessor mqp = new MessageQueryProcessor(smi, spaceManager);
		registerPreProcessor(mqp);
		registerProcessor(mqp);
		KeyPutRequestProcessor kpr = new KeyPutRequestProcessor(smi, spaceManager);
		registerPreProcessor(kpr);
		registerProcessor(kpr);
		KeyRequestProcessor krp = new KeyRequestProcessor(smi, spaceManager);
		registerPreProcessor(krp);
		registerProcessor(krp);
	}

	/**
	 * Returns the certificate manager used by this server.
	 * 
	 * @return The certificate manager used by this server.
	 */
	public CertificateManager getCertificateManager() {
		return certificateManager;
	}

	/**
	 * Starts the execution of this server.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
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
	 * 
	 * @param processor
	 *            The processor to register.
	 */
	public void registerProcessor(MessageProcessor processor) {
		this.processors.add(processor);
	}

	/**
	 * Registers a new {@link MessagePreProcessor}.
	 * 
	 * @param preProcessor
	 *            The preprocessor to register.
	 */
	public void registerPreProcessor(MessagePreProcessor preProcessor) {
		this.preProcessors.add(preProcessor);
	}

	/**
	 * Handles a client message.
	 * 
	 * @param client
	 *            The client that sent the message to handle.
	 * @param message
	 *            The received message.
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
	 * Returns the servers certificate for the specified binding.
	 * 
	 * @param serverAddress
	 *            The address of the binding.
	 * @return The servers certificate.
	 */
	public X509Certificate getCertificate(String serverAddress) {
		return bindings.getBinding(serverAddress).getCertificate();
	}

	/**
	 * Returns the servers private key for the specified binding.
	 * 
	 * @param serverAddress
	 *            The address of the binding.
	 * @return The servers private key for the specified binding.
	 */
	public PrivateKey getPrivateKey(String serverAddress) {
		return bindings.getBinding(serverAddress).getPrivateKey();
	}

	/**
	 * Returns the servers key pair for the specified binding.
	 * 
	 * @param serverAddress
	 *            The address of the binding.
	 * @return The servers key pair for the specified binding.
	 */
	public KeyPair getKeyPair(String serverAddress) {
		return new KeyPair(getCertificate(serverAddress).getPublicKey(),
				getPrivateKey(serverAddress));
	}

	/**
	 * Represents the communication thread of a server.
	 * 
	 * @author Marcel Singer
	 *
	 */
	private class ServerThread extends Thread implements ClientListener {

		@Override
		public void run() {
			super.run();

			final HelloMessageProcessor helloProcessor = new HelloMessageProcessor();
			while (!isCanceled) {
				try {
					final Socket client = socket.accept();
					System.out.println("New socket connection...");
					client.setSoTimeout(1000);
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								MessageParser mp = new MessageParser(client
										.getInputStream());
								MV2Message helloMessage = mp.readNext();
								System.out.println(helloMessage);
								String binding = helloProcessor
										.getHostName(helloProcessor
												.prepare(helloMessage));
								System.out.println("Client binding: " + binding);
								if (binding == null) {
									client.close();
									return;
								}
								if (!bindings.hasBidnding(binding)) {
									try {
										DomainNamesResponse resp = new DomainNamesResponse();
										resp.setAvailableDomainNames(bindings
												.getAvailableAddressesArray());
										resp.serialize(client.getOutputStream());
										client.getOutputStream().flush();
										client.close();
										LoggerManager.getCurrentLogger().push(
												LogEntryLevel.WARNING,
												"Connection",
												"There was no binding for "
														+ binding + ".");
										return;
									} catch (IOException ex) {
										ex.printStackTrace();
									}
								}
								LoggerManager
										.getCurrentLogger()
										.push(LogEntryLevel.INFORMATION,
												"Connection",
												"New client on binding "
														+ binding
														+ ": "
														+ client.getRemoteSocketAddress());

								ClientThread clientThread = new ClientThread(
										client, binding, settings.doClone(),
										getKeyPair(binding));
								clientThread
										.addClientListener(ServerThread.this);
								clientThread.start();
								clients.add(clientThread);
							} catch (IOException ex) {

							}
						}
					}).run();

				} catch (SocketTimeoutException ex) {

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			shutdown();
		}

		/**
		 * Attempts to stop the server thread.
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
