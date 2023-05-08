package exceptions;

import java.io.IOException;

public class FileWritingException extends IOException{
    public FileWritingException(){
        super("An error occurred while trying to write to the file.");
    }
}