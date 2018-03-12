package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.m5765968.MyHTTPFactory;

/**
 * This class tests the server with various types required one for each new
 * client, printing the reply.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPSingleClientTest {

	private Socket myClient;
	private Scanner in;
	private PrintWriter out;
	private String request;
	private String reply;
	private HTTPReply myReply;

	/**
	 * Creates the client and connect it to the server and start the InputStream
	 * Scanner and the OutpuStream PrintWriter.
	 */
	@Before
	public void setConnect() throws IOException {
		InetAddress host = InetAddress.getByName("localhost");
		myClient = new Socket(host, 12000);
		System.out.println("Connection client\n");
		in = new Scanner(myClient.getInputStream());
		out = new PrintWriter(myClient.getOutputStream());
	}

	/**
	 * Manages the sending of the request to the server, sending also the string of
	 * request "stop".
	 */
	public void sendRequest() {
		out.print(request);
		out.flush();
		out.println("stop");
		out.flush();
		System.out.println("Request send to server :\n\n" + request);
	}

	/**
	 * Manages reception of the reply from the server.
	 */
	public void readReply() throws IOException {
		reply = "";
		while (in.hasNextLine()) {
			String line = in.nextLine() + "\r\n";
			reply = reply + line;
		}
		System.out.println("Reply receipt from server :\n\n" + reply);
		createReply(reply);
	}

	/**
	 * Closes the InputStream, OutputStream and the client connection.
	 */
	public void exitConnect() throws IOException {
		out.close();
		in.close();
		myClient.close();
		System.out.println("Connection closed number\n\n");
	}

	/**
	 * Test a GET request with HTTP/1.1 version with the correct Host. It is managed
	 * with handlerHost1_1.
	 */
	@Test
	public void testGetRequest1_1() throws IOException {
		System.out.println("Request GET HTTP/1.1; Host correct: MIO.PC; Handler: HandlerHost1_1; Path: Ok\n");
		request = "GET /TestGetRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_1();
		exitConnect();
	}

	/**
	 * Test a HEAD request with HTTP/1.0 version with the correct Host. It is
	 * managed with handlerHost1_0.
	 */
	@Test
	public void testHeadRequest1_0() throws IOException {
		System.out.println("Request HEAD HTTP/1.0; Host correct: MIO.PC; Handler: HandlerHost1_0; Path: Ok\n");
		request = "HEAD /TestHeadRequest.html HTTP/1.0\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_0();
		exitConnect();
	}

	/**
	 * Test a POST request with HTTP/1.0 version without the Host. It is managed
	 * with handler1_0.
	 */
	@Test
	public void testPostRequest1_0() throws IOException {
		System.out.println("Request POST HTTP/1.0; Host inexistent; Handler: Handler1_0; Path: Ok\n");
		request = "POST /TestPostRequest.html HTTP/1.0\r\nConnection: Close\r\n\r\nOK\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_0();
		exitConnect();
	}

	/**
	 * Test a DELETE request with HTTP/1.1 version with the correct Host. It is
	 * managed with handlerHost1_1.
	 */
	@Test
	public void testDeleteRequest1_1() throws IOException {
		System.out.println("Request DELETE HTTP/1.1; Host correct: MIO.PC; Handler: HandlerHost1_1; Path: Ok\n");
		request = "DELETE /TestDeleteRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_1();
		exitConnect();
	}

	/**
	 * Test a PUT request with HTTP/1.1 version with the correct Host. It is managed
	 * with handlerHost1_1.
	 */
	@Test
	public void testPutExistsRequest1_1() throws IOException {
		System.out.println("Request PUT HTTP/1.1; Host correct: MIO.PC; Handler: HandlerHost1_1; Path: Ok\n");
		request = "PUT /TestPutExistsFileRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\nTest Put Exists File Request\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_1();
		exitConnect();
	}

	/**
	 * Test a PUT request with HTTP/1.1 version with the correct Host. It is managed
	 * with handlerHost1_1.
	 */
	@Test
	public void testPutNewFileRequest1_1() throws IOException {
		System.out.println("Request PUT HTTP/1.1; Host correct: MIO.PC; Handler: HandlerHost1_1; Path: Ok\n");
		request = "PUT /TestPutNewFileRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\nTest Put New File Request\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_1NewFile();
		exitConnect();
	}

	/**
	 * Test a PUT request with HTTP/1.1 version without the correct Host. It is
	 * managed by handler1_1.
	 */
	@Test
	public void testErrorHostRequest1_1() throws IOException {
		System.out.println("Request PUT HTTP/1.1; Host not correct: MIO.Pc; Handler: Handler1_1; Path: Ok\n");
		request = "PUT /TestPutExistsFileRequest.html HTTP/1.1\r\nHost: Mio.Pc\r\nConnection: Close\r\n\r\nTest Put Exists File Request\n";
		sendRequest();
		readReply();
		controlReplyHTTP1_1();
		exitConnect();
	}

	/**
	 * Test a GET request with HTTP/1.1 version without Host. It is not managed by
	 * any handler.
	 */
	@Test
	public void testNoHostRequst1_1() throws IOException {
		System.out.println("Request GET HTTP/1.1; Host inexistent; Handler inesistente; Path: Ok\n");
		request = "GET /TestGetRequest.html HTTP/1.1\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlErrorHTTP1_1();
		exitConnect();
	}

	/**
	 * Test a DELETE request with HTTP/1.0 version without the Host. It is managed
	 * with handler1_0.
	 */
	@Test
	public void testErrorMethodRequst1_0() throws IOException {
		System.out.println("Request DELETE HTTP/1.0; Host inexistent; Handler: Handler1_0; Path: Ok\n");
		request = "DELETE /TestGetRequest.html HTTP/1.0\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyBadRequest1_0();
		exitConnect();
	}

	/**
	 * Test a Head request with HTTP/1.1 version without the correct Host. It is
	 * managed with handler1_1.
	 */
	@Test
	public void testErrorMethodRequst1_1() throws IOException {
		System.out.println("Request Head HTTP/1.1; Host not correct: Mio.Pc; Handler: Handler1_1; Path: Ok\n");
		request = "Head /TestGetRequest.html HTTP/1.1\r\nHost: Mio.Pc\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyBadRequest1_1();
		exitConnect();
	}

	/**
	 * Test a GET request with HTTP/1.0 version without the Host and path not of a
	 * file. It is managed with handler1_0.
	 */
	@Test
	public void testNoFileRequst() throws IOException {
		System.out.println("Request GET HTTP/1.0; Host inexistent; Handler: Handler1_0; Path: No file\n");
		request = "GET / HTTP/1.0\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyNoFile();
		exitConnect();
	}

	/**
	 * Test a POST request with HTTP/1.0 version without the Host and non-existent
	 * file. It is managed with handler1_0.
	 */
	@Test
	public void testNoPathRequst() throws IOException {
		System.out.println("Request POST HTTP/1.0; Host inexistent; Handler: Handler1_0; Path: file inexistent\n");
		request = "POST /TestRequest.html HTTP/1.0\r\nConnection: Close\r\n\r\n";
		sendRequest();
		readReply();
		controlReplyNoPath();
		exitConnect();
	}

	/**
	 * Reads a reply from a string received from the server and create the reply
	 * object.
	 * 
	 * @param reply
	 *            Indicates the string that contains the reply.
	 */
	public void createReply(String reply) throws IOException {
		byte[] bufRep = reply.getBytes();
		InputStream inputRep = new ByteArrayInputStream(bufRep);
		HTTPFactory myFactory = new MyHTTPFactory();
		HTTPInputStream inputHTTPRep = myFactory.getHTTPInputStream(inputRep);
		myReply = inputHTTPRep.readHttpReply();
		inputHTTPRep.close();
	}

	/**
	 * Check the values of an answer whose request was correct with HTTP/1.0
	 * version.
	 */
	public void controlReplyHTTP1_0() {
		assertEquals(myReply.getVersion(), "HTTP/1.0");
		assertEquals(myReply.getStatusCode(), "200");
		assertEquals(myReply.getStatusMessage(), "OK");
	}

	/**
	 * Check the values of an answer whose request was correct with HTTP/1.1
	 * version.
	 */
	public void controlReplyHTTP1_1() {
		assertEquals(myReply.getVersion(), "HTTP/1.1");
		assertEquals(myReply.getStatusCode(), "200");
		assertEquals(myReply.getStatusMessage(), "OK");
	}

	/**
	 * Check the values of an answer whose request was correct with HTTP/1.1 version
	 * with new file.
	 */
	public void controlReplyHTTP1_1NewFile() {
		assertEquals(myReply.getVersion(), "HTTP/1.1");
		assertEquals(myReply.getStatusCode(), "201");
		assertEquals(myReply.getStatusMessage(), "Created");
	}

	/**
	 * Check the values of an answer whose request was completely wrong.
	 */
	public void controlErrorHTTP1_1() {
		assertEquals(myReply.getVersion(), "HTTP/1.1");
		assertEquals(myReply.getStatusCode(), "400");
		assertEquals(myReply.getStatusMessage(), "Bad Request");
	}

	/**
	 * Checks the values of a response whose request contained an incorrect method
	 * with HTTP/1.0 version.
	 */
	public void controlReplyBadRequest1_0() {
		assertEquals(myReply.getVersion(), "HTTP/1.0");
		assertEquals(myReply.getStatusCode(), "405");
		assertEquals(myReply.getStatusMessage(), "Method Not Allowed");
	}

	/**
	 * Checks the values of a response whose request contained an incorrect method
	 * with HTTP/1.1 version.
	 */
	public void controlReplyBadRequest1_1() {
		assertEquals(myReply.getVersion(), "HTTP/1.1");
		assertEquals(myReply.getStatusCode(), "405");
		assertEquals(myReply.getStatusMessage(), "Method Not Allowed");
	}

	/**
	 * Checks the values of a response whose request was on a directory.
	 */
	public void controlReplyNoFile() {
		assertEquals(myReply.getVersion(), "HTTP/1.0");
		assertEquals(myReply.getStatusCode(), "403");
		assertEquals(myReply.getStatusMessage(), "Directory listing not supported");
	}

	/**
	 * Checks the values of a response whose request was on a non-existent file.
	 */
	public void controlReplyNoPath() {
		assertEquals(myReply.getVersion(), "HTTP/1.0");
		assertEquals(myReply.getStatusCode(), "404");
		assertEquals(myReply.getStatusMessage(), "Not Found");
	}
}