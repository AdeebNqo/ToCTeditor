package za.co.mahlaza.research.tocteditor.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import za.co.mahlaza.research.tocteditor.MainApplication;
import za.co.mahlaza.research.tocteditor.ui.Screen;

import java.io.File;
import java.io.IOException;

import static za.co.mahlaza.research.tocteditor.ui.ScreenName.SHOW_OR_PREVIEW_TEMPLATE;

public class CreateOrOpenTemplateController {

    private FileChooser fileChooser = new FileChooser();
    @FXML
    Button chooseTemplateFileBtn;

    public CreateOrOpenTemplateController() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Template files", "*.ttl"));
    }

    @FXML
    public void onChooseTemplateFile(ActionEvent event) {
        File templateFile = fileChooser.showOpenDialog(Screen.mainStage);
        //TODO: do something with the file
        try {
            MainApplication.changeScreen(SHOW_OR_PREVIEW_TEMPLATE);
        } catch (IOException e) {
            //TODO: go to error screen
            e.printStackTrace();
        }
    }
}
