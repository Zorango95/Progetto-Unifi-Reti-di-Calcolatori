package it.unifi.rc.httpserver.m5765968;

import java.util.Map;

import it.unifi.rc.httpserver.HTTPReply;

/**
 * This class implements the HTTPReply interface and has the task of setting the
 * elements of an HTTP Reply.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPReply implements HTTPReply {

	String myVersion;
	String myStatusCode;
	String myStatusMessage;
	Map<String, String> myParameters;
	String myData;

	/**
	 * Creates a reply made up of a version, a statusCode, an statusMessage, a
	 * series of parameters and a data.
	 * 
	 * @param version
	 *            Indicates the version of the protocol used to agree on the format
	 *            of the message
	 * @param statusCode
	 *            Indicates the result of the receipt of the request with a
	 *            three-digit code
	 * @param statusMessage
	 *            Indicates a small description of the meaning of the code intended
	 *            for human use
	 * @param parameters
	 *            Indicate information about the request
	 * @param data
	 *            Indicates the data requested by the client: the hypertext
	 *            documents
	 */
	public MyHTTPReply(String version, String statusCode, String statusMessage, Map<String, String> parameters,
			String data) {
		this.myVersion = version;
		this.myStatusCode = statusCode;
		this.myStatusMessage = statusMessage;
		this.myParameters = parameters;
		this.myData = data;
	}

	@Override
	public String getVersion() {
		return myVersion;
	}

	@Override
	public String getStatusCode() {
		return myStatusCode;
	}

	@Override
	public String getStatusMessage() {
		return myStatusMessage;
	}

	@Override
	public String getData() {
		return myData;
	}

	@Override
	public Map<String, String> getParameters() {
		return myParameters;
	}

}
