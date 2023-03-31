package Exceptions;

public class IncorrectExpressionException extends Exception{
    public String toString(){
        return "The expression you entered contains an unrecognized character.";
    }
}
