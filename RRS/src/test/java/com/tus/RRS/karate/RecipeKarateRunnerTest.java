package com.tus.RRS.karate;

import org.springframework.boot.test.context.SpringBootTest;

import com.intuit.karate.junit5.Karate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RecipeKarateRunnerTest {

    @Karate.Test
    Karate runAll() {
        return Karate.run("classpath:features/recipe-admin.feature",
                          "classpath:features/recipe-customer-forbidden.feature",
        					"classpath:features/recipe-get.feature");
    }
}