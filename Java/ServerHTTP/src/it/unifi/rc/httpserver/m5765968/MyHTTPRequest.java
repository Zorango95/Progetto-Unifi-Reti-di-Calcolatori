package it.unifi.rc.httpserver.m5765968;

import java.util.Map;

import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class implements the HTTPRequest interface and has the task of setting
 * the elements of an HTTP request.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPRequest implements HTTPRequest {

	String myMethod;
	String myUrl;
	String myVersion;
	Map<String, String> myParameters;
	String myBody;

	/**
	 * This constructor creates a request made up of a method, a path, an HTTP
	 * version, a series of parameters and a body.
	 * 
	 * @param method
	 *            Indicates the operation to be performed on the resource identified
	 *            by the Request-URI.
	 * @param url
	 *            Indicates the resource to apply the request to.
	 * @param version
	 *            Indicates the version of the protocol used to agree on the format
	 *            of the message.
	 * @param parameters
	 *            Indicate information about the request.
	 * @param body
	 *            Indicates the data to be transmitted: input of programs or
	 *            information to be saved on the recipient server on behalf of the
	 *            client.
	 */
	public MyHTTPRequest(String method, String url, String version, Map<String, String> parameters, String body) {
		this.myMethod = method;
		this.myUrl = url;
		this.myVersion = version;
		this.myParameters = parameters;
		this.myBody = body;
	}

	@Override
	public String getVersion() {
		return myVersion;
	}

	@Override
	public String getMethod() {
		return myMethod;
	}

	@Override
	public String getPath() {
		return myUrl;
	}

	@Override
	public String getEntityBody() {
		return myBody;
	}

	@Override
	public Map<String, String> getParameters() {
		return myParameters;
	}

}
