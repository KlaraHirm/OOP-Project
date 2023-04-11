package server.api;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardServiceImpl;
import server.services.TagServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    TagServiceImpl tagService;

    /**
     * Retrieve particular Tag using ID
     * @param id - ID of the Tag
     * @return - Json representation of the Tag object
     * Gives 400 if the body of the retrieved Tag is malformed
     */
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable("id") long id) {
        Tag retrieved = tagService.getTag(id);
        if (retrieved == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(retrieved);
    }

    /**
     * Update a Tag
     * @param changedTag - the Tag object to edit, with the corresponding ID
     * @return - the edited Tag
     * Gives 404 if the Tag does not exist
     * Gives 400 if the body is malformed
     */
    @PutMapping("")
    public ResponseEntity<Tag> editTag(@RequestBody Tag changedTag) {
        if (changedTag == null) return ResponseEntity.badRequest().build();
        Tag retrieved = tagService.editTag(changedTag);
        if (retrieved == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(retrieved);
    }

    /**
     * Deletes a Tag with particular ID
     * @param tagId - ID of the Tag to delete
     * @return - Response indicating success fo deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> deleteTagWithId(@PathVariable("id") long tagId)
    {
        Tag retrieved = tagService.deleteTagWithId(tagId);
        if (retrieved  == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(retrieved);
    }
}

