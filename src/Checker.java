import Exceptions.BufferOverflowException;
import Exceptions.IncorrectExpressionException;

public class Checker {
//    private static String[] suitableSymbols = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", "^",
//        "(", ")", " "};
    private final static String suitableSymbols = "0123456789+-*./^( )";
    public static void checkExpression(String expression)
            throws BufferOverflowException, IncorrectExpressionException {
        if(expression.length() > 200){
            throw new BufferOverflowException();
        }
        var expressionArr = expression.split("");
        for(var i = 0; i < expression.length(); i++){
            if(!suitableSymbols.contains(expressionArr[i])){
                throw new IncorrectExpressionException();
            }
        }

    }
}
