# Welcome to Altenheim

## Coding best practices
Hier ne kurze Zusammenfassung was mir grad so zu Java einfällt und woran wir uns als Übung halten sollten.

### Naming Conventions
* Wir sollten alle Variablen, Methoden und Klassen auf englisch benennen. 
* Das gängige Schema ist camelCase für Variablen und Methodennamen und ein großer Anfangbuchstabe für Klassennamen. Jedes neue Wort des Namens bis auf das erste wird am Anfang groß geschrieben, Grammatik spielt hier keine Rolle. 
* Underscores oder so vor Namen sind in Java atypisch. 
* Zahlen in Namen werden ausgeschrieben, also nameOne, nameTwo usw.
* Alle Namen sollten sprechend sein statt kryptisch und dürfen auch mal länger ausfallen. Faustregel: je öfter das Objekt benutzt wird, desto kürzer der Name. 
* Klassennamen sind Nomen: ArticleController, CarModel usw., ebenso davon erstellte Objekte
* Methodennamen sind Verben/Tätigkeitsbeschreibungen: getCarColor, evualuateUserInput etc.
* Variablen enthalten Eigenschaften, es sollten also Nomen sein: color, size usw. Da sie als Teil des Klassenmodells im Kontext stehen, können sie meistens kurz sein. Wenn ich car.color aufrufe ist ja klar, dass sich color auf das Objekt car bezieht. Deshalb hier keine Redundanz carColor, dann wäre der Aufruf car.carColor

### Kommentare
Da kann man sicher lange diskutieren, aber best practice ist inzwischen so wenige Kommentare wie nötig. Außer für API Dokus oder Tutorials gelten aus Sicht vieler modernerer Statements im Netz un von Leuten mit denen ich gearbeitet habe diese Punkte:
* Grundsatz: Namen der Objekte und die Doku beschreiben, WAS der Code macht. Die Logik und gesamte Implementierung zeigen, WIE das gemacht wird. Kommentare bleiben nur für die Teile übrig in denen explizit beschrieben werden muss, WARUM dieser Teil des Codes so implementiert wurde.
* Wenn ihr was kommentiert, sollte man sich immer zuerst die Frage stellen, ob man den Code nicht so schreiben kann, dass dieses WARUM aus dem Code selbst hervorgeht
* Kommentare können in der Zusammenarbeit oder im Prozess für einen selbst Sinn machen um sich Hinweise zu geben. Eigentlich gehört das aber in die commits und nicht den Source Code.
* In den Release gehört kein auskommentierter Code. Alles was auskommentiert wurde, wird nicht gebraucht und kann demnach weg.
* Kommentare sind also kein NoGo, ,aber sollten sehr sparsam eingesetzt werden. Meistens sind sie Hinweise auf Code Smells - also weg damit und lieber ran an den Code.

### Statics
Machen das Leben erstmal vermeintlich leichter, sollten aber nur in absoluten Ausnahmen verwendet werden, da sie den Grundsätzen der OOp nicht folgen. I.d.R. sind sie gefährlich, weil übers gesamte Projekt hinweg der Wert geändert werden kann und die Objekte der Klasse alle den gleichen Wert haben. Das kann intendiert sein, sollte aber wenn es geht vermieden werden. Globale Variablen, z.B. zur Konfiguration sollten lieber als configfile (json, xml, whatever you like) gespeichert werden und daraus abgerufen werden.

### Fremde Libraries
Machen das Leben ebenfalls erstmal vermeintlich leichter, sollte man aber vor allem im Rahmen eines solchen Projekts die Finger von lassen, es sei denn man braucht sie wirklich (z.B. für die UI, Graphen usw.). Je mehr out of the box kommt desto wartbarer, sicherer und nachvollziehbarer.

### Github und Stackoverflow
Nur im Notfall, das meiste was man auf SOF findet ist veraltet und Müll. Zudem erfüllt es selten genau den Zweck den man braucht sondern macht zuwenig oder zuviel. Also als Verständnisgrundlage gut, aber nicht um eigenen Code zu schreiben. Ist bisschen wie Wiki: Gute Quelle für nen Überblick, aber abschreiben sollte man eher nicht.

### Debugging
Nutzt den Debugger so oft es geht. Also Breakpoint(s) und step by step durch. Kenne by Eclipse die Shortcuts nicht, in VS Code ist es F10 um einen Schritt weiter zu gehen, F5 um zum nächsten Breakpoint zu springen und F11 um in ne Methode reinzuspringen wenn wir da grad halt machen. Konsolenausgaben zum Debuggen braucht man nur in Ausnahmen.

### Laufzeitfehler
Java ist da ziemlich gut, weil typensicher, aber passieren kann es trotzdem, vor allem durch falsche Konvertierungen und null pointer. Deshalb ist am Ende das Testen so wichtig, da müssen die Tester grade bei Nutzereingaben versuchen, das Ding kaputt zu machen (Edge Cases anyone?).


## Projektmanagement
Auch hier kurz ungeordnet meine Gedanken dazu

### Tickets
* Die Issues sollten lieber kleinteilig und konkret, als zu allgemein, beschrieben werden
* Jedes Issue hat eine klare Definition of Done: also: welche Kriterien müssen erfüllt sein, damit es abgeschlossen werden kann?
* Jedes Issue hat mindestens ein PRIO, ein WORKLOAD, ein PROZESS und ein Tickettyp- Tag.
* Ein Issue muss dem Projekt Altenheim zugewiesen werden
* wenn mögloch sollte ein Issue einem Meilenstein zugewiesen werden
* wer welche Tickets übernimmt können wir im Team besprechen

### Kanban
Die Tickets können manuell in den nächsten Block gezogen werden, bei codebezogenen Tickets ändert sich der Status automatisch (s.u.). Jedem Ticket ist eine Person zugewiesen, bevor es als DONE deklariert wird, sollte diese Person es auf INREVIEW setzen und einen Reviewer zuweisen. Erst dann kann es vom Reviewer auf DONE gestellt werden.

### SCM


