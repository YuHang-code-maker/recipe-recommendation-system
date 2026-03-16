Feature: Customer should not be able to create recipe

  Scenario: Customer login succeeds but POST is forbidden
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
    And match response.role == 'ROLE_customer'

    * def customerToken = response.token

    Given url 'http://localhost:8085/api/recipes'
    And header Authorization = 'Bearer ' + customerToken
    And request
    """
    {
      "title": "Customer Forbidden Recipe",
      "instructions": "Should fail",
      "image": "test.jpg",
      "externalLink": "http://example.com",
      "ingredients": [
        { "name": "Tomato" }
      ]
    }
    """
    When method post
    Then status 401