package com.example;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> colors;
    private final String testName;
    private final String baseUrl = "https://qa-scooter.praktikum-services.ru";

    public CreateOrderTest(List<String> colors, String testName) {
        this.colors = colors;
        this.testName = testName;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] data() {
        return new Object[][]{
            {Arrays.asList("BLACK"), "Чёрный самокат"},
            {Arrays.asList("GRAY"), "Серый самокат"},
            {Arrays.asList("BLACK", "GRAY"), "Два цвета"},
            {null, "Без указания цвета"}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUrl;
    }

    @Test
    @Description("Создание заказа с разными цветами")
    public void createOrderWithColors() {
        given()
            .contentType(ContentType.JSON)
            .body(createOrderRequestBody())
            .when()
            .post("/api/v1/orders")
            .then()
            .statusCode(201)
            .body("track", notNullValue());
    }

    private Order createOrderRequestBody() {
        Order order = new Order();
        order.setFirstName("Naruto");
        order.setLastName("Uchiha");
        order.setAddress("Konoha, 142 apt.");
        order.setMetroStation("4");
        order.setPhone("+7 800 355 35 35");
        order.setRentTime(5);
        order.setDeliveryDate("2025-06-20");
        order.setComment("Saske, come back to Konoha");
        order.setColor(colors);
        return order;
    }

    // Внутренний класс для представления заказа
    private static class Order {
        private String firstName;
        private String lastName;
        private String address;
        private String metroStation;
        private String phone;
        private int rentTime;
        private String deliveryDate;
        private String comment;
        private List<String> color;

        // Геттеры
        public String getFirstName() {
            return firstName;
        }
        public String getLastName() {
            return lastName;
        }
        public String getAddress() {
            return address;
        }
        public String getMetroStation() {
            return metroStation;
        }
        public String getPhone() {
            return phone;
        }
        public int getRentTime() {
            return rentTime;
        }
        public String getDeliveryDate() {
            return deliveryDate;
        }
        public String getComment() {
            return comment;
        }
        public List<String> getColor() {
            return color;
        }

        // Сеттеры
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public void setMetroStation(String metroStation) {
            this.metroStation = metroStation;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public void setRentTime(int rentTime) {
            this.rentTime = rentTime;
        }
        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }
        public void setColor(List<String> color) {
            this.color = color;
        }
    }
}