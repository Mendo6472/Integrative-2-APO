package models;
import java.util.ArrayList;

public class Controller {

  private ArrayList<Product> products;
  private ArrayList<Order> orders;

  public ArrayList<Product> getProducts() {
    return products;
  }

  public void setProducts(ArrayList<Product> products) {
    this.products = products;
  }

  public ArrayList<Order> getOrders() {
    return orders;
  }
  
  public void setOrders(ArrayList<Order> orders) {
    this.orders = orders;
  }
  
}