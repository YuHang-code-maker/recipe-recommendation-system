package com.tus.RRS.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.tus.RRS.dto.IngredientDto;
import com.tus.RRS.entity.IngredientEntity;

class IngredientMapperTest {

    @Test
    void mapToIngredientDtoShouldMapEntityToDtoCorrectly() {

        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setName("Salt");

        IngredientDto result = IngredientMapper.mapToIngredientDto(
                ingredientEntity,
                new IngredientDto()
        );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Salt", result.getName());
    }

    @Test
    void mapToIngredientEntityShouldMapDtoToEntityCorrectly() {

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(2L);
        ingredientDto.setName("Pepper");

        IngredientEntity result = IngredientMapper.mapToIngredientEntity(
                ingredientDto,
                new IngredientEntity()
        );

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Pepper", result.getName());
    }
}
