package Exceptions;

public class BufferOverflowException extends Exception{
    public String toString(){
        return "The number of characters in an equation line exceeds 200.";
    }
}
