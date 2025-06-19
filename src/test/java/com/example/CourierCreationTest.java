package com.example;

import com.example.api.CourierApi;
import com.example.model.Courier;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierCreationTest {

    private final CourierApi courierApi = new CourierApi();
    private Integer courierId = null;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }

    @Test
    @Description("Успешное создание курьера")
    public void createCourierSuccess() {
        String login = "testuser" + System.currentTimeMillis();
        String password = "password123";

        courierApi.createCourier(login, password, "Test")
                .then().statusCode(201)
                .body("ok", equalTo(true));

        Response loginResponse = courierApi.loginCourier(login, password);
        loginResponse.then().statusCode(200);
        courierId = loginResponse.path("id");
    }

    @Test
    @Description("Нельзя создать двух одинаковых курьеров")
    public void createDuplicateCourier() {
        String login = "duplicateUser" + System.currentTimeMillis();
        String password = "password123";

        courierApi.createCourier(login, password, "Test")
                .then().statusCode(201);

        courierId = courierApi.loginCourier(login, password)
                .then().statusCode(200)
                .extract().path("id");

        courierApi.createCourier(login, password, "Test")
                .then().statusCode(409)
                .body("message", containsString("уже"));
    }

    @Test
    @Description("Создание курьера без логина")
    public void createCourierWithoutLogin() {
        Courier courier = new Courier(null, "pass", "Name");

        courierApi.createCourier(courier)
                .then()
                .statusCode(400);
    }

    @Test
    @Description("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier("login", null, "Name");

        courierApi.createCourier(courier)
                .then()
                .statusCode(400);
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierApi.deleteCourier(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}
