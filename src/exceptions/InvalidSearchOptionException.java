package exceptions;

public class InvalidSearchOptionException extends Exception{
    public InvalidSearchOptionException(){
        super("The search option entered is not valid.");
    }
}