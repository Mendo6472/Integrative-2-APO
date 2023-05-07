package models;
import java.util.ArrayList;
import java.util.Date;
import datastructure.Pair;

public class Order {
  
  private String buyerName;
  private ArrayList<Pair<String, Integer>> productsList;
  private double totalPrice;
  private Date purchaseDate;

  public Order(String buyerName, ArrayList<Pair<String,Integer>> productsList, double totalPrice, Date purchaseDate) {
    this.buyerName = buyerName;
    this.productsList = productsList;
    this.totalPrice = totalPrice;
    this.purchaseDate = purchaseDate;
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