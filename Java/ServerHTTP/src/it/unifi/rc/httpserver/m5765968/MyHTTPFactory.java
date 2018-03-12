package it.unifi.rc.httpserver.m5765968;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPServer;

/**
 * This class implements the HTTPFactory interface with the respective methods
 * for creating objects.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPFactory implements HTTPFactory {

	@Override
	public HTTPInputStream getHTTPInputStream(InputStream is) {
		HTTPInputStream myHTTPIs = new MyHTTPInputStream(is);
		return myHTTPIs;
	}

	@Override
	public HTTPOutputStream getHTTPOutputStream(OutputStream os) {
		HTTPOutputStream myHTTPOs = new MyHTTPOutputStream(os);
		return myHTTPOs;
	}

	@Override
	public HTTPServer getHTTPServer(int port, int backlog, InetAddress address, HTTPHandler... handlers) {
		HTTPServer myServer = new MyHTTPServer(port, backlog, address, handlers);
		return myServer;
	}

	@Override
	public HTTPHandler getFileSystemHandler1_0(File root) {
		HTTPHandler myHandler1_0 = new MyHTTPHandler1_0(root);
		return myHandler1_0;
	}

	@Override
	public HTTPHandler getFileSystemHandler1_0(String host, File root) {
		HTTPHandler myHandlerHost1_0 = new MyHTTPHandlerHost1_0(host, root);
		return myHandlerHost1_0;
	}

	@Override
	public HTTPHandler getFileSystemHandler1_1(File root) {
		HTTPHandler myHandler1_1 = new MyHTTPHandler1_1(root);
		return myHandler1_1;
	}

	@Override
	public HTTPHandler getFileSystemHandler1_1(String host, File root) {
		HTTPHandler myHandlerHost1_1 = new MyHTTPHandlerHost1_1(host, root);
		return myHandlerHost1_1;
	}

	@Override
	public HTTPHandler getProxyHandler() {
		return null;
	}

}
