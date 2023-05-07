package exceptions;

public class WrongQuantityException extends Exception{
    public WrongQuantityException(){
        super("The quantity entered is not correct.");
    }
}