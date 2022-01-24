import models.Part;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.template.*;
import za.co.mahlaza.research.grammarengine.base.models.template.Concord;
import za.co.mahlaza.research.grammarengine.base.models.template.Copula;
import za.co.mahlaza.research.grammarengine.base.models.template.Locative;
import za.co.mahlaza.research.grammarengine.base.models.template.Phrase;
import za.co.mahlaza.research.grammarengine.base.models.template.PolymorphicWord;
import za.co.mahlaza.research.grammarengine.base.models.template.Punctuation;
import za.co.mahlaza.research.grammarengine.base.models.template.Root;
import za.co.mahlaza.research.grammarengine.base.models.template.Slot;
import za.co.mahlaza.research.grammarengine.base.models.template.UnimorphicAffix;
import za.co.mahlaza.research.grammarengine.base.models.template.UnimorphicWord;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private List<TemplatePortion> templatePortions;
    private Template template;

    public static List<Part> parts;

    public DataModel(){

        templatePortions = new ArrayList<>();
        template = new Template(templatePortions);
        parts = new ArrayList<>();
    }

    public void setTemplate(Template template){
        this.template = template;
        this.templatePortions = template.getTemplatePortions();
        for (TemplatePortion part: templatePortions){
            addPart(part);
        }
    }

    public void addTemplatePortion(TemplatePortion templatePortion){
        templatePortions.add(templatePortion);
    }

    public void addTemplatePortion( int index, TemplatePortion templatePortion ){
        templatePortions.add(index, templatePortion);
    }

    public void removeTemplatePortion( int index ){
        templatePortions.remove(index);
    }

    public void removeTemplatePortion( String serialisedName ){
        for (int i = 0; i < templatePortions.size(); i ++ ){
            if (templatePortions.get(i).getSerialisedName().equals(serialisedName)){
                templatePortions.remove(i);
            }
        }
    }

    public void duplicateTemplatePortion( String serialisedName ){
        for (int i = 0; i < templatePortions.size(); i ++ ){
            if (templatePortions.get(i).getSerialisedName().equals(serialisedName)){
                TemplatePortion copy = new TemplatePortion();
                copy = templatePortions.get(i);
                templatePortions.add(copy);
                templatePortions.get(getTemplatePortions().size()-1).setSerialisedName(templatePortions.get(i).getSerialisedName() + "-(copy)");
            }
        }
    }

    public void updateNextPart(){
        for ( int i = 0; i < templatePortions.size(); i++ ){
            if (i < templatePortions.size() - 1){
                templatePortions.get(i).setNextPart(templatePortions.get(i+1));
            }
        }
        templatePortions.get(templatePortions.size() - 1).setNextPart(null);
    }

    public TemplatePortion getTemplatePortion(int index){
        if (index >= 0 && index < this.templatePortions.size()){
            return templatePortions.get(index);
        }
        else{
            return null;
        }
    }

    public int getTemplateSize(){
        return this.templatePortions.size();
    }

    public Template getTemplate(){
        return this.template;
    }

    public List<TemplatePortion> getTemplatePortions(){
        return templatePortions;
    }

    public void addPart(TemplatePortion part){
        String type = part.getClass().getSimpleName();
        if (type.equals("Slot")){
            int i = parts.size();
            parts.add(new Part(((Slot)part).getIdentification()));
            parts.get(i).setLabel( ((Slot)part).getLabel());
            parts.get(i).setValue( ((Slot)part).getValue() );
            parts.get(i).setNextPart( "placeholder" );
            parts.get(i).setMorpheme(false);
            parts.get(i).setType( "Slot" );
        }
        if (type.equals("Punctuation")){
            int i = parts.size();
            parts.add(new Part("placeholder"));
            parts.get(i).setValue( ((Punctuation)part).toString() );
            parts.get(i).setMorpheme ( false );
            parts.get(i).setType( "Punctuation" );
        }
        else if (type.equals("Phrase")){
            int i = parts.size();
            parts.add(new Part("Placeholder"));
            parts.get(i).setValue(((Phrase)part).getValue());
            parts.get(i).setNextPart("plceholder");
            parts.get(i).setMorpheme(false);
            parts.get(i).setType("Phrase");
        }

        else if (type.equals("Polymorphic word")){
            int i = parts.size();
            parts.add(new Part(((PolymorphicWord)part).getIdentification()));
            parts.get(i).setReliesOn( "placeholder" );
            parts.get(i).setFirstPart( "placeholder" );
            parts.get(i).setLastPart( "placeholder" );
            parts.get(i).setNextPart( "placeholder" );
            parts.get(i).setMorpheme( false );
            parts.get(i).setType( "Polymorphic word" );


            List<InternalSlotRootAffix> morphemes = ((PolymorphicWord)part).getAllMorphemes();
            if (morphemes.size() > 0){
                parts.get(i).setParent(true);
                parts.get(i).setMorphemes(morphemes.size());
            }
            //System.out.println("Polymophic type: " + ((PolymorphicWord)part).getIdentification() + "Affix boxes: " + morphemes.size());
            for (InternalSlotRootAffix affix : morphemes) {
                String affixType = affix.getType();
                if (affixType.equals("Concord")){
                    int j = parts.size();
                    parts.add(new Part(((Concord)affix).getIdentification()));
                    parts.get(j).setLabel( ((Concord)affix).getLabel() );
                    parts.get(j).setConcordType( ((Concord)affix).getConcordType().getTypeString() );
                    parts.get(j).setMorpheme( true );
                    parts.get(j).setParentId( parts.get(i).getPartName() );
                    parts.get(j).setType( "Concord" );
                }
                else if (affixType.equals("Copula")){
                    int j = parts.size();
                    parts.add(new Part(((Copula)affix).getIdentification()));
                    parts.get(j).setLabel( "placeholder" );
                    parts.get(j).setNextPart( "placeholder" );
                    parts.get(j).setMorpheme( true );
                    parts.get(j).setParentId( parts.get(i).getPartName() );
                    parts.get(j).setType( "Copula" );
                }
                else if (affixType.equals("Slot")){
                    int j = parts.size();
                    parts.add(new Part(((Slot)affix).getIdentification()));
                    parts.get(j).setLabel( ((Slot)affix).getLabel() );
                    parts.get(j).setValue( ((Slot)affix).getValue() );
                    parts.get(j).setNextPart( "placeholder" );
                    parts.get(j).setType("Slot");
                    parts.get(j).setMorpheme(true);
                    parts.get(j).setParentId( parts.get(i).getPartName() );
                }
                else if (affixType.equals("Unimorphic affix")){
                    int j = parts.size();
                    parts.add(new Part("placeholder"));
                    parts.get(j).setValue(((UnimorphicAffix)affix).getValue());
                    parts.get(j).setMorpheme( true );
                    parts.get(j).setParentId( parts.get(i).getPartName() );
                    parts.get(j).setType( "Unimorphic affix" );
                }
                else if (affixType.equals("Locative")){
                    int j = parts.size();
                    parts.add(new Part(((Locative)affix).getIdentification()));
                    parts.get(j).setValue(((Locative)affix).getValue());
                    parts.get(j).setMorpheme( true );
                    parts.get(j).setParentId( parts.get(i).getPartName() );
                    parts.get(j).setType( "Locative" );
                }
                else if (affixType.equals("Root")){
                    int j = parts.size();
                    parts.add(new Part("Placeholder"));
                    parts.get(j).setValue(((Root)affix).getValue());
                    parts.get(j).setMorpheme( true );
                    parts.get(j).setParentId( parts.get(i).getPartName() );
                    parts.get(j).setType( "Root" );
                }
            }
        }
        else if (type.equals("Unimorphic word")){
            int i = parts.size();
            parts.add(new Part(((UnimorphicWord)part).getIdentification()));
            parts.get(i).setValue( ((UnimorphicWord)part).getValue() );
            parts.get(i).setNextPart ("placeholder" );
            parts.get(i).setMorpheme ( false );
            parts.get(i).setType ( "Unimorphic word" );
        }
        else if (type.equals("Space")){
            int i = parts.size();
            parts.add(new Part("placeholder"));
            parts.get(i).setValue( ((UnimorphicWord)part).getValue() );

            parts.get(i).setMorpheme ( false );
            parts.get(i).setType ( "Space" );
        }

    }

    public Part getPart(int index){
        if (index >= 0 && index < this.parts.size()){
            return parts.get(index);
        }
        else{
            return null;
        }
    }

    public int getSize(){
        return this.parts.size();
    }

    public List<Part> getData(){
        return parts /**Collections.unmodifiableList(templateItems)*/;
    }


    public TemplatePortion findTemplatePortion(String updatedText) {

        for (TemplatePortion portion : templatePortions ){
            if (portion.getSerialisedName().equals(updatedText)){
                return portion;
            }
        }
        return null;
    }
    public InternalSlotRootAffix findWordPortion(String updatedText) {

        for (TemplatePortion portion : templatePortions ){
            String type = portion.getClass().getSimpleName();
            if ( type.equals("Polymorphic word")){
                if (portion.equals(ToCTeditor.gui.getCurrentTemplatePortion())){
                    for (InternalSlotRootAffix word : ((PolymorphicWord)portion).getAllMorphemes() ){
                        if (word.getSerialisedName().equals(updatedText)){
                            return word;
                        }
                    }
                }
            }
        }
        return null;
    }
}
