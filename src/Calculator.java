import java.util.Stack;

public class Calculator {
    private Stack<String> lexemeStack;
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

    private void Execute(){
        double x, y, result;
        String val1, val2;
        String operand;

        val1 = valuesStack.pop();
        val2 = valuesStack.pop();
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

        }
    }
}
