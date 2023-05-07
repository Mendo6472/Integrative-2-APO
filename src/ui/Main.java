package ui;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import datastructure.Pair;
import models.Category;

import models.Controller;
import models.Product;

public class Main {

    private Controller controller;
    private Scanner reader;

    public Main() throws FileNotFoundException {
        controller = new Controller();
        reader = new Scanner(System.in);

    }

    public static void main(String[] args) throws FileNotFoundException {
        Main main = new Main();       
        main.cleanConsole();

        int option = 0;

        do{
            option = main.getOptionShowMenu();
            main.cleanConsole();
            main.executeOption(option);
            main.reader.nextLine();
            System.out.print("Go to Menu...");
            main.reader.nextLine();
            main.cleanConsole();
        }while(option != 0);

        main.getReader().close();

    }

    public int getOptionShowMenu(){
        int option = 0;
        System.out.println("<<<<< Menu >>>>>");
        System.out.println(
            """
                    1. Add product
                    2. Add order
                    3. Increase inventory product
                    0. Exit"""
                        );
        option =  validateIntegerInput();
        return option;
    }

    public void executeOption(int option){
            
        switch(option) {
            case 1:
                uiAddProduct();
                break;
            case 2:
                uiAddOrder();
                break;
            case 3:
                uiIncreaseInventoryProduct();
                break;
            case 4:
                break;
            case 0:
                System.out.println("Exit program.");
                break;

            default:
                System.out.println("Invalid Option");
                break;
        }
    }

    public void uiAddProduct(){
        System.out.print("Name: ");
        String name = reader.next();

        System.out.print("Description: ");
        String description = reader.next();

        System.out.print("Price: ");
        double price = reader.nextDouble();

        System.out.print("Amount: ");
        int availableQuantity = reader.nextInt();

        System.out.print("Category: ");
        String categoryString = reader.next();
        Category category = Category.valueOf(categoryString);

        controller.addProduct(name, description, price, availableQuantity, category);
    }

    public void uiAddOrder(){
        System.out.print("Buyer name: ");
        String buyerName = reader.next();

        System.out.print("Quantity type products: ");
        int quantityTypeProducts = reader.nextInt();

        ArrayList<Pair<String, Integer>> productsList = new ArrayList<>();
        double totalPrice = 0;
        Date date = new Date();
        for (int i = 0; i < quantityTypeProducts; i++) {
            System.out.print("Name product: ");
            String nameProduct = reader.next();
            int productExist = controller.searchProduct(nameProduct);
            if (productExist != -1){
                Product product = controller.getProducts().get(productExist);
                System.out.print("Amount: ");
                int quantity = reader.nextInt();
                if (product.getAvailableQuantity()-quantity >= 0 && quantity > 0){
                    Pair<String, Integer> pair = new Pair<>(nameProduct,quantity);
                    productsList.add(pair);
                    totalPrice += product.getPrice()*quantity;
                    product.setTimesPurchased(product.getTimesPurchased()+quantity);
                    product.setAvailableQuantity(product.getAvailableQuantity()-quantity);
                }else{
                    System.out.println("Sorry, available in stock: " + product.getAvailableQuantity());
                    quantityTypeProducts+=1;
                }
            }else{
                System.out.println("This product does not exist, try again.");
                quantityTypeProducts+=1;
            }
        }
        controller.addOrder(buyerName, productsList, totalPrice, date);

    }

    public void uiIncreaseInventoryProduct(){
        System.out.print("Name: ");
        String name = reader.next();
        System.out.print("Amount: ");
        int amount = reader.nextInt();
        if(amount > 0) controller.increaseInventoryProduct(name, amount);
        else System.out.println("The increase must be positive");
    }

    public Scanner getReader(){
        return reader;
    }

    public int validateIntegerInput(){
        int option = 0;

        if(reader.hasNextInt()){
            option = reader.nextInt();
        }
        else{
            // clear reader.
            reader.nextLine();
            option = -1;
        }

        return option;
    }


    public void cleanConsole(){
        System.out.print("\033[H\033[2J");
    }
}
