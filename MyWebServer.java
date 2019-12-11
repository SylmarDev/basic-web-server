// import packages and classes
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Date;

public class MyWebServer {
	protected final File WEB_DIR = new File("./web_root"); 
	protected final int PORT_NUM = 8080; 
	
	public static void main(String[] args) { 
		new MyWebServer(); 
	}

	public MyWebServer() {
		try {
			BufferedReader bufferedReader0;
			PrintWriter printWriter0; 
			BufferedOutputStream bufferedOutputStream0; 
			Socket clientSocket0; 
			ServerSocket serverSocket0 = new ServerSocket(PORT_NUM); 
			serverSocket0.setReuseAddress(true);
			System.out.println("Listening ...");

			while(true) {
				clientSocket0 = serverSocket0.accept();
				System.out.println(clientSocket0);
				bufferedReader0 = new BufferedReader(new InputStreamReader(clientSocket0.getInputStream()));
				printWriter0 = new PrintWriter(clientSocket0.getOutputStream());
				bufferedOutputStream0 = new BufferedOutputStream(clientSocket0.getOutputStream()); 

				String rqstString = bufferedReader0.readLine();
				
				System.out.println(rqstString);
				StringTokenizer stringTokenizer0 = new StringTokenizer(rqstString);
				String method = stringTokenizer0.nextToken();
				// System.out.println("method string: " + method);
				// Extract requested file name from the request
				// (tokenizer method wasn't working so here we are)
				switch (rqstString) {
					case "GET / HTTP/1.1": 
						method = "index.html";
						// System.out.println("CASE 1, method string: " + method);
						break;
					case "GET /next.html HTTP/1.1": 
						method = "next.html"; 
						// System.out.println("CASE 2, method string: " + method);
						break;
					case "GET /exit.html HTTP/1.1":
						method = "exit.html";
						break;
					default: 
						method = "index.html"; 
						// System.out.println("CASE DEF, method string: " + method);
						break;
				}
				if (method.equals("exit.html")) {
					System.out.println("exit.html requested, exiting. . .");
					break;
				}
				File file0 = new File(WEB_DIR, method);
				int fileSize = (int)file0.length(); 
				System.out.println("file0: " + file0.toString() + " length: " + Integer.toString(fileSize));

				// read file data
				byte[] fileContent = readFileData(file0, fileSize);

				// send HTTP Headers of the response
				// the first header is completed for you
				
				// send content of requested file 
				printWriter0.println("HTTP/1.1 200 OK");
				printWriter0.println("Server: Java Web Server"); // ???
				printWriter0.println("Date: " + new Date());
				printWriter0.println("Content-type: " + file0.toString());
				printWriter0.println("Content-length: " + Integer.toString(fileSize));
				// a blank line after headers
				printWriter0.println();
				printWriter0.flush();
				
				bufferedOutputStream0.write(fileContent);
				bufferedOutputStream0.flush();
				
				bufferedReader0.close();
				printWriter0.close();
				bufferedOutputStream0.close();
				// close the client socket 
				clientSocket0.close();
			}
			// close the server socket 
			serverSocket0.close();
		} catch (Exception e) {
			System.out.println(e); 
		}
	}
	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
}
