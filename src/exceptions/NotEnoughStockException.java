package exceptions;

public class NotEnoughStockException extends Exception{
    public NotEnoughStockException(){
        super("The product doesn't have enough stock!");
    }
}
