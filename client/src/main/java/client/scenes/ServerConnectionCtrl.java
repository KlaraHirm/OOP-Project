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
package client.scenes;

import com.google.inject.Inject;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerConnectionCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;
    private String server_address;

    @FXML
    private TextField server_address_field;


    /**
     * constructor
     * @param server
     * @param mainCtrl
     */
    @Inject
    public ServerConnectionCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * method to execute when the connect server button is clicked
     * should connect to the given server address and go to the main page of that address
     */
    public void connectServer() {
        server_address = server_address_field.getText();
        server_address_field.clear();
        //will then go to main page onAction
    }
}