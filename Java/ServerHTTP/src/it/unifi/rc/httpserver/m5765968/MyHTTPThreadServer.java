package it.unifi.rc.httpserver.m5765968;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import it.unifi.rc.httpserver.HTTPHandler;

/**
 * This class implements the Runnable interface with the respective methods to
 * manage a server thread.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPThreadServer implements Runnable {

	private ArrayList<HTTPHandler> myListHandler;
	private int myPort;
	private int myBackLog;
	private InetAddress myBindAddr;
	private ServerSocket myServerSocket;
	private boolean controlStop;
	private ArrayList<Socket> myListClient;

	/**
	 * Creates a MyHTTPThreadServer object by assigning the respective port,
	 * backlog, address and the various handlers.
	 * 
	 * @param port
	 *            Indicates the server port to which the client accesses.
	 * 
	 * @param backlog
	 *            Indicates the series of operations waiting to be performed by a
	 *            server.
	 * 
	 * @param address
	 *            Indicates the server address.
	 * 
	 * @param handler
	 *            Indicates the handlers that handle the request.
	 */
	public MyHTTPThreadServer(int port, int backlog, InetAddress address, ArrayList<HTTPHandler> handler) {
		this.myListClient = new ArrayList<Socket>();
		this.myListHandler = new ArrayList<HTTPHandler>();
		this.myListHandler = handler;
		this.myPort = port;
		this.myBackLog = backlog;
		this.myBindAddr = address;
		this.controlStop = false;
	}

	@Override
	public void run() {
		openServerSocket();
		while (!isStop()) {
			Socket myClient = null;
			try {
				myClient = myServerSocket.accept();
				myListClient.add(myClient);
				System.out.println("Client accept : " + myClient.getRemoteSocketAddress().toString());
			} catch (IOException e) {
				if (isStop()) {
					System.out.println("Server stopped\n");
					return;
				}
				throw new RuntimeException("Error accepting client connection\n", e);
			}
			try {
				new Thread(new MyHTTPThreadClient(myClient, myListHandler, myListClient)).start();
			} catch (Exception e) {
				System.err.println("Error processing request\n");
			}
		}
	}

	/**
	 * Checks the status of the Server.
	 * 
	 * @return status of the Server.
	 */
	private synchronized boolean isStop() {
		return controlStop;
	}

	/**
	 * Manages the server and clients still active.
	 */
	public synchronized void stop() {
		controlStop = true;
		try {
			myServerSocket.close();
			for (Socket client : myListClient) {
				client.close();
				System.out.println("\nClosing sockets client : " + client.getRemoteSocketAddress().toString() + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException("Error closing server\n", e);
		}
	}

	/**
	 * Creates a ServerSocket object and opens the server in communication on that
	 * port, otherwise it calls exception.
	 */
	private void openServerSocket() {
		try {
			myServerSocket = new ServerSocket(myPort, myBackLog, myBindAddr);
			System.out.println("Start Server " + myServerSocket.getLocalSocketAddress() + "\n");
			System.out.println("Server open for 2 minutes\n");
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port\n", e);
		}
	}
}
