package models;
import java.util.ArrayList;
import java.util.Date;
import com.google.gson.reflect.TypeToken;
import datastructure.Pair;
public class Controller {

  private ArrayList<Product> products;
  private ArrayList<Order> orders;
  private Connection<Product> connectionProducts = new Connection<>("src/data/products.json", new TypeToken<ArrayList<Product>>(){}.getType());
  private Connection<Order> connectionOrders = new Connection<>("src/data/orders.json", new TypeToken<ArrayList<Order>>(){}.getType());

  public Controller(){
    products = new ArrayList<>();
    orders = new ArrayList<>();
    products = connectionProducts.getEntity();
    orders = connectionOrders.getEntity();
  }

  public void addProduct(String name, String description, double price, int availableQuantity, Category category) {
    Product product;
    int productExist = searchProduct(name);
    if (productExist != -1) {
      product = products.get(productExist);
      product.setAvailableQuantity(product.getAvailableQuantity()+availableQuantity);
    } else {
      product = new Product(name, description, price, availableQuantity, category);
      products.add(product);
    }
    connectionProducts.updateEntity(products);
  }

  public void addOrder(String buyerName, ArrayList<Pair<String,Integer>> productsList, double totalPrice, Date purchaseDate) {
    Order order = new Order(buyerName, productsList, totalPrice, purchaseDate);
    orders.add(order);
    connectionProducts.updateEntity(products);
    connectionOrders.updateEntity(orders);
  }

  public int searchProduct(String name) {
    int itWasFound = -1;
    for (int i = 0; i < products.size(); i++) {
      if (products.get(i).getName().equals(name)) {
        itWasFound = i;
        break;
      }
    }
    return itWasFound;
  }

  public void increaseInventoryProduct(String name, int amount){
    Product product;
    int productExist = searchProduct(name);
    if (productExist != -1) {
      product = products.get(productExist);
      product.setAvailableQuantity(product.getAvailableQuantity()+amount);
      connectionProducts.updateEntity(products);
    }else{
      System.out.println("This products does not exist");
    }
  }

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

  public static <T extends Comparable<T>> void insertionSort(ArrayList<T> arr) {
    for (int i = 1; i < arr.size(); i++) {
      T key = arr.get(i);
      int j = i - 1;
      while (j >= 0 && arr.get(j).compareTo(key) > 0) {
        arr.set(j + 1, arr.get(j));
        j -= 1;
      }
      arr.set(j + 1, key);
    }
  }

  public static <T extends Comparable<T>> int binarySearch(T goal, ArrayList<T> arr) {
    insertionSort(arr);
    int begin = 0;
    int end = arr.size() - 1;
    while(begin <= end){
      int midPoint = (end + begin)/2;
      T midValue = arr.get(midPoint);
      if(midValue.compareTo(goal) == 0){
        return midPoint;
      }else if(goal.compareTo(midValue) > 0){
        begin = midPoint + 1;
      }else{
        end = midPoint - 1;
      }
    }
    return -1;
  }
  
}