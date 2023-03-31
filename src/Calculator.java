import Exceptions.BufferOverflowException;

import java.util.Stack;

public class Calculator {
    private String[] lexemeArr;
    private int lexemePtr;
    private Stack<String> operandsStack;
    private Stack<String> valuesStack;

    public Calculator(){
        operandsStack = new Stack<String>();
        valuesStack = new Stack<String>();
    }

    private int definePriority(String op){
        int result = -1;
        switch (op)
        {
            case "(":
                result = 0;
                break;
            case "+":
            case "-":
                result = 1;
                break;
            case "*":
            case "/":
                result = 2;
                break;
            case "^":
                result = 3;
        }
        return result ;
    }

    private void execute(){
        double x, y, result;
        String val1, val2;
        String operand;

        val2 = valuesStack.pop();
        val1 = valuesStack.pop();
        operand = operandsStack.pop();

        x = Double.parseDouble(val1);
        y = Double.parseDouble(val2);

        result = 0.0;

        switch (operand){
            case "+":
                result = x + y;
                break;
            case "-":
                result = x - y;
                break;
            case "*":
                result = x * y;
                break;
            case "/":
                result = x / y;
                break;
            case "^":
                result = Math.pow(x, y);
                break;
        }
        val1 = Double.toString(result);
        valuesStack.push(val1);
    }

    public double calculate(String expression){
        int i;
        String current, top;


        parse(expression);

        for(i = 0; i <= lexemePtr; i++) {
            current = lexemeArr[i];

            switch (current) {
                case "(":
                    operandsStack.push(current);
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "^":
                    if (operandsStack.empty()) {
                        operandsStack.push(current);
                        break;
                    }
                    top = operandsStack.peek();
                    if (definePriority(current) > definePriority(top)) {
                        operandsStack.push(current);
                        break;
                    } else {
                        execute();
                        operandsStack.push(current);
                        break;
                    }
                case ")":
                    while (true) {
                        top = operandsStack.peek();
                        if (top.equals("(")) {
                            top = operandsStack.pop();
                            break;
                        }
                        execute();
                    }
                    break;
                default:
                    valuesStack.push(current);
            }
        }
        while (!operandsStack.empty()){
            execute();
        }

        return Double.parseDouble(valuesStack.peek());
    }

    private void parse(String expression){
        char symbol;
        int i;
        String tmp = "";

        lexemeArr = new String[200];
        for (i = 0; i < 200; i++) lexemeArr[i] = "";
        lexemePtr = 0;
        for(i = 0; i < expression.length(); i++){
            symbol = expression.charAt(i);

            switch(symbol){
                case '+':
                case '-':
                case '*':
                case '^':
                case '/':
                case '(':
                case ')':
                    if (tmp.length() > 0){
                        lexemeArr[lexemePtr++]=tmp;
                        tmp="";
                    }
                    lexemeArr[lexemePtr++]="" + symbol;
                    break;
                case ' ':
                    break;
                default:
                    tmp += symbol;
            }
        }
        if(tmp.length() > 0) lexemeArr[lexemePtr] = tmp;
    }
}
