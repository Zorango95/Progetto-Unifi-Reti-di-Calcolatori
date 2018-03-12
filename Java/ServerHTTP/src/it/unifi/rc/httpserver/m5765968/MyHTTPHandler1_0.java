package it.unifi.rc.httpserver.m5765968;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPReply;
import it.unifi.rc.httpserver.HTTPRequest;

/**
 * This class implements the HTTPHandler interface with the task of managing an
 * HTTP/1.0 version request, creating a response of the same version otherwise
 * it creates a null response.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPHandler1_0 implements HTTPHandler {

	File myRoot;
	HTTPReply myReply;
	File currentRoot;
	String myVersion;
	String myStatusCode;
	String myStatusMessage;
	Map<String, String> myParameters;
	String myData;
	HTTPRequest myRequest;
	int countLength;

	/**
	 * Creates an HTTP/1.0 request handler, on files searched from root path.
	 * 
	 * @param root
	 *            Indicates the path from where the file search starts.
	 */
	public MyHTTPHandler1_0(File root) {
		this.myRoot = root;
		setMyVersion("HTTP/1.0");
		this.myParameters = new TreeMap<String, String>();
		this.myData = "";
	}

	@Override
	public HTTPReply handle(HTTPRequest request) {
		myRequest = request;
		setPath();
		if (request.getVersion().equals(myVersion)) {
			existsVersion(currentRoot);
		} else {
			return myReply = null;
		}
		return myReply = new MyHTTPReply(myVersion, myStatusCode, myStatusMessage, myParameters, myData);
	}

	/**
	 * Checks if the file exists, otherwise call the setNotFound() method.
	 * 
	 * @param currentRoot
	 *            Indicates the path of the file referenced by the request.
	 */
	private void existsVersion(File currentRoot) {
		if (currentRoot.exists()) {
			foundFile(currentRoot);
		} else {
			setNotFound();
		}
	}

	/**
	 * Checks if the path refers to a file, otherwise call the setNoFile() method.
	 * 
	 * @param currentRoot
	 *            Indicates the path of the file referenced by the request.
	 */
	private void foundFile(File currentRoot) {
		if (currentRoot.isFile()) {
			okFile(currentRoot);
		} else {
			setNoFile();
		}
	}

	/**
	 * Checks if the request method matches one of the HTTP/1.0 version methods
	 * (GET, HEAD, POST) or one of the HTTP/1.1 version methods (PUT, DELETE),
	 * otherwise call the setBadRequest() method.
	 * 
	 * @param currentRoot
	 *            Indicates the path of the file referenced by the request.
	 */
	private void okFile(File currentRoot) {
		setOkRequest();
		String currentMethod = myRequest.getMethod();
		if (currentMethod.equals("GET") || currentMethod.equals("HEAD") || currentMethod.equals("POST")) {
			try {
				handlerReply(currentRoot, currentMethod);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setBadRequest();
		}
	}

	/**
	 * Manages the creation of the response based on the method and file indicated
	 * on the request.
	 * 
	 * @param file
	 *            Indicates the path of the file referenced by the request.
	 * @param currentMethod
	 *            Indicates the method of the request.
	 */
	private void handlerReply(File file, String currentMethod) throws IOException {
		FileReader fileRead = new FileReader(file.getPath());
		BufferedReader bufRead = new BufferedReader(fileRead);
		countLength = 0;
		String readFile = getReadFile(bufRead);
		bufRead.close();
		if (currentMethod.equals("POST")) {
			setFile(readFile, file);
		}
		setMyParameters(file);
	}

	/**
	 * Reads the text of the file, managing it according to the request method.
	 * 
	 * @param currentBufRead
	 *            Indicates the BufferedReader class for reading the file.
	 * @return string currentReadFile content file read (POST).
	 */
	private String getReadFile(BufferedReader currentBufRead) throws IOException {
		myData = "";
		String currentReadFile = "";
		String lineFile = currentBufRead.readLine();
		while (lineFile != null) {
			countLength = countLength + lineFile.length();
			if (myRequest.getMethod().equals("GET")) {
				myData = myData + lineFile;
			} else if (myRequest.getMethod().equals("POST")) {
				currentReadFile = currentReadFile + lineFile;
			}
			lineFile = currentBufRead.readLine();
		}
		return currentReadFile + " ";
	}

	/**
	 * Manages the writing on the file, of a text formed from the previous text
	 * linked to that inserted in the request with the POST method.
	 * 
	 * @param currentReadFile
	 *            Indicates the text of the read file.
	 * @param currentFile
	 *            Indicates the path of the file referenced by the request.
	 */
	public void setFile(String currentReadFile, File currentFile) throws IOException {
		currentReadFile = currentReadFile + myRequest.getEntityBody();
		countLength = currentReadFile.length();
		FileWriter fileWrite = new FileWriter(currentFile.getPath());
		BufferedWriter bufWrite = new BufferedWriter(fileWrite);
		bufWrite.write(currentReadFile);
		bufWrite.flush();
		bufWrite.close();
	}

	/**
	 * Manages the map by inserting the various parameters that will form the header
	 * lines of the reply.
	 * 
	 * @param fileSet
	 *            Indicates the path of the file referenced by the request.
	 */
	public void setMyParameters(File fileSet) {
		myParameters.put("Last-Modified:", " " + getDataLastMod(fileSet));
		myParameters.put("Content-Length:", " " + getLengthFile(countLength));
		myParameters.put("Location: ", fileSet.getPath());
		myParameters.put("Content-Type:", " text/HTML");
		setConnection();
		myParameters.put("Date:", " " + getDataOperation(fileSet));
	}

	/**
	 * Gets the date of the last modification of the file.
	 * 
	 * @param fileData
	 *            Indicates the path of the file referenced by the request.
	 * @return string dataMod containing the date of the last modification of the
	 *         file.
	 */
	private String getDataLastMod(File fileData) {
		long lastMod = fileData.lastModified();
		Date dataFile = new Date(lastMod);
		SimpleDateFormat dateFormatFile = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		String dataMod = dateFormatFile.format(dataFile);
		return dataMod;
	}

	/**
	 * Gets the length of the text of the file.
	 * 
	 * @param lengthFile
	 *            Indicates the path of the file referenced by the request.
	 * @return string length containing the length of the text of the file
	 */
	private String getLengthFile(int lengthFile) {
		String length = String.valueOf(lengthFile);
		return length;
	}

	/**
	 * Gets the date of the managed request.
	 * 
	 * @param fileData
	 *            Indicates the path of the file referenced by the request.
	 * @return string dataPc containing the date of the managed request.
	 */
	private String getDataOperation(File fileData) {
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormatPc = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		String dataPc = dateFormatPc.format(data);
		return dataPc;
	}

	/**
	 * Manages the status of a successful request.
	 */
	public void setOkRequest() {
		myStatusCode = "200";
		myStatusMessage = "OK";
		myParameters = new TreeMap<String, String>();
		myData = "";
	}

	/**
	 * Manages the status of a request with incorrect method.
	 */
	public void setBadRequest() {
		myStatusCode = "405";
		myStatusMessage = "Method Not Allowed";
		myParameters = new TreeMap<String, String>();
		setConnection();
		myData = "";
	}

	/**
	 * Manages the status of a request with path that does not refer to a file.
	 */
	public void setNoFile() {
		myStatusCode = "403";
		myStatusMessage = "Directory listing not supported";
		myParameters = new TreeMap<String, String>();
		setConnection();
		myData = "";
	}

	/**
	 * Manages the status of a request with path path of a file not found.
	 */
	public void setNotFound() {
		myStatusCode = "404";
		myStatusMessage = "Not Found";
		myParameters = new TreeMap<String, String>();
		setConnection();
		myData = "";
	}

	/**
	 * Builds the path of the file.
	 */
	public void setPath() {
		String pathFile = myRoot.getPath() + myRequest.getPath();
		currentRoot = new File(pathFile);
	}

	/**
	 * Assigns the right version for the reply.
	 * 
	 * @param version
	 *            Indicates the version of the reply (HTTP/1.0 or HTTP/1.1)
	 */
	public void setMyVersion(String version) {
		myVersion = version;
	}

	public void setConnection() {
		if (myRequest.getVersion().equals("HTTP/1.1")) {
			if (myRequest.getParameters().containsKey("Connection:")
					&& myRequest.getParameters().get("Connection:").equals(" Close")) {
				myParameters.put("Connection:", " Close");
			} else {
				myParameters.put("Connection:", " Keep-alive");
			}
		} else {
			if (myRequest.getParameters().containsKey("Connection:")
					&& myRequest.getParameters().get("Connection:").equals(" Keep-alive")) {
				myParameters.put("Connection:", " Keep-alive");
			} else {
				myParameters.put("Connection:", " Close");
			}
		}
	}
}