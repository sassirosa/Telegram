package Telegram;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    // Enum per i comandi
    public enum Command {
        SHOW_FILES("showfiles"),
        DOWNLOAD("download"),
        UPLOAD("upload");

        private final String command;

        Command(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    // Classe per le costanti
    public static class Constants {
        public static final int MAX_TRY = 3;
        public static final int BUFFER_SIZE = 1024;
        public static final int MILLISECOND_CONVERSION = 1000;
        public static final int INITIAL_RETRY_DELAY = 1000; // Tempo iniziale di attesa in millisecondi
        public static final int RETRY_DELAY_MULTIPLIER = 2; // Fattore di moltiplicazione per ogni tentativo

        public static final int MAX_FILE_NAME_LENGTH = 14; // Lunghezza massima del nome del file
        public static final int MAX_BYTES_TO_RECEIVE = 9; // Max byte da ricevere
        public static final int MAX_BYTES_TO_SEND_SHOWFILES = 9; // Max byte da inviare per "showfiles"
        public static final int MAX_BYTES_TO_SEND_OTHER = 13; // Max byte da inviare per altri comandi
    }

    // Metodo per attendere (con costante)
    public static void attendi(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // Ignora l'eccezione
        }
    }

    // Metodo per connettersi al server
    private static Socket connectToServer(String serverAddress, int port) {
        Socket link = null;
        int i = 0;
        while (link == null && i < Constants.MAX_TRY) {
            try {
                link = new Socket(serverAddress, port);
            } catch (IOException ex) {
                ex.printStackTrace();
                attendi((long) (Constants.INITIAL_RETRY_DELAY * Math.pow(Constants.RETRY_DELAY_MULTIPLIER, i)));
            }
            i++;
        }
        return link;
    }

    // Metodo per leggere il contenuto del server
    private static String leggiContenuto(Socket link) {
        byte[] buffer = new byte[Constants.BUFFER_SIZE];
        int count = 0;
        String anotherPath = "C:\\Users\\rossi.sara\\Documents\\telegram\\risultato.txt";
        

        try (InputStream in = link.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(anotherPath))) {
        	
        	//prima legge quanto è lungi il file e poi legge la lunghezza dei dati
        	
        	byte[] len = new byte[2];
        	in.read(len);
        	Integer lunghezza = Integer.parseInt(new String(len));
        	//short lunghezza = ByteBuffer.wrap(len).getShort(); 

            int totalBytesReceived = 0;
            while (totalBytesReceived < lunghezza  && (count = in.read(buffer)) > 0) {
                int bytesToWrite = Math.min(count, Constants.MAX_BYTES_TO_RECEIVE - totalBytesReceived);
                fos.write(buffer, 0, bytesToWrite);
                totalBytesReceived += bytesToWrite;
                System.out.println("Byte ricevuti e scritti nel file: " + bytesToWrite);
            }

            String message = Files.readString(Paths.get(anotherPath),StandardCharsets.UTF_8);
            System.out.println("Ricevuto il messaggio dal server");
            System.out.println(message);

            return message;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Metodo per inviare un file
    private static void sendFile(Socket link, String filePath, Command command) {
        try (FileInputStream fileStream = new FileInputStream(new File(filePath));
             BufferedInputStream instream = new BufferedInputStream(fileStream);
             ) {
        	OutputStream out = link.getOutputStream();

            PrintWriter writer = new PrintWriter(out, true);
            writer.println(command.getCommand());  // Invia il comando

            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            int count;
            int maxBytesToSend = command == Command.SHOW_FILES ? Constants.MAX_BYTES_TO_SEND_SHOWFILES : Constants.MAX_BYTES_TO_SEND_OTHER;
            int totalBytesSent = 0;

            while (totalBytesSent < maxBytesToSend && (count = instream.read(buffer)) > 0) {
                int bytesToSend = Math.min(count, maxBytesToSend - totalBytesSent);
                out.write(buffer, 0, bytesToSend);
                out.flush();
                totalBytesSent += bytesToSend;
                System.out.println("Sto inviando il messaggio al server");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1"; // IP del server (localhost)
        int port = 12345; // Porta del server

        // Connessione al server
        Socket link = connectToServer(serverAddress, port);
        if (link == null) {
            System.out.println("Impossibile collegarsi al server");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Che richiesta vuoi fare al server?");
            System.out.println("1. Mostra la lista dei file presenti");
            System.out.println("2. Scrivi il file che vuoi scaricare");
            System.out.println("3. Carica un file");

            int risposta = scanner.nextInt();
            scanner.nextLine();  // Consuma la newline

            String path = "C:\\Users\\rossi.sara\\Documents\\telegram";
            String filePath = path + "\\miofile.txt"; // file temporaneo per inviare il comando

            switch (risposta) {
                case 1:
                    sendFile(link, filePath, Command.SHOW_FILES);
                    String contenuto = leggiContenuto(link);
                    System.out.println("Il client ha ricevuto dal server i seguenti dati: " + contenuto);
                    break;

                case 2:
                    System.out.println("Inserisci il nome del file anche la tipologia (.qualcosa)");
                    String nomefile = scanner.nextLine();
                    sendFile(link, filePath, Command.DOWNLOAD);
                    leggiContenuto(link); // Legge e mostra il file ricevuto
                    break;

                case 3:
                    System.out.println("Inserisci il nome del file caricato, ovvero come vorresti salvarlo sul server (max " + Constants.MAX_FILE_NAME_LENGTH + " caratteri)");
                    nomefile = scanner.nextLine();
                    sendFile(link, filePath, Command.UPLOAD);
                    leggiContenuto(link); // Legge e mostra il risultato dell'upload
                    break;

                default:
                    System.out.println("Opzione non valida.");
            }

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
