package models;

import datastructure.Pair;
import exceptions.InvalidSearchQueryException;
import exceptions.NoOrdersFoundException;
import exceptions.NoProductsFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearchOrderTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    Controller controller;

    @BeforeEach
    void setupStage1() throws Exception {
        controller = new Controller();
        controller.getOrders().clear();

        ArrayList<Pair<String, Integer>> firstProductList = new ArrayList<>();
        firstProductList.add(new Pair<>("Laptop", 1));
        ArrayList<Pair<String, Integer>> secondProductList = new ArrayList<>();
        secondProductList.add(new Pair<>("Iphone X", 1));
        ArrayList<Pair<String, Integer>> thirdProductList = new ArrayList<>();
        thirdProductList.add(new Pair<>("Laptop", 3));
        ArrayList<Pair<String, Integer>> fourthProductList = new ArrayList<>();
        fourthProductList.add(new Pair<>("Pencils", 10));

        Order firstOrder = new Order("Julian", firstProductList, 600);
        firstOrder.setPurchaseDate(new SimpleDateFormat("dd/MM/yyyy").parse("10/04/2023"));
        Order secondOrder = new Order("Santiago", secondProductList, 500);
        secondOrder.setPurchaseDate(new SimpleDateFormat("dd/MM/yyyy").parse("15/04/2023"));
        Order thirdOrder = new Order("Alejandro", thirdProductList, 1500);
        thirdOrder.setPurchaseDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/03/2023"));
        Order fourthOrder = new Order("Santiago", fourthProductList, 100);
        fourthOrder.setPurchaseDate(new SimpleDateFormat("dd/MM/yyyy").parse("20/03/2023"));

        controller.getOrders().add(firstOrder);
        controller.getOrders().add(secondOrder);
        controller.getOrders().add(thirdOrder);
        controller.getOrders().add(fourthOrder);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void searchOrderBuyerNameTest() throws Exception {
        controller.searchOrder(1,1,"Julian");
        assertEquals("""
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchTwoOrdersBuyerNameTest() throws Exception {
        controller.searchOrder(1,1,"Santiago");
        assertEquals("""
                Buyer's name: Santiago, Total price:500.0, Date: 15/4/2023\r
                Order:\r
                Product name: Iphone X, Ammount: 1\r
                Buyer's name: Santiago, Total price:100.0, Date: 20/3/2023\r
                Order:\r
                Product name: Pencils, Ammount: 10""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentOrderBuyerNameTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(1,1,"Ricardo");
            exception = false;
        } catch (NoOrdersFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }



    @Test
    void searchOrderBuyerNameIntervalTest() throws Exception {
        controller.searchOrder(1,1,"a::j");
        assertEquals("""
                Buyer's name: Alejandro, Total price:1500.0, Date: 20/3/2023\r
                Order:\r
                Product name: Laptop, Ammount: 3\r
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchOrderBuyerNameIntervalDescendingTest() throws Exception {
        controller.searchOrder(1,2,"a::j");
        assertEquals("""
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1\r
                Buyer's name: Alejandro, Total price:1500.0, Date: 20/3/2023\r
                Order:\r
                Product name: Laptop, Ammount: 3""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentOrderBuyerNameIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(1,1,"x::z");
            exception = false;
        } catch (NoOrdersFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongOrderBuyerNameIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(1,1,"z::x");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }



    @Test
    void searchOrderPriceTest() throws Exception {
        controller.searchOrder(2,1,"600");
        assertEquals("""
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchOrderWrongPriceTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(2,1,"wawa");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchNonExistentOrderPriceTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(2,1,"2000");
            exception = false;
        } catch (NoOrdersFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }




    @Test
    void searchOrderPriceIntervalTest() throws Exception {
        controller.searchOrder(2,1,"500::600");
        assertEquals("""
                Buyer's name: Santiago, Total price:500.0, Date: 15/4/2023\r
                Order:\r
                Product name: Iphone X, Ammount: 1\r
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchOrderPriceIntervalDescendingTest() throws Exception {
        controller.searchOrder(2,2,"500::600");
        assertEquals("""
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1\r
                Buyer's name: Santiago, Total price:500.0, Date: 15/4/2023\r
                Order:\r
                Product name: Iphone X, Ammount: 1""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentOrderPriceIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(2,1,"20::40");
            exception = false;
        } catch (NoOrdersFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongOrderPriceIntervalTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(2,1,"600::500");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongOrderPriceIntervalTest2() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(2,1,"a::c");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }




    @Test
    void searchOrderDateTest() throws Exception {
        controller.searchOrder(3,1,"20/03/2023");
        assertEquals("""
                Buyer's name: Alejandro, Total price:1500.0, Date: 20/3/2023\r
                Order:\r
                Product name: Laptop, Ammount: 3\r
                Buyer's name: Santiago, Total price:100.0, Date: 20/3/2023\r
                Order:\r
                Product name: Pencils, Ammount: 10""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchOrderDateTest2() throws Exception {
        controller.searchOrder(3,1,"10/04/2023");
        assertEquals("""
                Buyer's name: Julian, Total price:600.0, Date: 10/4/2023\r
                Order:\r
                Product name: Laptop, Ammount: 1""", outputStreamCaptor.toString().trim());
    }

    @Test
    void searchNonExistentOrderDateTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(3,1,"20/03/2011");
            exception = false;
        } catch (NoOrdersFoundException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }

    @Test
    void searchWrongOrderDateTest() throws Exception {
        boolean exception = true;
        try{
            controller.searchOrder(3,1,"Santiago");
            exception = false;
        } catch (InvalidSearchQueryException e){
            e.printStackTrace();
        }
        assertTrue(exception);
    }



}