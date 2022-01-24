import za.co.mahlaza.research.grammarengine.base.models.mola.Languoid;
import za.co.mahlaza.research.grammarengine.base.models.template.Template;
import za.co.mahlaza.research.grammarengine.base.models.template.TemplatePortion;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluFeatureParser;
import za.co.mahlaza.research.templateparsing.TemplateReader;
import za.co.mahlaza.research.templateparsing.URIS;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CreateTemplate {
    private int frameX;
    private int frameY;
    private ToCTeditorFrame frame;

    //private ControlThread controlThread;
    CreateTemplate(ToCTeditorFrame frame/**, ControlThread controlThread*/){
        this.frameX = frame.getFrameX();
        this.frameY = frame.getFrameX();
        this.frame = frame;

        //this.controlThread = controlThread;
    }

    public void setupGUI() {
        /**
         * Create main panel that houses all other frame components
         */

        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.PAGE_AXIS));
        pnlContent.setSize(frame.getFrameX(),frame.getFrameY());
        pnlContent.add(Box.createRigidArea(new Dimension(0,100)));

        /**
         * Create panel to house title label, and add the said label to
         * the panel
         */
        JPanel pnlCreateTemplateTitle = new JPanel();
        pnlCreateTemplateTitle.setLayout(new BoxLayout(pnlCreateTemplateTitle, BoxLayout.LINE_AXIS));
        pnlCreateTemplateTitle.setMaximumSize(new Dimension(600,15));
        pnlCreateTemplateTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCreateTemplateTitle = new JLabel("Create New Template");
        lblCreateTemplateTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        //Add title to panel
        pnlCreateTemplateTitle.add(lblCreateTemplateTitle);
        // Add title panel to main panel
        pnlContent.add(pnlCreateTemplateTitle);
        pnlContent.add(Box.createRigidArea(new Dimension(0,5)));

        /**
         * Create panel to house the template name text field
         * as well as language drop-down box
         */
        JPanel pnlCreateTemplate = new JPanel();
        pnlCreateTemplate.setLayout(new BoxLayout(pnlCreateTemplate, BoxLayout.PAGE_AXIS));
        pnlCreateTemplate.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlCreateTemplate.setBackground(Color.LIGHT_GRAY);
        pnlCreateTemplate.setMaximumSize(new Dimension(600, 130));

        /**
         * Template name text field
         */
        HintTextField txtEntryTemplateName = new HintTextField("Template name");
        txtEntryTemplateName.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtEntryTemplateName.setMaximumSize(new Dimension(400,30));
        txtEntryTemplateName.setFont(new Font("Sans", Font.PLAIN, 14));
        //TemplateItems.addChangeListener(txtEntryTemplateName, e -> updateTemplateName(txtEntryTemplateName.getText()));
        /**txtEntryTemplateName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Inside action performed for textfield");
                String text = txtEntryTemplateName.getText().trim();
                //ToCTeditor.controller.setPartName(text);
                System.out.println("Current text: " + text);
            }
        });*/

        pnlCreateTemplate.add(Box.createRigidArea(new Dimension(0,10)));
        pnlCreateTemplate.add(txtEntryTemplateName);
        pnlCreateTemplate.add(Box.createRigidArea(new Dimension(0,10)));

        /**
         * Supported language dropdown button
         */
        String[] languages = { "Supported language", "Zulu", "Xhosa", "Swati", "Ndebele" }; //TODO: load from MoLa KB
        JComboBox supportedLanguage = new JComboBox(languages);
        supportedLanguage.setSelectedIndex(0);
        supportedLanguage.setAlignmentX(Component.CENTER_ALIGNMENT);
        supportedLanguage.setMaximumSize(new Dimension(400,30));
        supportedLanguage.setFont(new Font("Sans", Font.PLAIN, 14));

        pnlCreateTemplate.add(supportedLanguage);
        pnlCreateTemplate.add(Box.createRigidArea(new Dimension(0,10)));

        /**
         * Create button
         */
        JButton btnCreate = new JButton("Create Template");
        btnCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreate.setFont(new Font("Sans", Font.PLAIN, 15));
        btnCreate.setMaximumSize(new Dimension(400,30));


        /**
         * Add the listener to the JButton to handle the "pressed" event
         */
        btnCreate.addActionListener(e -> {
            Languoid languoid = new Languoid(); //TODO: load this from the MoLa KB
            languoid.setSerialisedName(supportedLanguage.getSelectedItem().toString());

            List<TemplatePortion> portions = new LinkedList<>();
            Template currTemplate = new Template(portions);
            currTemplate.setLanguage(languoid);
            currTemplate.setSerialisedName(txtEntryTemplateName.getText());
            String URI = "http://www.tocteditor.org/template/untitled-templates/"; //TODO: ask user for URI
            currTemplate.setURI(URI);
            frame.currTemplate = currTemplate;

            if (frame.currTemplate.getSerialisedName() == null ) {
                JOptionPane.showMessageDialog(
                                            frame,
                                    "Template name cannot be empty.",
                                        "Template Name Error",
                                        JOptionPane.ERROR_MESSAGE
                                        );
            }
            else if (supportedLanguage.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(
                                            frame,
                                    "Supported language cannot be empty.",
                                        "Supported Language Error",
                                            JOptionPane.ERROR_MESSAGE
                                        );
            }
            else {
                ToCTeditor.gui = new ViewThread();
                ToCTeditor.gui.setCallTemplateItems(true); //TODO: figure out what this is for5
                ToCTeditor.gui.start();
            }
        });

        pnlCreateTemplate.add(btnCreate);
        pnlCreateTemplate.add(Box.createRigidArea(new Dimension(0,10)));
        pnlContent.add(pnlCreateTemplate);


        /**
         * Create panel to house title label, and add the said label to
         * the panel
         */
        JPanel pnlOpenTemplateTitle = new JPanel();
        pnlOpenTemplateTitle.setLayout(new BoxLayout(pnlOpenTemplateTitle, BoxLayout.LINE_AXIS));
        pnlOpenTemplateTitle.setMaximumSize(new Dimension(600,15));
        pnlOpenTemplateTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblOpenTemplateTitle = new JLabel("Open Existing Template");
        lblOpenTemplateTitle.setFont(new Font("Sans", Font.PLAIN, 15));
        //Add title to panel
        pnlOpenTemplateTitle.add(lblOpenTemplateTitle);


        // Add title panel to main panel
        pnlContent.add(Box.createRigidArea(new Dimension(0,10)));
        pnlContent.add(pnlOpenTemplateTitle);
        pnlContent.add(Box.createRigidArea(new Dimension(0,5)));

        /**
         * Create panel to house the open template button
         */
        JPanel pnlOpenTemplate = new JPanel();
        pnlOpenTemplate.setLayout(new BoxLayout(pnlOpenTemplate, BoxLayout.PAGE_AXIS));
        pnlOpenTemplate.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        pnlOpenTemplate.setBackground(Color.LIGHT_GRAY);
        pnlOpenTemplate.setMaximumSize(new Dimension(600, 130));

        /**
         * Open button
         */
        JButton btnOpen = new JButton("Open Template");
        btnOpen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOpen.setFont(new Font("Sans", Font.PLAIN, 15));
        btnOpen.setMaximumSize(new Dimension(400,30));


        /**
         * Add the listener to the JButton to handle the "pressed" event
         */
        btnOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String templatePath;
                String templateName;
                String templateURI;

                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "TTL Templates", "ttl", "*");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    templatePath = chooser.getSelectedFile().getAbsolutePath().trim();

                    Collection<String> templateURIs = TemplateReader.getTemplateURIs(templatePath);
                    //TODO: Ask user to choose the URI
                    for (String uri: templateURIs) {
                        try {
                            Collection<Template> templates = TemplateReader.parseTemplates(uri, templatePath);
                            //TODO: Ask user to choose the template
                            Template chosenTemplate = templates.stream().iterator().next();
                            frame.currTemplate = chosenTemplate;
                            break;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    ToCTeditor.gui = new ViewThread();
                    ToCTeditor.gui.setCallTemplateItems(true);
                    ToCTeditor.gui.start();

//                    TemplateReader.Init(new ZuluFeatureParser());
//                    TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
//                    TemplateReader.IS_DEBUG_ENABLED = true;
//                    Template template;
//                    try {
//
//                        Collection<String> templateURIs = TemplateReader.getTemplateURIs(templatePath);
//                        if (templateURIs.size() == 1) {
//                            templateURI = templateURIs.iterator().next();
//
//                            Collection<Template> templates = TemplateReader.parseTemplates(templateURI, templatePath);
//                            if (templates.size() == 1) {
//                                template = templates.iterator().next();
//                                ToCTeditor.dataModel.setTemplate(template);
//
//                                ToCTeditor.gui = new ViewThread();
//                                ToCTeditor.gui.setCallTemplateItems(true);
//                                ToCTeditor.gui.start();
//                                System.out.println(template.toString());
//                            }
//                            else if (templates.size() == 0) {
//                                throw new UnsupportedOperationException("No template found in file.");
//
//                            }
//                            else {
//                                throw new UnsupportedOperationException("Support for opening a file with multiple templates not added yet. Please seperate the templates into seperate files.");
//
//                            }
//
//                        }
//                        else {
//                            throw new UnsupportedOperationException("Support for opening a file with multiple templates not added yet. Please seperate the templates into seperate files.");
//                        }
//                    }
//                    catch(Exception error) {
//                        System.out.println(error.getMessage());
//                    }

                }
                /**String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";
                String templatePath = "/home/root07/Documents/Academics/Templates/template1.1.ttl";
                String templateName = "templ1.1";
                System.out.println("Name:" + templateName + " Path: " + templatePath);

                TemplateReader.Init(new ZuluFeatureParser());
                TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
                TemplateReader.IS_DEBUG_ENABLED = true;
                Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
                System.out.println("Template parsed.");*/
            }
        });

        pnlOpenTemplate.add(Box.createRigidArea(new Dimension(0,50)));
        pnlOpenTemplate.add(btnOpen);
        pnlOpenTemplate.add(Box.createRigidArea(new Dimension(0,10)));
        pnlContent.add(pnlOpenTemplate);


        frame.setContentPane(pnlContent);
    }

    @Deprecated
    private void updateTemplateName(String text) {
        frame.currTemplate.setSerialisedName(text);
    }
}
