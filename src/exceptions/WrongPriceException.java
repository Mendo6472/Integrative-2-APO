package exceptions;

public class WrongPriceException extends Exception{
    public WrongPriceException(){
        super("The price entered is not correct.");
    }
}