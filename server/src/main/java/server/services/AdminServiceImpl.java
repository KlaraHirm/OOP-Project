package server.services;

import org.springframework.stereotype.Service;
import server.services.interfaces.AdminService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class AdminServiceImpl implements AdminService {

    /**
     * Checks if the given password is correct
     * @param password the password to check
     * @return true if the password is correct
     */
    @Override
    public boolean checkPassword(String password) {
        Scanner passwordFileScanner;
        try {
            passwordFileScanner = new Scanner(new File("password.txt"));
        } catch (FileNotFoundException e) {
            return false;
        }
        return passwordFileScanner.nextLine().equals(password);
    }

}
