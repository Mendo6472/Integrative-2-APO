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
    Type listType; //Type representa a los tipos en Java, al trabajar con genéricos, usamos esto para que tome el valor
    //de aquello que vaya a ser el Type.
    public Connection(String url, Type listType){
        this.url = url;
        this.listType = listType;
    }
    public ArrayList<T> getEntity(){
        Gson gson = new Gson(); //Se crea un Gson para poder tratar los datos a tipo Json.
        ArrayList<T> arrayList = null; //Aquí se guardará la información en tipo Json.
        FileReader fileReader = null;
        try{
            fileReader = new FileReader(this.url); //Se intenta leer el archivo, en caso de que falle tira excepción.
            arrayList = gson.fromJson(fileReader, listType); //Se pasa el archivo de tipo Json a algún tipo de Java.
        }catch(IOException e){
            e.printStackTrace();
        }finally{ //Siempre entra acá, haya o no excepción.
            if(fileReader != null){ //Si el archivo se pudo leer, se cierra, de lo contrario tira excepción.
                try{
                    fileReader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return arrayList; //Se retorna ahora la información que era tipo Json.
    }
    

    public void updateEntity(ArrayList<T> arrayList){
        gson = new Gson();
        try{
            FileWriter fileWriter = new FileWriter(this.url); //Se crea un FileWriter para que escriba sobre el archivo
            //representado por url.
            gson.toJson(arrayList, fileWriter); //Ahora pasa el arraylist a Json.
            fileWriter.close();
        } catch (IOException e) { //Si el archivo no existe o tiene problemas de lectura, lanza excepción y no se puede
            //actualizar.
            e.printStackTrace();
        }
    }
}
