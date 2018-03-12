package it.unifi.rc.httpserver.m5765968;

import java.io.File;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class extends the MyHTTPHandler1_1 class with the task to handle only
 * requests whose host specified in their header rows matches the host specified
 * by the handler.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPHandlerHost1_1 extends MyHTTPHandler1_1 {

	String myHost;

	/**
	 * Creates a HostHTTP/1.1 request handler, on the files searched by the main
	 * path, also specifying the host.
	 * 
	 * @param root
	 *            Indicates the path from where the file search starts.
	 * 
	 * @param host
	 *            Indicates the particular host that the handler handles
	 */
	public MyHTTPHandlerHost1_1(String host, File root) {
		super(root);
		this.myHost = " " + host;
	}

	public HTTPReply handle(HTTPRequest request) {
		super.myRequest = request;
		if (controlHost()) {
			return super.myReply = super.handle(myRequest);
		} else {
			return super.myReply = null;
		}
	}

	/**
	 * Controls the equality between the host declared by the manufacturer and the
	 * one declared by the request.
	 * 
	 * @return boolean value on equality between hosts.
	 */
	public boolean controlHost() {
		String hostRequest = super.myRequest.getParameters().get("Host:");
		if (myHost.equals(hostRequest)) {
			return true;
		} else {
			return false;
		}
	}

}
