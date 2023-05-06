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