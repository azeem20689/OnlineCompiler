package comregistration.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpSession;

public class SessionManager {
    private static Map<String, jakarta.servlet.http.HttpSession> activeSessions = new ConcurrentHashMap<>();
    private static int userCount = 0;

    public static void addSession(jakarta.servlet.http.HttpSession session) {
        activeSessions.put(session.getId(), session);
        userCount++;
    }

    public static void removeSession(HttpSession session) {
        activeSessions.remove(session.getId());
        userCount--;
    }

    public static int getActiveUserCount() {
        return userCount;
    }
}

