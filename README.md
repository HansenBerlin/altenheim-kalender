# Welcome to Altenheim

##Coding best practices
Hier ne kurze Zusammenfassung was mir grad so zu Java einfällt und woran wir uns als Übung halten sollten.

###Naming Conventions
* Wir sollten alle Variablen, Methoden und Klassen auf englisch benennen. 
* Das gängige Schema ist camelCase für Variablen und Methodennamen und ein großer Anfangbuchstabe für Klassennamen. Jedes neue Wort des Namens bis auf das erste wird am Anfang groß geschrieben, Grammatik spielt hier keine Rolle. 
* Underscores oder so vor Namen sind in Java atypisch. 
* Zahlen in Namen werden ausgeschrieben, also nameOne, nameTwo usw.
* Alle Namen sollten sprechend sein statt kryptisch und dürfen auch mal länger ausfallen. Faustregel: je öfter das Objekt benutzt wird, desto kürzer der Name. 
* Klassennamen sind Nomen: ArticleController, CarModel usw., ebenso davon erstellte Objekte
* Methodennamen sind Verben/Tätigkeitsbeschreibungen: getCarColor, evualuateUserInput etc.
* Variablen enthalten Eigenschaften, es sollten also Nomen sein: color, size usw. Da sie als Teil des Klassenmodells im Kontext stehen, können sie meistens kurz sein. Wenn ich car.color aufrufe ist ja klar, dass sich color auf das Objekt car bezieht. Deshalb hier keine Redundanz carColor, dann wäre der Aufruf car.carColor



