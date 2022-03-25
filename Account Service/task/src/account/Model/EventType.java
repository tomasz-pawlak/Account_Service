package account.Model;

public enum EventType {
    CREATE_USER,
    CHANGE_PASSWORD,
    ACCESS_DENIED,
    LOGIN_FAILED,
    GRANT_ROLE,
    REMOVE_ROLE,
    LOCK_USER,
    UNLOCK_USER,
    DELETE_USER,
    BRUTE_FORCE
}
