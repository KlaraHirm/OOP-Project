package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class EditCardCtrl {


    private final ServerUtils server;
    private final MainClientCtrl mainCtrl;


    @Inject
    public EditCardCtrl(ServerUtils server, MainClientCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

}