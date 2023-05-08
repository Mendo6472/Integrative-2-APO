package models;
import exceptions.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import java.util.Date;
import datastructure.Pair;
import java.util.Calendar;
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

  public void addProduct(String name, String description, double price, int availableQuantity, String category) throws Exception {
    Product product;
    int productExist = searchProduct(name);
    if (productExist != -1) {
      throw new ProductAlreadyExistsException();
    } else {
      product = new Product(name, description, price, availableQuantity, category);
      products.add(product);
    }
    connectionProducts.updateEntity(products);
  }

  public void addOrder(String buyerName, ArrayList<Pair<String,Integer>> productsList, double totalPrice) throws Exception {
    for(Pair<String, Integer> product : productsList){
      int pos = searchProduct(product.getLeft());
      if(pos == -1) throw new ProductDoesNotExistException();
      if(products.get(pos).getAvailableQuantity() < product.getRight()) throw new NotEnoughStockException();
      products.get(pos).setAvailableQuantity(products.get(pos).getAvailableQuantity()-product.getRight());
      products.get(pos).setTimesPurchased(products.get(pos).getTimesPurchased() + product.getRight());
    }
    Order order = new Order(buyerName, productsList, totalPrice);
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

  public void addInventory(String name, int amount) throws ProductDoesNotExistException, WrongQuantityException {
    if(amount <= 0) throw new WrongQuantityException();
    Product product;
    int productExist = searchProduct(name);
    if (productExist != -1) {
      product = products.get(productExist);
      product.setAvailableQuantity(product.getAvailableQuantity()+amount);
      connectionProducts.updateEntity(products);
    }else{
      throw new ProductDoesNotExistException();
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

  public void searchProduct(int searchOption, int order, String searchQuery) throws Exception{
    if(searchOption < 1 || searchOption > 4){
      throw new InvalidSearchOptionException();
    }
    switch(searchOption){
      case 1 -> searchProductName(searchOption, order, searchQuery);
      case 2 -> searchProductPrice(searchOption, order, searchQuery);
      case 3 -> searchProductCategory(searchOption, order, searchQuery);
      case 4 -> searchProductTimesPurchased(searchOption, order, searchQuery);
    }
  }

  public void searchOrder(int searchOption, int order, String searchQuery) throws Exception{
    if(searchOption < 1 || searchOption > 3){
      throw new InvalidSearchOptionException();
    }
    switch(searchOption){
      case 1 -> searchOrderBuyerName(searchOption, order, searchQuery);
      case 2 -> searchOrderTotalPrice(searchOption, order, searchQuery);
      case 3 -> searchOrderDate(searchOption, order, searchQuery);
    }
  }

  private void searchProductName(int option, int order, String searchQuery) throws Exception{
    products.sort(Product::compareToNames);
    if(searchQuery.contains("::")){
      searchProductIntervalQuery(option, order, searchQuery);
      return;
    }
    searchProductSingleQuery(option, order, searchQuery);
  }

  private void searchProductPrice(int option, int order, String searchQuery) throws Exception{
    products.sort(Product::compareToPrices);
    if(searchQuery.contains("::")){
      searchProductIntervalQuery(option, order, searchQuery);
      return;
    }
    searchProductSingleQuery(option, order, searchQuery);
  }

  private void searchProductCategory(int option, int order, String searchQuery) throws Exception {
    products.sort(Product::compareToCategories);
    if(searchQuery.contains("::")){
      searchProductIntervalQuery(option, order, searchQuery);
      return;
    }
    searchProductSingleQuery(option, order, searchQuery);

  }

  private void searchProductTimesPurchased(int option, int order, String searchQuery) throws Exception {
    products.sort(Product::compareToTimesPurchased);
    if(searchQuery.contains("::")){
      searchProductIntervalQuery(option, order, searchQuery);
      return;
    }
    searchProductSingleQuery(option, order, searchQuery);
  }

  private void searchOrderBuyerName(int option, int order, String searchQuery) throws Exception {
    orders.sort(Order::compareToBuyerName);
    if(searchQuery.contains("::")){
      searchOrderIntervalQuery(option, order, searchQuery);
      return;
    }
    searchOrderSingleQuery(option, order, searchQuery);
  }

  private void searchOrderTotalPrice(int option, int order, String searchQuery) throws Exception {
    orders.sort(Order::compareToTotalPrice);
    if(searchQuery.contains("::")){
      searchOrderIntervalQuery(option, order, searchQuery);
      return;
    }
    searchOrderSingleQuery(option, order, searchQuery);
  }

  private void searchOrderDate(int option, int order, String searchQuery) throws Exception {
    orders.sort(Order::compareToPurchaseDate);
    if(searchQuery.contains("::")){
      searchOrderIntervalQuery(option, order, searchQuery);
      return;
    }
    searchOrderSingleQuery(option, order, searchQuery);
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
        throw new InvalidSearchQueryException();
      }
    }
    if(option == 4){
      try{
        timesSoldQuery = Integer.parseInt(searchQuery);
      }catch (Exception e){
        throw new InvalidSearchQueryException();
      }
    }
    if(option == 3){
      try{
        Category category = Category.valueOf(searchQuery);
      } catch (Exception e){
        throw new InvalidSearchQueryException();
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
    boolean wrongIntervalCondition = false;
    try {
      switch (option) {
        case 1, 3 -> {
          characterIntervalStart = intervalStart.toLowerCase().charAt(0);
          characterIntervalEnd = intervalEnd.toLowerCase().charAt(0);
          wrongIntervalCondition = characterIntervalStart.compareTo(characterIntervalEnd) > 0;
        }
        case 2 -> {
          doubleIntervalStart = Double.parseDouble(intervalStart);
          doubleIntervalEnd = Double.parseDouble(intervalEnd);
          wrongIntervalCondition = doubleIntervalStart.compareTo(doubleIntervalEnd) > 0;
        }
        case 4 -> {
          integerIntervalStart = Integer.parseInt(intervalStart);
          integerIntervalEnd = Integer.parseInt(intervalEnd);
          wrongIntervalCondition = integerIntervalStart.compareTo(integerIntervalEnd) > 0;
        }
      }
      if(wrongIntervalCondition){
        throw new InvalidSearchQueryException();
      }
    } catch (Exception e) {
      throw new InvalidSearchQueryException();
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
              Character value = products.get(endPoint).getName().toLowerCase().charAt(0);
              condition = value.compareTo(characterIntervalEnd) > 0;
            }
            case 2 -> {
              Double value = products.get(endPoint).getPrice();
              condition = value.compareTo(doubleIntervalEnd) > 0;
            }
            case 3 -> {
              Character value = products.get(endPoint).getCategory().toString().toLowerCase().charAt(0);
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

  private void searchOrderSingleQuery(int option, int order, String searchQuery) throws Exception{
    int begin = 0;
    int end = orders.size() - 1;
    Double doubleQuery = 0.0;
    Date dateQuery = new Date(System.currentTimeMillis());
    if(option == 2){
      try{
        doubleQuery = Double.parseDouble(searchQuery);
      } catch (Exception e){
        throw new InvalidSearchQueryException();
      }
    }
    if(option == 3){
      try{
        dateQuery = new SimpleDateFormat("dd/MM/yyyy").parse(searchQuery);
      }catch (Exception e){
        throw new InvalidSearchQueryException();
      }
    }
    while(begin <= end){
      int midPoint = (end + begin)/2;
      String midValue;
      Double doubleMidValue;
      Date dateMidValue;
      boolean condition = false;
      boolean secondCondition = false;
      switch (option) {
        case 1 -> {
          midValue = orders.get(midPoint).getBuyerName();
          condition = midValue.compareTo(searchQuery) == 0;
          secondCondition = searchQuery.compareTo(midValue) > 0;
        }
        case 2 -> {
          doubleMidValue = orders.get(midPoint).getTotalPrice();
          condition = doubleMidValue.compareTo(doubleQuery) == 0;
          secondCondition = doubleQuery.compareTo(doubleMidValue) > 0;
        }
        case 3 -> {
          dateMidValue = orders.get(midPoint).getPurchaseDate();
          condition = dateMidValue.compareTo(dateQuery) == 0;
          secondCondition = dateQuery.compareTo(dateMidValue) > 0;
        }
      }
      if(condition){
        boolean stop = false;
        int startPoint = midPoint;
        int endPoint = midPoint;
        while (!stop){
          switch (option) {
            case 1 -> {
              String value = orders.get(startPoint).getBuyerName();
              condition = value.compareTo(searchQuery) < 0;
            }
            case 2 -> {
              Double value = orders.get(startPoint).getTotalPrice();
              condition = value.compareTo(doubleQuery) < 0;
            }
            case 3 -> {
              Date value = orders.get(startPoint).getPurchaseDate();
              condition = value.compareTo(dateQuery) < 0;
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
              String value = orders.get(endPoint).getBuyerName();
              condition = value.compareTo(searchQuery) > 0;
            }
            case 2 -> {
              Double value = orders.get(endPoint).getTotalPrice();
              condition = value.compareTo(doubleQuery) > 0;
            }
            case 3 -> {
              Date value = orders.get(endPoint).getPurchaseDate();
              condition = value.compareTo(dateQuery) > 0;
            }
          }
          if(condition){
            stop = true;
            endPoint--;
          } else if (endPoint >= orders.size() - 1) {
            stop = true;
          } else {
            endPoint++;
          }
        }
        printOrders(startPoint, endPoint, order);
        return;
      }else if(secondCondition){
        begin = midPoint + 1;
      }else{
        end = midPoint - 1;
      }
    }
    throw new NoOrdersFoundException();
  }

  private void searchOrderIntervalQuery(int option, int order, String searchQuery) throws  Exception {
    String[] interval = searchQuery.split("::");
    if(interval.length > 2){
      throw new InvalidSearchQueryException();
    }
    String intervalStart = interval[0];
    String intervalEnd = interval[1];
    Character characterIntervalStart = 0;
    Character characterIntervalEnd = 0;
    Double doubleIntervalStart = 0.0;
    Double doubleIntervalEnd = 0.0;
    Date dateIntervalStart = new Date(System.currentTimeMillis());
    Date dateIntervalEnd = new Date(System.currentTimeMillis());
    boolean condition = false;
    boolean secondCondition = false;
    boolean wrongIntervalCondition = false;
    try {
      switch (option) {
        case 1 -> {
          characterIntervalStart = intervalStart.toLowerCase().charAt(0);
          characterIntervalEnd = intervalEnd.toLowerCase().charAt(0);
          wrongIntervalCondition = characterIntervalStart.compareTo(characterIntervalEnd) > 0;
        }
        case 2 -> {
          doubleIntervalStart = Double.parseDouble(intervalStart);
          doubleIntervalEnd = Double.parseDouble(intervalEnd);
          wrongIntervalCondition = doubleIntervalStart.compareTo(doubleIntervalEnd) > 0;
        }
        case 3 -> {
          dateIntervalStart = new SimpleDateFormat("dd/MM/yyyy").parse(intervalStart);
          dateIntervalEnd = new SimpleDateFormat("dd/MM/yyyy").parse(intervalEnd);
          wrongIntervalCondition = dateIntervalStart.compareTo(dateIntervalEnd) > 0;
        }
      }
      if(wrongIntervalCondition){
        throw new InvalidSearchQueryException();
      }
    } catch (Exception e) {
      throw new InvalidSearchQueryException();
    }
    int begin = 0;
    int end = orders.size() - 1;
    while (begin <= end) {
      int midPoint = (end + begin) / 2;
      switch (option){
        case 1 -> {
          Character midValue = orders.get(midPoint).getBuyerName().toLowerCase().charAt(0);
          condition = midValue.compareTo(characterIntervalStart) >= 0 && midValue.compareTo(characterIntervalEnd) <= 0;
          secondCondition = characterIntervalStart.compareTo(midValue) > 0;
        }
        case 2 -> {
          Double midValue = orders.get(midPoint).getTotalPrice();
          condition = midValue.compareTo(doubleIntervalStart) >= 0 && midValue.compareTo(doubleIntervalEnd) <= 0;
          secondCondition = doubleIntervalStart.compareTo(midValue) > 0;
        }
        case 3 -> {
          Date midValue = orders.get(midPoint).getPurchaseDate();
          condition = midValue.compareTo(dateIntervalStart) >= 0 && midValue.compareTo(dateIntervalEnd) <= 0;
          secondCondition = dateIntervalStart.compareTo(midValue) > 0;
        }
      }
      if(condition) {
        boolean stop = false;
        int startPoint = midPoint;
        int endPoint = midPoint;
        while (!stop) {
          switch (option) {
            case 1 -> {
              Character value = orders.get(startPoint).getBuyerName().toLowerCase().charAt(0);
              condition = value.compareTo(characterIntervalStart) < 0;
            }
            case 2 -> {
              Double value = orders.get(startPoint).getTotalPrice();
              condition = value.compareTo(doubleIntervalStart) < 0;
            }
            case 3 -> {
              Date value = orders.get(startPoint).getPurchaseDate();
              condition = value.compareTo(dateIntervalStart) < 0;
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
              Character value = orders.get(endPoint).getBuyerName().toLowerCase().charAt(0);
              condition = value.compareTo(characterIntervalEnd) > 0;
            }
            case 2 -> {
              Double value = orders.get(endPoint).getTotalPrice();
              condition = value.compareTo(doubleIntervalEnd) > 0;
            }
            case 3 -> {
              Date value = orders.get(endPoint).getPurchaseDate();
              condition = value.compareTo(dateIntervalStart) > 0;
            }
          }
          if (condition) {
            stop = true;
            endPoint--;
          } else if (endPoint >= orders.size() - 1) {
            stop = true;
          } else {
            endPoint++;
          }
        }
        printOrders(startPoint, endPoint, order);
        return;
      }else if(secondCondition){
        begin = midPoint + 1;
      }else{
        end = midPoint - 1;
      }
    }
    throw new NoOrdersFoundException();
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

  private void printOrders(int startPoint, int endPoint, int order){
    if(order == 1){
      for(int i = startPoint; i <= endPoint; i++){
        Order currentOrder = orders.get(i);
        Calendar date = Calendar.getInstance();
        date.setTime(currentOrder.getPurchaseDate());
        System.out.println("Buyer's name: " + currentOrder.getBuyerName() + ", Total price:" + currentOrder.getTotalPrice() + ", Date: " + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.YEAR) );
        System.out.println("Order:");
        ArrayList<Pair<String, Integer>> productList = currentOrder.getProductsList();
        for (Pair<String, Integer> stringIntegerPair : productList) {
          System.out.println("Product name: " + stringIntegerPair.getLeft() + ", Ammount: " + stringIntegerPair.getRight());
        }
      }
    } else {
      for(int i = endPoint; i >= startPoint; i--){
        Order currentOrder = orders.get(i);
        Calendar date = Calendar.getInstance();
        date.setTime(currentOrder.getPurchaseDate());
        System.out.println("Buyer's name: " + currentOrder.getBuyerName() + ", Total price:" + currentOrder.getTotalPrice() + ", Date: " + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.YEAR) );
        System.out.println("Order:");
        ArrayList<Pair<String, Integer>> productList = currentOrder.getProductsList();
        for (Pair<String, Integer> stringIntegerPair : productList) {
          System.out.println("Product name: " + stringIntegerPair.getLeft() + ", Ammount: " + stringIntegerPair.getRight());
        }
      }
    }
  }



  
}