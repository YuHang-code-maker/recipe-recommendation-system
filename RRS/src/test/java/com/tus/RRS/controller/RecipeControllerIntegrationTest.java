package com.tus.RRS.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tus.RRS.dto.IngredientDto;
import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.dto.ResponseDto;
import com.tus.RRS.dto.auth.JwtRequest;
import com.tus.RRS.dto.auth.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class RecipeControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String recipeBaseUrl;
    private String authBaseUrl;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    private static final String CUSTOMER_USERNAME = "customer";
    private static final String CUSTOMER_PASSWORD = "customer";

    @BeforeEach
    void setUp() {
        recipeBaseUrl = "http://localhost:" + port + "/api/recipes";
        authBaseUrl = "http://localhost:" + port + "/auth/login";
    }

    private RecipeDto buildRecipeDto(String title) {
        IngredientDto ingredient1 = new IngredientDto();
        ingredient1.setName("Tomato");

        IngredientDto ingredient2 = new IngredientDto();
        ingredient2.setName("Cheese");

        Set<IngredientDto> ingredients = new HashSet<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        RecipeDto dto = new RecipeDto();
        dto.setTitle(title);
        dto.setInstructions("Bake for 20 minutes");
        dto.setImage("5.jpg");
        dto.setExternalLink("http://example.com");
        dto.setIngredients(ingredients);

        return dto;
    }

    private String loginAndGetToken(String username, String password) {
        JwtRequest requestBody = new JwtRequest(username, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<JwtRequest> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(
                authBaseUrl,
                request,
                JwtResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getToken());

        return response.getBody().getToken();
    }

    private HttpHeaders getAuthHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    @Test
    void getAllRecipesShouldReturn200ForAuthenticatedUser() {
        String token = loginAndGetToken(ADMIN_USERNAME, ADMIN_PASSWORD);

        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders(token));

        ResponseEntity<List<RecipeDto>> response = restTemplate.exchange(
                recipeBaseUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<RecipeDto>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getRecipeByIdShouldReturn404WhenRecipeNotFound() {
        String token = loginAndGetToken(ADMIN_USERNAME, ADMIN_PASSWORD);

        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders(token));

        ResponseEntity<String> response = restTemplate.exchange(
                recipeBaseUrl + "/999",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createRecipeShouldReturn201ForAdmin() {
        String token = loginAndGetToken(ADMIN_USERNAME, ADMIN_PASSWORD);

        RecipeDto recipeDto = buildRecipeDto("Integration Test Recipe");

        HttpEntity<RecipeDto> request = new HttpEntity<>(recipeDto, getAuthHeaders(token));

        ResponseEntity<ResponseDto> response = restTemplate.postForEntity(
                recipeBaseUrl,
                request,
                ResponseDto.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("201", response.getBody().getStatusCode());
    }

    @Test
    void createRecipeShouldReturn401ForCustomer() {
        String token = loginAndGetToken(CUSTOMER_USERNAME, CUSTOMER_PASSWORD);

        RecipeDto recipeDto = buildRecipeDto("Integration Test Recipe");

        HttpEntity<RecipeDto> request = new HttpEntity<>(recipeDto, getAuthHeaders(token));

        ResponseEntity<String> response = restTemplate.postForEntity(
                recipeBaseUrl,
                request,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    
    @Test
    void getAllRecipesShouldReturn401WhenNoToken() {
        ResponseEntity<String> response = restTemplate.exchange(
                recipeBaseUrl,
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}