Feature: Character API

  Scenario Outline: Get multiple characters by ID
    Given I get a character with id <characterId>
    Then The character name should not be empty

    Examples:
      | characterId |
      | 1           |
      | 2           |
      | 3           |
