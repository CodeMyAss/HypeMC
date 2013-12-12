package me.loogeh.Hype.cpanel;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class CPanel {
	
	public void init() {
		ServerSocket serverSocket = null;
		DataInputStream is;
		PrintStream os;
		String line;
		Socket clientSocket = null;
		
		try {
			serverSocket = new ServerSocket(25535, 100);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			clientSocket = serverSocket.accept();
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			
			while(true) {
				line = is.readUTF();
				os.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
