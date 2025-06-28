# Parser für arithmetische Ausdrücke (Praktikum Theoretische Informatik)

Dieses Repository enthält die schrittweise Lösung der Praktikumsaufgabe aus der Vorlesung "Theoretische Informatik" an der Hochschule Bochum. Das Ziel ist die Implementierung eines Parsers für arithmetische Ausdrücke nach dem Prinzip des rekursiven Abstiegs in Java.

## Projektübersicht

Der `ArithParser` ist ein Kommandozeilenprogramm, das einen String aus einer Datei (`testdatei.txt`) liest und überprüft, ob dieser eine syntaktisch korrekte Form gemäß einer vorgegebenen, kontextfreien Grammatik hat. Der Parser gibt den Ablauf der Syntaxanalyse auf der Konsole aus, um die angewendeten Grammatikregeln nachzuvollziehen.

Die Implementierung orientiert sich stark am Beispiel `NumParser.java`, das in der Vorlesung vorgestellt wurde.

## Verwendete Grammatik

Der Parser implementiert die folgende rechtsrekursive, kontextfreie Grammatik, um Probleme mit Linksrekursion beim rekursiven Abstieg zu vermeiden:

```gram
expression      → term rightExpression
rightExpression → '+' term rightExpression | '-' term rightExpression | ε
term            → operator rightTerm
rightTerm       → '*' operator rightTerm | '/' operator rightTerm | ε
operator        → '(' expression ')' | num
num             → digit num | digit
digit           → '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'
```

## Verwendung

### Voraussetzungen
* Java Development Kit (JDK)

### Ausführung
1.  **Kompilieren:** Navigiere im Terminal zum Projektverzeichnis und kompiliere die Java-Datei:
    ```sh
    javac ArithParser.java
    ```

2.  **Testausdruck vorbereiten:** Erstelle eine Datei mit dem Namen `testdatei.txt` im selben Verzeichnis und füge den zu parsenden arithmetischen Ausdruck ein. Wichtig: Der Ausdruck sollte keine Leerzeichen enthalten. Beispiel:
    ```
    (12+3)*5
    ```

3.  **Parser ausführen:** Führe das kompilierte Programm aus:
    ```sh
    java ArithParser
    ```

Der Parser liest den Ausdruck aus der `testdatei.txt`, analysiert ihn und gibt den Syntaxbaum sowie das Ergebnis der Prüfung auf der Konsole aus.

## Aufgabenstellung und Fortschritt

Das Projekt löst die folgende, vierteilige Aufgabenstellung:

-   **[✔️] a) Parser implementieren:** Erstellung eines Parsers in Java für die angegebene Grammatik.
-   **[❌] b) Syntaxbaum und Fehlerbehandlung:** Sicherstellen, dass der Parser einen aussagekräftigen Syntaxbaum und hilfreiche Fehlermeldungen (insbesondere für Klammerfehler) ausgibt.
-   **[❌] c) Manueller Test:** Vorbereitung eines von Hand erstellten Syntaxbaums für einen Testausdruck, um die Korrektheit des Parsers zu validieren.
-   **[❌] d) Analyse:** Begründung, warum eine linksrekursive Implementierung der `num`-Regel (`num → num digit | digit`) für diesen Parser-Typ ungeeignet ist.