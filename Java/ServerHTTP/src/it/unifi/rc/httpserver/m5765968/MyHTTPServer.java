package it.unifi.rc.httpserver.m5765968;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPServer;

/**
 * This class implements the HTTPServer interface with the respective methods to
 * manage a server object.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPServer implements HTTPServer {

	private ArrayList<HTTPHandler> myHandler;
	private int myPort;
	private int myBackLog;
	private InetAddress myBindAddr;
	private MyHTTPThreadServer myServer;

	/**
	 * Creates a MyHTTPServer object by assigning the respective port, backlog,
	 * address and the various handlers.
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
	 * @param handlers
	 *            Indicates the handlers that handle the request.
	 */
	public MyHTTPServer(int port, int backlog, InetAddress address, HTTPHandler... handlers) {
		this.myHandler = new ArrayList<HTTPHandler>();
		this.myPort = port;
		this.myBackLog = backlog;
		this.myBindAddr = address;
		for (int i = 0; i < handlers.length; i++) {
			addHandler(handlers[i]);
		}
	}

	@Override
	public void addHandler(HTTPHandler handler) {
		myHandler.add(handler);
	}

	@Override
	public void start() throws IOException {
		myServer = new MyHTTPThreadServer(myPort, myBackLog, myBindAddr, myHandler);
		new Thread(myServer).start();
		try {
			Thread.sleep(120 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		myServer.stop();
	}

}
