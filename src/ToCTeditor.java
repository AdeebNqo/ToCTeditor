
/*
 * @(#) ToCTeditor.java   1.0   Nov 10, 2021
 *
 * Sindiso Mkhatshwa (mkhsin035@myuct.ac.za)
 * Zola Mahlaza
 *
 * @(#) $Id$
 */
public class ToCTeditor {

    static boolean DEBUG = true;
    static ViewThread gui;
    //static DataModel dataModel;

    //static TurtleCode turtleGen;

    static ToCTeditorFrame frame;
    static InitialCreateTemplateScreen homeScreen;
    static MainTemplateViewScreen templateItems;
    static CreateTemplateItemScreen createItem;
    static CreateMorpheme createMorpheme;

    static int prevToggleBtnState;
    // By default, set the gui to show the turtle preview
    static int toggleBtnState;

    public static void main(String[] args) {
        // By default, set the gui to show the turtle preview
        toggleBtnState = 1;
        prevToggleBtnState = 1;

        frame = new ToCTeditorFrame(1000, 600);
        homeScreen = new InitialCreateTemplateScreen(frame);
        templateItems = new MainTemplateViewScreen(frame);
        createItem = new CreateTemplateItemScreen(frame);
        createMorpheme = new CreateMorpheme(frame);

        //turtleGen = new TurtleCode();
        //dataModel = new DataModel();

        homeScreen.setupGUI();
    }
}
