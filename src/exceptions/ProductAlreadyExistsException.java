package exceptions;

public class ProductAlreadyExistsException extends Exception{
    public ProductAlreadyExistsException(){
        super("The product to be entered already exists.");
    }
}