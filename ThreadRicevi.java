package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;k
import java.net.Socket;


public class ThreadRicevi implements Runnable{
	
	private Socket socket;
	BufferedReader in;
	
	private ThreadRicevi(Socket socket) throws IOException{
		this.socket = socket;
		in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
	}
	
	public void run() {
		
		String file; // da cambiare in file
		try {
			file = in.readLine();
			while(file!= null) {
				System.out.println(file);
				file = in.readLine();
			}
			System.out.println("Server chiuso");
			socket.close();
		}catch (IOException e) {
			System.out.println("Errore di connesione"); // si presuppone 
		}
		
	}

}
