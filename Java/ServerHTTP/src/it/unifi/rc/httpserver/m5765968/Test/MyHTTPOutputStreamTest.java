package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5765968.MyHTTPFactory;

/**
 * This class tests the writing of a Request and / or a Reply on an
 * OutputStream.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPOutputStreamTest {

	private HTTPFactory myFactory;
	private HTTPInputStream inputHTTPReq;
	private HTTPInputStream inputHTTPRep;
	private HTTPOutputStream outputHTTPReq;
	private HTTPOutputStream outputHTTPRep;
	private HTTPRequest myRequest;
	private HTTPReply myReply;
	private String request;
	private String reply;

	/**
	 * Recalls the methods for reading a request and / or a reply.
	 */
	@Before
	public void setUpOutputStream() throws IOException {
		myFactory = new MyHTTPFactory();
		newRequest();
		newReply();
	}

	/**
	 * Reads a request from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequest() throws IOException {
		request = "GET /Scrivania/ProvaHTTP/ControlOutputStream HTTP/1.0\r\nConnection: Close\r\n\r\n";
		byte[] bufReq = request.getBytes();
		setInputStreamRequest(bufReq);
	}

	/**
	 * Reads a reply from a basic string and recall the method to create the reply
	 * object.
	 */
	public void newReply() throws IOException {
		reply = "HTTP/1.0 200 OK\r\nConnection: Close\r\n\r\n";
		byte[] bufRep = reply.getBytes();
		setInputStreamReply(bufRep);
	}

	/**
	 * Creates a object Request from a basic string contained in the buffer in the
	 * form of bytes.
	 * 
	 * @param buf
	 *            contains the string Request in bytes.
	 */
	public void setInputStreamRequest(byte[] buf) throws IOException {
		InputStream inputReq = new ByteArrayInputStream(buf);
		inputHTTPReq = myFactory.getHTTPInputStream(inputReq);
		myRequest = inputHTTPReq.readHttpRequest();
		inputHTTPReq.close();
	}

	/**
	 * Creates a object Reply from a basic string contained in the buffer in the
	 * form of bytes.
	 * 
	 * @param buf
	 *            contains the string Reply in bytes.
	 */
	public void setInputStreamReply(byte[] buf) throws IOException {
		InputStream inputRep = new ByteArrayInputStream(buf);
		inputHTTPRep = myFactory.getHTTPInputStream(inputRep);
		myReply = inputHTTPRep.readHttpReply();
		inputHTTPReq.close();
	}

	/**
	 * Creates a Requested string, by writing with the HTTPOutputStream object.
	 * 
	 * @return requested string.
	 */
	public String setOutputRequest() throws IOException {
		OutputStream outputReq = new ByteArrayOutputStream();
		outputHTTPReq = myFactory.getHTTPOutputStream(outputReq);
		outputHTTPReq.writeHttpRequest(myRequest);
		outputHTTPReq.close();
		String currentReq = new String(((ByteArrayOutputStream) outputReq).toByteArray());
		return currentReq;
	}

	/**
	 * Creates a Reply string, by writing with the HTTPOutputStream object.
	 * 
	 * @return reply string.
	 */
	public String setOutputReply() throws IOException {
		OutputStream outputRep = new ByteArrayOutputStream();
		outputHTTPRep = myFactory.getHTTPOutputStream(outputRep);
		outputHTTPRep.writeHttpReply(myReply);
		outputHTTPReq.close();
		String currentRep = new String(((ByteArrayOutputStream) outputRep).toByteArray());
		return currentRep;
	}

	/**
	 * Tests the equality between the written request/reply and a requested/reply
	 * string.
	 */
	@Test
	public void testOutput() throws IOException {
		request = "GET /Scrivania/ProvaHTTP/ControlOutputStream HTTP/1.0\r\nConnection: Close\r\n\r\n";
		reply = "HTTP/1.0 200 OK\r\nConnection: Close\r\n\r\n";
		assertEquals(setOutputRequest(), request);
		assertEquals(setOutputReply(), reply);
	}
}
