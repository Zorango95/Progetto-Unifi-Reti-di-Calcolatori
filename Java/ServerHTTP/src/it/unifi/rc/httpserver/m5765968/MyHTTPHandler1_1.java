package it.unifi.rc.httpserver.m5765968;

import java.io.File;
import java.io.IOException;

import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class extends the HTTPHandler class 1_0 with the task of managing a
 * Request HTTP/1.1 version as if it were an HTTP/1.0 version request for
 * methods (GET, HEAD, POST). Instead for requests with methods (PUT, DELETE)
 * implements new methods. In both cases create a response of the same version
 * otherwise it creates a null response.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPHandler1_1 extends MyHTTPHandler1_0 {

	/**
	 * Creates an HTTP/1.1 request handler, on files searched from root path.
	 * 
	 * @param root
	 *            Indicates the path from where the file search starts.
	 */
	public MyHTTPHandler1_1(File root) {
		super(root);
		super.setMyVersion("HTTP/1.1");
	}

	public HTTPReply handle(HTTPRequest request) {
		super.myRequest = request;
		super.setPath();
		if (request.getVersion().equals("HTTP/1.1") && request.getParameters().containsKey("Host:")) {
			String currentMethod = request.getMethod();
			if (currentMethod.equals("GET") || currentMethod.equals("HEAD") || currentMethod.equals("POST")) {
				return super.myReply = super.handle(request);
			} else if (currentMethod.equals("PUT")) {
				handlePut();
			} else if (currentMethod.equals("DELETE")) {
				handleDelete();
			} else {
				super.setBadRequest();
			}
			return super.myReply = new MyHTTPReply(myVersion, super.myStatusCode, super.myStatusMessage,
					super.myParameters, super.myData);
		} else {
			return super.myReply = null;
		}
	}

	/**
	 * Manages the deletion of a file, first checking its existence.
	 */
	private void handleDelete() {
		if (super.currentRoot.exists()) {
			if (super.currentRoot.isFile()) {
				super.currentRoot.delete();
				super.setOkRequest();
				super.myParameters.put("Connection:", " Close");
			} else {
				super.setNoFile();
			}
		} else {
			super.setNotFound();
		}
	}

	/**
	 * Checks the existence or otherwise of the file, on which you have to write.
	 */
	public void handlePut() {
		if (super.currentRoot.exists() && super.currentRoot.isFile()) {
			try {
				setFilePut(super.currentRoot);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				setFileNotExists(super.currentRoot);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Manages writing to an existing file.
	 * 
	 * @param file
	 *            Indicates the path of the file referenced by the request.
	 */
	private void setFilePut(File file) throws IOException {
		super.setOkRequest();
		super.setFile("", file);
		super.setMyParameters(file);
	}

	/**
	 * Manages writing to a new file.
	 * 
	 * @param file
	 *            Indicates the path of the file referenced by the request.
	 */
	private void setFileNotExists(File file) throws IOException {
		super.myStatusCode = "201";
		super.myStatusMessage = "Created";
		File newFile = new File(file.getPath());
		newFile.createNewFile();
		super.setFile("", newFile);
		super.setMyParameters(newFile);
	}

}
