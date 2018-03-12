package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.*;

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
 * This class tests the processing of an HTTP/1.1 request.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPHandler1_1Test {

	private HTTPHandler myHandler1_1;
	private File myFile;
	private String myRoot;
	private HTTPInputStream inputHTTPReq;
	private HTTPInputStream inputHTTPrep;
	private HTTPRequest myRequest;
	private HTTPReply currentReply;
	private HTTPReply myReply;
	private HTTPFactory myFactory;

	/**
	 * Creates an HTTPFactory object for general object management, calls the method
	 * setfile(), for creating files to test requests, and then creates an
	 * HTTPHandler1_1 to handle requests.
	 */
	@Before
	public void setUp() throws Exception {
		myFactory = new MyHTTPFactory();
		setfile();
		myHandler1_1 = myFactory.getFileSystemHandler1_1(myFile);
	}

	/**
	 * Creates the path of the files and for each calls the method createFile() to
	 * create them.
	 */
	public void setfile() throws IOException {
		myRoot = new File("").getAbsolutePath();
		myFile = new File(myRoot);
		File get = new File(myRoot + "/TestGetRequest.html");
		File head = new File(myRoot + "/TestHeadRequest.html");
		File post = new File(myRoot + "/TestPostRequest.html");
		File delete = new File(myRoot + "/TestDeleteRequest.html");
		File putnew = new File(myRoot + "/TestPutNewFileRequest.html");
		File putexist = new File(myRoot + "/TestPutExistsFileRequest.html");
		if (!get.exists()) {
			createFile(get, "Test Get Request");
		}
		if (!head.exists()) {
			createFile(head, "Test Head Request");
		}
		if (!post.exists()) {
			createFile(post, "Test Post Request");
		}
		if (!delete.exists()) {
			createFile(delete, "Test Delete Request");
		}
		if (putnew.exists()) {
			putnew.delete();
		}
		if (!putexist.exists()) {
			createFile(putexist, "Test PutExist Request");
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
	public void createFile(File newFile, String text) throws IOException {
		newFile.createNewFile();
		FileWriter writeFile = new FileWriter(newFile);
		writeFile.write(text);
		writeFile.flush();
		writeFile.close();
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
		inputHTTPrep = myFactory.getHTTPInputStream(inputRep);
		myReply = inputHTTPrep.readHttpReply();
		inputHTTPrep.close();
	}

	/**
	 * Creates a requested object, a response(waiting) object, and a response object
	 * on the request, by calling the methods(newRequestGet(), newReplyGet(),
	 * handle(request)), and then tests the value of the two response objects with
	 * the control() method.
	 */
	@Test
	public void testReplyGet() throws IOException {
		newRequestGet();
		newReplyGet();
		currentReply = myHandler1_1.handle(myRequest);
		control();
	}

	/**
	 * Reads a requestGet from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestGet() throws IOException {
		byte[] inputStreamRequest = "GET /TestGetRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyGet from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyGet() throws IOException {
		byte[] inputStreamReply = "HTTP/1.1 200 OK\r\nConnection: Close\r\nContent-Type: text/HTML\r\nContent-Length: 16\r\n\r\nTest Get Request"
				.getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Creates a requested object, a response(waiting) object, and a response object
	 * on the request, by calling the methods(newRequestHead(), newReplyHead(),
	 * handle(request)), and then tests the value of the two response objects with
	 * the control() method.
	 */
	@Test
	public void testReplyHead() throws IOException {
		newRequestHead();
		newReplyHead();
		currentReply = myHandler1_1.handle(myRequest);
		control();
	}

	/**
	 * Reads a requestHead from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestHead() throws IOException {
		byte[] inputStreamRequest = "HEAD /TestHeadRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyHead from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyHead() throws IOException {
		byte[] inputStreamReply = "HTTP/1.1 200 OK\r\nConnection: Close\r\nContent-Type: text/HTML\r\nContent-Length: 16\r\n\r\n"
				.getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Creates a requested object, a response(waiting) object, and a response object
	 * on the request, by calling the methods(newRequestPost(), newReplyPost(),
	 * handle(request)), and then tests the value of the two response objects with
	 * the control() method.
	 */
	@Test
	public void testReplyPost() throws IOException {
		newRequestPost();
		newReplyPost();
		currentReply = myHandler1_1.handle(myRequest);
		control();
	}

	/**
	 * Reads a requestPost from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestPost() throws IOException {
		byte[] inputStreamRequest = "POST /TestPostRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\nOK"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyPost from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyPost() throws IOException {
		byte[] inputStreamReply = "HTTP/1.1 200 OK\r\nConnection: Close\r\nContent-Type: text/HTML\r\nContent-Length: 20\r\n\r\n"
				.getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Creates a requested object, a response(waiting) object, and a response object
	 * on the request, by calling the methods(newRequestDelete(), newReplyDelete(),
	 * handle(request)), and then tests the value of the two response objects with
	 * the control() method.
	 */
	@Test
	public void testReplyDelete() throws IOException {
		newRequestDelete();
		newReplyDelete();
		currentReply = myHandler1_1.handle(myRequest);
		control();
		File delete = new File(myRoot + "/TestDeleteRequest.html");
		assertEquals(false, delete.exists());
	}

	/**
	 * Reads a requestDelete from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestDelete() throws IOException {
		byte[] inputStreamRequest = "DELETE /TestDeleteRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyDelete from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyDelete() throws IOException {
		byte[] inputStreamReply = "HTTP/1.1 200 OK\r\nConnection: Close\r\n\r\n".getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Creates a requested object, a response(waiting) object, and a response object
	 * on the request, by calling the methods(newRequestPutExists()/newRequestPut(),
	 * newReplyPutExists()/newRequestPut(), handle(request)), and then tests the
	 * value of the two response objects with the control() method.
	 */
	@Test
	public void testReplyPut() throws IOException {
		newRequestPutExists();
		newReplyPutExists();
		currentReply = myHandler1_1.handle(myRequest);
		control();
		newRequestPut();
		newReplyPut();
		currentReply = myHandler1_1.handle(myRequest);
		control();
	}

	/**
	 * Reads a requestPutExists from a basic string and recall the method to create
	 * the requested object.
	 */
	public void newRequestPutExists() throws IOException {
		byte[] inputStreamRequest = "PUT /TestPutExistsFileRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\nTest Put Request"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyPutExist from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyPutExists() throws IOException {
		byte[] inputStreamReply = "HTTP/1.1 200 OK\r\nConnection: Close\r\nContent-Type: text/HTML\r\nContent-Length: 16\r\n\r\n"
				.getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Reads a requestPut from a basic string and recall the method to create the
	 * requested object.
	 */
	public void newRequestPut() throws IOException {
		byte[] inputStreamRequest = "PUT /TestPutNewFileRequest.html HTTP/1.1\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\nTest Put Request File Nuovo"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Reads a replyPut from a basic string and recall the method to create the
	 * reply object.
	 */
	public void newReplyPut() throws IOException {
		byte[] inputStreamReply = "HTTP/1.1 201 Created\r\nConnection: Close\r\nContent-Type: text/HTML\r\nContent-Length: 27\r\n\r\n"
				.getBytes();
		setInputStreamReply(inputStreamReply);
	}

	/**
	 * Creates a requested object and a response object on the request, by calling
	 * the methods(newRequestError(), handle(request)), and then test the value of
	 * the response object with the null value.
	 */
	@Test
	public void testErrorVersion() throws IOException {
		newRequestError();
		currentReply = myHandler1_1.handle(myRequest);
		assertEquals(null, currentReply);
	}

	/**
	 * Reads a requestError from a basic string and recall the method to create the
	 * requested object.
	 */
	private void newRequestError() throws IOException {
		byte[] inputStreamRequest = "GET /TestGetRequest.html HTTP/1.0\r\nHost: Mio.PC\r\nConnection: Close\r\n\r\n"
				.getBytes();
		setInputStreamRequest(inputStreamRequest);
	}

	/**
	 * Checks the parameters of the two replies.
	 */
	public void control() {
		assertEquals(myReply.getVersion(), currentReply.getVersion());
		assertEquals(myReply.getStatusCode(), currentReply.getStatusCode());
		assertEquals(myReply.getStatusMessage(), currentReply.getStatusMessage());
		assertEquals(myReply.getData(), currentReply.getData());
	}
}
