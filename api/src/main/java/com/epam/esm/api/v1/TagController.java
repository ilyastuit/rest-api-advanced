package com.epam.esm.api.v1;

import com.epam.esm.api.assembler.TagModelAssembler;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.entity.tag.TagModel;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.TagNotFoundException;
import com.epam.esm.service.tag.validation.TagValidationErrors;
import com.epam.esm.service.tag.validation.TagValidator;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.ValidatorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Api Controller for Tags.
 */
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;
    private final TagModelAssembler tagModelAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    private final TagValidator tagValidator;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public TagController(
            TagService tagService,
            TagModelAssembler tagModelAssembler,
            PagedResourcesAssembler<Tag> pagedResourcesAssembler,
            TagValidator tagValidator
    ) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.tagValidator = tagValidator;
    }

    /**
     * Get list of all Tags.
     *
     * @return List of all Tags.
     */
    @GetMapping("/")
    public ResponseEntity<PagedModel<TagModel>> all(Pageable pageable) {

        Page<Tag> tags = this.tagService.getAll(pageable);

        PagedModel<TagModel> tagModels = pagedResourcesAssembler.toModel(tags, tagModelAssembler);

        return new ResponseEntity<>(tagModels, HttpStatus.OK);
    }

    /**
     * Get Tag by id.
     *
     * @param id Id of Tag
     * @return Found Tag by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagModel> one(@PathVariable("id") int id) throws TagNotFoundException {

        TagModel tagModel = tagModelAssembler.toModel(this.tagService.getById(id));

        return new ResponseEntity<>(tagModel, HttpStatus.OK);
    }

    /**
     * Create new Tag.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param tagDto Tag values
     * @return Id of created Tag.
     */
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TagDTO tagDto) throws TagNameAlreadyExistException {
        final BindingResult bindingResult = ValidatorUtil.validate(tagDto, this.tagValidator);

        if (bindingResult.hasErrors()) {
            TagValidationErrors result = new TagValidationErrors(HttpStatus.BAD_REQUEST, TagValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.tagService.save(tagDto), HttpStatus.CREATED);
    }

    /**
     * Delete Tag by id.
     *
     * @param id Id of Tag
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) throws TagNotFoundException {
        this.tagService.deleteById(id);
    }

    @GetMapping("/ordered-highest-price")
    public ResponseEntity<PagedModel<TagModel>> orderedTagsWithHighestPrice(Pageable pageable) {

        Page<Tag> tags = tagService.getOrderedTagsWithHighestPrice(pageable);

        PagedModel<TagModel> tagModels = pagedResourcesAssembler.toModel(tags, tagModelAssembler);

        return new ResponseEntity<>(tagModels, HttpStatus.OK);
    }

    @GetMapping("/most-ordered")
    public ResponseEntity<PagedModel<TagModel>> mostOrdered(Pageable pageable) {

        Page<Tag> tags = tagService.getMostOrdered(pageable);

        PagedModel<TagModel> tagModels = pagedResourcesAssembler.toModel(tags, tagModelAssembler);

        return new ResponseEntity<>(tagModels, HttpStatus.OK);
    }
}
