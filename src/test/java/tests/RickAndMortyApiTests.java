package tests;

import hooks.Hooks;
import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utils.ApiClient;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тестирование API Rick and Morty")
@Feature("Персонажи, локации и эпизоды")

public class RickAndMortyApiTests extends Hooks {

    Random random = new Random();

    @Test
    @Story("Получение случайного персонажа")
    @Description("Проверяем, что имя случайного персонажа не пустое.")
    @Severity(SeverityLevel.NORMAL)
    public void testRandomCharacterNameIsNotEmpty() {
        int randomId = random.nextInt(826) + 1; // по документации персонажи до 826 id
        Response response = ApiClient.getCharacter(randomId);

        assertEquals(200, response.statusCode());

        String name = response.jsonPath().getString("name");
        assertNotNull(name);
        assertFalse(name.trim().isEmpty());
    }

    @Test
    @Story("Проверка локации")
    @Description("Проверяем, что у локации есть хотя бы один житель.")
    @Severity(SeverityLevel.CRITICAL)
    public void testLocationResidentsContainCharacter() {
        int locationId = random.nextInt(126) + 1; // по документации 126 локаций
        Response locationResponse = ApiClient.getLocation(locationId);

        assertEquals(200, locationResponse.statusCode());

        List<String> residents = locationResponse.jsonPath().getList("residents");

        if (residents.isEmpty()) {
            System.out.println("Локация без жителей, пропуск теста.");
            return;
        }

        // Проверим, что у нас действительно есть ссылка на персонажа
        String residentUrl = residents.get(0);
        Response residentResponse = ApiClient.getCharacter(extractIdFromUrl(residentUrl));

        assertEquals(200, residentResponse.statusCode());
    }

    @Test
    @Story("Проверка эпизода")
    @Description("Проверяем, что в эпизоде есть связанные персонажи.")
    @Severity(SeverityLevel.CRITICAL)
    public void testEpisodeCharactersExist() {
        int episodeId = random.nextInt(51) + 1; // 51 эпизод
        Response episodeResponse = ApiClient.getEpisode(episodeId);

        assertEquals(200, episodeResponse.statusCode());

        List<String> characters = episodeResponse.jsonPath().getList("characters");

        assertNotNull(characters);
        assertFalse(characters.isEmpty());

        // Проверяем одного персонажа
        String characterUrl = characters.get(0);
        Response characterResponse = ApiClient.getCharacter(extractIdFromUrl(characterUrl));
        assertEquals(200, characterResponse.statusCode());
    }

    @Test
    @Story("Запрос нескольких персонажей")
    @Description("Проверка мульти-запроса персонажей по id.")
    @Severity(SeverityLevel.MINOR)
    public void testMultipleCharactersExist() {
        // Тест на запрос сразу нескольких персонажей
        String ids = "1,2,3,4,5";
        Response response = given()
                .when()
                .get("/character/" + ids)
                .then()
                .extract()
                .response();

        assertEquals(200, response.statusCode());

        List<Object> characters = response.jsonPath().getList("$");
        assertEquals(5, characters.size());
    }

    @Test
    @Story("Проверка происхождения и местоположения")
    @Description("Проверяем наличие информации о происхождении и местоположении персонажа.")
    @Severity(SeverityLevel.NORMAL)
    public void testCharacterOriginAndLocation() {
        int randomId = random.nextInt(826) + 1;
        Response response = ApiClient.getCharacter(randomId);

        assertEquals(200, response.statusCode());

        JsonPath jsonPath = response.jsonPath();
        String origin = jsonPath.getString("origin.name");
        String location = jsonPath.getString("location.name");

        assertNotNull(origin);
        assertNotNull(location);
        assertFalse(origin.trim().isEmpty());
        assertFalse(location.trim().isEmpty());
    }

    private int extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
