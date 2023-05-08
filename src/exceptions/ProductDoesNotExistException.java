package exceptions;

public class ProductDoesNotExistException extends Exception{
    public ProductDoesNotExistException(){
        super("This product doesn't exist!");
    }
}
