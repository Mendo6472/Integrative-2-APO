package models;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import datastructure.Pair;

public class Order {
  
  private String buyerName;
  private ArrayList<Pair<String, Integer>> productsList; //Lista de tipo Pair que guarda el nombre en la izquierda
  //y la cantidad comprada a la derecha.
  private double totalPrice;
  private Date purchaseDate;

  public Order(String buyerName, ArrayList<Pair<String,Integer>> productsList, double totalPrice) throws ParseException {
    this.buyerName = buyerName;
    this.productsList = productsList;
    this.totalPrice = totalPrice;
    LocalDate currentDate = LocalDate.now();
    int currentDay = currentDate.getDayOfMonth();
    int currentMonth = currentDate.getMonthValue();
    int currentYear = currentDate.getYear();
    String date = currentDay+"/"+currentMonth+"/"+currentYear;
    purchaseDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
  }
  
  public int compareToBuyerName(Order OtherOrder) {
    return this.buyerName.compareTo(OtherOrder.buyerName);
  }

  public int compareToTotalPrice(Order otherOrder) {
    return Double.compare(this.totalPrice, otherOrder.totalPrice);
  }

  public int compareToPurchaseDate(Order otherOrder) {
    return Long.compare(this.purchaseDate.getTime(), otherOrder.purchaseDate.getTime());
  }

  public String getBuyerName() {
    return buyerName;
  }

  public void setBuyerName(String buyerName) {
    this.buyerName = buyerName;
  }

  public ArrayList<Pair<String, Integer>> getProductsList() {
    return productsList;
  }

  public void setProductsList(ArrayList<Pair<String, Integer>> productsList) {
    this.productsList = productsList;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
  }
}