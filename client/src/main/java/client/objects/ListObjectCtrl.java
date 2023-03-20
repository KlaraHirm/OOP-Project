package client.objects;

import client.scenes.EditCtrl;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class ListObjectCtrl {
    @FXML
    private CheckBox name;

    @FXML
    private HBox listHBox;

    private EditCtrl editCtrl;

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setEditCtrl(EditCtrl e) {
        this.editCtrl = e;
    }

    public void deleteTask() {
        editCtrl.deleteTask(listHBox);
    }
}
