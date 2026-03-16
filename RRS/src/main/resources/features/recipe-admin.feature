Feature: Admin recipe API flow

  Scenario: Admin can login and create a recipe
    Given url 'http://localhost:8085/auth/login'
    And request
    """
    {
      "username": "admin",
      "password": "admin"
    }
    """
    When method post
    Then status 200
    And match response.token != null
    And match response.role == 'ROLE_admin'

    * def adminToken = response.token

    Given url 'http://localhost:8085/api/recipes'
    And header Authorization = 'Bearer ' + adminToken
    And request
    """
    {
      "title": "Karate Test Recipe",
      "instructions": "Bake for 20 minutes",
      "image": "test.jpg",
      "externalLink": "http://example.com",
      "ingredients": [
        { "name": "Tomato" },
        { "name": "Cheese" }
      ]
    }
    """
    When method post
    Then status 201
    And match response.statusCode == '201'