package com.example.api;

import com.example.model.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private final String basePath = "/orders";

    @Step("Получение списка заказов с лимитом {limit}")
    public Response getOrdersList(int limit) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("limit", limit)
                .when()
                .get(basePath);
    }

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(basePath);
    }

    @Step("Получение заказа по номеру трека {track}")
    public Response getOrderByTrack(int track) {
        return given()
                .queryParam("t", track)
                .when()
                .get(basePath + "/track");
    }

    @Step("Отмена заказа с треком {track}")
    public Response cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"track\": " + track + "}")
                .when()
                .put(basePath + "/cancel");
    }
}
