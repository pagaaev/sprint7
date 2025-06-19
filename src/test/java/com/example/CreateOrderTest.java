package com.example;

import com.example.model.Order;
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
}
