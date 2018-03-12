package it.unifi.rc.httpserver.m5765968;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class implements the Runnable interface with the respective methods to
 * manage a client thread.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPThreadClient implements Runnable {

	private Socket myClient;
	private ArrayList<HTTPHandler> myListHandler;
	private HTTPReply myReply;
	private HTTPRequest myRequest;
	private Scanner in;
	private PrintWriter out;
	private boolean isClose;
	private ArrayList<Socket> myListClient;

	/**
	 * Creates a MyHTTPThreadClient object by assigning the respective client, the
	 * various objects (request and reply) and the various handlers.
	 * 
	 * @param client
	 *            Indicates the client accepted by the server.
	 * 
	 * @param handler
	 *            Indicates the series of operations waiting to be performed by a
	 *            server.
	 * @param socket
	 *            Indicates the series of client accepted by server.
	 */
	public MyHTTPThreadClient(Socket client, ArrayList<HTTPHandler> handler, ArrayList<Socket> socket) {
		this.myClient = client;
		this.myListClient = socket;
		this.myListHandler = new ArrayList<HTTPHandler>();
		this.myListHandler = handler;
		this.myReply = null;
		this.myRequest = null;
		this.isClose = false;
	}

	@Override
	public void run() {
		while (!isClose) {
			try {
				in = new Scanner(myClient.getInputStream());
				out = new PrintWriter(myClient.getOutputStream());
				readRequest(in);
				writeReply(out);
			} catch (IOException e) {
				break;
			}
			closeClient();
		}
	}

	/**
	 * Reads the string request (sent by the client), creating a corresponding
	 * request object.
	 * 
	 * @param in
	 *            Indicates the InputStream scanner with which the reading is
	 *            performed.
	 */
	private void readRequest(Scanner in) throws IOException {
		String request = "";
		while (in.hasNextLine()) {
			String line = in.nextLine();
			if (!line.equals("stop")) {
				request = request + line + "\r\n";
			} else {
				setRequest(request);
				break;
			}
		}
	}

	/**
	 * Writes and sends the Reply string to the client, taking the information from
	 * the reply object and control Connection.
	 * 
	 * @param out
	 *            Indicates the OutputStream PrintWriter with which the writing is
	 *            performed.
	 */
	private void writeReply(PrintWriter out) throws IOException {
		boolean controlRep = false;
		for (int i = 0; i < myListHandler.size(); i++) {
			myReply = myListHandler.get(i).handle(myRequest);
			if (myReply != null) {
				String reply = getReply();
				out.print(reply);
				out.flush();
				controlRep = true;
				if (myReply.getParameters().get("Connection:").equals(" Close")) {
					isClose = true;
				}
				break;
			}
		}
		if (!controlRep) {
			String version = myRequest.getVersion();
			String reply = version + " 400 Bad Request\r\nConnection: Close\r\n\r\n";
			out.print(reply);
			out.flush();
			isClose = true;
		}
	}

	/**
	 * Creates the Request object starting from the request string sent by the
	 * client.
	 */
	public void setRequest(String request) throws IOException {
		byte[] inputReq = request.getBytes();
		InputStream inputHTTPRep = new ByteArrayInputStream(inputReq);
		HTTPInputStream myInputRequest = new MyHTTPInputStream(inputHTTPRep);
		myRequest = myInputRequest.readHttpRequest();
		myInputRequest.close();
	}

	/**
	 * Creates the Reply string starting from the reply object returned by the
	 * server handler.
	 */
	public String getReply() throws IOException {
		OutputStream outputRep = new ByteArrayOutputStream();
		HTTPOutputStream outputHTTPRep = new MyHTTPOutputStream(outputRep);
		outputHTTPRep.writeHttpReply(myReply);
		String reply = new String(((ByteArrayOutputStream) outputRep).toByteArray());
		outputHTTPRep.close();
		return reply;
	}

	/**
	 * Closes the client socket and remove from the array.
	 */
	public void closeClient() {
		if (isClose) {
			try {
				in.close();
				out.close();
				myClient.close();
				myListClient.remove(myClient);
				System.out.println("\nClosing sockets client : " + myClient.getRemoteSocketAddress().toString() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
