package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ThreadInvio implements Runnable {
	
	private Scanner sc;
	private PrintWriter out;
	
	public ThreadInvio(Socket socket) throws IOException{
		sc = new Scanner(System.in);
		out = new PrintWriter(socket.getOutputStream());
	}
	
	public void run() {
		
		String file; //poi capiremo come modificare il messaggio in file
		boolean primo = true; // se è la prima volta che l'utente scrive deve inserire il suo nick
		// possibile implementazione del json
		while(!Thread.interrupted()) {
			if(primo) {
				System.out.println("Scrivi il tuo nick");
			}
			
			file = sc.nextLine(); // va cambiato per riuscire a prendere un file da locale
			out.println(file);
			out.flush(); //pulisceil printwriter
			if(primo) {
				System.out.println("Nick acquisito, seleziona il file");
				primo = false;
			}
			}
		
	}

}
