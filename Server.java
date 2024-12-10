package Telegram;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
	
	
	public static final int CLIENT_DELAY = 200;

	public static void attendi(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	
	static class NClients {
		int i = 0;
		
		synchronized void inc() {
			i++;
		}
		
		synchronized void dec() {
			i--;
		}
		
		synchronized int get() {
			return i;
		}
	}
	
	static class ClientHandler implements Runnable {

		Socket link;
		NClients counter;
		
		public ClientHandler(Socket s, NClients v) {
			this.link = s;
			this.counter = v;
		}
		
		public void cleanup() {
			try {
				link.close();
			} catch (IOException e) {
			}
			counter.dec();
		}
		
		@Override
		public void run() {
			
			int count ;
			byte[] buffer = new byte[1024];
			String anotherPath = "C:\\Users\\Sara\\Documents\\scuola\\twepsit\\testo.txt"; //va cambiato con quello del pc in uso
			
			BufferedInputStream instream = null;
			PrintWriter writer = null;
			String message = null;
			
			
			try {
				
				InputStream in = link.getInputStream();
				FileOutputStream fos = new FileOutputStream(anotherPath);
				
				//codice per ricevere i byte dal server
			    while((count=in.read(buffer)) >0){
			        fos.write(buffer); //salvato in anotherPath
			    }
			    fos.close();
				
				writer = new PrintWriter(link.getOutputStream(), true);
			}catch(IOException ex) {
				ex.printStackTrace();
				System.out.println("Client died too early, cleaning up the mess!");
				cleanup();
				return;
			}
			
			while("".equalsIgnoreCase(message) == false) {
	            try {
					message = Files.readString(Paths.get("C:\\Users\\Sara\\Documents\\scuola\\twepsit\\testo.txt")); //dovrebbe leggere il contenuto del file inviato dal client
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
	            // Legge il messaggio dal client
	            System.out.println("Messaggio ricevuto dal client: " + message);
	            
	            
	            
	
	            //Verifico se il messaggio è valido e rispondo solo in quel caso
	            
	            String[] arraymessage = message.split(" ");
	            String comando = arraymessage[0];
	            String nomefile = arraymessage[1]; //(solo il nome del file)
	            
	         //SHOWFILES
	            if ("showfiles".equalsIgnoreCase(message)) {
	                // Calcola un tempo casuale basato sul numero di client attivi
	                int delay = counter.get() * CLIENT_DELAY; // Millisecondi
	                System.out.println("Tempo di attesa per rispondere: " + delay + " ms");
	
	                // Attende per il tempo calcolato
	                attendi(delay);
	
	                // Invia la risposta al client
	                writer.println("World");					//lista dei file
	                System.out.println("Risposta inviata al client: World");
	            } else {
	            	//Tentativo di hacking - chiudo la connessione
	            	writer.println("You are trying to hack into private system");
	            	writer.println("All your data are logged and they will sent to the authority");
	            	writer.println("for investigation.");
	            	
	            	writer.println("You are:"+link.getRemoteSocketAddress());
	            }
	            
	          //DOWNLOAD
	            
	             
	            if ("download".equalsIgnoreCase(comando)) {
	                // Calcola un tempo casuale basato sul numero di client attivi
	                int delay = counter.get() * CLIENT_DELAY; // Millisecondi
	                System.out.println("Tempo di attesa per rispondere: " + delay + " ms");
	
	                // Attende per il tempo calcolato
	                attendi(delay);
	
	                // Invia la risposta al client
	                
	                writer.println("World");
	                System.out.println("Risposta inviata al client: World");
	            } else {
	            	//Tentativo di hacking - chiudo la connessione
	            	writer.println("You are trying to hack into private system");
	            	writer.println("All your data are logged and they will sent to the authority");
	            	writer.println("for investigation.");
	            	
	            	writer.println("You are:"+link.getRemoteSocketAddress());
	            }
	            
	            
	          //UPLOAD 
	            
	           
	            if ("upload".equalsIgnoreCase(comando)) {
	                // Calcola un tempo casuale basato sul numero di client attivi
	                int delay = counter.get() * CLIENT_DELAY; // Millisecondi
	                System.out.println("Tempo di attesa per rispondere: " + delay + " ms");
	
	                // Attende per il tempo calcolato
	                attendi(delay);
	
	                // Invia la risposta al client
	                
	                writer.println("World");
	                System.out.println("Risposta inviata al client: World");
	            } else {
	            	//Tentativo di hacking - chiudo la connessione
	            	writer.println("You are trying to hack into private system");
	            	writer.println("All your data are logged and they will sent to the authority");
	            	writer.println("for investigation.");
	            	
	            	writer.println("You are:"+link.getRemoteSocketAddress());
	            }
	            
	            
	            
			}
			
			/*
        	try {
				
	        	writer.close();
			} catch (IOException e) {
			} */
        	cleanup();
        	
		}
	}
	
    public static void main(String[] args) {
        int port = 12345; // Porta su cui il server ascolta
        NClients clients = new NClients();

        try {
        	ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server avviato. In attesa di connessioni...");
        	
            while (true) {
                // Accetta e aspetta la connessione di un client una nuova connessione dal client
                Socket client = serverSocket.accept();
                System.out.println("Nuovo client connesso.");
                clients.inc();
                Thread handler = new Thread(new ClientHandler(client,clients));
                handler.start();
            }
        
        } catch (Exception e) {
            System.err.println("Errore del server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
