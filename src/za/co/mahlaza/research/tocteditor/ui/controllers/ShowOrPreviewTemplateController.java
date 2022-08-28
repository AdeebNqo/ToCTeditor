package za.co.mahlaza.research.tocteditor.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ShowOrPreviewTemplateController {
    @FXML
    Button addTemplateItemsBtn;
    @FXML
    Button backBtn;
    @FXML
    Button saveBtn;

    @FXML
    public void onAddTemplateItem(ActionEvent event) {
        System.out.println("Add item");
    }

    @FXML
    public void onBack(ActionEvent event) {
        System.out.println("on go back");
    }

    @FXML
    public void onTemplateSave(ActionEvent event) {
        System.out.println("on save template");
    }
}
