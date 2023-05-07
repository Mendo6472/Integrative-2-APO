package exceptions;

public class NoOrdersFoundException extends Exception{
    public NoOrdersFoundException(){
        super("No orders were found.");
    }
}