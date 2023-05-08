package models;

import exceptions.InvalidSearchQueryException;
import exceptions.NoProductsFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SearchProductTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    Controller controller;

    @BeforeEach
    void setupStage1() throws Exception {
        controller = new Controller();
        controller.getProducts().clear();
        controller.getProducts().add(new Product("Laptop", "Portable computer", 500, 20, "ELECTRONIC"));
        controller.getProducts().add(new Product("Fahrenheit 451", "Novel of Ray Bradbury", 25, 20, "BOOKS"));
        Product thirdProduct = new Product("Iphone X", "Cellphone", 500, 50, "ELECTRONIC");
        thirdProduct.setTimesPurchased(30);
        controller.getProducts().add(thirdProduct);
        Product fourthProduct = new Product("Pencils", "School supplies", 10, 100, "STATIONERY_PRODUCTS");
        fourthProduct.setTimesPurchased(20);
        controller.getProducts().add(fourthProduct);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void searchOneProductTest() throws Exception {
        controller.searchProduct(1,1,"Laptop");
        assertEquals("Name: Laptop, Description: Portable computer, Price: 500.0, Quantity: 20, Category: ELECTRONIC, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentProductTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(1,1,"PS5");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchProductNameIntervalTest() throws Exception {
        controller.searchProduct(1,1,"e::j");
        assertEquals("Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0\r\n" +
                "Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductNameIntervalDescendingTest() throws Exception {
        controller.searchProduct(1,2,"e::j");
        assertEquals("Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30\r\n" +
                "Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentProductNameIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(1,1,"x::z");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongIntervalNameTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(1,1,"z::x");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchProductPriceTest() throws Exception {
        controller.searchProduct(2,1,"500");
        assertEquals("Name: Laptop, Description: Portable computer, Price: 500.0, Quantity: 20, Category: ELECTRONIC, Times sold: 0\r\n" +
                "Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductPriceDescendingTest() throws Exception {
        controller.searchProduct(2,2,"500");
        assertEquals("Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30\r\n" +
                "Name: Laptop, Description: Portable computer, Price: 500.0, Quantity: 20, Category: ELECTRONIC, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentProductPriceTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(2,1,"1000");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongPriceTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(2,1,"Laptop");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }




    @Test
    void searchProductPriceIntervalTest() throws Exception {
        controller.searchProduct(2,1,"10::25");
        assertEquals("Name: Pencils, Description: School supplies, Price: 10.0, Quantity: 100, Category: STATIONERY_PRODUCTS, Times sold: 20\r\n" +
                "Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductPriceIntervalDescendingTest() throws Exception {
        controller.searchProduct(2,2,"10::25");
        assertEquals("Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0\r\n" +
                "Name: Pencils, Description: School supplies, Price: 10.0, Quantity: 100, Category: STATIONERY_PRODUCTS, Times sold: 20", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentProductPriceIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(2,1,"1000::2000");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongPriceIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(2,1,"a::c");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongPriceIntervalTest2() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(2,1,"200::100");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }



    @Test
    void searchProductElectronicCategoryTest() throws Exception {
        controller.searchProduct(3,1,"ELECTRONIC");
        assertEquals("Name: Laptop, Description: Portable computer, Price: 500.0, Quantity: 20, Category: ELECTRONIC, Times sold: 0\r\n" +
                "Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductBooksCategoryTest() throws Exception {
        controller.searchProduct(3,1,"BOOKS");
        assertEquals("Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentCategoryTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(3,1,"DONUTS");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchCategoryNoProductsTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(3,1,"SPORTS");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }




    @Test
    void searchProductTimesSoldTest() throws Exception {
        controller.searchProduct(4,1,"0");
        assertEquals("Name: Laptop, Description: Portable computer, Price: 500.0, Quantity: 20, Category: ELECTRONIC, Times sold: 0\r\n" +
                "Name: Fahrenheit 451, Description: Novel of Ray Bradbury, Price: 25.0, Quantity: 20, Category: BOOKS, Times sold: 0", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductTimesSoldTest2() throws Exception {
        controller.searchProduct(4,1,"20");
        assertEquals("Name: Pencils, Description: School supplies, Price: 10.0, Quantity: 100, Category: STATIONERY_PRODUCTS, Times sold: 20", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentTimesSoldTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(4,1,"100");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongTimesSoldTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(4,1,"Wawa");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }



    @Test
    void searchProductTimesSoldIntervalTest() throws Exception {
        controller.searchProduct(4,1,"10::50");
        assertEquals("Name: Pencils, Description: School supplies, Price: 10.0, Quantity: 100, Category: STATIONERY_PRODUCTS, Times sold: 20\r\n" +
                "Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchProductTimesSoldDescendingTest() throws Exception {
        controller.searchProduct(4,2,"10::50");
        assertEquals("Name: Iphone X, Description: Cellphone, Price: 500.0, Quantity: 50, Category: ELECTRONIC, Times sold: 30\r\n" +
                "Name: Pencils, Description: School supplies, Price: 10.0, Quantity: 100, Category: STATIONERY_PRODUCTS, Times sold: 20", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentProductTimesSoldIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(2,1,"100::200");
            exception = false;
        } catch (NoProductsFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongTimesSoldIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(4,1,"a::c");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongTimesSoldDoubleTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(4,1,"1.5::3.4");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongTimesSoldTest3() throws Exception {
        boolean exception = true;
        try{
            controller.searchProduct(4,2,"50::10");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

}