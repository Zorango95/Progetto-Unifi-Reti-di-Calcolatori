package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.m5765968.MyHTTPReply;

/**
 * This class tests the creation of a reply and its methods.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPReplyTest {

	private HTTPReply myReply;
	private Map<String, String> par;

	/**
	 * Creates a Reply object to test it.
	 */
	@Before
	public void setUp() throws Exception {
		par = new TreeMap<String, String>();
		par.put("Server", "Apache");
		myReply = new MyHTTPReply("HTTP/1.0", "404", "Not Found", par, "Ciao mamma");
	}

	/**
	 * Tests the return of the Version.
	 */
	@Test
	public void testVersion() {
		assertEquals("HTTP/1.0", myReply.getVersion());
	}

	/**
	 * Tests the return of the StatusCode.
	 */
	@Test
	public void testStatusCode() {
		assertEquals("404", myReply.getStatusCode());
	}

	/**
	 * Tests the return of the StatusMessage.
	 */
	@Test
	public void testStatusMessage() {
		assertEquals("Not Found", myReply.getStatusMessage());
	}

	/**
	 * Tests the return of the Parameters.
	 */
	@Test
	public void testParameters() {
		assertEquals(par, myReply.getParameters());
	}

	/**
	 * Tests the return of the Data.
	 */
	@Test
	public void testData() {
		assertEquals("Ciao mamma", myReply.getData());
	}
}
