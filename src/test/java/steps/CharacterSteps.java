package steps;

import hooks.Hooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import utils.ApiClient;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CharacterSteps extends Hooks {

    private Response response;

    @Given("I get a character with id {int}")
    public void iGetACharacterWithId(int int1) {
        response = ApiClient.getCharacter(int1);
        throw new io.cucumber.java.PendingException();
    }

    @Then("The character name should not be empty")
    public void theCharacterNameShouldNotBeEmpty() {
        String name = response.jsonPath().getString("name");
        assertFalse(name.isEmpty(), "Character name should not be empty");
        throw new io.cucumber.java.PendingException();
    }
}
