package it.unifi.rc.httpserver.m5765968;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import it.unifi.rc.httpserver.HTTPOutputStream;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class extends the HTTPOutputStream class with the task of managing the
 * writing of a Reply and Request object.
 *
 * @author Mattia D'Autilia
 */
public class MyHTTPOutputStream extends HTTPOutputStream {

	private OutputStream myOs;
	private PrintWriter outputHTTP;
	private String myRequest;
	private String myReply;

	/**
	 * Creates an HTTPOutputStream object taking an OutputStream as a parameter and
	 * initialize the Request and Reply strings.
	 * 
	 * @param os
	 *            OutputStream object used to manage outgoing data.
	 */
	public MyHTTPOutputStream(OutputStream os) {
		super(os);
		this.myRequest = "";
		this.myReply = "";
	}

	@Override
	protected void setOutputStream(OutputStream os) {
		myOs = os;
	}

	@Override
	public void writeHttpReply(HTTPReply reply) {
		outputHTTP = new PrintWriter(myOs);
		String myVersion = reply.getVersion() + " ";
		String myStatusCode = reply.getStatusCode() + " ";
		String myMessageCode = reply.getStatusMessage() + "\r\n";
		String myParameters = "";
		if (!reply.getParameters().isEmpty()) {
			for (Map.Entry<String, String> myEntry : reply.getParameters().entrySet()) {
				myParameters = myParameters + myEntry.getKey() + myEntry.getValue() + "\r\n";
			}
		}
		String myLine = "\r\n";
		String myData = reply.getData();
		myReply = myVersion + myStatusCode + myMessageCode + myParameters + myLine + myData;
		outputHTTP.print(myReply);
		outputHTTP.flush();
	}

	@Override
	public void writeHttpRequest(HTTPRequest request) {
		outputHTTP = new PrintWriter(myOs);
		String myMethod = request.getMethod() + " ";
		String myUrl = request.getPath() + " ";
		String myVersion = request.getVersion() + "\r\n";
		String myParameters = "";
		if (!request.getParameters().isEmpty()) {
			for (Map.Entry<String, String> myEntry : request.getParameters().entrySet()) {
				myParameters = myParameters + myEntry.getKey() + myEntry.getValue() + "\r\n";
			}
		}
		String myLine = "\r\n";
		String myEntitBody = request.getEntityBody();
		myRequest = myMethod + myUrl + myVersion + myParameters + myLine + myEntitBody;
		outputHTTP.print(myRequest);
		outputHTTP.flush();
	}

	@Override
	public void close() throws IOException {
		myOs.close();
	}
}
