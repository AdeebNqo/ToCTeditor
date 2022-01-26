import models.Part;
import za.co.mahlaza.research.grammarengine.base.models.template.TemplatePortion;
import javax.swing.*;
import java.util.List;
/*
 * @(#) ViewThread.java   1.0   Nov 18, 2021
 *
 * Sindiso Mkhatshwa (mkhsin035@myuct.ac.za)
 * Zola Mahlaza
 *
 * @(#) $Id$
 */
public class ViewThread extends Thread {

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

    public ViewThread(CreateTemplate homeScreen, TemplateItems templateItems, CreateItem createItem) {
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
        List<TemplatePortion> portions = ToCTeditorFrame.currTemplate.getTemplatePortions();

        if (portions.size() != 0) {
            currentTemplatePortion = portions.get(index);
            for (int i=0; i<portions.size()-1; i++) {
                TemplatePortion nextTemplatePortion = portions.get(i+1);
                currentTemplatePortion.setNextPart(nextTemplatePortion);
            }
        }

        if (callCreateTemplate) {
            ToCTeditor.homeScreen.setupGUI();
            callCreateTemplate = false;
        }
        else if (callTemplateItems) {
            JPanel partEditorPanel = ToCTeditor.templateItems.getPartPanelEditor(currentTemplatePortion);
            JPanel ttlPreviewPanel = ToCTeditor.templateItems.getPreviewPanel();
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
            JPanel ttlPreviewPanel = ToCTeditor.templateItems.getPreviewPanel();
            ToCTeditor.templateItems.updateEditorTurtlePanel(partEditorPanel, ttlPreviewPanel);
            ToCTeditor.prevToggleBtnState = ToCTeditor.toggleBtnState;
        }

        System.out.println("ViewThread. Current template = "+ToCTeditorFrame.currTemplate.getSerialisedName()+" Size = "+ToCTeditorFrame.currTemplate.getTemplatePortions().size());
    }
}
