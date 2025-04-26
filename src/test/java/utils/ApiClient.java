package utils;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public static Response getCharacter(int id) {
        return given()
                .when()
                .get("/character/" + id)
                .then()
                .extract()
                .response();
    }

    public static Response getCharactersPage(int page) {
        return given()
                .queryParam("page", page)
                .when()
                .get("/character")
                .then()
                .extract()
                .response();
    }

    public static Response getEpisode(int id) {
        return given()
                .when()
                .get("/episode/" + id)
                .then()
                .extract()
                .response();
    }

    public static Response getLocation(int id) {
        return given()
                .when()
                .get("/location/" + id)
                .then()
                .extract()
                .response();
    }
}
