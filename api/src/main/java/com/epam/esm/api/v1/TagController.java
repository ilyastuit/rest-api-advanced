package com.epam.esm.api.v1;

import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.TagNotFoundException;
import com.epam.esm.service.tag.validation.TagValidationErrors;
import com.epam.esm.service.tag.validation.TagValidator;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.ValidatorUtil;
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
    private final TagValidator tagValidator;

    public TagController(TagService tagService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }

    /**
     * Get list of all Tags.
     *
     * @return List of all Tags.
     */
    @GetMapping("/")
    public ResponseEntity<List<TagDTO>> all() {
        return new ResponseEntity<>(this.tagService.getAll(), HttpStatus.OK);
    }

    /**
     * Get Tag by id.
     *
     * @param id Id of Tag
     * @return Found Tag by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id) throws TagNotFoundException {
        return new ResponseEntity<>(this.tagService.getById(id), HttpStatus.OK);
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
    public void delete(@PathVariable("id") int id) {
        this.tagService.deleteById(id);
    }

    @GetMapping("/ordered-highest-price")
    public ResponseEntity<List<TagDTO>> orderedTagsWithHighestPrice() {
        return new ResponseEntity<>(tagService.getOrderedTagsWithHighestPrice(), HttpStatus.OK);
    }

    @GetMapping("/most-ordered")
    public ResponseEntity<List<TagDTO>> mostOrdered() {
        return new ResponseEntity<>(tagService.getMostOrdered(), HttpStatus.OK);
    }
}
