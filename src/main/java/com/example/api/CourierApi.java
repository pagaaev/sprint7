package com.example.api;

import com.example.model.Courier;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    private final String BASE_URL = "https://qa-scooter.praktikum-services.ru/api/v1";

    public CourierApi() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .contentType("application/json")
                .body(courier)
                .when()
                .post("/courier");
    }

    @Step("Логин курьера")
    public Response loginCourier(String login, String password) {
        return given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("/courier/login");
    }

    @Step("Удаление курьера по ID: {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .when()
                .delete("/courier/" + courierId);
    }
}
