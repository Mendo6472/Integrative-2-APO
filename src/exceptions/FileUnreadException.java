package exceptions;

import java.io.IOException;

public class FileUnreadException extends IOException{
    public FileUnreadException(){
        super("There were problems reading the file.");
    }
}