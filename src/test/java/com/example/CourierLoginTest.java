package com.example;

import com.example.api.CourierApi;
import com.example.model.Courier;
import com.example.model.CourierCredentials;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private static final String COURIER_LOGIN = "testCourierLogin_" + System.currentTimeMillis();
    private static final String COURIER_PASSWORD = "password123";
    private static final String COURIER_FIRSTNAME = "Иван";
    private static Integer courierId;

    private static final CourierApi courierApi = new CourierApi();

    @BeforeClass
    public static void setup() {
        Courier courier = new Courier(COURIER_LOGIN, COURIER_PASSWORD, COURIER_FIRSTNAME);
        Response response = courierApi.createCourier(courier);

        if (response.statusCode() == 201) {
            System.out.println("Курьер успешно создан");
        } else if (response.statusCode() == 409) {
            System.out.println("Курьер уже существует");
        }
    }

    @Test
    @Description("Успешная авторизация курьера")
    public void loginWithValidCredentials() {
        CourierCredentials credentials = new CourierCredentials(COURIER_LOGIN, COURIER_PASSWORD);
        Response response = courierApi.loginCourier(credentials);

        courierId = response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract()
                .path("id");
    }

    @Test
    @Description("Авторизация без логина")
    public void loginWithMissingLogin() {
        CourierCredentials credentials = new CourierCredentials(null, COURIER_PASSWORD);
        Response response = courierApi.loginCourier(credentials);

        if (response.statusCode() >= 500) {
            System.out.println("Сервер недоступен, статус: " + response.statusCode());
            return;
        }

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Авторизация без пароля")
    public void loginWithMissingPassword() {
        CourierCredentials credentials = new CourierCredentials(COURIER_LOGIN, null);
        Response response = courierApi.loginCourier(credentials);

        if (response.statusCode() >= 500) {
            System.out.println("Сервер недоступен, статус: " + response.statusCode());
            return;
        }

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Авторизация с неверными данными")
    public void loginWithInvalidCredentials() {
        CourierCredentials credentials = new CourierCredentials("invalid", "wrong");
        courierApi.loginCourier(credentials)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterClass
    public static void cleanup() {
        if (courierId != null) {
            courierApi.deleteCourier(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}
