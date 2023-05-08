//You need to put "products.json" found in "tests/data/productTest" in "src/data"
//Everytime you run this test you need to do that
package models;

import exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {



    Controller controller;
    Product firstProduct;
    Product secondProduct;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    void setupStage1(){
        controller = new Controller();
        controller.getProducts().clear();
    }

    void setupStage2() throws Exception {
        controller = new Controller();
        controller.getProducts().clear();
        firstProduct = new Product("Laptop", "Portable computer", 500, 20, "Electronic");
        secondProduct = new Product("Fahrenheit 451", "Novel of Ray Bradbury", 25, 20, "Books");
    }

    void setupStage3(){
        controller = new Controller();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void createProductTest(){
        boolean done = false;
        try{
            Product product = new Product("Laptop","Portable", 500, 20, "Electronic");
            done = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(done);
    }

    @Test
    void createProductWrongCategoryTest(){
        setupStage1();
        Exception exception = assertThrows(WrongCategoryException.class, () -> {
            Product product = new Product("Laptop","Portable", 500, 20, "abcd");
        });
    }

    @Test
    void createProductWrongPriceTest(){
        setupStage1();
        Exception exception = assertThrows(WrongPriceException.class, () -> {
            Product product = new Product("Laptop","Portable", -100, 20, "Electronic");
        });
    }

    @Test
    void createProductWrongQuantityTest(){
        setupStage1();
        Exception exception = assertThrows(WrongQuantityException.class, () -> {
            Product product = new Product("Laptop","Portable", 500, -10, "Electronic");
        });
    }

    @Test
    void addProductToListTest() throws Exception {
        setupStage2();
        boolean done = false;
        try{
            controller.getProducts().add(firstProduct);
            done = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(done);
    }

    @Test
    void addTwoProductsToListTest() throws Exception {
        setupStage2();
        boolean done = false;
        try{
            controller.getProducts().add(firstProduct);
            controller.getProducts().add(secondProduct);
            done = true;
        } catch (Exception e){
            e.printStackTrace();
        }
        assertTrue(done);
    }

    @Test
    void searchProductAddedByPersistenceTest() throws Exception {
        setupStage3();
        controller.searchProduct(1,1,"Laptop");
        //Quantity = 30 because another tests adds stock to this
        assertEquals("Name: Laptop, Description: Portable Computer, Price: 500.0, Quantity: 30, Category: ELECTRONIC, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductAddedByPersistenceTest2() throws Exception {
        setupStage3();
        controller.searchProduct(1,1, "Fahrenheit 451");
        assertEquals("Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0",outputStreamCaptor.toString().trim());
    }

    @Test
    void addInventoryTest() throws Exception {
        setupStage3();
        String productName = "Laptop";
        int stockToAdd = 10;
        controller.addInventory(productName, stockToAdd);
        int laptop = controller.searchProduct("Laptop");
        assertEquals(controller.getProducts().get(laptop).getAvailableQuantity(), 30);
    }

    @Test
    void addNegativeInventoryTest() throws Exception {
        setupStage3();
        Exception exception = assertThrows(WrongQuantityException.class, () -> {
            String productName = "Laptop";
            int stockToAdd = -10;
            controller.addInventory(productName, stockToAdd);
        });
        int laptop = controller.searchProduct("Laptop");
        //Is 30 because another tests adds 10 of stock to this product
        assertEquals(controller.getProducts().get(laptop).getAvailableQuantity(), 30);
    }

    @Test
    void addInventoryNonExistentProductTest() throws Exception {
        setupStage3();
        Exception exception = assertThrows(ProductDoesNotExistException.class, () -> {
            String productName = "PS5";
            int stockToAdd = 10;
            controller.addInventory(productName, stockToAdd);
        });
    }


}