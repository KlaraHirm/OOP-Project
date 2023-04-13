package server.services.interfaces;

import commons.Subtask;

public interface SubtaskService {
    Subtask getSubtask(long subtaskId);
    Subtask editSubtask(Subtask subtask);
    Subtask deleteSubtask(long boardId, long listId, long cardId, long subtaskId);
}
