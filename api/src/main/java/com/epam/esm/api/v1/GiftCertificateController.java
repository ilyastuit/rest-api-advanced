package com.epam.esm.api.v1;

import com.epam.esm.api.assembler.GiftCertificateModelAssembler;
import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.giftcertificate.GiftCertificateModel;
import com.epam.esm.service.exceptions.*;
import com.epam.esm.service.giftcertificate.validation.GiftCertificateValidationErrors;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import com.epam.esm.service.ValidatorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.*;

/**
 * Rest Api Controller for GiftCertificate.
 */
@RestController
@RequestMapping("/api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModelAssembler certificateModelAssembler;
    private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler;
    private final Validator giftCertificateValidator;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GiftCertificateController(
            GiftCertificateService giftCertificateService,
            GiftCertificateModelAssembler certificateModelAssembler,
            PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler,
            Validator giftCertificateValidator
    ) {
        this.giftCertificateService = giftCertificateService;
        this.certificateModelAssembler = certificateModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    /**
     * Get list of all GiftCertificates (optional: with Tags).
     * Sort by date, name or with both of them.
     *
     * @param tags Include tags (true|false)
     * @return List of all GiftCertificates.
     */
    @GetMapping(value = "/")
    public ResponseEntity<PagedModel<GiftCertificateModel>> all(
            @RequestParam("tags") Optional<String> tags,
            Pageable pageable
    ) {

        Page<GiftCertificate> certificateEntities = this.giftCertificateService.getAll(tags, pageable);

        PagedModel<GiftCertificateModel> certificateModels = pagedResourcesAssembler.toModel(certificateEntities, certificateModelAssembler);

        return new ResponseEntity<>(certificateModels, HttpStatus.OK);
    }

    /**
     * Get Gift Certificate by id (optional: with tags).
     *
     * @param id   id of the GiftCertificate
     * @param tags Include Tags (true|false)
     * @return One GiftCertificate or Not Found if not.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateModel> one(
            @PathVariable("id") int id,
            @RequestParam("tags") Optional<String> tags
    ) throws GiftCertificateNotFoundException {
        boolean withTags = ValidatorUtil.isValidBoolean(tags);

        GiftCertificateModel model =  certificateModelAssembler.toModel(this.giftCertificateService.getOne(id, withTags));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    /**
     * Search GiftCertificates matching by name or description.
     *
     * @param q Search parameter
     * @return List of found GiftCertificates.
     */
    @GetMapping(value = "/search")
    public ResponseEntity<PagedModel<GiftCertificateModel>> search(
            @RequestParam("q") Optional<String> q,
            Pageable pageable
    ) throws GiftCertificateSearchParameterNotProvidedException {
        if (q.isPresent()) {
            Page<GiftCertificate> certificateEntities = this.giftCertificateService.getAllByNameOrDescription(q.get(), pageable);
            PagedModel<GiftCertificateModel> certificateModels = pagedResourcesAssembler.toModel(certificateEntities, certificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
        }
        throw new GiftCertificateSearchParameterNotProvidedException();
    }

    /**
     * Search GiftCertificates matching by Tag name.
     *
     * @param tag Tag name
     * @return List of found GiftCertificates.
     */
    @GetMapping(value = "/search-by-tag")
    public ResponseEntity<?> searchByTag(
            @RequestParam("tag") Optional<String[]> tag,
            Pageable pageable
    ) throws GiftCertificateSearchParameterNotProvidedException {
        if (tag.isPresent()) {
            Page<GiftCertificate> certificateEntities = this.giftCertificateService.getAllByTagNames(tag.get(), pageable);
            PagedModel<GiftCertificateModel> certificateModels = pagedResourcesAssembler.toModel(certificateEntities, certificateModelAssembler);
            return new ResponseEntity<>(certificateModels, HttpStatus.OK);
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
    public ResponseEntity<?> create(
            @RequestBody GiftCertificateDTO giftCertificate
    ) throws TagNameAlreadyExistException {
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
     * @param id              Id of GiftCertificate to update.
     * @param giftCertificate GiftCertificate values.
     * @return Id of updated GiftCertificate.
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") int id,
            @RequestBody GiftCertificateDTO giftCertificate
    ) throws GiftCertificateNotFoundException {

        if (!this.giftCertificateService.isExistById(id)) {
            throw new GiftCertificateNotFoundException(id);
        }

        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateValidationErrors result = new GiftCertificateValidationErrors(HttpStatus.BAD_REQUEST, GiftCertificateValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        GiftCertificateModel model =  certificateModelAssembler.toModel(this.giftCertificateService.getOne(id, true));
        return new ResponseEntity<>(model, HttpStatus.OK);
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
     * @param tagId  Id of Tag to be assigned
     */
    @PutMapping("/{giftId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    public void assignTag(
            @PathVariable("giftId") int giftId,
            @PathVariable("tagId") int tagId
    ) throws GiftCertificateNotFoundException, TagNotFoundException {
        this.giftCertificateService.assignTagToCertificate(giftId, tagId);
    }

    /**
     * Change name of GifCertificate
     *
     * @param giftId         Id of GiftCertificate to change
     * @param certificateDTO DTO with new name of certificate to change
     */
    @PutMapping("/{giftId}/name")
    public ResponseEntity<?> changeName(
            @PathVariable("giftId") int giftId,
            @RequestBody GiftCertificateDTO certificateDTO
    ) {

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
