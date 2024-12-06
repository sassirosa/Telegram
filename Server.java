package Telegram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
			BufferedReader reader = null; 
			PrintWriter writer = null;
			String message = null;
			try {
				reader = new BufferedReader(new InputStreamReader(link.getInputStream()));		//vanno cambiati con i file reader e writer e buffered
				writer = new PrintWriter(link.getOutputStream(), true);
			}catch(IOException ex) {
				ex.printStackTrace();
				System.out.println("Client died too early, cleaning up the mess!");
				cleanup();
				return;
			}
			
			while("".equalsIgnoreCase(message) == false) {
	            try {
					message = reader.readLine();		//cambiare come legge il messaggio ovvero il file (codice nella parte client)
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
	            // Legge il messaggio dal client
	            System.out.println("Messaggio ricevuto dal client: " + message);
	
	            //Verifico se il messaggio è valido e rispondo solo in quel caso
	            if ("Hello".equalsIgnoreCase(message)) {						//fare i vari if in base alla richiesta letta, prima di finire fare una prova con delle risposte di file semplici
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
        	try {
				reader.close();
	        	writer.close();
			} catch (IOException e) {
			}
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
