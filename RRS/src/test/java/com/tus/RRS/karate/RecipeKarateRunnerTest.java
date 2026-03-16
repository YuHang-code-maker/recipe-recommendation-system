package com.tus.RRS.karate;

import com.intuit.karate.junit5.Karate;

class RecipeKarateRunnerTest {

    @Karate.Test
    Karate runAll() {
        return Karate.run("classpath:features/recipe-admin.feature",
                          "classpath:features/recipe-customer-forbidden.feature");
    }
}