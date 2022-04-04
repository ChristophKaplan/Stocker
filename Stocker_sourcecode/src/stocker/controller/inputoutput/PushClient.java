package stocker.controller.inputoutput;

import java.net.URI;

import org.java_websocket.client.*;
import org.java_websocket.handshake.*;

/**
 * Websocket "push" client, handles the subscription requests for the real-time data.
 * 
 * @author Christoph Kaplan
 */

public class PushClient extends WebSocketClient implements Comparable<PushClient>{
	private boolean isConnected = false;
	private NetworkClient networkClient;
	private String serverUri;
	private int connectionTries;

	/**
	 * {@code PushClient} constructor
	 * 
	 * @param serverUri     the pushUrl
	 * @param networkClient the {@code NetworkClient} object
	 */
	public PushClient(String serverUri, NetworkClient networkClient) {
		super(URI.create(serverUri));
		this.networkClient = networkClient;
		this.serverUri = serverUri;
	}

	/**
	 * connect client to server
	 */
	public void doConnect() {
		connectionTries = 0;
		connect();
		while (!isConnected) {
			try {
				//System.out.println("no connection? ");

				if (connectionTries >= 20)
					return;
				connectionTries++;

				Thread.sleep(100L);
			} catch (InterruptedException iex) {
				System.out.println("connection error:" + iex.getMessage());
			}
		}
	}

	/**
	 * Sends a (request) message to the server i.e. subscribe
	 * 
	 * @param message the message
	 */
	public void doPushRequest(String message) {
		if(isConnected)	send(message);
	}

	/**
	 * When connection established
	 */
	@Override
	public void onOpen(ServerHandshake handshake) {
		// System.out.println("connection established");
		isConnected = true;
		if (isCurrentConnection()) {
			networkClient.setStatus("");
		}

	}

	/**
	 * When message received
	 */
	@Override
	public void onMessage(String message) {
		// System.out.println("received: " + message);
		this.networkClient.pushClientMessage(message);
	}

	/**
	 * When connection closed
	 */
	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("PushClient: connection closed " + this.serverUri);
		isConnected = false;
		if (isCurrentConnection()) {
			networkClient.setStatus("");
		}

	}

	/**
	 * When connection error occurred
	 */
	@Override
	public void onError(Exception ex) {
		String error = "PushClient: unknown error!";
		if (ex != null) {
			error = "PushClient: " + ex.getMessage();
			// ex.printStackTrace();
		}

		System.out.println(error);
		if (isCurrentConnection())	networkClient.setStatus("connection error: "+error);
	}

	/**
	 * Checks if network client still has the correct push url
	 * 
	 * @return true if still the current connection
	 */
	boolean isCurrentConnection() {
		if (this.networkClient.getPushClient() != null && this.networkClient.getPushClient() == this)
			return true;
		return false;
	}
	
	/**
	 * Gets the isConnected flag.
	 * @return the isConnected flag
	 */
	public boolean isConnected() {
		return this.isConnected;
	}

	
	@Override
	public int compareTo(PushClient o) {
		// TODO Auto-generated method stub
		return this.serverUri.compareTo(o.serverUri);
	}
}
