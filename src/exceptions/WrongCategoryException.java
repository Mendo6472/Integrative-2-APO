package exceptions;

public class WrongCategoryException extends Exception{
    public WrongCategoryException(){
        super("The category entered is not correct.");
    }
}