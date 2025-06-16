package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreationTest {

    private String baseUrl = "https://qa-scooter.praktikum-services.ru/api/v1";
    private Integer courierId = null;

    @Before
    public void setUp() {
        RestAssured.baseURI = baseUrl;
    }

    @Step("Создание курьера с логином {login}")
    private void createCourier(String login, String password) {
        String courier = String.format(
            "{ \"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"Test\" }",
            login, password);

        given()
            .contentType("application/json")
            .body(courier)
            .when()
            .post("/courier")
            .then()
            .statusCode(201)
            .body("ok", equalTo(true));
    }

    @Step("Получение ID курьера")
    private Integer getCourierId(String login, String password) {
        return given()
            .contentType("application/json")
            .body(String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password))
            .when()
            .post("/courier/login")
            .then()
            .statusCode(200)
            .extract()
            .path("id");
    }

    @Test
    @Description("Успешное создание курьера")
    public void createCourierSuccess() {
        String login = "testuser" + System.currentTimeMillis();
        String password = "password123";

        createCourier(login, password);
        courierId = getCourierId(login, password);
    }

    @Test
    @Description("Нельзя создать двух одинаковых курьеров")
    public void createDuplicateCourier() {
        String login = "duplicateUser" + System.currentTimeMillis();
        String password = "password123";

        createCourier(login, password);
        courierId = getCourierId(login, password);

        given()
            .contentType("application/json")
            .body(String.format(
                "{ \"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"Test\" }",
                login, password))
            .when()
            .post("/courier")
            .then()
            .statusCode(409)
            .body("message", containsString("уже"));
    }

    @Test
    @Description("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        given()
            .contentType("application/json")
            .body("{ \"password\": \"pass\", \"firstName\": \"Name\" }")
            .when()
            .post("/courier")
            .then()
            .statusCode(400);
    }

    @Test
    @Description("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        given()
            .contentType("application/json")
            .body("{ \"login\": \"login\", \"firstName\": \"Name\" }")
            .when()
            .post("/courier")
            .then()
            .statusCode(400);
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            try {
                given()
                    .contentType("application/json")
                    .when()
                    .delete("/courier/" + courierId)
                    .then()
                    .statusCode(200);
            } catch (Exception e) {
                System.err.println("Failed to delete courier: " + e.getMessage());
            }
        }
    }
}