import za.co.mahlaza.research.grammarengine.base.models.template.Template;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluFeatureParser;
import za.co.mahlaza.research.templateparsing.TemplateReader;
import za.co.mahlaza.research.templateparsing.URIS;

/*
 * @(#) ToCTeditor.java   1.0   Nov 10, 2021
 *
 * Sindiso Mkhatshwa (mkhsin035@myuct.ac.za)
 *
 * Nitschke Laboratory, UCT
 *
 * @(#) $Id$
 */
public class ToCTeditor {

    static boolean DEBUG = true;
    static ViewThread gui;
    static DataModel dataModel;

    //static TurtleCode turtleGen;

    static ToCTeditorFrame frame;
    static CreateTemplate homeScreen;
    static TemplateItems templateItems;
    static CreateItem createItem;
    static CreateMorpheme createMorpheme;

    static int prevToggleBtnState;
    // By default, set the gui to show the turtle preview
    static int toggleBtnState;

    public static void main(String[] args) {
        // By default, set the gui to show the turtle preview
        toggleBtnState = 1;
        prevToggleBtnState = 1;

        frame = new ToCTeditorFrame(1000, 600);
        homeScreen = new CreateTemplate(frame);
        templateItems = new TemplateItems(frame);
        createItem = new CreateItem(frame);
        createMorpheme = new CreateMorpheme(frame);

        //turtleGen = new TurtleCode();
        dataModel = new DataModel();

        homeScreen.setupGUI();
    }
}
