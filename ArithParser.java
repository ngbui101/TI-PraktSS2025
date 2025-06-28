import java.io.*;

/*
  ArithParser.java
  
  Praktikumslösung, umgesetzt im Stil des Vorlesungsbeispiels NumParser.java.
  
  Realisiert die folgende rechtsrekursive, kontextfreie Grammatik:
  expression      → term rightExpression
  rightExpression → '+' term rightExpression | '-' term rightExpression | ε
  term            → operator rightTerm
  rightTerm       → '*' operator rightTerm | '/' operator rightTerm | ε
  operator        → '(' expression ')' | num
  num             → digit num | digit
  digit           → '0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9'
  
  Der Parser ist nach dem Prinzip des rekursiven Abstiegs programmiert.
*/
public class ArithParser {

  // --- Attribute (wie in NumParser.java) ---
  static final char EOF = (char) 255;
  static int pointer = 0;
  static int maxPointer = 0;
  static char[] input;

  //-------------------------------------------------------------------------
  //-------------------Methoden der Grammatik--------------------------------
  //-------------------------------------------------------------------------
  
  /*
   * Entspricht dem Nicht-Terminalsymbol <expression>
   * Regel: expression → term rightExpression
   */
  static boolean expression(int t) {
    ausgabe("expression->", t);
    return term(t + 1) && rightExpression(t + 1);
  }

  /*
   * Entspricht dem Nicht-Terminalsymbol <rightExpression>
   * Regel: rightExpression → ('+'|'-') term rightExpression | ε
   */
  static boolean rightExpression(int t) {
    ausgabe("rightExpression->", t);
    char[] ops = {'+', '-'};
    if (pointer < maxPointer && (input[pointer] == '+' || input[pointer] == '-')) {
      match(ops, t + 1);
      return term(t + 1) && rightExpression(t + 1);
    }
    ausgabe("Epsilon", t + 1); // Angepasst für Aufgabe b)
    return true;
  }

  /*
   * Entspricht dem Nicht-Terminalsymbol <term>
   * Regel: term → operator rightTerm
   */
  static boolean term(int t) {
    ausgabe("term->", t);
    return operator(t + 1) && rightTerm(t + 1);
  }

  /*
   * Entspricht dem Nicht-Terminalsymbol <rightTerm>
   * Regel: rightTerm → ('*'|'/') operator rightTerm | ε
   */
  static boolean rightTerm(int t) {
    ausgabe("rightTerm->", t);
    char[] ops = {'*', '/'};
    if (pointer < maxPointer && (input[pointer] == '*' || input[pointer] == '/')) {
      match(ops, t + 1);
      return operator(t + 1) && rightTerm(t + 1);
    }
    ausgabe("Epsilon", t + 1); // Angepasst für Aufgabe b)
    return true;
  }

  /*
   * Entspricht dem Nicht-Terminalsymbol <operator>
   * Regel: operator → '(' expression ')' | num
   */
  static boolean operator(int t) {
    ausgabe("operator->", t);
    char[] openBrace = {'('};
    if (match(openBrace, t + 1)) {
      if (!expression(t + 1)) return false;
      
      char[] closeBrace = {')'};
      if (match(closeBrace, t + 1)) {
        return true;
      }
      syntaxError("Schließende Klammer ')' erwartet");
      return false;
    }
    return num(t + 1);
  }

  /*
   * Entspricht dem Nicht-Terminalsymbol <num>
   * Regel: num → digit num | digit
   */
  static boolean num(int t) {
    ausgabe("num->", t);
    char[] digitSet = {'1','2','3','4','5','6','7','8','9','0'};
    if (lookAhead(digitSet)) {
      return digit(t + 1) && num(t + 1);
    }
    return digit(t + 1);
  }

  /*
   * Entspricht dem Nicht-Terminalsymbol <digit>
   * Regel: digit → '0'...'9'
   */
  static boolean digit(int t) {
    ausgabe("digit->", t);
    char[] matchSet = {'1','2','3','4','5','6','7','8','9','0'};
    if (match(matchSet, t + 1)) {
      return true;
    }
    return false;
  }

  //-------------------------------------------------------------------------
  //-------------------Hilfsmethoden (aus NumParser.java)--------------------
  //-------------------------------------------------------------------------

  /*
   * Methode, die testet, ob das aktuelle Eingabezeichen unter den Zeichen
   * ist, die als Parameter (matchSet) übergeben wurden.
   * Bei Erfolg wird true zurückgegeben und der Eingabezeiger weitergesetzt.
   */
  static boolean match(char[] matchSet, int t) {
    for (int i = 0; i < matchSet.length; i++) {
      if (pointer < maxPointer && input[pointer] == matchSet[i]) {
        ausgabe("match: " + input[pointer], t);
        pointer++;
        return true;
      }
    }
    return false;
  }

  /*
   * Methode, die testet, ob das auf das aktuelle Zeichen folgende Zeichen
   * unter den Zeichen ist, die als Parameter (aheadSet) übergeben wurden.
   * Der Eingabepointer wird nicht verändert.
   */
  static boolean lookAhead(char[] aheadSet) {
    if ((pointer + 1) >= maxPointer) return false;
    for (int i = 0; i < aheadSet.length; i++) {
      if (input[pointer + 1] == aheadSet[i]) {
        return true;
      }
    }
    return false;
  }

  /*
   * Methode zum zeichenweisen Einlesen der Eingabe aus einer Datei.
   * Whitespace wird ignoriert. Das Ende wird mit EOF markiert.
   */
  static boolean readInput(String name) {
    int c;
    try {
      input = new char[256];
      FileReader f = new FileReader(name);
      int i = 0;
      while (i < 255 && (c = f.read()) != -1) {
        if (!Character.isWhitespace(c)) {
          input[i++] = (char) c;
        }
      }
      maxPointer = i;
      input[i] = EOF;
      f.close();
    } catch (Exception e) {
      System.out.println("Fehler beim Dateizugriff: " + name);
      return false;
    }
    return true;
  }

  /*
   * Methode, die testet, ob das Ende der Eingabe erreicht ist.
   */
  static boolean inputEmpty() {
    if (pointer == maxPointer) {
      ausgabe("Eingabe leer!", 0);
      return true;
    }
    syntaxError("Eingabe bei Ende des Parserdurchlaufs nicht leer");
    return false;
  }

  /*
   * Methode zum korrekt eingerückten Ausgeben des Syntaxbaumes.
   */
  static void ausgabe(String s, int t) {
    for (int i = 0; i < t; i++) System.out.print("  ");
    System.out.println(s);
  }

  /*
   * Methode zum Ausgeben eines Syntaxfehlers.
   */
  static void syntaxError(String s) {
    // EOF ('ÿ') ist für den Benutzer nicht hilfreich, also geben wir eine bessere Meldung aus.
    if (input[pointer] == EOF) {
        System.out.println("\nSyntax Fehler am Ende der Eingabe.");
    } else {
        System.out.println("\nSyntax Fehler beim " + (pointer + 1) + ". Zeichen: '" + input[pointer] + "'");
    }
    System.out.println(s);
  }

  //-------------------------------------------------------------------------
  // Main Methode, startet den Parser
  //-------------------------------------------------------------------------
  public static void main(String args[]) {
    if (readInput("testdatei.txt")) {
      if (expression(0) && inputEmpty()) {
        System.out.println("\nKorrekter Ausdruck");
      } else {
        System.out.println("\nFehler im Ausdruck");
      }
    }
  }
}