package it.unifi.rc.httpserver.m5765968.Test;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import it.unifi.rc.httpserver.HTTPRequest;
import it.unifi.rc.httpserver.m5765968.MyHTTPRequest;

/**
 * This class tests the creation of a request and its methods.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPRequestTest {

	private HTTPRequest myRequest;
	private Map<String, String> par;

	/**
	 * Creates a Request object to test it.
	 */
	@Before
	public void setUp() throws Exception {
		par = new TreeMap<String, String>();
		par.put("Server", "Apache");
		myRequest = new MyHTTPRequest("GET", "/cgil/form.txt", "HTTP/1.0", par, "Ciao mamma");
	}

	/**
	 * Tests the return of the Method.
	 */
	@Test
	public void testMethod() {
		assertEquals("GET", myRequest.getMethod());
	}

	/**
	 * Tests the return of the Path.
	 */
	@Test
	public void testPath() {
		assertEquals("/cgil/form.txt", myRequest.getPath());
	}

	/**
	 * Tests the return of the Version.
	 */
	@Test
	public void testVersion() {
		assertEquals("HTTP/1.0", myRequest.getVersion());
	}

	/**
	 * Tests the return of the Parameter.
	 */
	@Test
	public void testParameters() {
		assertEquals(par, myRequest.getParameters());
	}

	/**
	 * Tests the return of the Body.
	 */
	@Test
	public void testBody() {
		assertEquals("Ciao mamma", myRequest.getEntityBody());
	}

}
