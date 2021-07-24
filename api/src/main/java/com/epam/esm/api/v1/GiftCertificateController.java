package com.epam.esm.api.v1;

import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.service.exceptions.GiftCertificateDeleteRestriction;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.GiftCertificateSearchParameterNotProvidedException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.giftcertificate.validation.GiftCertificateValidationErrors;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import com.epam.esm.service.ValidatorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Rest Api Controller for GiftCertificate.
 */
@RestController
@RequestMapping("/api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final Validator giftCertificateValidator;

    public GiftCertificateController(GiftCertificateService giftCertificateService, Validator giftCertificateValidator) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    /**
     *  Get list of all GiftCertificates (optional: with Tags).
     *  Sort by date, name or with both of them.
     *
     * @param tags Include tags (true|false)
     * @param date Sort by date (asc|desc)
     * @param name Sort by name (asc|desc)
     * @return List of all GiftCertificates.
     */
    @GetMapping(value = "/")
    public ResponseEntity<List<GiftCertificateDTO>> all(@RequestParam("tags") Optional<String> tags,
                                                  @RequestParam("date") Optional<String> date,
                                                  @RequestParam("name") Optional<String> name) {

        return new ResponseEntity<>(this.giftCertificateService.getAll(tags, date, name), HttpStatus.OK);
    }

    /**
     * Get Gift Certificate by id (optional: with tags).
     *
     * @param id id of the GiftCertificate
     * @param tags Include Tags (true|false)
     * @return One GiftCertificate or Not Found if not.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id, @RequestParam("tags") Optional<String> tags) throws GiftCertificateNotFoundException {
        boolean withTags = ValidatorUtil.isValidBoolean(tags);

        return new ResponseEntity<>(this.giftCertificateService.getOne(id, withTags), HttpStatus.OK);
    }

    /**
     * Search GiftCertificates matching by name or description.
     * Sort by date, name or with both of them.
     *
     * @param q Search parameter
     * @param date Sort by date (asc|desc)
     * @param name Sort by name (asc|desc)
     * @return List of found GiftCertificates.
     */
    @GetMapping(value = "/search")
    public ResponseEntity<?> search(@RequestParam("q") Optional<String> q,
                                    @RequestParam("date") Optional<String> date,
                                    @RequestParam("name") Optional<String> name) throws GiftCertificateSearchParameterNotProvidedException {
        if (q.isPresent()) {
            return new ResponseEntity<>(this.giftCertificateService.getAllByNameOrDescription(q.get(), date, name), HttpStatus.OK);
        }
        throw new GiftCertificateSearchParameterNotProvidedException();
    }

    /**
     * Search GiftCertificates matching by Tag name.
     * Sort by date, name or with both of them.
     *
     * @param tag Tag name
     * @param date Sort by date (asc|desc)
     * @param name Sort by name (asc|desc)
     * @return List of found GiftCertificates.
     */
    @GetMapping(value = "/search-by-tag")
    public ResponseEntity<?> searchByTag(@RequestParam("tag") Optional<String[]> tag,
                                    @RequestParam("date") Optional<String> date,
                                    @RequestParam("name") Optional<String> name) throws GiftCertificateSearchParameterNotProvidedException {
        if(tag.isPresent()) {
            return new ResponseEntity<>(this.giftCertificateService.getAllByTagNames(tag.get()), HttpStatus.OK);
        }
        throw new GiftCertificateSearchParameterNotProvidedException();
    }

    /**
     * Create new GiftCertificate.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param giftCertificate Request body representation of GiftCertificate
     * @return Id of created GiftCertificate.
     */
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody GiftCertificateDTO giftCertificate) throws TagNameAlreadyExistException {
        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateValidationErrors result = new GiftCertificateValidationErrors(HttpStatus.BAD_REQUEST, GiftCertificateValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.giftCertificateService.save(giftCertificate), HttpStatus.CREATED);
    }

    /**
     * Update GiftCertificate by id.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param id Id of GiftCertificate to update.
     * @param giftCertificate GiftCertificate values.
     * @return Id of updated GiftCertificate.
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody GiftCertificateDTO giftCertificate) throws GiftCertificateNotFoundException, TagNameAlreadyExistException {

        if (!this.giftCertificateService.isExistById(id)) {
            throw new GiftCertificateNotFoundException(id);
        }

        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateValidationErrors result = new GiftCertificateValidationErrors(HttpStatus.BAD_REQUEST, GiftCertificateValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        GiftCertificateDTO result = this.giftCertificateService.getOne(this.giftCertificateService.update(id, giftCertificate), true);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete GiftCertificate by id.
     *
     * @param id Id of GiftCertificate to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) throws GiftCertificateDeleteRestriction {
        this.giftCertificateService.delete(id);
    }

    /**
     * Assign Tag to GiftCertificate.
     *
     * @param giftId Id of GiftCertificate to assign
     * @param tagId Id of Tag to be assigned
     */
    @PutMapping("/{giftId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void assignTag(@PathVariable("giftId") int giftId, @PathVariable("tagId") int tagId) {
        this.giftCertificateService.assignTagToCertificate(giftId, tagId);
    }

    /**
     * Change name of GifCertificate
     *
     * @param giftId Id of GiftCertificate to change
     * @param certificateDTO DTO with new name of certificate to change
     */
    @PutMapping("/{giftId}/name")
    public ResponseEntity<?> changeName(@PathVariable("giftId") int giftId, @RequestBody GiftCertificateDTO certificateDTO) {

        final BindingResult bindingResult = ValidatorUtil.validate(certificateDTO, this.giftCertificateValidator);

        if (bindingResult.hasFieldErrors("name")) {
            GiftCertificateValidationErrors result = new GiftCertificateValidationErrors(
                    HttpStatus.BAD_REQUEST,
                    GiftCertificateValidationErrors.DEFAULT_ERROR_MESSAGE,
                    ValidatorUtil.getFieldErrors(bindingResult, "name")
            );

            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        this.giftCertificateService.changeName(giftId, certificateDTO.getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
