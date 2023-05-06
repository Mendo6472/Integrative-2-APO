package models;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;

public class Connection<T> {
    Gson gson;
    String url;
    Type listType;
    public Connection(String url, Type listType){
        this.url = url;
        this.listType = listType;
    }
    public ArrayList<T> getEntity(){
        Gson gson = new Gson();
        ArrayList<T> arrayList = null;
        FileReader fileReader = null;
        try{
            fileReader = new FileReader(this.url);
            arrayList = gson.fromJson(fileReader, listType);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(fileReader != null){
                try{
                    fileReader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }
    

    public void updateEntity(ArrayList<T> arrayList){
        gson = new Gson();
        try{
            FileWriter fileWriter = new FileWriter(this.url);
            gson.toJson(arrayList, fileWriter);
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
