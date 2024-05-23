package dao;
import java.util.HashMap;
import java.util.Map;

public class SessionDAO {
    private Map<String, Integer> sessions = new HashMap<>();

    public void saveSession(String token, int userId) {
        sessions.put(token, userId);
    }

    public boolean isValidToken(String token) {
        return sessions.containsKey(token);
    }

    public Integer getUserIdByToken(String token) {
        return sessions.get(token);
    }

    public void invalidateSession(String token) {
        sessions.remove(token);
    }
}
