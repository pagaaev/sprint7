package com.example;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private String baseUrl = "https://qa-scooter.praktikum-services.ru/api/v1";

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUrl;
    }

    @Test
    @Description("Проверка получения списка заказов")
    public void getOrdersListReturnsOrdersArray() {
        given()
            .header("Content-type", "application/json")
            .queryParam("limit", 3) // Ограничиваем количество заказов
            .when()
            .get("/orders")
            .then()
            .statusCode(200)
            .body("orders", hasSize(greaterThanOrEqualTo(0)))
            .body("orders[0]", hasKey("id"))
            .body("orders[0]", hasKey("status"))
            .body("orders[0]", hasKey("courierId"));
    }
}