Feature: Get recipes API

Scenario: Customer can view recipes list

    Given url 'http://localhost:8085/auth/login'
    And request
    """
    {
      "username": "customer",
      "password": "customer"
    }
    """
    When method post
    Then status 200
    And match response.token != null

    * def token = response.token

    Given url 'http://localhost:8085/api/recipes'
    And header Authorization = 'Bearer ' + token
    When method get
    Then status 200
    And match response != null