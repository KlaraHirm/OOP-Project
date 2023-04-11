package client.utils;

import commons.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class PreferenceUtils {

    public Preferences preferences;

    /**
     * Constructor for PreferenceUtils
     */
    public PreferenceUtils() {
        this.preferences = Preferences.userRoot();
    }

    /**
     * Saves the board id to the preferences store, so that it appears in the board list
     * @param serverUrl the connection string (to isolate prefs by workspace)
     * @param board The board to save to preferences
     */
    public void saveBoardId(String serverUrl, Board board) {
        String joinedBoardIDs = preferences.get(serverUrl, "");
        if (joinedBoardIDs == null) joinedBoardIDs = "";
        if (!joinedBoardIDs.isBlank()) joinedBoardIDs += ",";
        joinedBoardIDs += board.id;
        preferences.put(serverUrl, joinedBoardIDs);
    }

    /**
     * Removes the board id from the preferences store, so that it is hidden in the board list
     * @param serverUrl the connection string (to isolate prefs by workspace)
     * @param board The board to remove from preferences
     */
    public void removeBoardId(String serverUrl, Board board) {
        List<String> joinedBoards = new ArrayList<>(Arrays.asList(
                preferences.get(serverUrl, "").split(",")
        ));
        joinedBoards.remove(Long.toString(board.id));
        preferences.put(serverUrl, String.join(",", joinedBoards));
    }

    /**
     * Get the ids of the boards the user has joined
     * @param serverUrl the connection string (to isolate prefs by workspace)
     * @return a list of ids
     */
    public List<String> getJoinedBoardIds(String serverUrl) {
        return Arrays.asList(
                preferences.get(serverUrl, "").split(",")
        );
    }
}
