package za.co.mahlaza.research.tocteditor.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Screen {
    private ScreenName name;
    private String fxmlFilename;
    private Node parent;
    private Node renderableScreen;
    public static Stage mainStage;

    public Screen(ScreenName screenName, String fxmlFilename) {
        this.fxmlFilename = "./fxml/" + fxmlFilename;
        this.name = screenName;
    }

    public Screen(ScreenName screenName, String fxmlFilename, Node parent) {
        this(screenName, fxmlFilename);
        this.parent = parent;
    }

    public ScreenName getName() {
        return name;
    }

    ;

    public URL getFXMLFile() {
        return getClass().getResource(fxmlFilename);
    }

    ;

    public Node getParent() {
        return parent;
    }

    public Node getRenderableScreen() throws IOException {
        if (renderableScreen == null) {
            URL url = getFXMLFile();
            System.out.println(url);
            FXMLLoader loader = new FXMLLoader(url);
            renderableScreen = loader.load();
        }
        return renderableScreen;
    }
}
