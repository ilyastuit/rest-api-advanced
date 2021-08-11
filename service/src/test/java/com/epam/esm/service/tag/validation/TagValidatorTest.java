package com.epam.esm.service.tag.validation;

import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.tag.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TagValidatorTest {

    @Mock
    private TagService tagService;

    @BeforeEach
    public void createMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidationWithEmptyName() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(false);

        TagValidator tagValidator = new TagValidator(tagService);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName(null);

        Errors errors = new BeanPropertyBindingResult(tagDTO, "tagDTO");
        tagValidator.validate(tagDTO, errors);

        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("name"));
        assertTrue(tagValidator.supports(tagDTO.getClass()));
    }

    @Test
    public void testValidationWithNotUniqueName() {
        assertNotNull(tagService);
        when(tagService.isExistByName("tag")).thenReturn(true);

        TagValidator tagValidator = new TagValidator(tagService);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(1);
        tagDTO.setName("tag");

        final BindingResult bindingResult = ValidatorUtil.validate(tagDTO, tagValidator);
        Map<String, String> errors = ValidatorUtil.getErrors(bindingResult);

        assertFalse(errors.isEmpty());
        assertTrue(bindingResult.hasErrors());
        assertTrue(bindingResult.hasFieldErrors("name"));
        assertTrue(tagValidator.supports(tagDTO.getClass()));
    }

}
