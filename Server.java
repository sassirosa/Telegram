package Telegram;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import Telegram.Client.Command;
import Telegram.Client.Constants;

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
		PrintWriter writer = null;
		

		public ClientHandler(Socket s, NClients v) {

			this.link = s;

			this.counter = v;
			
			try {
				writer = new PrintWriter(this.link.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void cleanup() {

			try {
				writer = null;
				link.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			counter.dec();

		}

		@Override

		public void run() {

			int count = 0;

			byte[] buffer = new byte[1024];

			// String anotherPath =
			// "C:\\Users\\rossi.sara\\Documents\\informazioni\\info.txt";

			// String anotherPath =
			// "e:\\utenti\\rossi.sara\\Documents\\informazioni\\info.txt";



	

			String message = null;
			//Se writer è null vuol dire che è caduta la connessione ed è inutile andare avanti, ogni operazione fallirà
			while (writer != null) {
				message = doReadCommand();
				String[] parts = parseMessage(message);
				String comando = null;
				String nome = null;
				switch(parts.length) {
				case 2:
					comando = parts[0];
					nome = parts[1];
					break;
				case 1:
					comando = parts[0];
					break;

				}
				executeCommand(comando, new String[] {nome});
			}

		}

		private String[] parseMessage(String message) {

			// Legge il messaggio dal client

			System.out.println("Messaggio ricevuto dal client: " + message);

			// Verifico se il messaggio è valido e rispondo solo in quel caso

			String[] arraymessage = message.split(" ");
			return arraymessage;
		}

		private void commandShowFiles() {
			// Calcola un tempo casuale basato sul numero di client attivi

			int delay = counter.get() * CLIENT_DELAY; // Millisecondi

			System.out.println("Tempo di attesa per rispondere: " + delay + " ms");

			// Attende per il tempo calcolato

			attendi(delay);

			// Invia la risposta al client

			writer.println("5World"); // lista dei file

			System.out.println("Risposta inviata al client: World");	
		}
		
		private void commandDownloadFile() {
			
			 try (FileInputStream fileStream = new FileInputStream(new File("C:\\Users\\rossi.sara\\Documents\\informazioni\\dolce.txt"));
		             BufferedInputStream instream = new BufferedInputStream(fileStream);
		             ) {
		        	OutputStream out = link.getOutputStream();

		            PrintWriter writer = new PrintWriter(out, true);
		          
		            //è stato divertente provarci...ci rivedremo....forse....

		            byte[] buffer = new byte[Constants.BUFFER_SIZE];
		            int count;
		            int maxBytesToSend = 9;
		            int totalBytesSent = 0;

		            while (totalBytesSent < maxBytesToSend && (count = instream.read(buffer)) > 0) {
		                int bytesToSend = Math.min(count, maxBytesToSend - totalBytesSent);
		                out.write(buffer, 0, bytesToSend);
		                out.flush();
		                totalBytesSent += bytesToSend;
		                System.out.println("Sto inviando il file al client");
		            }

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
			
		
			
		
		private void executeCommand(String cmd, String[] args) {
			if ("showfiles".equalsIgnoreCase(cmd)) {
				commandShowFiles();
			}else if("download".equalsIgnoreCase(cmd)) {
				commandDownloadFile();
			}
			//crea una funzione per ogni tipo di comando
		}

		private String doReadCommand() {
			byte[] buffer = new byte[1024];
			int count = 0;
			//String anotherPath = "C:\\Users\\Sara\\Documents\\scuola\\twepsit\\telegram\\miofile.txt";
			String anotherPath = "C:\\Users\\rossi.sara\\Documents\\telegram\\miofile.txt";
			// va cambiato
			// con quello
			// del pc in uso
			
			if(link.isClosed()) {
				System.out.println("Il socket è chiuso");
			}else {

			try {

				File salvato = new File(anotherPath);

				InputStream in = link.getInputStream();

				FileOutputStream fos = new FileOutputStream(salvato);

				// codice per ricevere i byte dal server

				int totalBytesReceived = 0; // Variabile per tenere traccia dei byte inviati

				int maxBytesToReceive = 9; // Numero massimo di byte da inviare

				while (totalBytesReceived < maxBytesToReceive && (count = in.read(buffer)) > 0) {
					// Calcola quanti byte scrivere senza superare il limite
					int bytesToWrite = Math.min(count, maxBytesToReceive - totalBytesReceived);

					// Scrivi i byte nel file
					fos.write(buffer, 0, bytesToWrite);

					System.out.println("Il server sta scrivendo il file");

					totalBytesReceived += bytesToWrite; // Aggiungi i byte ricevuti al totale
					System.out.println("Byte ricevuti e scritti nel file: " + bytesToWrite);
				}

				fos.close();

			} catch (IOException ex) {

				ex.printStackTrace();

				System.out.println("Client died too early, cleaning up the mess!");

				cleanup();

				return null;

			}
			}

			try {

				String message = Files.readString(Paths.get("C:\\Users\\rossi.sara\\Documents\\telegram\\miofile.txt")); 
																														// dovrebbe
																														// leggere
																														// il
																														// contenuto
																														// del
																														// file
																														// inviato
																														// dal
																														// client

				//message = Files.readString(Paths.get("C:\\Users\\rossi.sara\\Documents\\informazioni\\info.txt")); questo path contiene la risposta del server

				System.out.println("Ricevuto il messaggio dal client");
				System.out.println(message);

				return message;
			} catch (IOException e) {

				e.printStackTrace();

				return null;

			}

		}
	}

	public static void main(String[] args) {

		int port = 12345; // Porta su cui il server ascolta

		NClients clients = new NClients();

		try {

			ServerSocket serverSocket = new ServerSocket(port);

			System.out.println("Server avviato. In attesa di connessioni...");

			while (true) {

				// Accetta e aspetta la connessione di un client una nuova connessione dal
				// client

				Socket client = serverSocket.accept();

				System.out.println("Nuovo client connesso.");

				clients.inc();

				Thread handler = new Thread(new ClientHandler(client, clients));

				handler.start();

			}

		} catch (Exception e) {

			System.err.println("Errore del server: " + e.getMessage());

			e.printStackTrace();

		}

	}

}
