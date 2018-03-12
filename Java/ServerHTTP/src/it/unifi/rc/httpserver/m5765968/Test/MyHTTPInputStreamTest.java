package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5765968.MyHTTPFactory;

/**
 * This class tests the reading of a request and / or a response from an
 * InputStream.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPInputStreamTest {

	private HTTPFactory myFactory;
	private HTTPInputStream inputHTTPReq;
	private HTTPInputStream inputHTTPRep;
	private HTTPRequest myRequest;
	private HTTPReply myReply;

	/**
	 * Recalls the methods for reading a request and / or a reply.
	 */
	@Before
	public void setUpInputStream() throws IOException {
		myFactory = new MyHTTPFactory();
		setUpInputRequest();
		setUpInputReply();
	}

	/**
	 * Creates a object Request from a basic string to perform the check.
	 */
	public void setUpInputRequest() throws IOException {
		String stringReq = "GET /somedir/page.html HTTP/1.1\r\nHost: www.someschool.edu\r\nConnection: Close\r\n\r\n";
		byte[] bufReq = stringReq.getBytes();
		InputStream inputReq = new ByteArrayInputStream(bufReq);
		inputHTTPReq = myFactory.getHTTPInputStream(inputReq);
		myRequest = inputHTTPReq.readHttpRequest();
		inputHTTPReq.close();
	}

	/**
	 * Creates a object Reply from a basic string to perform the check.
	 */
	public void setUpInputReply() throws IOException {
		String stringRep = "HTTP/1.1 200 OK\r\nConnection: Close\r\nContent-Type: text/html\r\n\r\nData";
		byte[] bufRep = stringRep.getBytes();
		InputStream inputRep = new ByteArrayInputStream(bufRep);
		inputHTTPRep = myFactory.getHTTPInputStream(inputRep);
		myReply = inputHTTPRep.readHttpReply();
		inputHTTPRep.close();
	}

	/**
	 * Tests the equality of the Method field.
	 */
	@Test
	public void testMethod() throws HTTPProtocolException {
		assertEquals("GET", myRequest.getMethod());
	}

	/**
	 * Tests the equality of the VersionRequest field.
	 */
	@Test
	public void testVersionRequest() throws HTTPProtocolException {
		assertEquals("HTTP/1.1", myRequest.getVersion());
	}

	/**
	 * Tests the equality of the Path field.
	 */
	@Test
	public void testPath() throws HTTPProtocolException {
		assertEquals("/somedir/page.html", myRequest.getPath());
	}

	/**
	 * Tests the equality of the ParametersRequest map.
	 */
	@Test
	public void testParametersRequest() throws HTTPProtocolException {
		Map<String, String> myParametersRequest = new TreeMap<String, String>();
		myParametersRequest.put("Host:", " www.someschool.edu");
		myParametersRequest.put("Connection:", " Close");
		assertEquals(myParametersRequest, myRequest.getParameters());
	}

	/**
	 * Tests the equality of the EntityBody field.
	 */
	@Test
	public void testEntityBody() throws HTTPProtocolException {
		assertEquals("", myRequest.getEntityBody());
	}

	/**
	 * Tests the equality of the VersionReply field.
	 */
	@Test
	public void testVersionReply() throws HTTPProtocolException {
		assertEquals("HTTP/1.1", myReply.getVersion());
	}

	/**
	 * Tests the equality of the StatusCode field.
	 */
	@Test
	public void testStatusCode() throws HTTPProtocolException {
		assertEquals("200", myReply.getStatusCode());
	}

	/**
	 * Tests the equality of the StatusMessage field.
	 */
	@Test
	public void testStatusMessage() throws HTTPProtocolException {
		assertEquals("OK", myReply.getStatusMessage());
	}

	/**
	 * Tests the equality of the ParametersReply map.
	 */
	@Test
	public void testParametersReply() throws HTTPProtocolException {
		Map<String, String> myParametersReply = new TreeMap<String, String>();
		myParametersReply.put("Connection:", " Close");
		myParametersReply.put("Content-Type:", " text/html");
		assertEquals(myParametersReply, myReply.getParameters());
	}

	/**
	 * Tests the equality of the Data field.
	 */
	@Test
	public void testData() throws HTTPProtocolException {
		assertEquals("Data", myReply.getData());
	}
}
