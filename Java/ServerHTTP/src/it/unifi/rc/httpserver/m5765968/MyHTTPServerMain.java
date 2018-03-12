package it.unifi.rc.httpserver.m5765968;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import it.unifi.rc.httpserver.HTTPFactory;
import it.unifi.rc.httpserver.HTTPHandler;
import it.unifi.rc.httpserver.HTTPServer;

/**
 * This class starts the operation of an HTTPServer object.
 * 
 * @author Mattia D'Autilia
 */
public class MyHTTPServerMain {

	private static HTTPHandler handler1_0;
	private static HTTPHandler handler1_1;
	private static HTTPHandler handlerHost1_0;
	private static HTTPHandler handlerHost1_1;
	private static InetAddress myHost;
	private static HTTPFactory myFactory;
	private static HTTPServer myServer;
	private static String myRoot;
	private static File myFile;

	public static void main(String[] args) throws IOException {
		setUpServer();
		try {
			myServer.start();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		myServer.stop();
	}

	/**
	 * Creates a factory object, calls the newHandler() method to create the various
	 * handlers and setFile() method to create the files to test the server on and
	 * creates an HTTPServer.
	 */
	public static void setUpServer() throws IOException {
		myFactory = new MyHTTPFactory();
		setFile();
		newHandler();
		myHost = InetAddress.getByName("localhost");
		myServer = myFactory.getHTTPServer(12000, 50, myHost, handler1_1, handlerHost1_0, handlerHost1_1, handler1_0);
	}

	/**
	 * Creates the path of the files and for each calls the method createFile() to
	 * create them.
	 */
	public static void setFile() throws IOException {
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
			createFile(putexist, "Test Put Exists File Request");
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
	public static void createFile(File newFile, String text) throws IOException {
		newFile.createNewFile();
		FileWriter writeFile = new FileWriter(newFile);
		writeFile.write(text);
		writeFile.flush();
		writeFile.close();
	}

	/**
	 * Creates the various handlers that will manage the request in the server.
	 */
	public static void newHandler() {
		String host = "Mio.PC";
		handler1_0 = myFactory.getFileSystemHandler1_0(myFile);
		handler1_1 = myFactory.getFileSystemHandler1_1(myFile);
		handlerHost1_0 = myFactory.getFileSystemHandler1_0(host, myFile);
		handlerHost1_1 = myFactory.getFileSystemHandler1_1(host, myFile);
	}
}
