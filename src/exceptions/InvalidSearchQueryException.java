package exceptions;

public class InvalidSearchQueryException extends Exception{
    public InvalidSearchQueryException(){
        super("The search query entered is not valid.");
    }
}

