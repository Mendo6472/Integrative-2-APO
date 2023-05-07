package exceptions;

public class WrongTimesSoldException extends Exception{
    public WrongTimesSoldException(){
        super("The number of times sold is not correct.");
    }
}