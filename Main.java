import java.util.*;

class Main {
  public static void main(String[] args){
    //While loop that is true until false by "quit". 
    //Prompts user for an expression or to quit.
    while (true) {
      Scanner input = new Scanner(System.in);
      System.out.print("\nEnter an expression or type \"quit\" to stop the program: ");
      String expression = input.nextLine();
      if (expression.equalsIgnoreCase("quit")){
        System.out.print("\nExiting...\t\tcompleted\t\t");
        break;
      }
      //Creates the output base.
      //Calls the function fracCalc (nested methods)
      System.out.print("\n_____________________\n\n" + expression + " = [");
      String[] isError = errorHandling();
      if (syntaxCheck(expression).equals("syntax")){
        System.out.println("]\n\t["+isError[1] + "]");
      }
      else{
        fracCalc(expression);
        System.out.println();  
      }
    }
  }

  public static String syntaxCheck(String expression){
    String s = expression;
    String[] partition = s.split(" ");

    if((s.contains("++") || s.contains("--") || s.contains("**") || s.contains("//"))) {
      return "syntax";
    }

    String syntaxes = "1234567890+-*/_ ";
    for (int i = 0; i < partition.length; i++){
      for (int j = 0; j < partition[i].length(); j++){
        char position = partition[i].charAt(j);
        if (syntaxes.contains(String.valueOf(position))){
          return "!error";
        }
        else{
          return "syntax";
        }
      }
    }


    return "!error";
  }

  public static String[] errorHandling(){
    String s = "Cannot perform operation. ";
    String[] error = new String[]{s+ " Reason: Division by zero", s + "Invalid Syntax."};
    return error;
  }

  //Parses through the string expression and isolates into operators and operands. Due to split by " ".
  public static void fracCalc(String expression) {
    String[] isError = errorHandling();
    String[] exp = expression.split(" ");
    String[] operators = {"+","-","*","/"};
    //variable intizalization.
    int oper = 0;
    int x = 0;
    int y = 1;
    //Running until all operations are completed.
    for (int i = 0; i < exp.length; i++) {
      String pos = exp[i];
      int num = 0, den = 0, whole = 0;
      int dashes = pos.indexOf("/"); 
      //Testing the condition whether the part of the expression, split " " -> var pos, contains an operator that is valid.
      //Switch statements allow for multiple 'cases' rather than an if, elif and else statement being true then disregarding the rest.
      if(pos.equals(operators[0]) || pos.equals(operators[1]) || pos.equals(operators[2]) || pos.equals(operators[3])) {
        switch(pos) {
          case "+": 
            oper = 0; break;
          case "-": 
            oper = 1; break;
          case "*": 
            oper = 2; break;
          case "/": 
            oper = 3; break;          
        }
      }
      //Whether it is a mixed fraction --> goes to the impromper method to convert for easier usage of the fractions.
      else if (pos.contains("_")) {
        whole = Integer.parseInt(pos.substring(0, pos.indexOf("_")));
        num = Integer.parseInt(pos.substring(pos.indexOf("_") + 1, dashes));
        den = Integer.parseInt(pos.substring(dashes + 1, pos.length()));
        num = improper(whole, num, den);
      }
      //Normal fracs.
      else if (pos.length() != 1 && pos.contains("/")) {
        num = Integer.parseInt(pos.substring(0, dashes));
        den = Integer.parseInt(pos.substring(dashes + 1, pos.length()));
      }
      else {
        //No given denominator, creates a denominator of 1 for easier usage.
        num = Integer.parseInt(pos);
        den = 1;
      }
      //https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html (Explains why I used switch statements rather than if, elif, else.)

      if(num != 0 && den != 0) {

        switch(oper) {
          case 0:   
            x *= den; x += num * y; y *= den; break;
          case 1: 
            x *= den; x -= num * y; y *= den; break;
          case 2: 
            x *= num; y *= den; break;
          case 3:
            x *= den; y *= num; break;
          }
        
        if (x < 0) { 
          x = -x;
          y = -y;
        }
      }

      else if (num == 0) {
        x += 0;
      }
      else if (den == 0){
        System.out.print("]\n\t["+ isError[0] + "]");
        return;
      }


    }
    //Calls reduce to 'reduce'? 
    reduce(x, y);
  }



/*
  public static void divByZero(String expression){
    String[] zeroes = expression.split(" ");
    String[] operand;
    String[] operator;

    for (int i = 0; i < zeroes.length; i+=2){
      operand += zeroes[i];
      operator += zeroes[i+1];
    }

    char preOperand;
    char preOperator;

    for (int x = 0; x < operator.length; x++){
      if (operand[x].equals("0")
    }

  }
  */

  public static int improper(int whole, int num, int den) {
    if (!(whole > 0)) {// Negative wholes.
      num = -num + whole * den;
    }
    else {// Normal wholes.
      num = num + (whole * den);
    }
    return num;
  }

  public static void reduce(int x, int y) {
    //Math. Gets the GCF
    int gcd = GCF(x, y);
    int whole = 0;
    //Puts both x, numerator, and y, denominator, over the GCF to be proportional.
    x = x / gcd;
    y = y / gcd;
    //For whole numbers modulo leftovers is the numerator.
    if (x >= y) {
      whole = x / y;
      x %= y;
    }
    //Since the deno is 1, then it would remain itself. Thus whole will be num/deno (x = 0) since there is no other frac. Calls answer.
    if(y == 1) {
      whole += x / y;
      x = 0;
      answer(whole, x, y);
    }
    //Normal procedure after special cases.
    else {
      answer(whole, x, y);
    }
  }

  //Finds the GCF
  public static int GCF(int num, int den) {
    if(den == 0) {
      return num;
    }
    return GCF(den, num%den);
  }

  public static void answer(int whole, int x, int y) {
    if (whole == 0 && !(x == 0 || y == 0)) { //Continuation from reduce's procedure, just prints num/deno.
      System.out.println(x + "/" + y + "]");
    }
    else if (x == 0 || y == 0) { //Just whole.
      if (x == 0) {
        System.out.println(whole + "]");
      }
    }
    else { //Combination of both.
      System.out.println(whole + "_" + (Math.abs(x) + "/" + Math.abs(y)) + "]");
    }
  }
}


//Slot saved for extra credit: Error handling.

/* How will do that? --> deno = 0 -> Error
only digits ->  int[] digitArray = {1,2,3,4,5,6,7,8,9,0} -> of not operators then it's an error.
Check Spacings, etc.
*/

//Time completed. 10/29/19 (3:08am)
