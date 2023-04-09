package server.api;

import commons.Board;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import server.api.BoardController;
import server.api.repository.TestBoardRepository;
import server.api.repository.TestCardListRepository;
import server.services.BoardServiceImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest
{

    private AdminController sut;

    @BeforeEach
    public void setup()
    {
        sut = new AdminController();
    }

    /**
     * Test that the password is validated if it is correct
     */
    @Test
    public void testCorrectPassword() throws IOException {
        FileWriter writer = new FileWriter("password.txt");
        writer.write("password");
        writer.flush();
        assertEquals(ResponseEntity.ok().build(), sut.checkPassword("password"));
    }
    /**
     * Test that the password is rejected if it is incorrect
     */
    @Test
    public void testIncorrectPassword() throws IOException {
        FileWriter writer = new FileWriter("password.txt");
        writer.write("password");
        writer.flush();
        assertEquals(ResponseEntity.badRequest().build(), sut.checkPassword("wrong"));
    }
}
