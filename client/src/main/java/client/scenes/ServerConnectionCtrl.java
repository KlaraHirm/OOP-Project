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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.concurrent.TimeUnit;

public class ServerConnectionCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField server_address_field;

    @FXML
    private Label status_label;


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
        String serverAddress = server_address_field.getText();
        status_label.setText("Connecting...");
        boolean success = server.connect(serverAddress);
        status_label.setText(success ? "Connected!" : "Connection failed");
        mainCtrl.resetOverview();
        //will then go to main page onAction
    }

    /**
     * method to execute when the done button is clicked
     * returns to main page of the UI
     */
    public void goToMainPage() {
        mainCtrl.refreshOverview();
        try {
            mainCtrl.showOverview(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * sets the initial values of the UI elements when the scene is loaded
     */
    public void setUIValues() {
        server_address_field.setText(server.getServerURL());
    }
}