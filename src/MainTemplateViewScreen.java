
import models.OnTemplateChangeListener;
import models.OnTemplateItemClickListener;
import org.apache.jena.reasoner.rulesys.builtins.Print;
import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.models.template.*;
import za.co.mahlaza.research.templateparsing.TemplateWriter;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class MainTemplateViewScreen implements OnTemplateChangeListener, OnTemplateItemClickListener {
    private int frameX;
    private int frameY;
    private ToCTeditorFrame frame;

    JPanel pnlTemplateItems;
    JPanel pnlParentForEditorAndTTLPreview;
    JPanel pnlItemPropertyEditor;
    JPanel pnlTurtlePreview;
    JPanel pnlAboveEditorAndTurtleWithTitleOnly;

    PrintWriter previewOut;
    JTextArea txtArea;

    public MainTemplateViewScreen(ToCTeditorFrame frame/**, ControlThread controlThread*/) {
        this.frameX = frame.getFrameX();
        this.frameY = frame.getFrameY();
        this.frame = frame;
    }

    public void setupGUI(JPanel currentPartProperties) {
        // Frame init and dimensions
        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setSize(frameX,frameY);
        g.add(Box.createRigidArea(new Dimension(0,12)));
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setMaximumSize(new Dimension(800,550));


        //setting up the panels
        pnlTurtlePreview = setupPreviewPanel();
        pnlTemplateItems = setupTemplateRendererPanel();
        pnlAboveEditorAndTurtleWithTitleOnly = setupPanelForTitlesAboveEditorAndTTLPrefix();
        pnlItemPropertyEditor = setupPanelForlItemPropertyEditor();
        pnlParentForEditorAndTTLPreview = setupParentPanelForEditorAndTTLPreview();

        setupEditorTurtlePanel(currentPartProperties, pnlTurtlePreview);

        //Organising all the views in the main list pane
        JPanel listPane  = setupOrganisationofViewsInMainContainerPanel();

        // Add list panel to main panel
        pnlMain.add(listPane);
        pnlMain.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlMain.setBackground(Color.LIGHT_GRAY);

        g.add(pnlMain);
        frame.setContentPane(g);
    }

    public JPanel setupPanelForlItemPropertyEditor () {
        //Editor Panel
        JPanel pnlItemPropertyEditor = new JPanel();
        pnlItemPropertyEditor.setLayout(new BoxLayout(pnlItemPropertyEditor, BoxLayout.Y_AXIS));

        pnlItemPropertyEditor.setBackground(Color.lightGray);
        pnlItemPropertyEditor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        return pnlItemPropertyEditor;
    }


    public JPanel setupParentPanelForEditorAndTTLPreview() {
        /**
         * Editor Preview panel
         */
        JPanel pnlParentForEditorAndTTLPreview = new JPanel();
        pnlParentForEditorAndTTLPreview.setLayout(new BoxLayout(pnlParentForEditorAndTTLPreview, BoxLayout.LINE_AXIS));
        pnlParentForEditorAndTTLPreview.setMaximumSize(new Dimension(700,250));
        pnlParentForEditorAndTTLPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlParentForEditorAndTTLPreview.setBackground(Color.lightGray);

        return pnlParentForEditorAndTTLPreview;
    }


    public JPanel setupPanelForTitlesAboveEditorAndTTLPrefix() {
        // Panel for Editor and Turtle titles
        JPanel pnlAboveEditorAndTurtleWithTitleOnly = new JPanel();
        pnlAboveEditorAndTurtleWithTitleOnly.setLayout(new BoxLayout(pnlAboveEditorAndTurtleWithTitleOnly, BoxLayout.LINE_AXIS));
        pnlAboveEditorAndTurtleWithTitleOnly.setMaximumSize(new Dimension(700,15));
        pnlAboveEditorAndTurtleWithTitleOnly.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlAboveEditorAndTurtleWithTitleOnly.setBackground(Color.lightGray);
        return pnlAboveEditorAndTurtleWithTitleOnly;
    }

    public JPanel setupToggleTTLPreviewButtonPanel() {
        /**
         * Panel for turtle syntax toggle button
         */
        JPanel pnlToggleButton = new JPanel();
        pnlToggleButton.setLayout(new BoxLayout(pnlToggleButton, BoxLayout.LINE_AXIS));
        pnlToggleButton.setMaximumSize(new Dimension(700,30));
        pnlToggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlToggleButton.setBackground(Color.lightGray);

        JToggleButton tbtnTurtle = new JToggleButton("Show turtle preview");
        tbtnTurtle.setFont(new Font("Sans", Font.PLAIN, 14));
        tbtnTurtle.setMaximumSize(new Dimension(343,30));
        //tbtnTurtle.setSelected(true);


        /**
         * Add label and toggle button panel
         */
        tbtnTurtle.setSelected(ToCTeditor.gui.getToggleBtnState() == 1);
        pnlToggleButton.add(tbtnTurtle);

        ItemListener itemListener = itemEvent -> {
            int state = itemEvent.getStateChange();
            ToCTeditor.gui = new ViewThread();
            ToCTeditor.gui.setToggleBtnState(state);
            //ToCTeditor.gui.start();
            SwingUtilities.invokeLater(ToCTeditor.gui);
            System.out.println("State (2): " + state);
        };
        tbtnTurtle.addItemListener(itemListener);
        return pnlToggleButton;
    }

    public JPanel setupTitleScreenPanel() {
        /**
         * Title panel
         */
        JPanel pnlItemsTitle = new JPanel();
        pnlItemsTitle.setLayout(new BoxLayout(pnlItemsTitle, BoxLayout.LINE_AXIS));
        pnlItemsTitle.setMaximumSize(new Dimension(700,15));
        pnlItemsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label = new JLabel("Template Items");
        pnlItemsTitle.setBackground(Color.lightGray);
        label.setFont(new Font("Sans", Font.PLAIN, 15));

        pnlItemsTitle.add(label);
        return pnlItemsTitle;
    }

    public JPanel setupOrganisationofViewsInMainContainerPanel() {
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        // Add heading label to list panel
        JPanel pnlItemsTitle = setupTitleScreenPanel();
        listPane.add(pnlItemsTitle);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));

        // Add template name text field to list panel
        listPane.add(pnlTemplateItems);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel pnlToggleButton = setupToggleTTLPreviewButtonPanel();
        listPane.add(pnlToggleButton);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));
        listPane.add(pnlAboveEditorAndTurtleWithTitleOnly);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));

        // Add supported language dropdown button to list panel
        listPane.add(pnlParentForEditorAndTTLPreview);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));

        // Add create button to list panel
        JPanel pnlButtons = setupButtonsPanel();
        listPane.add(pnlButtons);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        listPane.setBackground(Color.LIGHT_GRAY);

        return listPane;
    }

    public void setupEditorTurtlePanel(JPanel currentPartProperties, JPanel turtlePreview) {
        JLabel lblEditorTitle = new JLabel("Item property editor");
        lblEditorTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblEditorTitle.setMaximumSize(new Dimension(345,15));

        JLabel lblTurtleTitle = new JLabel("Item turtle preview");
        lblTurtleTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblTurtleTitle.setMaximumSize(new Dimension(345,15));


        if (ToCTeditor.gui.getToggleBtnState() == 1) {
            /**
             * Add title labels to labels panel
             */
            pnlAboveEditorAndTurtleWithTitleOnly.add(lblEditorTitle);
            pnlAboveEditorAndTurtleWithTitleOnly.add(Box.createRigidArea(new Dimension(10,0)));
            pnlAboveEditorAndTurtleWithTitleOnly.add(lblTurtleTitle);

            pnlItemPropertyEditor.setMinimumSize(new Dimension(345,250));
            pnlItemPropertyEditor.setMaximumSize(new Dimension(345,250));

            /**
             * Add panels to editor preview panel
             */
            pnlParentForEditorAndTTLPreview.add(pnlItemPropertyEditor);
            pnlParentForEditorAndTTLPreview.add(Box.createRigidArea(new Dimension(10,0)));
            pnlParentForEditorAndTTLPreview.add(pnlTurtlePreview);
        }
        else {
            /**
             * Add title labels to labels panel
             */
            pnlAboveEditorAndTurtleWithTitleOnly.add(lblEditorTitle);
            pnlAboveEditorAndTurtleWithTitleOnly.add(Box.createRigidArea(new Dimension(10,0)));
            pnlItemPropertyEditor.setMaximumSize(new Dimension(700,250));

            pnlParentForEditorAndTTLPreview.add(pnlItemPropertyEditor);
        }

    }

    public void updateEditorTurtlePanel(JPanel currentTurtleCode){

        JLabel lblEditorTitle = new JLabel("Item property editor");
        lblEditorTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblEditorTitle.setMaximumSize(new Dimension(345,15));

        JLabel lblTurtleTitle = new JLabel("Item turtle preview");
        lblTurtleTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblTurtleTitle.setMaximumSize(new Dimension(345,15));

        int titleCount = pnlAboveEditorAndTurtleWithTitleOnly.getComponentCount();
        for (int i = titleCount - 1; i >= 0 ; i--){
            pnlAboveEditorAndTurtleWithTitleOnly.remove(i);
        }

        if (ToCTeditor.gui.getToggleBtnState() == 1) {
            /**
             * Add title labels to labels panel
             */
            pnlAboveEditorAndTurtleWithTitleOnly.add(lblEditorTitle);
            pnlAboveEditorAndTurtleWithTitleOnly.add(Box.createRigidArea(new Dimension(10,0)));
            pnlAboveEditorAndTurtleWithTitleOnly.add(lblTurtleTitle);

            int compCount = pnlParentForEditorAndTTLPreview.getComponentCount();
            for (int i = compCount - 1; i >= 0 ; i--){
                pnlParentForEditorAndTTLPreview.remove(i);
            }

            pnlItemPropertyEditor.setMaximumSize(new Dimension(345,250));
            pnlTurtlePreview.setMaximumSize(new Dimension(345,250));

            pnlTurtlePreview.remove(0);
            pnlTurtlePreview.add(currentTurtleCode);

            pnlParentForEditorAndTTLPreview.add(pnlItemPropertyEditor);
            pnlParentForEditorAndTTLPreview.add(Box.createRigidArea(new Dimension(10,0)));
            pnlParentForEditorAndTTLPreview.add(pnlTurtlePreview);

            this.pnlParentForEditorAndTTLPreview.revalidate();
        }
        else{
            /**
             * Add title label to labels panel
             */
            pnlAboveEditorAndTurtleWithTitleOnly.add(lblEditorTitle);
            int compCount = pnlParentForEditorAndTTLPreview.getComponentCount();
            for (int i = compCount - 1; i >= 0 ; i--){
                pnlParentForEditorAndTTLPreview.remove(i);
            }
            pnlItemPropertyEditor.setMaximumSize(new Dimension(700,250));
            pnlParentForEditorAndTTLPreview.add(pnlItemPropertyEditor);
            this.pnlParentForEditorAndTTLPreview.revalidate();
        }

    }

    public JPanel setupTemplateRendererPanel() {
        JPanel pnlItems = new JPanel();
        pnlItems.setLayout(new BoxLayout(pnlItems, BoxLayout.LINE_AXIS));
        pnlItems.setMaximumSize(new Dimension(700,145));
        pnlItems.setBackground(Color.lightGray);
        pnlItems.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlItems.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlItems.add(setupRenderedTemplatePortions(frame.currTemplate.getTemplatePortions()));

        return pnlItems;
    }

    public JComponent setupRenderedTemplatePortions(List<TemplatePortion> list) {
        Box box = Box.createHorizontalBox();

        JScrollPane pnlScroll = new JScrollPane(box, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlScroll.setMinimumSize(new Dimension(700, 145));
        pnlScroll.setMaximumSize(new Dimension(700, 145));

        DragMouseAdapter dh = new DragMouseAdapter();
        box.addMouseListener(dh);
        box.addMouseMotionListener(dh);

        for (TemplatePortion templatePortion : list) {
            String type = templatePortion.getClass().getSimpleName();

            if ( type.equals("PolymorphicWord") && ((PolymorphicWord)templatePortion).getAllMorphemes().size() > 0){
                box.add(createCompoundTemplatePortionBoxComponent(templatePortion, type));
            }
            else{
                JLabel name = new JLabel(templatePortion.getSerialisedName());
                JLabel lblType = new JLabel(type);
                box.add(createSingleTemplatePortionBoxComponent(name, lblType, false));
            }
        }
        JPanel p = new JPanel();
        p.setMaximumSize(new Dimension(700, 145));
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setBackground(Color.lightGray);
        p.add(pnlScroll);
        return p;
    }

    private JPanel createCompoundTemplatePortionBoxComponent(TemplatePortion templatePortion, String type) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.white);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 5, 0, 5),
                BorderFactory.createLineBorder(Color.BLUE, 2)));

        /**
         * Set up panel for menu button, and then add menu button to the panel
         */
        JPanel pnlKebab = new JPanel();
        pnlKebab.setLayout(new BoxLayout(pnlKebab, BoxLayout.LINE_AXIS));
        pnlKebab.setBackground(Color.white);

        JPopupMenu menu = new JPopupMenu();
        menu.setMaximumSize(new Dimension(200, 100));
        menu.add(new JMenuItem("Duplicate"));
        menu.add(new JMenuItem("Change type"));
        menu.add(new JMenuItem("Remove"));

        final JButton button = new JButton();
        /**
         * Images folder is stored in the bin folder.
         * Reason why found here (last answer is the only one that works with a Makefile):
         * https://stackoverflow.com/questions/13151979/null-pointer-exception-when-an-imageicon-is-added-to-jbutton-in-netbeans
         */
        button.setIcon(createImageIcon("/images/kebab.png"));
        button.setBorderPainted(false);
        button.setBackground(Color.white);
        button.setMinimumSize(new Dimension(35,8));
        button.setMaximumSize(new Dimension(35,8));


        button.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ev){
                menu.show(button, 0, button.getHeight());
            }
        });

        /**
         * Set up panel for the item name, and then add a label to the panel
         */

        JLabel name = new JLabel(templatePortion.getSerialisedName());
        name.setFont(new Font("Sans", Font.PLAIN, 14));

        JPanel pnlName = new JPanel();
        pnlName.setLayout(new BoxLayout(pnlName, BoxLayout.Y_AXIS));
        pnlName.setBackground(Color.white);
        pnlName.add(name);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nameAndButton = new JPanel();
        nameAndButton.setLayout(new BoxLayout(nameAndButton, BoxLayout.LINE_AXIS));
        nameAndButton.add((JLabel)name);
        nameAndButton.add(Box.createRigidArea(new Dimension(5,0)));
        nameAndButton.add(button);
        nameAndButton.setBackground(Color.white);
        /**
         * Set up morphemes
         */
        JPanel pnlMorphemes = new JPanel();
        pnlMorphemes.setLayout(new BoxLayout(pnlMorphemes, BoxLayout.LINE_AXIS));
        DragMouseAdapter dh = new DragMouseAdapter();
        pnlMorphemes.addMouseListener(dh);
        int maxWidth = 0;
        pnlMorphemes.addMouseMotionListener(dh);
        List<InternalSlotRootAffix> morphemesList = ((PolymorphicWord)templatePortion).getAllMorphemes();
        for (int i = 0; i < morphemesList.size(); i++){
            JLabel mName = new JLabel(morphemesList.get(i).getSerialisedName());
            JLabel mType = new JLabel(morphemesList.get(i).getType());
            maxWidth += 116;
            pnlMorphemes.add(createSingleTemplatePortionBoxComponent(mName, mType, true));
        }

        pnlMorphemes.setBackground(Color.white);
        pnlKebab.setMinimumSize(new Dimension(maxWidth ,8));
        pnlKebab.setMaximumSize(new Dimension(maxWidth ,8));
        pnlKebab.add(Box.createRigidArea(new Dimension((maxWidth),0)));

        pnlKebab.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlName.setMinimumSize(new Dimension(maxWidth ,15));
        pnlName.setMaximumSize(new Dimension(maxWidth ,15));

        nameAndButton.setMinimumSize(new Dimension(maxWidth ,15));
        pnlName.add(nameAndButton);
        pnlMorphemes.setMinimumSize(new Dimension(maxWidth, 42));
        pnlMorphemes.setMaximumSize(new Dimension(maxWidth, 42));
        p.add(pnlName);
        pnlName.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(pnlMorphemes);
        pnlMorphemes.setAlignmentX(Component.CENTER_ALIGNMENT);

        /**
         * Set up panel for the item type, and then add a label to the panel
         */
        JLabel lblType = new JLabel(type);
        lblType.setFont(new Font("Sans", Font.PLAIN, 8));

        JPanel pnlType = new JPanel();
        pnlType.setLayout(new BoxLayout(pnlType, BoxLayout.LINE_AXIS));
        pnlType.setMaximumSize(new Dimension(maxWidth,8));
        pnlType.add(lblType);
        pnlType.setBackground(Color.white);
        p.add(pnlType);

        p.setMaximumSize(new Dimension(maxWidth , 90));
        p.setMinimumSize(new Dimension(maxWidth, 90));
        p.setOpaque(false);
        return p;
    }

    private JPanel createSingleTemplatePortionBoxComponent(JComponent name, JComponent type, boolean isMorpheme) {

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.white);


        /**
         * Set up panel for menu button, and then add menu button to the panel
         */
        JPanel pnlKebab = new JPanel();
        pnlKebab.setLayout(new BoxLayout(pnlKebab, BoxLayout.LINE_AXIS));
        if (isMorpheme){
            p.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 2, 0, 2),
                    BorderFactory.createLineBorder(Color.BLUE, 2)));
            pnlKebab.setMinimumSize(new Dimension(100,8));
            pnlKebab.setMaximumSize(new Dimension(100,8));
        }
        else{
            p.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 5, 0, 5),
                    BorderFactory.createLineBorder(Color.BLUE, 2)));
            pnlKebab.setMaximumSize(new Dimension(100,20));
        }

        pnlKebab.setBackground(Color.white);

        JPopupMenu menu = new JPopupMenu();
        menu.setMaximumSize(new Dimension(200, 100));
        menu.add("Duplicate").addActionListener(
            e -> {
                /**ToCTeditor.dataModel.duplicateTemplatePortion(((JLabel)name).getText().trim());
                ToCTeditor.dataModel.updateNextPart();
                updateItems();*/
            }
        );
        menu.add(new JMenuItem("Change type"));
        menu.add("Remove").addActionListener( e -> {
            //TODO: replace the following two lines
            //ToCTeditor.dataModel.removeTemplatePortion(((JLabel)name).getText().trim());
            //ToCTeditor.dataModel.updateNextPart();
            onAddNewTemplate(ToCTeditorFrame.currTemplate);
        });

        final JButton button = new JButton();
        /**
         * Images folder is stored in the bin folder.
         * Reason why found here (last answer is the only one that works with a Makefile):
         * https://stackoverflow.com/questions/13151979/null-pointer-exception-when-an-imageicon-is-added-to-jbutton-in-netbeans
         */
        button.setIcon(createImageIcon("/images/kebab.png"));
        button.setBorderPainted(false);
        button.setBackground(Color.white);
        if (isMorpheme){
            pnlKebab.add(Box.createRigidArea(new Dimension(50,0)));
            button.setMinimumSize(new Dimension(35,8));
            button.setMaximumSize(new Dimension(35,8));
        }
        else {
            pnlKebab.add(Box.createRigidArea(new Dimension(30,0)));
            button.setMaximumSize(new Dimension(35, 15));
        }
        button.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent ev){
                menu.show(button, 0, button.getHeight());
            }
        });


        pnlKebab.add(button);
        /**
         * Set up panel for the item name, and then add a label to the panel
         */
        name.setFont(new Font("Sans", Font.PLAIN, 14));

        JPanel pnlName = new JPanel();
        pnlName.setLayout(new BoxLayout(pnlName, BoxLayout.Y_AXIS));
        if (isMorpheme){
            pnlName.setMinimumSize(new Dimension(100,15));
            pnlName.setMaximumSize(new Dimension(100,15));
        }
        else{
            pnlName.setMaximumSize(new Dimension(100,50));
        }
        pnlName.add(name);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlName.setBackground(Color.white);

        /**
         * Set up panel for the item type, and then add a label to the panel
         */
        type.setFont(new Font("Sans", Font.PLAIN, 8));

        JPanel pnlType = new JPanel();
        pnlType.setLayout(new BoxLayout(pnlType, BoxLayout.LINE_AXIS));
        pnlType.add(type);
        pnlType.setBackground(Color.white);


        if (isMorpheme) {
            pnlType.setMinimumSize(new Dimension(100,8));
            pnlType.setMaximumSize(new Dimension(100,8));
            p.setMaximumSize(new Dimension(100, 42));
        }
        else{
            pnlType.setMaximumSize(new Dimension(100,30));
            p.setMaximumSize(new Dimension(100, 90));
        }
        p.add(pnlKebab);
        pnlKebab.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(pnlName);
        pnlName.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(pnlType);
        pnlType.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.setOpaque(false);
        return p;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public JPanel setupPartEditorPanelForTemplatePortion(TemplatePortion currentTemplatePortion) {
        JPanel currentPanel = null;
        if (currentTemplatePortion != null) {
            String type = currentTemplatePortion.getClass().getSimpleName();
            if (!type.isEmpty()) {
                currentPanel = setupPropertiesInEditorPanel(currentTemplatePortion);
            }
            else{
                currentPanel = new JPanel();
                currentPanel.setBackground(Color.lightGray);
            }
        }
        else{
            currentPanel = new JPanel();
            currentPanel.setBackground(Color.lightGray);
        }
        return currentPanel;
    }

    public JPanel setupButtonsPanel() {
        /**
         * Buttons panel
         */
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS));
        pnlButtons.setMaximumSize(new Dimension(700,30));
        pnlButtons.setBackground(Color.lightGray);

        /**
         * Save template button
         */
        JButton btnSaveTemplate = new JButton("Save Template");
        btnSaveTemplate.setFont(new Font("Sans", Font.PLAIN, 15));
        btnSaveTemplate.setMaximumSize(new Dimension(226,30));

        /**
         * Back button
         */
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Sans", Font.PLAIN, 15));
        btnBack.setMaximumSize(new Dimension(226,30));

        // add the listener to the jbutton to handle the "pressed" event
        btnBack.addActionListener(e -> {
            ToCTeditor.gui = new ViewThread();
            ToCTeditor.gui.setCallCreateTemplate(true);
            ToCTeditor.gui.start();
        });

        /**
         * Add button
         */
        JButton btnAdd = new JButton("Add Template Item");
        btnAdd.setFont(new Font("Sans", Font.PLAIN, 15));
        btnAdd.setMaximumSize(new Dimension(226,30));

        // add the listener to the jbutton to handle the "pressed" event
        btnAdd.addActionListener(e -> {
            ToCTeditor.gui = new ViewThread(ToCTeditor.homeScreen, ToCTeditor.templateItems, ToCTeditor.createItem);

            ToCTeditor.gui.setCallCreateItem(true);
            //ToCTeditor.gui.start();
            SwingUtilities.invokeLater(ToCTeditor.gui);
        });


        /**
         * Add buttons to buttons panel
         */
        pnlButtons.add(btnBack);
        pnlButtons.add(Box.createRigidArea(new Dimension(11,0)));
        pnlButtons.add(btnAdd);
        pnlButtons.add(Box.createRigidArea(new Dimension(11,0)));
        pnlButtons.add(btnSaveTemplate);
        return pnlButtons;
    }

    public JPanel setupPreviewPanel() {
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
        currentPanel.setBackground(Color.lightGray);
        currentPanel.setMaximumSize(new Dimension(345, 250));
        txtArea = new JTextArea();
        txtArea.setEditable(false);
        txtArea.setMaximumSize(new Dimension(345, 250));
        txtArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtArea.setFont(new Font("Sans", Font.PLAIN, 12));
        txtArea.setForeground(Color.blue);
        JScrollPane pnlScroll = new JScrollPane(txtArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlScroll.setMaximumSize(new Dimension(345, 250));
        pnlScroll.setBackground(Color.lightGray);

        previewOut = new PrintWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                String text = ""+(char)(b & 0xFF);
                txtArea.append(text);
                currentPanel.add(pnlScroll);
            }
        }, true);
        onAddNewTemplate(ToCTeditorFrame.currTemplate);

        return currentPanel;
    }

    public JPanel setupPropertiesInEditorPanel(TemplatePortion currentTemplatePortion) {
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
        editorPanel.setMaximumSize(new Dimension(345,250));
        editorPanel.setBackground(Color.lightGray);

        Method[] methods0 = TemplatePortion.class.getDeclaredMethods();
        Method[] methods1 = SlotFiller.class.getDeclaredMethods();

        Method[] methods = Stream.concat(Arrays.stream(methods0), Arrays.stream(methods1))
                .toArray(Method[]::new);

        for (Method method : methods) {
            if (method.isAnnotationPresent(RelationSetter.class)) {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annot : annotations) {
                    RelationSetter relsetter = (RelationSetter) annot;

                    JPanel fieldSetterPanel = new JPanel();
                    fieldSetterPanel.setLayout(new BoxLayout(fieldSetterPanel, BoxLayout.LINE_AXIS));
                    fieldSetterPanel.setMaximumSize(new Dimension(340,30));
                    fieldSetterPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    fieldSetterPanel.setBackground(Color.lightGray);

                    JLabel relLabel = new JLabel(relsetter.RangeName());
                    relLabel.setFont(new Font("Sans", Font.PLAIN, 14));
                    relLabel.setMaximumSize(new Dimension(100, 30));
                    fieldSetterPanel.add(relLabel);

                    switch (relsetter.RdfName()) {
                        case "hasValue" : {
                            JTextField txtNextPart = new JTextField();
                            txtNextPart.setFont(new Font("Sans", Font.PLAIN, 14));
                            txtNextPart.setMaximumSize(new Dimension(225, 30));
                            fieldSetterPanel.add(txtNextPart);
                            editorPanel.add(fieldSetterPanel);

                            txtNextPart.addCaretListener(caretEvent -> {
                                String txtVal = txtNextPart.getText();
                                if (currentTemplatePortion instanceof SlotFiller) {
                                    ((SlotFiller) currentTemplatePortion).setValue(txtVal);
                                }
                                else if (currentTemplatePortion instanceof Word) {
                                    ((Word) currentTemplatePortion).setValue(txtVal);
                                }
                                else if (currentTemplatePortion instanceof Punctuation) {
                                    ((Punctuation) currentTemplatePortion).setValue(txtVal);
                                }
                                onAddNewTemplate(ToCTeditorFrame.currTemplate);
                            });
                            break;
                        }
                        case "serialisedName" : {
                            JTextField txtNextPart = new JTextField();
                            txtNextPart.setFont(new Font("Sans", Font.PLAIN, 14));
                            txtNextPart.setMaximumSize(new Dimension(225, 30));
                            fieldSetterPanel.add(txtNextPart);
                            editorPanel.add(fieldSetterPanel);

                            txtNextPart.setText(currentTemplatePortion.getSerialisedName());
                            txtNextPart.addCaretListener(caretEvent -> {
                                String txt = txtNextPart.getText();
                                Objects.requireNonNull(txt);
                                currentTemplatePortion.setSerialisedName(txt);
                                onAddNewTemplate(ToCTeditorFrame.currTemplate);
                            });
                            break;
                        }
                        case "hasNextPart" : {
                            int index = ToCTeditor.gui.getIndex();
                            List<TemplatePortion> portions = ToCTeditorFrame.currTemplate.getTemplatePortions();

                            TemplatePortion[] choices = new TemplatePortion[portions.size()-1];
                            portions.subList(index+1, portions.size()).toArray(choices);

                            final JComboBox<TemplatePortion> dropdown = new JComboBox<>(choices);
                            fieldSetterPanel.add(dropdown);
                            editorPanel.add(fieldSetterPanel);
                            dropdown.setRenderer(new BasicComboBoxRenderer() {
                                @Override
                                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                                    if (value instanceof TemplatePortion) {
                                        TemplatePortion val = (TemplatePortion) value;
                                        setText(val.getSerialisedName());
                                        onAddNewTemplate(ToCTeditorFrame.currTemplate);
                                    }
                                    return this;
                                }
                            });
                            dropdown.addItemListener(itemEvent -> {
                                TemplatePortion chosenNextItem = (TemplatePortion) itemEvent.getItem();
                                currentTemplatePortion.setNextPart(chosenNextItem);
                                onAddNewTemplate(ToCTeditorFrame.currTemplate);
                            });
                            dropdown.setEnabled(choices.length > 0 ? true : false);
                            break;
                        }
                        case "hasLabel" : {
                            JTextField txtLabel = new JTextField();
                            txtLabel.setFont(new Font("Sans", Font.PLAIN, 14));
                            txtLabel.setMaximumSize(new Dimension(225, 30));
                            fieldSetterPanel.add(txtLabel);
                            editorPanel.add(fieldSetterPanel);

                            String label = txtLabel.getText();
                            if (currentTemplatePortion instanceof Slot) {
                                ((Slot) currentTemplatePortion).setLabel(label);
                                onAddNewTemplate(ToCTeditorFrame.currTemplate);
                            }
                        }
                    }
                }
            }
        }
        return editorPanel;
    }

    @Override
    public void onAddNewTemplate(Template template) {
        SwingUtilities.invokeLater(() -> {
            if (txtArea != null) {
                txtArea.setText("");  //clearing the existing preview
            }

            // printing the new template in the preview window
            List<Template> templates = new LinkedList<>();
            templates.add(ToCTeditorFrame.currTemplate);
            String uri = ToCTeditorFrame.currTemplate.getURI();
            onAddNewTemplates(templates, uri);

            // re-rendering the template items at the top.
            if (pnlTemplateItems != null) {
                int numberOfRenderedTemplates = pnlTemplateItems.getComponentCount();
                if (numberOfRenderedTemplates > 0) {
                    pnlTemplateItems.remove(0);
                }
                Component newlyRenderedTemplates = setupRenderedTemplatePortions(ToCTeditorFrame.currTemplate.getTemplatePortions());
                pnlTemplateItems.add(newlyRenderedTemplates);
                pnlTemplateItems.getParent().revalidate();
            }
        });
    }

    @Override
    public void onAddNewTemplates(List<Template> templates, String uri) {
        try {
            TemplateWriter.saveTemplates(templates, uri, previewOut);

            pnlTurtlePreview.revalidate();
            pnlTurtlePreview.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(TemplatePortion templatePortion) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //loading the template portion's panel (i.e., panel is loaded with the properties and their values)
                JPanel pnlChosenPortionsProrties = setupPartEditorPanelForTemplatePortion(templatePortion);
                int numberOfLoadedPanelInPropEditor = pnlItemPropertyEditor.getComponentCount();
                if (numberOfLoadedPanelInPropEditor > 0) {
                    pnlItemPropertyEditor.remove(0); //if there was an item already loaded in prop. editor then remove it first.
                }
                pnlItemPropertyEditor.add(pnlChosenPortionsProrties);
                pnlItemPropertyEditor.getParent().revalidate();
            }
        });
    }
}
