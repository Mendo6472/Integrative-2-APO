package ui;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import datastructure.Pair;
import exceptions.ProductDoesNotExistException;
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

    public static void main(String[] args) throws Exception {
        Main main = new Main();       
        main.cleanConsole();

        int option = 0;

        do{
            try {
                option = main.getOptionShowMenu();
                main.cleanConsole();
                main.executeOption(option);
                System.out.println("Go to Menu...");
                main.cleanConsole();
            } catch (Exception e){
                e.printStackTrace();
            }
        }while(option != 0);

        main.getReader().close();

    }

    public int getOptionShowMenu() throws NumberFormatException {
        int option = 0;
        System.out.println("<<<<< Menu >>>>>");
        System.out.println(
                """
                        1. Add product
                        2. Add order
                        3. Increase inventory product
                        4. Search product
                        5. Search order
                        0. Exit"""
        );
        option =  validateIntegerInput();
        return option;
    }

    public void executeOption(int option) throws Exception {

        switch (option) {
            case 1 -> uiAddProduct();
            case 2 -> uiAddOrder();
            case 3 -> uiIncreaseInventoryProduct();
            case 4 -> uiSearchProduct();
            case 5 -> uiSearchOrder();
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid Option");
        }
    }

    public void uiAddProduct() throws Exception {
        System.out.print("Name: ");
        String name = reader.nextLine();

        System.out.print("Description: ");
        String description = reader.nextLine();

        System.out.print("Price: ");
        double price = Double.parseDouble(reader.next());

        System.out.print("Amount: ");
        int availableQuantity = validateIntegerInput();

        System.out.println("""
                List of categories:
                        BOOKS, ELECTRONIC, CLOTHING_AND_ACCESSORIES, FOOD_AND_BEVERAGES, STATIONERY_PRODUCTS, SPORTS,
                        BEAUTY_AND_PERSONAL_CARE_PRODUCTS, TOYS_AND_GAMES.
                """);
        System.out.print("Category: ");
        String categoryString = reader.next();
        controller.addProduct(name, description, price, availableQuantity, categoryString);
    }

    public void uiAddOrder() throws Exception {
        System.out.print("Buyer name: ");
        String buyerName = reader.nextLine();

        System.out.print("Quantity of products to order: ");
        int quantityTypeProducts = validateIntegerInput();

        ArrayList<Pair<String, Integer>> productsList = new ArrayList<>();
        double totalPrice = 0;
        for (int i = 0; i < quantityTypeProducts; i++) {
            System.out.print("Name product: ");
            String nameProduct = reader.nextLine();
            int productExist = controller.searchProduct(nameProduct);
            if (productExist != -1){
                Product product = controller.getProducts().get(productExist);
                System.out.print("Amount: ");
                int quantity = validateIntegerInput();
                if (product.getAvailableQuantity()-quantity >= 0 && quantity > 0){
                    Pair<String, Integer> pair = new Pair<>(nameProduct,quantity);
                    productsList.add(pair);
                    totalPrice += product.getPrice()*quantity;
                }else{
                    System.out.println("Sorry, available in stock: " + product.getAvailableQuantity());
                    quantityTypeProducts+=1;
                }
            }else{
                System.out.println("This product does not exist, try again.");
                quantityTypeProducts+=1;
            }
        }
        controller.addOrder(buyerName, productsList, totalPrice);

    }

    public void uiIncreaseInventoryProduct() throws Exception{
        System.out.print("Name: ");
        String name = reader.nextLine();
        System.out.print("Amount: ");
        int amount = validateIntegerInput();
        if(amount > 0) controller.addInventory(name, amount);
        else System.out.println("The increase must be positive");

    }

    public void uiSearchProduct() throws Exception{
        System.out.println("""
                Input your search option:
                1. Search by name/name interval
                2. Search by price/price interval
                3. Search by category/category name interval
                4. Search by times purchased/times purchased interval
                """);
        int option = validateIntegerInput();
        System.out.println("""
                Input the desired order of the results
                1. Ascending
                2. Descending
                """);
        int order = validateIntegerInput();
        System.out.println("""
                Input your search
                To search an interval you must specify the interval using "::"
                Example: Searching by name interval = a::c, Searching by price interval = 10::20
                """);
        String searchQuery = reader.nextLine();
        if(order < 1 || order > 2){
            System.out.println("Invalid order of results.");
            return;
        }
        controller.searchProduct(option,order,searchQuery);
    }

    public void uiSearchOrder() throws Exception{
        System.out.println("""
                Input your search option:
                1. Search by buyer name/name interval
                2. Search by total price/price interval
                3. Search by date/date interval
                """);
        int option = validateIntegerInput();
        System.out.println("""
                Input the desired order of the results
                1. Ascending
                2. Descending
                """);
        int order = validateIntegerInput();
        System.out.println("""
                Input your search
                To search an interval you must specify the interval using "::"
                Example: Searching by name interval = a::c, Searching by price interval = 10::20
                """);
        if(option == 3){
            System.out.println("The correct Syntax to search dates is dd/mm/yyyy, example: 05/05/2023, 02/05/2023::05/05/2023 ");
        }
        String searchQuery = reader.nextLine();
        if(order < 1 || order > 2){
            System.out.println("Invalid order of results.");
            return;
        }
        controller.searchOrder(option,order,searchQuery);
    }

    public Scanner getReader(){
        return reader;
    }

    public int validateIntegerInput() throws NumberFormatException{
        int option = 0;
        option = Integer.parseInt(reader.next());
        reader.nextLine();
        return option;
    }


    public void cleanConsole(){
        System.out.print("\033[H\033[2J");
    }
}
