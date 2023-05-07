package exceptions;

import java.io.IOException;

public class FileCloseException extends IOException{
    public FileCloseException(){
        super("An error occurred while closing the file.");
    }
}