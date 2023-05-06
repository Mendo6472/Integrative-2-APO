package models;
import java.util.ArrayList;
import java.util.Date;
import datastructure.Pair;

public class Order {
  
  private String buyerName;
  private ArrayList<Pair<String, Integer>> productsList;
  private double totalPrice;
  private Date date;

  public Order() {
    
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

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }


}