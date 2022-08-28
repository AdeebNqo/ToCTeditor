package za.co.mahlaza.research.tocteditor;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import za.co.mahlaza.research.tocteditor.ui.Screen;
import za.co.mahlaza.research.tocteditor.ui.UIScreenManager;
import za.co.mahlaza.research.tocteditor.ui.ScreenName;

import java.io.IOException;

import static za.co.mahlaza.research.tocteditor.ui.ScreenName.SHOW_OR_PREVIEW_TEMPLATE;

public class MainApplication extends Application {

    public static UIScreenManager screenManager;

    @Override
    public void start(Stage stage) throws Exception {
        Group appRoot = new Group();

        screenManager = new UIScreenManager();
        screenManager.goToScreen(ScreenName.OPEN_OR_CREATE_TEMPLATE);
        appRoot.getChildren().add(screenManager);
        Screen.mainStage = stage;

        Scene scene = new Scene(appRoot, 500, 500);
        stage.setScene(scene);

        //force the screen manager to resize with stage
        screenManager.prefWidthProperty().bind(scene.widthProperty());
        screenManager.prefHeightProperty().bind(scene.heightProperty());

        stage.show();
    }

    public static void changeScreen(ScreenName name) throws IOException {
        screenManager.goToScreen(SHOW_OR_PREVIEW_TEMPLATE);
    }
}
