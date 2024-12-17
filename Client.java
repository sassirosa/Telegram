package Telegram;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    private static final int MAX_TRY = 3;

    public static void attendi(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // Ignora l'eccezione
        }
    }

    private static Socket connectToServer(String serverAddress, int port) {
        Socket link = null;
        int i = 0;
        while (link == null && i < MAX_TRY) {
            try {
                link = new Socket(serverAddress, port);
            } catch (IOException ex) {
                ex.printStackTrace();
                attendi((long) (1000 * Math.pow(2, i)));
            }
            i++;
        }
        return link;
    }

    private static String leggiContenuto(Socket link) {
        byte[] buffer = new byte[1024];
        int count = 0;
        String anotherPath = "C:\\Users\\Sara\\Documents\\scuola\\twepsit\\telegram\\risultato.txt";

        try (InputStream in = link.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(anotherPath))) {

            int totalBytesReceived = 0;
            int maxBytesToReceive = 9;
            while (totalBytesReceived < maxBytesToReceive && (count = in.read(buffer)) > 0) {
                int bytesToWrite = Math.min(count, maxBytesToReceive - totalBytesReceived);
                fos.write(buffer, 0, bytesToWrite);
                totalBytesReceived += bytesToWrite;
                System.out.println("Byte ricevuti e scritti nel file: " + bytesToWrite);
            }

            String message = Files.readString(Paths.get(anotherPath));
            System.out.println("Ricevuto il messaggio dal server");
            System.out.println(message);

            return message;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void sendFile(Socket link, String filePath, String command) {
        try (FileInputStream fileStream = new FileInputStream(new File(filePath));
             BufferedInputStream instream = new BufferedInputStream(fileStream);
            ) {
        	 OutputStream out = link.getOutputStream();

            byte[] buffer = new byte[1024];
            int count;
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(command);  // Invia il comando

            int totalBytesSent = 0;
            int maxBytesToSend = command.equals("showfiles") ? 9 : 13;
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

            String path = "C:\\Users\\Sara\\Documents\\scuola\\twepsit\\telegram";
            String filePath = path + "\\miofile.txt"; // file temporaneo per inviare il comando

            switch (risposta) {
                case 1:
                    sendFile(link, filePath, "showfiles");
                    String contenuto = leggiContenuto(link);
                    System.out.println("Il client ha ricevuto dal server i seguenti dati: " + contenuto);
                    break;

                case 2:
                    System.out.println("Inserisci il nome del file anche la tipologia (.qualcosa)");
                    String nomefile = scanner.nextLine();
                    sendFile(link, filePath, "download " + nomefile);
                    leggiContenuto(link); // Legge e mostra il file ricevuto
                    break;

                case 3:
                    System.out.println("Inserisci il nome del file caricato, ovvero come vorresti salvarlo sul server (max 14 caratteri)");
                    nomefile = scanner.nextLine();
                    sendFile(link, filePath, "upload " + nomefile);
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

