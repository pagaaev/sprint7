package com.example.tests;

import com.example.api.OrderApi;
import com.example.model.Order;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private final OrderApi orderApi = new OrderApi();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }

    @Test
    @Description("Создание заказа и проверка успешного создания")
    public void createOrderSuccess() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "ул. Пушкина, д. 10",
                "+79270000000",
                "5",
                "2025-07-01",
                "Комментарий"
        );

        Response response = orderApi.createOrder(order);
        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }

    @Test
    @Description("Получение списка заказов")
    public void getOrdersListTest() {
        Response response = orderApi.getOrdersList(5);
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    @Description("Получение заказа по трек-номеру")
    public void getOrderByTrackTest() {
        // Создаем заказ, чтобы получить track
        Order order = new Order(
                "Иван",
                "Иванов",
                "ул. Пушкина, д. 10",
                "+79270000000",
                "3",
                "2025-07-01",
                "Комментарий"
        );

        int track = orderApi.createOrder(order)
                .then()
                .statusCode(201)
                .extract()
                .path("track");

        orderApi.getOrderByTrack(track)
                .then()
                .statusCode(200)
                .body("order", notNullValue())
                .body("order.track", equalTo(track));
    }
}
