package com.epam.esm.service.giftcertificate.validation;

import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.service.tag.validation.TagValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Service
public class GiftCertificateValidator implements Validator {

    @Qualifier("tagValidator")
    private final TagValidator tagValidator;

    public GiftCertificateValidator(TagValidator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required and should not be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.empty", "Description is required and should not be empty.");

        GiftCertificateDTO giftCertificateDTO = (GiftCertificateDTO) target;

        if (giftCertificateDTO.getPrice() == null || giftCertificateDTO.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
            errors.rejectValue("price", "price.required", "Price is required and should be positive.");
        }
        if (giftCertificateDTO.getDuration() == null || giftCertificateDTO.getDuration() <= 0) {
            errors.rejectValue("duration", "duration.required", "Duration is required and should be greater than zero.");
        }

        if (giftCertificateDTO.getTags() != null && !giftCertificateDTO.getTags().isEmpty()) {
            int i = 0;
            for (TagDTO tagDTO : giftCertificateDTO.getTags()) {
                try {
                    errors.pushNestedPath("tags[" + (i++) + "]");
                    ValidationUtils.invokeValidator(this.tagValidator, tagDTO, errors);
                } finally {
                    errors.popNestedPath();
                }
            }
        }
    }
}
