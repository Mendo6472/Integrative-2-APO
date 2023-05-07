package exceptions;

public class NoProductsFoundException extends Exception{
    public NoProductsFoundException(){
        super("No products were found.");
    }
}