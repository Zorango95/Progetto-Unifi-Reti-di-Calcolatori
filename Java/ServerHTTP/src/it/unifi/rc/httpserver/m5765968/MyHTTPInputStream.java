package it.unifi.rc.httpserver.m5765968;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import it.unifi.rc.httpserver.HTTPInputStream;
import it.unifi.rc.httpserver.HTTPProtocolException;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class extends the HTTPInputStream class with the task of managing the
 * reading of a Reply and Request object.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPInputStream extends HTTPInputStream {

	private InputStream myIs;
	private Scanner inputHTTP;

	/**
	 * Creates an HTTPInputStream object taking an InputStream as a parameter.
	 * 
	 * @param is
	 *            InputStream object that used to retrieves data.
	 */
	public MyHTTPInputStream(InputStream is) {
		super(is);
	}

	@Override
	protected void setInputStream(InputStream is) {
		this.myIs = is;
	}

	@Override
	public HTTPRequest readHttpRequest() throws HTTPProtocolException {
		inputHTTP = new Scanner(myIs);
		String myMethod = getMethod();
		String myUrl = getUrl();
		String myVersion = getVersionRequest();
		Map<String, String> myParameters = getParameters();
		String myBody = getBody();
		MyHTTPRequest myRequest = new MyHTTPRequest(myMethod, myUrl, myVersion, myParameters, myBody);
		return myRequest;
	}

	/**
	 * Reads the field containing the Method from the InputStream.
	 * 
	 * @return string Method read.
	 */
	public String getMethod() {
		String method = inputHTTP.next();
		return method;
	}

	/**
	 * Reads the field containing the Path from the InputStream.
	 * 
	 * @return string Path read.
	 */
	public String getUrl() {
		String url = inputHTTP.next();
		return url;
	}

	/**
	 * Reads the field containing the Version from the InputStream.
	 * 
	 * @return string Version read.
	 */
	public String getVersionRequest() {
		String version = inputHTTP.nextLine();
		Scanner controlVersion = new Scanner(version);
		String currentVersion = controlVersion.next();
		controlVersion.close();
		return currentVersion;
	}

	/**
	 * Reads the field containing the EntityBody from the InputStream.
	 * 
	 * @return string EntityBody read.
	 */
	public String getBody() {
		String body = "";
		while (inputHTTP.hasNext()) {
			body = body + inputHTTP.nextLine();
		}
		return body;
	}

	@Override
	public HTTPReply readHttpReply() throws HTTPProtocolException {
		inputHTTP = new Scanner(myIs);
		String myVersion = getVersionReply();
		String myStatusCode = getStatusCode();
		String myStatusMessage = getStatusMessage();
		Map<String, String> myParameters = getParameters();
		String myData = getData();
		MyHTTPReply myReply = new MyHTTPReply(myVersion, myStatusCode, myStatusMessage, myParameters, myData);
		return myReply;
	}

	/**
	 * Reads the field containing the Version from the InputStream.
	 * 
	 * @return string Version read.
	 */
	public String getVersionReply() {
		String version = inputHTTP.next();
		return version;
	}

	/**
	 * Reads the field containing the StatusCode from the InputStream.
	 * 
	 * @return string StatusCode read.
	 */
	public String getStatusCode() {
		String statusCode = inputHTTP.next();
		return statusCode;
	}

	/**
	 * Reads the field containing the StatusMessage from the InputStream.
	 * 
	 * @return string StatusMessage read.
	 */
	public String getStatusMessage() {
		String message = inputHTTP.nextLine();
		Scanner controlMessage = new Scanner(message);
		String statusMessage = controlMessage.next();
		;
		while (controlMessage.hasNext()) {
			statusMessage = statusMessage + " " + controlMessage.next();
		}
		controlMessage.close();
		return statusMessage;
	}

	/**
	 * Reads the field containing the Data from the InputStream.
	 * 
	 * @return string Data read.
	 */
	public String getData() {
		String data = "";
		while (inputHTTP.hasNext()) {
			data = data + inputHTTP.nextLine();
		}
		return data;
	}

	/**
	 * Reads the fields containing the values ​​of the various header lines from the
	 * InputStream.
	 * 
	 * @return Map containing the header lines read.
	 */
	public Map<String, String> getParameters() {
		Map<String, String> parameters = new TreeMap<String, String>();
		while (inputHTTP.hasNextLine()) {
			String headerLine = inputHTTP.nextLine();
			if (headerLine.length() != 0) {
				Scanner controlHeaderLine = new Scanner(headerLine);
				String keyField = controlHeaderLine.next();
				String valueField = controlHeaderLine.nextLine();
				parameters.put(keyField, valueField);
				controlHeaderLine.close();
			} else {
				break;
			}
		}
		return parameters;
	}

	@Override
	public void close() throws IOException {
		myIs.close();
	}
}