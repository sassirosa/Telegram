# Telegram
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
