# Telegram
## Contenuti
1. [Informazioni generali](#informazioni-generali)
2. [Risorse](#risorse)
3. [Progettazione](#progettazione)
4. [Utilizzo](#utilizzo)
5. [FAQs](#faqs)
### Informazioni Generali
***
Write down the general informations of your project. It is worth to always put a project status in the Readme file. This is where you can add it. 
### Screenshot
![Image text](https://www.united-internet.de/fileadmin/user_upload/Brands/Downloads/Logo_IONOS_by.jpg)
## Risorse
***
A list of technologies used within the project:
* [Technologie name](https://example.com): Version 12.3 
* [Technologie name](https://example.com): Version 2.34
* [Library name](https://example.com): Version 1234
## Progettazione
***
Il nostro team ha iniziato dal definire il protocollo per la connessione. Successivamente ha definito il tipo di pacchetto e infine ha verificato la connessione tra il client e il server per assicurarsi che il protocollo progettato funzionasse.
***
Il protocollo definisce 3 comandi ed è stato definito nel seguente modo:
```
-> showfiles = richiede l'elenco dei file presenti sul server
-> download <nome_file> = richiede il file menzionato
-> upload <nome_file> = richiede di caricare (sul server) il file menzionato
```
Il tipo di pacchetto è **File**.
Questo significa che tutte le richieste (o comandi) effettuati dal client sono trasmessi al server sottoforma di byte. Lo stesso vale per le risposte del server al client. 
Quindi le risposte del client ai diversi comandi saranno:
```
-> showfiles = restutuzione dei file presenti scrivendo solo i loro nomi all'interno di un nuovo file (chiamato info.txt)
-> download <nome_file> = Invio dei byte del file menzionato
-> upload <nome_file> = Ricezione dei byte dal client e salvataggio nella directory dedicata allo storage di file
```
## Utilizzo
***
Give instructions on how to collaborate with your project.
> Maybe you want to write a quote in this part. 
> It should go over several rows?
> This is how you do it.
## FAQs
***
A list of frequently asked questions
1. **This is a question in bold**
Answer of the first question with _italic words_. 
2. __Second question in bold__ 
To answer this question we use an unordered list:
* First point
* Second Point
* Third point
3. **Third question in bold**
Answer of the third question with *italic words*.
4. **Fourth question in bold**
| Headline 1 in the tablehead | Headline 2 in the tablehead | Headline 3 in the tablehead |
|:--------------|:-------------:|--------------:|
| text-align left | text-align center | text-align right |
