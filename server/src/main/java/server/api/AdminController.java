/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import commons.Board;
import commons.CardList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.BoardServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;

@RestController
@RequestMapping("/api/admin")
public class AdminController
{

    /**
     * Checks if the password is correct
     * @param password The password to check
     * @returns either 200 if it is correct or 400 if it is not
     */
    @GetMapping("/check")
    public ResponseEntity checkPassword(@RequestParam("password") String password) throws IOException {
        Scanner passwordFileScanner = new Scanner(new File("password.txt"));
        if (passwordFileScanner.nextLine().equals(password)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
