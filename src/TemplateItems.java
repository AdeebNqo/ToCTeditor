
import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.template.*;
import za.co.mahlaza.research.templateparsing.TemplateWriter;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class TemplateItems {
    private int frameX;
    private int frameY;
    private ToCTeditorFrame frame;

    JPanel pnlItems;
    JPanel pnlEditorPreview;
    JPanel pnlItemEditor;
    JPanel pnlTurtlePreview;
    JPanel pnlEditorTurtleTitle;

    public TemplateItems(ToCTeditorFrame frame/**, ControlThread controlThread*/) {
        this.frameX = frame.getFrameX();
        this.frameY = frame.getFrameY();
        this.frame = frame;
    }

    public void setupGUI(JPanel currentPartProperties, JPanel turtlePreview){
        // Frame init and dimensions
        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setSize(frameX,frameY);
        g.add(Box.createRigidArea(new Dimension(0,12)));
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setMaximumSize(new Dimension(800,550));

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

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

        /**
         * Items panel
         */
        pnlItems = new JPanel();
        pnlItems.setLayout(new BoxLayout(pnlItems, BoxLayout.LINE_AXIS));
        pnlItems.setMaximumSize(new Dimension(700,145));
        pnlItems.setBackground(Color.lightGray);
        pnlItems.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlItems.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        pnlItems.add(setupTemplatePortions(frame.currTemplate.getTemplatePortions()));

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
            ToCTeditor.gui.start();
            System.out.println("State (2): " + state);
        };
        tbtnTurtle.addItemListener(itemListener);

        /**
         * Panel for Editor and Turtle titles
         */
        pnlEditorTurtleTitle = new JPanel();
        pnlEditorTurtleTitle.setLayout(new BoxLayout(pnlEditorTurtleTitle, BoxLayout.LINE_AXIS));
        pnlEditorTurtleTitle.setMaximumSize(new Dimension(700,15));
        pnlEditorTurtleTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlEditorTurtleTitle.setBackground(Color.lightGray);

        /**
         * Editor Preview panel
         */
        pnlEditorPreview = new JPanel();
        pnlEditorPreview.setLayout(new BoxLayout(pnlEditorPreview, BoxLayout.LINE_AXIS));
        pnlEditorPreview.setMaximumSize(new Dimension(700,250));
        pnlEditorPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlEditorPreview.setBackground(Color.lightGray);

        setupEditorTurtlePanel(currentPartProperties, turtlePreview);

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
            ToCTeditor.gui.start();
        });

        /**
         * Save template button
         */
        JButton btnSaveTemplate = new JButton("Save Template");
        btnSaveTemplate.setFont(new Font("Sans", Font.PLAIN, 15));
        btnSaveTemplate.setMaximumSize(new Dimension(226,30));

        /**
         * Buttons panel
         */
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.LINE_AXIS));
        pnlButtons.setMaximumSize(new Dimension(700,30));
        pnlButtons.setBackground(Color.lightGray);

        /**
         * Add buttons to buttons panel
         */
        pnlButtons.add(btnBack);
        pnlButtons.add(Box.createRigidArea(new Dimension(11,0)));
        pnlButtons.add(btnAdd);
        pnlButtons.add(Box.createRigidArea(new Dimension(11,0)));
        pnlButtons.add(btnSaveTemplate);

        // Add heading label to list panel
        listPane.add(pnlItemsTitle);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        // Add template name text field to list panel
        listPane.add(pnlItems);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));
        listPane.add(pnlToggleButton);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));
        listPane.add(pnlEditorTurtleTitle);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        // Add supported language dropdown button to list panel
        listPane.add(pnlEditorPreview);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));
        // Add create button to list panel
        listPane.add(pnlButtons);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        listPane.setBackground(Color.LIGHT_GRAY);

        // Add list panel to main panel
        pnlMain.add(listPane);
        pnlMain.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlMain.setBackground(Color.LIGHT_GRAY);

        g.add(pnlMain);
        frame.setContentPane(g);
    }

    public void setupEditorTurtlePanel(JPanel currentPartProperties, JPanel turtlePreview) {
        JLabel lblEditorTitle = new JLabel("Item property editor");
        lblEditorTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblEditorTitle.setMaximumSize(new Dimension(345,15));

        JLabel lblTurtleTitle = new JLabel("Item turtle preview");
        lblTurtleTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblTurtleTitle.setMaximumSize(new Dimension(345,15));


        /**
         * Editor Panel
         */
        pnlItemEditor = new JPanel();
        pnlItemEditor.setLayout(new BoxLayout(pnlItemEditor, BoxLayout.Y_AXIS));

        pnlItemEditor.setBackground(Color.lightGray);
        pnlItemEditor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlItemEditor.add(currentPartProperties);

        /**
         * Turtle Preview Panel
         */
        pnlTurtlePreview = new JPanel();
        pnlTurtlePreview.setLayout(new BoxLayout(pnlTurtlePreview, BoxLayout.Y_AXIS));
        pnlTurtlePreview.setMinimumSize(new Dimension(345,250));
        pnlTurtlePreview.setMaximumSize(new Dimension(345,250));
        pnlTurtlePreview.setBackground(Color.lightGray);
        pnlTurtlePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlTurtlePreview.add(turtlePreview);

        if (ToCTeditor.gui.getToggleBtnState() == 1) {
            /**
             * Add title labels to labels panel
             */
            pnlEditorTurtleTitle.add(lblEditorTitle);
            pnlEditorTurtleTitle.add(Box.createRigidArea(new Dimension(10,0)));
            pnlEditorTurtleTitle.add(lblTurtleTitle);

            pnlItemEditor.setMinimumSize(new Dimension(345,250));
            pnlItemEditor.setMaximumSize(new Dimension(345,250));

            /**
             * Add panels to editor preview panel
             */
            pnlEditorPreview.add(pnlItemEditor);
            pnlEditorPreview.add(Box.createRigidArea(new Dimension(10,0)));
            pnlEditorPreview.add(pnlTurtlePreview);
        }
        else {
            System.out.println("The second if stmt");
            /**
             * Add title labels to labels panel
             */
            pnlEditorTurtleTitle.add(lblEditorTitle);
            pnlEditorTurtleTitle.add(Box.createRigidArea(new Dimension(10,0)));
            pnlItemEditor.setMaximumSize(new Dimension(700,250));

            pnlEditorPreview.add(pnlItemEditor);
        }

    }

    public void updateEditorTurtlePanel(JPanel currentPartProperties, JPanel currentTurtleCode){

        JLabel lblEditorTitle = new JLabel("Item property editor");
        lblEditorTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblEditorTitle.setMaximumSize(new Dimension(345,15));

        JLabel lblTurtleTitle = new JLabel("Item turtle preview");
        lblTurtleTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblTurtleTitle.setMaximumSize(new Dimension(345,15));

        int titleCount = pnlEditorTurtleTitle.getComponentCount();
        for (int i = titleCount - 1; i >= 0 ; i--){
            pnlEditorTurtleTitle.remove(i);
        }

        if (ToCTeditor.gui.getToggleBtnState() == 1) {
            /**
             * Add title labels to labels panel
             */
            pnlEditorTurtleTitle.add(lblEditorTitle);
            pnlEditorTurtleTitle.add(Box.createRigidArea(new Dimension(10,0)));
            pnlEditorTurtleTitle.add(lblTurtleTitle);

            int compCount = pnlEditorPreview.getComponentCount();
            for (int i = compCount - 1; i >= 0 ; i--){
                pnlEditorPreview.remove(i);
            }

            pnlItemEditor.setMaximumSize(new Dimension(345,250));
            pnlTurtlePreview.setMaximumSize(new Dimension(345,250));

            pnlTurtlePreview.remove(0);
            pnlTurtlePreview.add(currentTurtleCode);

            pnlEditorPreview.add(pnlItemEditor);
            pnlEditorPreview.add(Box.createRigidArea(new Dimension(10,0)));
            pnlEditorPreview.add(pnlTurtlePreview);

            this.pnlEditorPreview.revalidate();
        }
        else{
            /**
             * Add title label to labels panel
             */
            pnlEditorTurtleTitle.add(lblEditorTitle);
            int compCount = pnlEditorPreview.getComponentCount();
            for (int i = compCount - 1; i >= 0 ; i--){
                pnlEditorPreview.remove(i);
            }
            pnlItemEditor.setMaximumSize(new Dimension(700,250));
            pnlEditorPreview.add(pnlItemEditor);
            this.pnlEditorPreview.revalidate();
        }

    }

    public void updateEditorPanel(JPanel currentPartProperties) {
        pnlItemEditor.remove(0);
        this.pnlItemEditor.add(currentPartProperties);
        this.pnlItemEditor.getParent().revalidate();
    }

    public void updateTurtlePanel(JPanel currentPartProperties){
        if (ToCTeditor.gui.getToggleBtnState() == 1) {
            this.pnlTurtlePreview.remove(0);
            this.pnlTurtlePreview.add(currentPartProperties);
            this.pnlTurtlePreview.getParent().revalidate();
        }
    }

    public void updateShownTemplate() {
        this.pnlItems.remove(0);
        pnlItems.add(setupTemplatePortions(ToCTeditorFrame.currTemplate.getTemplatePortions()));
        this.pnlItems.getParent().revalidate();
    }

    public JComponent setupTemplatePortions(List<TemplatePortion> list) {
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
                box.add(createPortionComponent(templatePortion, type));
            }
            else{
                JLabel name = new JLabel(templatePortion.getSerialisedName());
                JLabel lblType = new JLabel(type);
                box.add(createItemComponent(name, lblType, false));
            }
        }
        JPanel p = new JPanel();
        p.setMaximumSize(new Dimension(700, 145));
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setBackground(Color.lightGray);
        p.add(pnlScroll);
        return p;
    }

    private JPanel createPortionComponent(TemplatePortion templatePortion, String type) {
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
            pnlMorphemes.add(createItemComponent(mName, mType, true));
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

    private JPanel createItemComponent(JComponent name, JComponent type, boolean isMorpheme) {

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
            updateShownTemplate();
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

    public JPanel getPartPanelEditor(TemplatePortion currentTemplatePortion) {
        JPanel currentPanel = null;
        if (currentTemplatePortion != null) {
            String type = currentTemplatePortion.getClass().getSimpleName();
            if (!type.isEmpty()) {
                currentPanel = setupEditorVars(currentTemplatePortion);
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

    public JPanel getPreviewPanel() {
        //String type = ToCTeditor.turtleGen.getPartType(currentPart);
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
        currentPanel.setBackground(Color.lightGray);
        currentPanel.setMaximumSize(new Dimension(345, 250));
        JTextArea txtArea = new JTextArea();
        txtArea.setEditable(false);
        txtArea.setMaximumSize(new Dimension(345, 250));
        txtArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtArea.setFont(new Font("Sans", Font.PLAIN, 12));
        txtArea.setForeground(Color.blue);
        JScrollPane pnlScroll = new JScrollPane(txtArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlScroll.setMaximumSize(new Dimension(345, 250));
        pnlScroll.setBackground(Color.lightGray);

        PrintWriter previewOut = new PrintWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                String text = ""+(char)(b & 0xFF);
                txtArea.append(text);
                currentPanel.add(pnlScroll);
            }
        });

        Collection<Template> templates = new LinkedList<>();
        templates.add(frame.currTemplate);
        String uri = frame.currTemplate.getURI();
        try {
            TemplateWriter.saveTemplates(templates, uri, previewOut);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentPanel;
    }

    public JPanel setupEditorVars(TemplatePortion currentTemplatePortion) {
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

                            if (currentTemplatePortion instanceof SlotFiller) {
                                txtNextPart.addCaretListener(caretEvent -> {
                                    String txtVal = txtNextPart.getText();
                                    ((SlotFiller) currentTemplatePortion).setValue(txtVal);
                                    System.out.println("Set value of curr. template portion");
                                });
                            }
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
                                    }
                                    return this;
                                }
                            });
                            dropdown.addItemListener(itemEvent -> {
                                TemplatePortion chosenNextItem = (TemplatePortion) itemEvent.getItem();
                                currentTemplatePortion.setNextPart(chosenNextItem);
                            });
                            dropdown.setEnabled(choices.length > 0 ? true : false);
                        }
                    }
                }
            }
        }

        return editorPanel;
    }

    /**
     * Installs a listener to receive notification when the text of any
     * {@code JTextComponent} is changed. Internally, it installs a
     * {@link DocumentListener} on the text component's {@link Document},
     * and a {@link PropertyChangeListener} on the text component to detect
     * if the {@code Document} itself is replaced.
     *
     * @param text any text component, such as a {@link JTextField}
     *        or {@link JTextArea}
     * @param changeListener a listener to receieve {@link ChangeEvent}s
     *        when the text is changed; the source object for the events
     *        will be the text component
     * @throws NullPointerException if either parameter is null
     */
    public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(changeListener);
        DocumentListener dl = new DocumentListener() {
            private int lastChange = 0, lastNotifiedChange = 0;

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lastChange++;
                SwingUtilities.invokeLater(() -> {
                    if (lastNotifiedChange != lastChange) {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(text));
                    }
                });
            }
        };
        text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
            Document d1 = (Document)e.getOldValue();
            Document d2 = (Document)e.getNewValue();
            if (d1 != null) d1.removeDocumentListener(dl);
            if (d2 != null) d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });
        Document d = text.getDocument();
        if (d != null) d.addDocumentListener(dl);
    }
}
