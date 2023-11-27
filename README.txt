Le configurazioni si trovano in src/main/resources/config.properties.

nel file va configurato:
url del database
user
password

inoltre ci sono due variabili applicative
db.frequency = 10 
db.numExecution = 100

frequency stabilisce l'intervallo di transazioni prima di effettuare la commit
numExecution è il numero totale di trasazioni effettuate.


Lo script della tabella si trova nella cartella SQLScripts.
Ho incluso anche la creazione del database. 

