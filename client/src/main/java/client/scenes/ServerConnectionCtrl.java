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

import java.io.IOException;

public class ServerConnectionCtrl {

    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;

    @FXML
    private TextField serverAddressField;

    @FXML
    public Label serverAddressLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label adminLabel;

    @FXML
    private Label statusLabel;

    private boolean adminPageShown = false;

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
        String serverAddress = serverAddressField.getText();
        String password = passwordField.getText();
        statusLabel.setText("Connecting...");

        if (adminPageShown) {
            boolean success = server.connectAdmin(serverAddress, password);
            statusLabel.setText(success ? "Connected!" : "Connection failed");
            mainCtrl.resetOverview();
            mainCtrl.refreshOverview();
            if (success) {
                System.out.println("Success");
            }
        } else {
            boolean success = server.connect(serverAddress);
            statusLabel.setText(success ? "Connected!" : "Connection failed");
            mainCtrl.resetOverview();
            mainCtrl.refreshOverview();
            if (success) {
                try {
                    mainCtrl.showOverview(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //will then go to main page onAction
    }

    /**
     * sets the initial values of the UI elements when the scene is loaded
     */
    public void setUIValues() {
        serverAddressField.setText(server.getServerURL());
    }

    /**
     * changes between admin page connect (with password) and user page connect
     */
    public void toggleAdminPage() {
        adminPageShown = !adminPageShown;
        passwordField.setVisible(adminPageShown);
        passwordLabel.setVisible(adminPageShown);
        adminLabel.setText(adminPageShown ? "User login" : "Admin login");
    }
}