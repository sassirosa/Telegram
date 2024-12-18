# Telegram 

## Contenuti 

1. [Informazioni generali](#informazioni-generali) 

2. [Risorse](#risorse) 

3. [Progettazione](#progettazione) 

4. [Utilizzo](#utilizzo) 

### Informazioni Generali 

Abbiamo a disposizione un Client che nel momento in cui si crea la connessione con il Server può scegliere tra tre azioni da dover eseguire in base alla nostra necessità: 

Mostrare una lista di file 

Eseguire il download di un file 

Mettere a disposizione e quindi aggiungere al Server un nuovo file 

Successivamente il Server riceve la richiesta dal Client e va ad esaudire quello che è il comando immesso dal Client. 

## Risorse 

Per un primo approccio verso il nostro progetto è stato utilizzato del codice discusso in classe: teaching/tepsit/networking/src/main/java/it/edu/marconipontedera/tepsit/tcp/hello at main · kismet/teaching · GitHub 

Per capire il ragionamento che si trova dietro al trasferimento di file tra client e server: Trasferisci il file "Client Socket to Server Socket" in Java - GeeksforGeeks 

Per farci delle idee più chiare sul programma: SimpleFileServer in Java - GeeksforGeeks 

Per ragionare sulla creazione, eliminazione, scrittura e lettura dei file: Java file: creazione, eliminazione e scrittura file di testo | HTML.it 

Per mandare e ricevere file: java send file using sockets - Stack Overflow 

Per comprendere come creare una lista di file: Aprire una directory e creare una lista di file con Java 

## Progettazione 

*** 

Step: 

1. Definizione del protocollo per la connessione. 

2. Definizione del tipo di pacchetto. 

3. Verifica della connessione tra il client e il server per assicurarsi che il protocollo sia stato progettato correttamente. 

4. Risoluzione di diverse problematiche. 

5. Sviluppo del primo comando lato client. 

6. Sviluppo del secondo comando lato client. 

7. Sviluppo del terzo comando lato client. 

8. Risoluzione di nuove problematiche di connessione. 

9. Sviluppo del primo comando lato server. 

*** 

--------------------------------------- -> noi siamo qui <- --------------------------------------------- 

*** 

10. Sviluppo del secondo comando lato server. 

11. Sviluppo del terzo comando lato server. 

12. Ultimi controlli e revisioni 

    

*** 

Il protocollo definisce 3 comandi ed è stato definito nel seguente modo: 

``` 

-> showfiles = richiesta dell'elenco dei file presenti sul server 

-> download <nome_file> = richiesta del file menzionato 

-> upload <nome_file> = richiesta di caricare (sul server) il file menzionato 

``` 

Il tipo di pacchetto è **File**. 

Questo significa che tutte le richieste (o comandi) effettuati dal client sono trasmessi al server sottoforma di byte. Lo stesso vale per le risposte del server al client.  

Le risposte del client ai diversi comandi sono: 

``` 

-> showfiles = restituzione dei file presenti scrivendo solo i loro nomi all'interno di un nuovo file (chiamato info.txt) 

-> download <nome_file> = Invio dei byte del file menzionato 

-> upload <nome_file> = Ricezione dei byte dal client e salvataggio nella directory dedicata allo storage di file 

``` 

## Utilizzo 

Ecco i vari step per utilizzare il nostro programma: 

``` 

Aprire Eclipse e il progetto con le varie classi. 

Creare due console, nella prima ci avviamo il Server e nella seconda avviamo il Client. 

Una volta avviato il Client viene creata la connessione tra i due e quindi nella console del Client (seconda console) possiamo inserire il comando che vogliamo eseguire. 

Nelle due console vediamo i risultati delle varie istruzioni eseguite dal codice. ram
COSA FA IL NOSTRO PROGETTO:

Il client può:

  -> leggere i file disponibili sul server
  
  -> scaricare IL file desiderato
  
  -> caricare un SINGOLO file alla volta
  

Il server può:

  -> salvare / scaricarsi i file
  
  -> restituire una lista dei file che ha scaricato
  

STEP:
  1. Definire il protocollo per la connessione
  2. Definire il tipo di pacchetto
  3. Verificare la connesione stabilita tra client - server

PROTOCOLLO :

  identificare i 3 comandi :
  
    -> showfiles (txt / json) = elenco dei file presenri sul server
    
    -> download nome_file (txt/json) = manda al server un txt/json e riceve byte/binario
    
    -> upload nome_file (txt/json) //no rinomina del file   = manda al server un txt/json e riceve un txt/json di conferma, dopo la connessione manda i byte/binario
    

  risposte del server:
  
   -> a "showfiles" risponde con un elenco txt


   //struttura del documento -> tramite i commenti del codice java o cmq un documento in grado di riuscire a vedere le sezioni in modo pratico
