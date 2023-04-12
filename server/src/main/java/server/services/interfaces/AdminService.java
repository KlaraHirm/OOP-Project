package server.services.interfaces;

public interface AdminService {

    /**
     * Checks if the given password is correct
     * @param password the password to check
     * @return true if the password is correct
     */
    boolean checkPassword(String password);
}
