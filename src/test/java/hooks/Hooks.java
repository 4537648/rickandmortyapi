package hooks;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class Hooks {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://rickandmortyapi.com/api";
    }
}
