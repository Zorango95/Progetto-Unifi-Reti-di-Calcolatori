package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5765968.MyHTTPFactory;

/**
 * This class tests the processing of an HostHTTP/1.0 request.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPHandlerHost1_0Test {

	private HTTPHandler myHandlerHost1_0;
	private File myFile;
	private HTTPInputStream inputHTTPReq;
	private HTTPInputStream inputHTTPRep;
	private HTTPRequest myRequest;
	private HTTPReply myReply;
	private HTTPReply currentReply;
	private HTTPFactory myFactory;
	private String myRoot;

	/**
	 * Creates an HTTPFactory object for general object management, calls the method
	 * setfile(), for creating files to test requests, and then creates an
	 * HostHTTPHandler1_0 to handle requests.
	 */
	@Before
	public void setUp() throws Exception {
		myFactory = new MyHTTPFactory();
		createfile();
		myHandlerHost1_0 = myFactory.getFileSystemHandler1_0("Mio.PC", myFile);
	}

	/**
	 * Creates the path of the files and for each calls the method createFile() to
	 * create them.
	 */
	public void createfile() throws IOException {
		myRoot = new File("").getAbsolutePath();
		myFile = new File(myRoot);
		File get = new File(myRoot + "/TestGetRequest.html");
		File head = new File(myRoot + "/TestHeadRequest.html");
		if (!get.exists()) {
			createFile(get, "Test Get Request");
		}
		if (!head.exists()) {
			createFile(head, "Test Head Request");
		}
	}

	/**
	 * Creates files.
	 * 
	 * @param newFile
	 *            Indicates the path of the file.
	 * @param text
	 *            Indicates the string to write to the file.
	 */
	private void createFile(File newFile, String text) throws IOException {
		newFile.createNewFile();
		FileWriter writeFile = new FileWriter(newFile);
		writeFile.write(text);
		writeFile.flush();
		writeFile.close();
	}

	/**
	 * Create a object Request from a basic string contained in the buffer in the
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
		inputHTTPRep.close();
	}

	/**
	 * Creates a requested object, a response(waiting) object, and a response object
	 * on the request, by calling the methods(newRequestHost(), newReplyHost(),
	 * handle(request)), and then tests the value of the two response objects with
	 * the control() method.
	 */
	@Test
	public void testHost() throws IOException {
		newRequestHost();
		newReplyHost();
		currentReply = myHandlerHost1_0.handle(myRequest);
		controlHost();
	}

	/**
	 * Reads a requestHost from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestHost() throws IOException {
		byte[] inputStreamRequest = "GET /TestGetRequest.html HTTP/1.0\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyHost from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyHost() throws IOException {
		byte[] inputStreamReply = "HTTP/1.0 200 OK\r\nConnection: Close\r\nContent-Type: text/HTML\r\nContent-Length: 16\r\n\r\nTest Get Request"
				.getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Creates a requested object and a response object on the request, by calling
	 * the methods(newRequestHost(), handle(request)), and then test the value of
	 * the response object with the null value.
	 */
	@Test
	public void testNoHost() throws IOException {
		newRequestNoHost();
		currentReply = myHandlerHost1_0.handle(myRequest);
		assertEquals(null, currentReply);
	}

	/**
	 * Reads a requestNoHost from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestNoHost() throws IOException {
		byte[] inputStreamRequest = "HEAD /TestHeadRequest.html HTTP/1.0\r\nHost: Mio.P\r\nConnection: Close\r\n\r\n"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Checks the parameters of the two replies.
	 */
	public void controlHost() {
		assertEquals(myReply.getVersion(), currentReply.getVersion());
		assertEquals(myReply.getStatusCode(), currentReply.getStatusCode());
		assertEquals(myReply.getStatusMessage(), currentReply.getStatusMessage());
		assertEquals(myReply.getData(), currentReply.getData());
	}
}
