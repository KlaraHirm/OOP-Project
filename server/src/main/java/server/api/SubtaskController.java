package server.api;

import commons.Subtask;
import commons.Subtask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.SubtaskServiceImpl;
import server.services.interfaces.SubtaskService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/subtasks")
public class SubtaskController {

    @Autowired
    SubtaskService subtaskService;
    @Autowired
    SimpMessageSendingOperations messageTemplate;

    private final Object lock = new Object();

    String update = "updates";

    /**
     * Get info about a subtask
     * @param subtaskId the id of the subtask
     * @return the subtask object
     * Returns 404 if the subtask does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subtask> getSubtask(
            @PathVariable("id") long subtaskId
    ) {
        if(subtaskId < 0){
            return ResponseEntity.badRequest().build();
        }
        Subtask ret = subtaskService.getSubtask(subtaskId);
        if(ret==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ret);
    }

    /**
     * Update a certain subtask
     * @param newSubtask the subtask object to edit, with the corresponding id
     * @return the edited subtask
     * Gives 404 if the subtask does not exist
     * Gives 400 if the body is malformed
     */
    @PutMapping("")
    public ResponseEntity<Subtask> editSubtask(@RequestBody Subtask newSubtask) {
        if (newSubtask == null || newSubtask.title == null) return ResponseEntity.badRequest().build();
        Subtask ret = subtaskService.editSubtask(newSubtask);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

    /**
     * Delete a subtask
     * @param subtaskId the id of the subtask to delete
     * @param listId the id of the list where subtask is
     * @param boardId the id of the board
     * @return the whole board as updated
     * Returns 404 if the subtask, list or board do not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Subtask> deleteSubtask(
            @RequestParam("boardId") long boardId,
            @RequestParam("listId") long listId,
            @RequestParam("cardId") long cardId,
            @PathVariable("id") long subtaskId
    ) {
        if(subtaskId < 0 || cardId < 0 || listId < 0 || boardId < 0)
            return ResponseEntity.badRequest().build();
        Subtask ret = subtaskService.deleteSubtask(boardId, listId, cardId, subtaskId);
        if (ret == null) return ResponseEntity.notFound().build();
        messageTemplate.convertAndSend("/topic/updates", update);
        return ResponseEntity.ok(ret);
    }

}
