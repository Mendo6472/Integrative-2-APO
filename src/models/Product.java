package models;

public class Product {

  private String name;
  private String description;
  private double price;
  private int availableQuantity;
  private Category category; 
  private int timesPurchased;

  public Product() {

  }
  
  public int compareToNames(Product otherProduct){
    return this.name.compareTo(otherProduct.name);
  }

  public int compareToPrices(Product otherProduct) {
    return Double.compare(otherProduct.price, this.price);
  }

  public int compareToCategories(Product otherProduct) {
    return this.category.toString().compareTo(otherProduct.category.toString());
  }

  public int compareToTimesPurchased(Product otherProduct) {
    return Integer.compare(this.timesPurchased, otherProduct.timesPurchased);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getAvailableQuantity() {
    return availableQuantity;
  }

  public void setAvailableQuantity(int availableQuantity) {
    this.availableQuantity = availableQuantity;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getTimesPurchased() {
    return timesPurchased;
  }

  public void setTimesPurchased(int timesPurchased) {
    this.timesPurchased = timesPurchased;
  }
  
}