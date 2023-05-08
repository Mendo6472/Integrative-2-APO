//Before starting the test you should put the products.json and orders.json that are found on Tests/data/orderTest into src/data
//Everytime you run this test you need to do that

package models;

import datastructure.Pair;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;

import exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    Controller controller;

    void setupStage1(){
        controller = new Controller();
        controller.getProducts().get(0).setAvailableQuantity(20);
    }

    @Test
    void placeOrderTest() throws Exception {
        setupStage1();
        ArrayList<Pair<String, Integer>> productsList = new ArrayList<>();
        Pair<String, Integer> firstOrder = new Pair<>("Laptop", 1);
        Pair<String, Integer> secondOrder = new Pair<>("Fahrenheit 451", 1);
        productsList.add(firstOrder);
        productsList.add(secondOrder);
        String buyerName = "Julian";
        double totalPrice = 525;
        controller.addOrder(buyerName, productsList, totalPrice);
        int laptop = controller.searchProduct("Laptop");
        int book = controller.searchProduct("Fahrenheit 451");
        assertEquals(controller.getProducts().get(laptop).getAvailableQuantity(), 19);
        assertEquals(controller.getProducts().get(book).getAvailableQuantity(), 19);
    }

    @Test
    void placeOrderNotEnoughStockTest() throws ParseException {
        setupStage1();
        Exception exception = assertThrows(NotEnoughStockException.class, () -> {
            ArrayList<Pair<String, Integer>> productsList = new ArrayList<>();
            Pair<String, Integer> firstOrder = new Pair<>("Laptop", 25);
            productsList.add(firstOrder);
            String buyerName = "Julian";
            double totalPrice = 525;
            controller.addOrder(buyerName, productsList, totalPrice);
        });
        int laptop = controller.searchProduct("Laptop");
        assertEquals(controller.getProducts().get(laptop).getAvailableQuantity(), 20);
    }


}