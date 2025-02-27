import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.models.template.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateTemplateItemScreen {
    private int frameX;
    private int frameY;
    private ToCTeditorFrame frame;

    public CreateTemplateItemScreen(ToCTeditorFrame frame/**, ControlThread controlThread*/) {
        this.frameX = frame.getFrameX();
        this.frameY = frame.getFrameY();
        this.frame = frame;
    }

    public void setupGUI() {

        // Frame init and dimensions
        JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
        g.setSize(frameX,frameY);
        g.add(Box.createRigidArea(new Dimension(0,25)));
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setMaximumSize(new Dimension(800,525));

        // Organise all views in the list panel
        JPanel listPane = setupOrganisationofViewsInMainContainerPanel();

        // Add list panel to main panel
        pnlMain.add(listPane);
        pnlMain.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlMain.setBackground(Color.LIGHT_GRAY);

        g.add(pnlMain);

        frame.setContentPane(g);
    }


    public JComponent createTemplateItem(String type) {

        JButton btnType = new JButton(type);
        btnType.setFont(new Font("Sans", Font.PLAIN, 13));
        btnType.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnType.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnType.setMaximumSize(new Dimension(150, 50));
        btnType.setBackground(Color.white);

        /**
         * Add the listener to the JButton to handle the "pressed" event
         */

        if (type.equals("Slot")) {

            btnType.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    List<Feature> featureList = new ArrayList<>();
                    int templateSize = frame.currTemplate.getTemplatePortions().size();
                    String id = "slot" + templateSize;
                    Slot slot = new Slot("", featureList);
                    slot.setSerialisedName(id);
                    ToCTeditorFrame.currTemplate.addTemplatePortion(slot);

                    viewCurrentTemplate();
                }
            });
        }
        else if (type.equals("UnimorphicWord")) {
            btnType.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int templateSize = frame.currTemplate.getTemplatePortions().size();
                    String id = "uniword" + templateSize;
                    UnimorphicWord unimorph = new UnimorphicWord("");
                    unimorph.setSerialisedName(id);
                    ToCTeditorFrame.currTemplate.addTemplatePortion(unimorph);

                    viewCurrentTemplate();
                }
            });
        }
        else if (type.equals("PolymorphicWord")) {
            btnType.addActionListener(e -> {
                ToCTeditor.gui = new ViewThread();
                ToCTeditor.gui.setCallCreateMorpheme(true);
                ToCTeditor.gui.setIndex(-1);
                //ToCTeditor.gui.start();
                SwingUtilities.invokeLater(ToCTeditor.gui);
            });
        }

        else if (type.equals("Punctuation")) {
            btnType.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int templateSize = frame.currTemplate.getTemplatePortions().size();
                    String id = "punct" + templateSize;
                    Punctuation punc = new Punctuation("");
                    punc.setSerialisedName(id);
                    ToCTeditorFrame.currTemplate.addTemplatePortion(punc);

                    viewCurrentTemplate();
                }
            });
        }
        else if (type.equals("Phrase")) {
            btnType.addActionListener(e -> {
                int templateSize = frame.currTemplate.getTemplatePortions().size();
                String id = "phrase" + templateSize;
                List<Word> words = new LinkedList<>();
                Phrase newPhrase = new Phrase(words);
                newPhrase.setSerialisedName(id);
                ToCTeditorFrame.currTemplate.addTemplatePortion(newPhrase);

                viewCurrentTemplate();
            });
        }

        return btnType;
    }

    public void viewCurrentTemplate() {
        ToCTeditor.gui = new ViewThread(ToCTeditor.homeScreen, ToCTeditor.templateItems, ToCTeditor.createItem);
        ToCTeditor.gui.setCallTemplateItems(true);
        ToCTeditor.gui.setIndex(frame.currTemplate.getTemplatePortions().size()-1);
        //ToCTeditor.gui.start();
        SwingUtilities.invokeLater(ToCTeditor.gui);
    }

    public JPanel setupOrganisationofViewsInMainContainerPanel() {
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

        //Title panel
        JPanel pnlItemsTitle = setupCreateItemLabelPanel();

        //Panel for Existing item selection title
        JPanel pnlExistingItemTitle = setupContainerPanelForSearchExistingItemsControls();
        JPanel pnlExistingItem = setupSearchExistingItemsPanel();

        // Add heading label to list panel
        listPane.add(pnlItemsTitle);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));

        // Add template name text field to list panel
        listPane.add(createPartOptions());
        listPane.add(Box.createRigidArea(new Dimension(0,10)));
        listPane.add(pnlExistingItemTitle);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));

        // Add supported language dropdown button to list panel
        listPane.add(pnlExistingItem);
        listPane.add(Box.createRigidArea(new Dimension(0,10)));

        // Add create button to list panel
        JPanel pnlButtons = setupButtonsPanel();
        listPane.add(pnlButtons);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        listPane.setBackground(Color.LIGHT_GRAY);
        return listPane;
    }

    public JPanel setupCreateItemLabelPanel () {
        JPanel pnlItemsTitle = new JPanel();
        pnlItemsTitle.setLayout(new BoxLayout(pnlItemsTitle, BoxLayout.LINE_AXIS));
        pnlItemsTitle.setMaximumSize(new Dimension(700,15));
        pnlItemsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel label = new JLabel("Create item");
        pnlItemsTitle.setBackground(Color.lightGray);
        label.setFont(new Font("Sans", Font.PLAIN, 15));
        pnlItemsTitle.add(label);
        return pnlItemsTitle;
    }

    public JPanel setupContainerPanelForSearchExistingItemsControls() {
        JPanel pnlExistingItemTitle = new JPanel();
        pnlExistingItemTitle.setLayout(new BoxLayout(pnlExistingItemTitle, BoxLayout.LINE_AXIS));
        pnlExistingItemTitle.setMaximumSize(new Dimension(700,15));
        pnlExistingItemTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlExistingItemTitle.setBackground(Color.lightGray);

        JLabel lblExistingItemTitle = new JLabel("Select from existing items");
        lblExistingItemTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        lblExistingItemTitle.setMaximumSize(new Dimension(345,15));

        /**
         * Add title labels to labels panel
         */
        pnlExistingItemTitle.add(lblExistingItemTitle);
        return pnlExistingItemTitle;
    }

    public JPanel setupSearchExistingItemsPanel () {
        JPanel pnlExistingItems = new JPanel();
        pnlExistingItems.setLayout(new BoxLayout(pnlExistingItems, BoxLayout.Y_AXIS));
        pnlExistingItems.setMaximumSize(new Dimension(700,250));
        pnlExistingItems.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlExistingItems.setBackground(Color.lightGray);
        pnlExistingItems.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel pnlSearchField = new JPanel();
        pnlSearchField.setLayout(new BoxLayout(pnlSearchField, BoxLayout.LINE_AXIS));
        pnlSearchField.setMaximumSize(new Dimension(700,30));
        pnlSearchField.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlSearchField.setBackground(Color.LIGHT_GRAY);

        /**
         * Search part text field
         */
        HintTextField txtSearch = new HintTextField("Search from existing items");
        txtSearch.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtSearch.setMaximumSize(new Dimension(400,30));
        txtSearch.setFont(new Font("Sans", Font.PLAIN, 14));
        pnlSearchField.add(Box.createRigidArea(new Dimension(150,0)));
        pnlSearchField.add(txtSearch);
        pnlExistingItems.add(Box.createRigidArea(new Dimension(0,5)));
        pnlExistingItems.add(pnlSearchField);
        pnlExistingItems.add(setupExistingItems(ToCTeditorFrame.currTemplate.getTemplatePortions()));

        return pnlExistingItems;
    }

    public JPanel setupButtonsPanel() {
        /**
         * Buttons panel
         */
        JPanel pnlButtons = new JPanel();
        pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
        pnlButtons.setMaximumSize(new Dimension(700,30));
        pnlButtons.setBackground(Color.lightGray);

        /**
         * Back button
         */
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Sans", Font.PLAIN, 15));
        btnBack.setMinimumSize(new Dimension(700,30));
        btnBack.setMaximumSize(new Dimension(700,30));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        // add the listener to the jbutton to handle the "pressed" event
        btnBack.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                ToCTeditor.gui = new ViewThread();
                ToCTeditor.gui.setCallTemplateItems(true);
                //ToCTeditor.gui.start();
                SwingUtilities.invokeLater(ToCTeditor.gui);
            }
        });

        /**
         * Add button
         */
        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Sans", Font.PLAIN, 15));
        btnAdd.setMaximumSize(new Dimension(345,30));


        /**
         * Add buttons to buttons panel
         */
        pnlButtons.add(btnBack);
        pnlButtons.add(Box.createRigidArea(new Dimension(10,0)));
        pnlButtons.add(btnAdd);

        return pnlButtons;
    }

    public JPanel createPartOptions() {
        JPanel pnlItems = new JPanel();
        pnlItems.setLayout(new BoxLayout(pnlItems, BoxLayout.Y_AXIS));
        pnlItems.setMaximumSize(new Dimension(700,120/**170*/));
        pnlItems.setBackground(Color.lightGray);
        pnlItems.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlItems.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JPanel pnlRow1 = new JPanel();
        pnlRow1.setLayout(new BoxLayout(pnlRow1, BoxLayout.LINE_AXIS));
        pnlRow1.setMaximumSize(new Dimension(700,50));
        pnlRow1.setBackground(Color.lightGray);
        pnlRow1.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlRow1.add(Box.createRigidArea(new Dimension(43,0)));
        /**
        pnlRow1.add(createTemplateItem("Concord"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow1.add(createTemplateItem("Copula"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow1.add(createTemplateItem("Locative"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));*/

        pnlRow1.add(createTemplateItem("Phrase"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow1.add(createTemplateItem("PolymorphicWord"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow1.add(createTemplateItem("Punctuation"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow1.add(createTemplateItem("Slot"));
        pnlRow1.add(Box.createRigidArea(new Dimension(5,0)));

        JPanel pnlRow2 = new JPanel();
        pnlRow2.setLayout(new BoxLayout(pnlRow2, BoxLayout.LINE_AXIS));
        pnlRow2.setMaximumSize(new Dimension(700,50));
        pnlRow2.setBackground(Color.lightGray);
        pnlRow2.setAlignmentX(Component.CENTER_ALIGNMENT);

        /**
        pnlRow2.add(Box.createRigidArea(new Dimension(43,0)));
        pnlRow2.add(createTemplateItem("Polymorphic word"));
        pnlRow2.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow2.add(createTemplateItem("Punctuation"));
        pnlRow2.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow2.add(createTemplateItem("Root"));
        pnlRow2.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow2.add(createTemplateItem("Slot"));
        pnlRow2.add(Box.createRigidArea(new Dimension(5,0)));*/

        pnlRow2.add(Box.createRigidArea(new Dimension(43,0)));
        pnlRow2.add(createTemplateItem("UnimorphicWord"));

        /**
        JPanel pnlRow3 = new JPanel();
        pnlRow3.setLayout(new BoxLayout(pnlRow3, BoxLayout.LINE_AXIS));
        pnlRow3.setMaximumSize(new Dimension(700,50));
        pnlRow3.setBackground(Color.lightGray);
        pnlRow3.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlRow3.add(Box.createRigidArea(new Dimension(43,0)));
        pnlRow3.add(createTemplateItem("Unimorphic affix"));
        pnlRow3.add(Box.createRigidArea(new Dimension(5,0)));

        pnlRow3.add(createTemplateItem("Unimorphic word"));
        pnlRow3.add(Box.createRigidArea(new Dimension(5,0)));*/


        pnlItems.add(Box.createRigidArea(new Dimension(0,5)));
        pnlItems.add(pnlRow1);
        pnlItems.add(Box.createRigidArea(new Dimension(0,5)));
        pnlItems.add(pnlRow2);
        //pnlItems.add(Box.createRigidArea(new Dimension(0,5)));
        //pnlItems.add(pnlRow3);
        return pnlItems;
    }

    /**public JComponent setupExistingItems(List<TemplatePart> list) {
        Box box = Box.createVerticalBox();
        box.setBackground(Color.lightGray);
        box.setMaximumSize(new Dimension(400, 200));
        JScrollPane pnlScroll = new JScrollPane(box, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pnlScroll.setMaximumSize(new Dimension(400, 200));
        pnlScroll.setBackground(Color.lightGray);


        for (TemplatePart part : list) {
            JLabel name = new JLabel(part.getPartName());
            JLabel type = new JLabel(ToCTeditor.gui.templateItems.getPartType(part));
            box.add(createItemComponent(name, type));
        }
        JPanel p = new JPanel();
        p.setMaximumSize(new Dimension(700, 200));
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setBackground(Color.lightGray);
        p.add(Box.createRigidArea(new Dimension(150,0)));
        p.add(pnlScroll);
        return p;
    }*/

    public JComponent setupExistingItems(List<TemplatePortion> list) {
        Box box = Box.createVerticalBox();
        box.setBackground(Color.lightGray);
        box.setMaximumSize(new Dimension(400, 250/**200*/));
        JScrollPane pnlScroll = new JScrollPane(box, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pnlScroll.setMaximumSize(new Dimension(400, 250/**200*/));
        pnlScroll.setBackground(Color.lightGray);

        /**DragMouseAdapter dh = new DragMouseAdapter();
         box.addMouseListener(dh);
         box.addMouseMotionListener(dh);*/

        for (TemplatePortion part : list) {
            JLabel name = new JLabel(part.serialisedName);
            JLabel type = new JLabel(part.getClass().getSimpleName());
            box.add(createItemComponent(name, type));
        }
        JPanel p = new JPanel();
        p.setMaximumSize(new Dimension(700, 200));
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setBackground(Color.lightGray);
        p.add(Box.createRigidArea(new Dimension(150,0)));
        p.add(pnlScroll);
        return p;
    }

    private static JComponent createItemComponent(JComponent name, JComponent type) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(Color.BLUE, 2)));

        JPanel pnlName = new JPanel();
        pnlName.setLayout(new BoxLayout(pnlName, BoxLayout.Y_AXIS));
        pnlName.setMaximumSize(new Dimension(400,50));
        pnlName.setBackground(Color.white);
        name.setFont(new Font("Sans", Font.PLAIN, 14));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlName.add(name);

        type.setFont(new Font("Sans", Font.PLAIN, 8));
        type.setMaximumSize(new Dimension(400,50));


        JPanel pnlType = new JPanel();
        pnlType.setLayout(new BoxLayout(pnlType, BoxLayout.LINE_AXIS));
        pnlType.setMaximumSize(new Dimension(400,50));
        pnlType.add(type);
        pnlType.add(Box.createRigidArea(new Dimension(60,0)));
        pnlType.setBackground(Color.white);



        p.setMaximumSize(new Dimension(400, 50));
        p.setMinimumSize(new Dimension(400, 50));
        p.add(pnlName);
        p.add(pnlType);
        p.setBackground(Color.lightGray);
        return p;
    }
}
