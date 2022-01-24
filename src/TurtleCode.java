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

/*
 * @(#) TurtleCode.java   1.0   Dec 01, 2021
 *
 * Sindiso Mkhatshwa (mkhsin035@myuct.ac.za)
 *
 * Nitschke Laboratory, UCT
 *
 * @(#) $Id$
 */
public class TurtleCode {
    public String getPartTurtle(Part part){
        String turtle = "";
        //String type = getPartType(part);
        if (part != null) {
            if (part.getType().equals("Slot")) {
                turtle = "<" + part.getPartName() + "> a toct:Slot\n" +
                        "    ; toct:hasLabel \"" + part.getLabel() + "\"^^xsd:string\n" +
                        "    ; toct:hasNextPart " + part.getNextPart() + " .";
            } else if (part.getType().equals("Unimorphic word")) {
                turtle = "<" + part.getPartName() + "> a toct:UnimorphicWord\n" +
                        "    ; toct:hasValue \"" + part.getValue() + "\"^^xsd:string\n" +
                        "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
            } else if (part.getType().equals("Polymorphic word")) {
                turtle = "<" + part.getPartName() + "> a toct:PolymorphicWord\n" +
                        "    ; toct:relies on <" + part.getReliesOn() + ">\n" +
                        "    ; toct:hasFirstPart <" + part.getFirstPart()  + ">\n" +
                        "    ; toct:hasLastPart <" + part.getLastPart() + ">\n" +
                        "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
            }
            else if ( part.getType().equals("Phrase") ){
                 turtle = "<" + part.getPartName() + "> a toct:Phrase\n" +
                         "    ; toct:hasValue \"" + part.getValue() + "\"^^xsd:string\n" +
                         "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
             }
            else if (part.getType().equals("Punctuation")) {
                turtle = "<" + part.getPartName() + "> a toct:Punctuation\n" +
                        "    ; toct:hasValue \"" + part.getValue()  + "^^xsd:string .";
            }
            else if (part.getType().equals("Concord")){
                turtle = "<" + part.getPartName() + "> a toct:Concord\n" +
                        "    ; cao:hasConcordType <" + part.getType() + ">\n" +
                        "    ; toct:hasLabel \"" + part.getLabel() + "\"^^xsd:string\n" +
                        "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
            }
            else if (part.getType().equals("Copula")){
                turtle = "<" + part.getPartName() + "> a toct:Copula\n" +
                        "    ; toct:hasLabel \"" + part.getLabel() + "\"^^xsd:string\n" +
                        "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
            }
            else if (part.getType().equals("Locative")){
                turtle = "<" + part.getPartName() + "> a toct:Locative\n" +
                        "    ; toct:hasLabel \"" + part.getLabel() + "\"\n" +
                        "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
            }
            else if (part.getType().equals("Unimorphic affix")){
                turtle = "<" + part.getPartName() + "> a toct:UnimorphicAffix\n" +
                        "    ; toct:hasValue \"" + part.getValue() + "\"^^xsd:string\n" +
                        "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
            }
            else if (part.getType().equals("Root")){
                turtle = "<" + part.getPartName() + "> a toct:Root\n" +
                        "    ; toct:hasValue \"" + part.getValue() + "\"^^xsd:string .";
            }

        }
        return turtle;
    }
    public String getPartTurtle(TemplatePortion templatePortion){
        String turtle = "";
        String type = getPartType(templatePortion);
        if (type.equals("Slot")) {
            turtle = "<" + templatePortion.getSerialisedName() + "> a toct:Slot\n" +
                    "    ; toct:hasLabel \"" + ((Slot)templatePortion).getLabel() + "\"^^xsd:string\n";
            if (templatePortion.getNextPart() != null ){
                turtle += "    ; toct:hasNextPart <" + templatePortion.getNextPart().getSerialisedName() + "> .";
            }
            else {
                turtle += "    ; toct:hasNextPart <> .";
            }
        } else if (type.equals("Unimorphic word")) {
            turtle = "<" + templatePortion.getSerialisedName() + "> a toct:UnimorphicWord\n" +
                    "    ; toct:hasValue \"" + ((UnimorphicWord)templatePortion).getValue() + "\"^^xsd:string\n";
            if (templatePortion.getNextPart() != null ){
                turtle += "    ; toct:hasNextPart <" + templatePortion.getNextPart().getSerialisedName() + "> .";
            }
            else {
                turtle += "    ; toct:hasNextPart <> .";
            }
        } else if (type.equals("Polymorphic word")) {
            turtle = "<" + templatePortion.getSerialisedName() + "> a toct:PolymorphicWord\n";
            if ( ((PolymorphicWord)templatePortion).getItemsItReliesOn() != null ) {
                for (int i = 0; i < ((PolymorphicWord) templatePortion).getItemsItReliesOn().size(); i++) {
                    turtle += "    ; toct:relies on <" + ((PolymorphicWord) templatePortion).getItemsItReliesOn().get(i) + ">\n";
                }
            }
            else {
                turtle += "    ; toct:relies on <>\n";
            }
            if ( ((PolymorphicWord)templatePortion).getAllMorphemes().size() > 0 ){
                turtle +=   "    ; toct:hasFirstPart <" + ((PolymorphicWord)templatePortion).getFirstItem().getSerialisedName()  + ">\n";
            }
            else{
                turtle +=   "    ; toct:hasFirstPart <>\n";
            }
            if ( ((PolymorphicWord)templatePortion).getAllMorphemes().size() > 0 ){
                turtle += "    ; toct:hasLastPart <" + ((PolymorphicWord)templatePortion).getLastItem().getSerialisedName() + ">\n";
            }
            else {
                turtle += "    ; toct:hasLastPart <>\n";
            }
            if (((PolymorphicWord)templatePortion).getNextPart() != null ){
                turtle += "    ; toct:hasNextPart <" + ((PolymorphicWord)templatePortion).getNextPart().getSerialisedName() + "> .";
            }
            else {
                turtle += "    ; toct:hasNextPart <> .";
            }


        }
        else if ( type.equals("Phrase") ){
            turtle = "<" + templatePortion.getSerialisedName() + "> a toct:Phrase\n" +
                    "    ; toct:hasValue \"" + ((Phrase)templatePortion).getValue() + "\"^^xsd:string\n";

            if (templatePortion.getNextPart() != null ){
                turtle += "    ; toct:hasNextPart <" + templatePortion.getNextPart().getSerialisedName() + "> .";
            }
            else {
                turtle += "    ; toct:hasNextPart <> .";
            }
        }
        else if (type.equals("Punctuation")) {
            turtle = "<" + templatePortion.getSerialisedName() + "> a toct:Punctuation\n" +
                    "    ; toct:hasValue \"" + ((Punctuation)templatePortion).getValue()  + "^^xsd:string .";
        }
        /**else if (part.getType().equals("Concord")){
            turtle = "<" + part.getPartName() + "> a toct:Concord\n" +
                    "    ; cao:hasConcordType <" + part.getType() + ">\n" +
                    "    ; toct:hasLabel \"" + part.getLabel() + "\"^^xsd:string\n" +
                    "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
        }
        else if (part.getType().equals("Copula")){
            turtle = "<" + part.getPartName() + "> a toct:Copula\n" +
                    "    ; toct:hasLabel \"" + part.getLabel() + "\"^^xsd:string\n" +
                    "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
        }
        else if (part.getType().equals("Locative")){
            turtle = "<" + part.getPartName() + "> a toct:Locative\n" +
                    "    ; toct:hasLabel \"" + part.getLabel() + "\"\n" +
                    "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
        }
        else if (part.getType().equals("Unimorphic affix")){
            turtle = "<" + part.getPartName() + "> a toct:UnimorphicAffix\n" +
                    "    ; toct:hasValue \"" + part.getValue() + "\"^^xsd:string\n" +
                    "    ; toct:hasNextPart <" + part.getNextPart() + "> .";
        }
        else if (part.getType().equals("Root")){
            turtle = "<" + part.getPartName() + "> a toct:Root\n" +
                    "    ; toct:hasValue \"" + part.getValue() + "\"^^xsd:string .";
        }*/
        return turtle;
    }
    public String getPartType(TemplatePortion part){
        String type;
        if (part instanceof Slot){
            type = "Slot";
        }
        else if (part instanceof UnimorphicWord){
            type = "Unimorphic word";
        }
        else if (part instanceof PolymorphicWord){
            type = "Polymorphic word";
        }
        else if (part instanceof Phrase){
            type = "Phrase";
        }
        else if (part instanceof Punctuation){
            type = "Punctuation";
        }
        else{
            type = "Unknown type";
        }
        return type;
    }
    public String getPartId(TemplatePortion part){
        String id;
        if (part instanceof Slot){
            id = ((Slot)part).getSerialisedName();
        }
        else if (part instanceof UnimorphicWord){
            id = ((UnimorphicWord)part).getSerialisedName();
        }
        else if (part instanceof PolymorphicWord){
            id = ((PolymorphicWord)part).getSerialisedName();
        }
        else if (part instanceof Phrase){
            id = ((Phrase)part).getSerialisedName();
        }
        else if (part instanceof Punctuation){
            id = ((Punctuation)part).getSerialisedName();
        }
        else{
            id = "Unknown type";
        }
        return id;
    }
    public String getTemplateTurtle() {
        String turtle = "@base <http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/> .\n" +
                "@prefix toct: <https://people.cs.uct.ac.za/~zmahlaza/ontologies/ToCT#> .\n" +
                "@prefix mola: <https://ontology.londisizwe.org/mola#> .\n" +
                "@prefix co: <http://purl.org/co/> .\n" +
                "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
                "@prefix cao: <http://people.cs.uct.ac.za/~zmahlaza/ontologies/ConcordAnnotationOntology#> .\n\n" +
                "<" + ToCTeditor.dataModel.getTemplate().getSerialisedName() + "> a toct:Template\n" +
                "    ; toct:supportsLanguage <" + ToCTeditor.dataModel.getTemplate().getLanguage().getSerialisedName() + ">\n";
                if (ToCTeditor.dataModel.getTemplateSize() > 0) {
                    turtle +=   "    ; toct:hasFirstPart <" + ToCTeditor.dataModel.getTemplatePortion(0).getSerialisedName() + ">\n" +
                                "    ; toct:hasLastPart <" + ToCTeditor.dataModel.getTemplatePortion
                                        (ToCTeditor.dataModel.getTemplatePortions().size() - 1)
                                            .getSerialisedName() + ">";
                }
                else{
                    turtle +=   "    ; toct:hasFirstPart <null>\n" +
                                "    ; toct:hasLastPart <null>";
                }

        if (ToCTeditor.dataModel.getTemplateSize() > 2) {
            turtle += "\n    ; toct:hasPart";

            for (int i = 1; i < ToCTeditor.dataModel.getTemplateSize() - 1; i++) {
                turtle += " <" + ToCTeditor.dataModel.getTemplatePortion(i).getSerialisedName() + ">";
                if (i < ToCTeditor.dataModel.getTemplateSize() - 2) {
                    turtle += ",";
                }
            }
            turtle += " .\n\n";
        }
        else{
            turtle += " .\n\n";
        }
        for (TemplatePortion part: ToCTeditor.dataModel.getTemplatePortions() ){
            turtle += getPartTurtle(part) + "\n\n";
        }
        return turtle;
    }
    public String getMorphemeId(InternalSlotRootAffix affix) {
        String id;
        if (affix  instanceof AffixChunk) {
            id = ((AffixChunk)affix).getSerialisedName();
        }
        else if (affix instanceof Concord){
            id = ((Concord)affix).getSerialisedName();
        }
        else if (affix instanceof Copula){
            id = ((Copula)affix).getSerialisedName();
        }
        else if (affix instanceof Locative){
            id = ((Locative)affix).getSerialisedName();
        }
        else if (affix instanceof  Slot) {
            id = ((Slot)affix).getSerialisedName();
        }
        else if (affix instanceof UnimorphicAffix){
            id = ((UnimorphicAffix)affix).getSerialisedName();
        }
        else if (affix instanceof Root){
            id = ((Root)affix).getSerialisedName();
        }
        else{
            id = "Unknown type";
        }
        return id;
    }

    public String getMorphemeType(InternalSlotRootAffix affix) {
        String type;
        if (affix  instanceof AffixChunk) {
            type = "Affix chunk";
        }
        else if (affix instanceof Concord){
            type = "Concord";
        }
        else if (affix instanceof Copula){
            type = "Copula";
        }
        else if (affix instanceof Locative){
            type = "Locative";
        }
        else if (affix instanceof  Slot) {
            type = "Slot";
        }
        else if (affix instanceof UnimorphicAffix){
            type = "Unimorphic affix";
        }
        else if (affix instanceof Root){
            type = "Root";
        }
        else{
            type = "Unknown type";
        }
        return type;
    }
}
