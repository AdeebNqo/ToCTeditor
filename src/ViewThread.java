import models.Part;
import za.co.mahlaza.research.grammarengine.base.models.template.TemplatePortion;

import javax.swing.*;

/*
 * @(#) ViewThread.java   1.0   Nov 18, 2021
 *
 * Sindiso Mkhatshwa (mkhsin035@myuct.ac.za)
 *
 * @(#) $Id$
 */
public class ViewThread extends Thread {
    //private CreateTemplate homeScreen;
    //TemplateItems templateItems;
    //private CreateItem createItem;
    //private DataModel dataModel;

    boolean callCreateTemplate;
    boolean callTemplateItems;
    boolean callCreateItem;
    boolean callCreateMorpheme;

    private Part currentPart;
    private TemplatePortion currentTemplatePortion;

    private int index;

    public ViewThread() {
        this.callCreateTemplate = false;
        this.callTemplateItems = false;
        this.callCreateItem = false;
        this.callCreateMorpheme = false;
    }

    public ViewThread(CreateTemplate homeScreen, TemplateItems templateItems, CreateItem createItem, DataModel datamodel) {
        this.callCreateTemplate = false;
        this.callTemplateItems = false;
        this.callCreateItem = false;
        this.callCreateMorpheme = false;
    }

    public int getToggleBtnState() {
        return ToCTeditor.toggleBtnState;
    }

    public void setToggleBtnState(int toggleBtnState) {
        ToCTeditor.toggleBtnState = ToCTeditor.toggleBtnState;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Part getCurrentPart() {
        currentPart = ToCTeditor.dataModel.getPart(index);
        return currentPart;
    }


    public TemplatePortion getCurrentTemplatePortion(){
        currentTemplatePortion = ToCTeditor.dataModel.getTemplatePortion(index);
        return currentTemplatePortion;
    }


    public void setCallCreateTemplate(boolean callCreateTemplate) {
        this.callCreateTemplate = callCreateTemplate;
    }

    public void setCallTemplateItems(boolean callTemplateItems) {
        this.callTemplateItems = callTemplateItems;
    }

    public void setCallCreateItem(boolean callCreateItem) {
        this.callCreateItem = callCreateItem;
    }
    public void setCallCreateMorpheme(boolean callCreateMorpheme) {
        this.callCreateMorpheme = callCreateMorpheme;
    }

    @Override
    public void run() {
        currentTemplatePortion = ToCTeditor.dataModel.getTemplatePortion(index);
        if (callCreateTemplate) {
            ToCTeditor.homeScreen.setupGUI();
            callCreateTemplate = false;
        }
        else if (callTemplateItems) {
            JPanel partEditorPanel = ToCTeditor.templateItems.getPartPanelEditor(currentTemplatePortion);
            JPanel ttlPreviewPanel = ToCTeditor.templateItems.getPartPanelTurtle();
            ToCTeditor.templateItems.setupGUI(partEditorPanel, ttlPreviewPanel);
            callTemplateItems = false;
        }
        else if (callCreateItem) {
            ToCTeditor.createItem.setupGUI();
            callCreateItem = false;
        }
        else if (callCreateMorpheme) {
            ToCTeditor.createMorpheme.setupGUI(currentTemplatePortion);
            callCreateItem = false;
        }
        else if (ToCTeditor.toggleBtnState != ToCTeditor.prevToggleBtnState) {
            JPanel partEditorPanel = ToCTeditor.templateItems.getPartPanelEditor(currentTemplatePortion);
            JPanel ttlPreviewPanel = ToCTeditor.templateItems.getPartPanelTurtle();
            ToCTeditor.templateItems.updateEditorTurtlePanel(partEditorPanel, ttlPreviewPanel);
            ToCTeditor.prevToggleBtnState = ToCTeditor.toggleBtnState;
        }
    }
}
