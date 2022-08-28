package za.co.mahlaza.research.tocteditor.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

import java.io.IOException;
import java.util.HashMap;

public class UIScreenManager extends StackPane {
    public HashMap<ScreenName, Screen> screens = new HashMap<>();

    public UIScreenManager() {
        setBackground(new Background(
                        new BackgroundFill(
                                new LinearGradient(0, 0, 0, 1, true,
                                        CycleMethod.NO_CYCLE,
                                        new Stop(0, Color.web("#cccccc")),
                                        new Stop(1, Color.web("#cccc00"))
                                ), CornerRadii.EMPTY, Insets.EMPTY
                        )
                )
        );
        screens.put(ScreenName.OPEN_OR_CREATE_TEMPLATE,
                new Screen(ScreenName.OPEN_OR_CREATE_TEMPLATE, "createOrOpenTemplate.fxml")
        );
        screens.put(ScreenName.SHOW_OR_PREVIEW_TEMPLATE,
                new Screen(ScreenName.SHOW_OR_PREVIEW_TEMPLATE, "createTemplateItemsOrPreviewTemplate.fxml")
        );
    }

    public void goToScreen(ScreenName name) throws IOException {
        if (screens.containsKey(name)) {
            if (getChildren().size() > 0) {
                getChildren().remove(0);
            }
            getChildren().add(screens.get(name).getRenderableScreen());
        }
    }
}
