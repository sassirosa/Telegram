package Telegram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {

	private static final int MAX_TRY = 3;
	
	public static void attendi(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	
	public static void main(String[] args) {
		// Configura l'indirizzo IP e la porta del server
		String serverAddress = "127.0.0.1"; // IP del server (localhost)
		int port = 12345; // Porta del server

		Socket link = null;
		int i = 0;
		
		while( link == null && i < MAX_TRY ) {
			try {
				// Crea una connessione al server
				link = new Socket(serverAddress, port);
			}catch(IOException ex) {
				ex.printStackTrace();
				attendi((long) (1000*Math.pow(2, i)));
			}
			i++;
		}
		
		if( link == null ) {
			System.out.println("Impossibile collegarsi al server");
			return;
		}
		
		try {

				String anotherPath = "C:\\Users\\Sara\\Documents\\scuola\\twepsit\\download";
				String path = "C:\\Users\\Sara\\Documents\\scuola\\twepsit\\messaggio.txt";
				
				byte[] buffer = new byte[1024];
			    int count ;
			    File file = new File(path);
				
				OutputStream out = link.getOutputStream();
				InputStream in = link.getInputStream();
				BufferedInputStream instream = new BufferedInputStream(new FileInputStream(file));
				PrintWriter writer = new PrintWriter(out, true);
				FileOutputStream fos = new FileOutputStream(anotherPath);
				BufferedOutputStream outstream = new BufferedOutputStream(fos);
				
				
				Scanner n = new Scanner(System.in);
				System.out.println("Che richiesta vuoi fare al server??");
				System.out.println("1. mostra la lista dei file presenti");
				System.out.println("2. scrivi il file che vuoi scaricare");
				System.out.println("3. carica un file");
				
				int risposta = n.nextInt();
				
				
				switch(risposta) {
				
				case 1:
					
				    //tipologia di richiesta 1: ricevere una lista di file
					
					try {
				        
				        FileWriter fw = new FileWriter(file);
				        BufferedWriter bw = new BufferedWriter(fw);
				        bw.write("showfiles");  //si potrebbe fare uno switch solo per questa riga di comando
				        bw.flush();
				        bw.close();
				    }
				    catch(IOException e) {
				        e.printStackTrace();
				    }
				    //fine creazione e scrittura del file contenente la richiesta
					
				    //codice per inviare byte del file al server 
				    while ((count = instream.read(buffer)) > 0) {
				         out.write(buffer, 0, count);
				         out.flush();
				    }
				    
				    //codice per ricevere i byte dal server
				    while((count=in.read(buffer)) >0){
				        fos.write(buffer);
				    }
				    fos.close();
				    
					
					return;
					
				case 2:
				    //tipologia di richiesta 2: download file
					
					System.out.println("Inserisci il nome del file ");
					String nomefile = n.nextLine();
					
				    try {
				       
				        FileWriter fw = new FileWriter(file);
				        BufferedWriter bw = new BufferedWriter(fw);
				        bw.write("download" + nomefile);
				        bw.flush();
				        bw.close();
				    }
				    catch(IOException e) {
				        e.printStackTrace();
				    }
				    
				    //codice per inviare byte del file al server 
				    while ((count = instream.read(buffer)) > 0) {
				         out.write(buffer, 0, count);
				         out.flush();
				    }
				    
				  //codice per ricevere i byte dal server
				    while((count=in.read(buffer)) >0){
				        fos.write(buffer);
				    }
				    fos.close();
					
					return;
					
				case 3:
					
					//tipologia di richiesta 3: caricare un file
				    try {
				        
				        FileWriter fw = new FileWriter(file);
				        BufferedWriter bw = new BufferedWriter(fw);
				        bw.write("upload");  
				        bw.flush();
				        bw.close();
				    }
				    catch(IOException e) {
				        e.printStackTrace();
				    }
					
				    //codice per inviare byte del file al server 
				    while ((count = instream.read(buffer)) > 0) {
				         out.write(buffer, 0, count);
				         out.flush();
				    }
				    
				  //codice per ricevere i byte dal server
				    while((count=in.read(buffer)) >0){
				        fos.write(buffer);
				    }
				    fos.close();
					
					return;
				
				}
				
				

				
				    
				    
				    
				
				
				
				
			/*	//dove manda il messaggio il codice del prof
				for (int j = 0; j < 5; j++) {
					long send = System.currentTimeMillis();
					writer.println("Hello");				
					System.out.println("Messaggio inviato al server: Hello");
					
					
					System.out.println(reader.readLine());
					long receive = System.currentTimeMillis();
					
					System.out.println("Ricevuta risposta dopo:"+(receive-send)+"ms");				
					attendi((long) (250*Math.pow(2, j)));
				}
				
				//Inviamo linea vuota per indicare la fine di comunicazione
				writer.println();
				
				*/
				
		} catch (Exception e) {
			// Gestisce eventuali errori
			System.err.println("Errore: " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}

}
