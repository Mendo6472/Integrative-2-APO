package models;
import exceptions.*;

import java.util.ArrayList;

public class Controller {

  public Controller(){
    products = new ArrayList<>();
    orders = new ArrayList<>();
  }

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

  public void searchProduct(int searchOption, int order, String searchQuery) throws Exception{
    try{
      if(searchOption < 1 || searchOption > 4){
        throw new InvalidSearchOptionException();
      }
      switch(searchOption){
        case 1 -> searchProductName(searchOption, order, searchQuery);
        case 2 -> searchProductPrice(searchOption, order, searchQuery);
        case 3 -> searchProductCategory(searchOption, order, searchQuery);
        case 4 -> searchProductTimesPurchased(order, searchQuery);
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }


  private void searchOrderSingleQuery(){

  }

  private void searchOrderIntervalQuery(){

  }

  private void searchProductName(int option, int order, String searchQuery) throws Exception{
    products.sort(Product::compareToNames);
    try{
      if(searchQuery.contains("::")){
        searchProductIntervalQuery(option, order, searchQuery);
        return;
      }
      searchProductSingleQuery(option, order, searchQuery);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private void searchProductPrice(int option, int order, String searchQuery) throws Exception{
    products.sort(Product::compareToPrices);
    try{
      if(searchQuery.contains("::")){
        searchProductIntervalQuery(option, order, searchQuery);
        return;
      }
      searchProductSingleQuery(option, order, searchQuery);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private void searchProductCategory(int option, int order, String searchQuery){
    products.sort(Product::compareToCategories);
    try{
      if(searchQuery.contains("::")){
        searchProductIntervalQuery(option, order, searchQuery);
        return;
      }
      searchProductSingleQuery(option, order, searchQuery);
    } catch (Exception e){
      e.printStackTrace();
    }

  }

  private void searchProductTimesPurchased(int order, String searchQuery){

  }

  private void searchProductSingleQuery(int option, int order, String searchQuery) throws Exception{
    int begin = 0;
    int end = products.size() - 1;
    Integer timesSoldQuery = 0;
    Double priceQuery = 0.0;
    if(option == 2){
      try{
        priceQuery = Double.parseDouble(searchQuery);
      } catch (Exception e){
        e.printStackTrace();
        return;
      }
    }
    if(option == 4){
      try{
        timesSoldQuery = Integer.parseInt(searchQuery);
      }catch (Exception e){
        e.printStackTrace();
        return;
      }
    }
    while(begin <= end){
      int midPoint = (end + begin)/2;
      String midValue;
      Integer intMidValue;
      Double doubleMidValue;
      boolean condition = false;
      boolean secondCondition = false;
      switch (option) {
        case 1 -> {
          midValue = products.get(midPoint).getName();
          condition = midValue.compareTo(searchQuery) == 0;
          secondCondition = searchQuery.compareTo(midValue) > 0;
        }
        case 2 -> {
          doubleMidValue = products.get(midPoint).getPrice();
          condition = doubleMidValue.compareTo(priceQuery) == 0;
          secondCondition = priceQuery.compareTo(doubleMidValue) > 0;
        }
        case 3 -> {
          midValue = products.get(midPoint).getCategory().toString();
          condition = midValue.compareTo(searchQuery) == 0;
          secondCondition = searchQuery.compareTo(midValue) > 0;
        }
        case 4 -> {
          intMidValue = products.get(midPoint).getTimesPurchased();
          condition = intMidValue.compareTo(timesSoldQuery) == 0;
          secondCondition = timesSoldQuery.compareTo(intMidValue) > 0;
        }
      }
      if(condition){
        boolean stop = false;
        int startPoint = midPoint;
        int endPoint = midPoint;
        while (!stop){
          switch (option) {
            case 1 -> {
              String value = products.get(startPoint).getName();
              condition = value.compareTo(searchQuery) < 0;
            }
            case 2 -> {
              Double value = products.get(startPoint).getPrice();
              condition = value.compareTo(priceQuery) < 0;
            }
            case 3 -> {
              String value = products.get(startPoint).getCategory().toString();
              condition = value.compareTo(searchQuery) < 0;
            }
            case 4 -> {
              Integer value = products.get(startPoint).getTimesPurchased();
              condition = value.compareTo(timesSoldQuery) < 0;
            }
          }
          if(condition){
            stop = true;
            startPoint++;
          } else if (startPoint == 0) {
            stop = true;
          } else {
            startPoint--;
          }
        }
        stop = false;
        while (!stop){
          switch (option) {
            case 1 -> {
              String value = products.get(endPoint).getName();
              condition = value.compareTo(searchQuery) > 0;
            }
            case 2 -> {
              Double value = products.get(endPoint).getPrice();
              condition = value.compareTo(priceQuery) > 0;
            }
            case 3 -> {
              String value = products.get(endPoint).getCategory().toString();
              condition = value.compareTo(searchQuery) > 0;
            }
            case 4 -> {
              Integer value = products.get(endPoint).getTimesPurchased();
              condition = value.compareTo(timesSoldQuery) > 0;
            }
          }
          if(condition){
            stop = true;
            endPoint--;
          } else if (endPoint >= products.size() - 1) {
            stop = true;
          } else {
            endPoint++;
          }
        }
        printProducts(startPoint, endPoint, order);
        return;
      }else if(secondCondition){
        begin = midPoint + 1;
      }else{
        end = midPoint - 1;
      }
    }
    throw new NoProductsFoundException();
  }

  private void searchProductIntervalQuery(int option, int order, String searchQuery) throws  Exception {
    String[] interval = searchQuery.split("::");
    if(interval.length > 2){
      throw new InvalidSearchQueryException();
    }
    String intervalStart = interval[0];
    String intervalEnd = interval[1];
    Character characterIntervalStart = 0;
    Character characterIntervalEnd = 0;
    Integer integerIntervalStart = 0;
    Integer integerIntervalEnd = 0;
    Double doubleIntervalStart = 0.0;
    Double doubleIntervalEnd = 0.0;
    boolean condition = false;
    boolean secondCondition = false;
    try {
      switch (option) {
        case 1, 3 -> {
          characterIntervalStart = intervalStart.toLowerCase().charAt(0);
          characterIntervalEnd = intervalEnd.toLowerCase().charAt(0);
        }
        case 2 -> {
          doubleIntervalStart = Double.parseDouble(intervalStart);
          doubleIntervalEnd = Double.parseDouble(intervalEnd);
        }
        case 4 -> {
          integerIntervalStart = Integer.parseInt(intervalStart);
          integerIntervalEnd = Integer.parseInt(intervalEnd);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    int begin = 0;
    int end = products.size() - 1;
    while (begin <= end) {
      int midPoint = (end + begin) / 2;
      switch (option){
        case 1 -> {
          Character midValue = products.get(midPoint).getName().toLowerCase().charAt(0);
          condition = midValue.compareTo(characterIntervalStart) >= 0 && midValue.compareTo(characterIntervalEnd) <= 0;
          secondCondition = characterIntervalStart.compareTo(midValue) > 0;
        }
        case 2 -> {
          Double midValue = products.get(midPoint).getPrice();
          condition = midValue.compareTo(doubleIntervalStart) >= 0 && midValue.compareTo(doubleIntervalEnd) <= 0;
          secondCondition = doubleIntervalStart.compareTo(midValue) > 0;
        }
        case 3 -> {
          Character midValue = products.get(midPoint).getCategory().toString().toLowerCase().charAt(0);
          condition = midValue.compareTo(characterIntervalStart) >= 0 && midValue.compareTo(characterIntervalEnd) <= 0;
          secondCondition = characterIntervalStart.compareTo(midValue) > 0;
        }
        case 4 -> {
          Integer midValue = products.get(midPoint).getTimesPurchased();
          condition = midValue.compareTo(integerIntervalStart) >= 0 && midValue.compareTo(integerIntervalEnd) <= 0;
          secondCondition = integerIntervalStart.compareTo(midValue) > 0;
        }
      }
      if(condition) {
        boolean stop = false;
        int startPoint = midPoint;
        int endPoint = midPoint;
        while (!stop) {
          switch (option) {
            case 1 -> {
              Character value = products.get(startPoint).getName().toLowerCase().charAt(0);
              condition = value.compareTo(characterIntervalStart) < 0;
            }
            case 2 -> {
              Double value = products.get(startPoint).getPrice();
              condition = value.compareTo(doubleIntervalStart) < 0;
            }
            case 3 -> {
              Character value = products.get(startPoint).getCategory().toString().toLowerCase().charAt(0);
              condition = value.compareTo(characterIntervalStart) < 0;
            }
            case 4 -> {
              Integer value = products.get(startPoint).getTimesPurchased();
              condition = value.compareTo(integerIntervalStart) < 0;
            }
          }
          if (condition) {
            stop = true;
            startPoint++;
          } else if (startPoint == 0) {
            stop = true;
          } else {
            startPoint--;
          }
        }
        stop = false;
        while (!stop) {
          switch (option) {
            case 1 -> {
              Character value = products.get(endPoint).getName().charAt(0);
              condition = value.compareTo(characterIntervalEnd) > 0;
            }
            case 2 -> {
              Double value = products.get(endPoint).getPrice();
              condition = value.compareTo(doubleIntervalEnd) > 0;
            }
            case 3 -> {
              Character value = products.get(endPoint).getCategory().toString().charAt(0);
              condition = value.compareTo(characterIntervalEnd) > 0;
            }
            case 4 -> {
              Integer value = products.get(endPoint).getTimesPurchased();
              condition = value.compareTo(integerIntervalEnd) > 0;
            }
          }
          if (condition) {
            stop = true;
            endPoint--;
          } else if (endPoint >= products.size() - 1) {
            stop = true;
          } else {
            endPoint++;
          }
        }
        printProducts(startPoint, endPoint, order);
        return;
      }else if(secondCondition){
        begin = midPoint + 1;
      }else{
        end = midPoint - 1;
      }
    }
    throw new NoProductsFoundException();
  }

  private void printProducts(int startPoint, int endPoint, int order) {
    if(order == 1){
      for(int i = startPoint; i <= endPoint; i++){
        Product product = products.get(i);
        System.out.println("Name: " + product.getName() +", Description: " + product.getDescription() + ", Price: " + product.getPrice() + ", Quantity: " + product.getAvailableQuantity() + ", Category: " + product.getCategory().toString() + ", Times sold: " + product.getTimesPurchased());
      }
    } else {
      for(int i = endPoint; i >= startPoint; i--){
        Product product = products.get(i);
        System.out.println("Name: " + product.getName() +", Description: " + product.getDescription() + ", Price: " + product.getPrice() + ", Quantity: " + product.getAvailableQuantity() + ", Category: " + product.getCategory().toString() + ", Times sold: " + product.getTimesPurchased());
      }
    }

  }



  
}