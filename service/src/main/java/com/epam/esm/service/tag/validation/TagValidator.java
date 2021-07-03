package com.epam.esm.service.tag.validation;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.tag.TagService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class TagValidator implements Validator {

    private boolean fromGiftCertificate = false;
    private final TagService tagService;

    public TagValidator(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Tag tag = (Tag) target;
        if (tag.getName() == null || tag.getName().isEmpty()) {
            errors.rejectValue("name", "name.required", "Name is required and should not be empty.");
        }

        if (!this.fromGiftCertificate && this.tagService.isExistByName(tag.getName())) {
            errors.rejectValue("name", "name.unique", "Tag with this name is already exist.");
        }
    }

    public TagValidator fromGiftCertificate() {
        this.fromGiftCertificate = true;
        return this;
    }
}