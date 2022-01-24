package models;

public class Part{

    /**
     * Template part fields that can be modified/set by user
     */
    private String partName;
    private boolean isPartNameChanged;
    private String label;
    private boolean isLabelChanged;
    private String type;
    private boolean isTypeChanged;
    private String value;
    private boolean isValueChanged;
    private String reliesOn;
    private boolean isRelianceChanged;
    private String firstPart;
    private boolean isFirstPartChanged;
    private String lastPart;
    private boolean isLastPartChanged;
    private String nextPart;
    private boolean isNextPartChanged;
    private String concordType;
    private boolean isParent;
    private boolean isMorpheme;
    private String parentId;
    private int morphemes;

    public int getMorphemes() {
        return morphemes;
    }

    public void setMorphemes(int morphemes) {
        this.morphemes = morphemes;
    }

    public Part(String partName){
        this.partName = partName;
        this.isPartNameChanged = false;
        this.isLabelChanged = false;
        this.isTypeChanged = false;
        this.isValueChanged = false;
        this.isRelianceChanged = false;
        this.isFirstPartChanged = false;
        this.isLastPartChanged = false;
        this.isNextPartChanged = false;
        this.isParent = false;
        this.morphemes = 0;
    }
    public void setPartName(String partName) {
        this.partName = partName;
        this.isPartNameChanged = true;
    }

    public void setLabel(String label) {
        this.label = label;
        this.isLabelChanged = true;
    }

    public void setType(String type) {
        this.type = type;
        this.isTypeChanged = true;
    }

    public void setValue(String value) {
        this.value = value;
        this.isValueChanged = true;
    }

    public void setReliesOn(String reliesOn) {
        this.reliesOn = reliesOn;
        this.isRelianceChanged = true;
    }

    public void setFirstPart(String firstPart) {
        this.firstPart = firstPart;
        this.isFirstPartChanged = true;
    }

    public void setPartNameChanged(boolean partNameChanged) {
        isPartNameChanged = partNameChanged;
    }

    public void setLabelChanged(boolean labelChanged) {
        isLabelChanged = labelChanged;
    }

    public void setTypeChanged(boolean typeChanged) {
        isTypeChanged = typeChanged;
    }

    public void setValueChanged(boolean valueChanged) {
        isValueChanged = valueChanged;
    }

    public void setRelianceChanged(boolean relianceChanged) {
        isRelianceChanged = relianceChanged;
    }

    public void setFirstPartChanged(boolean firstPartChanged) {
        isFirstPartChanged = firstPartChanged;
    }

    public void setLastPartChanged(boolean lastPartChanged) {
        isLastPartChanged = lastPartChanged;
    }

    public void setNextPartChanged(boolean nextPartChanged) {
        isNextPartChanged = nextPartChanged;
    }

    public void setConcordType(String concordType) {
        this.concordType = concordType;
    }

    public void setMorpheme(boolean morpheme) {
        isMorpheme = morpheme;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public void setLastPart(String lastPart) {
        this.lastPart = lastPart;
        this.isLastPartChanged = true;
    }

    public void setNextPart(String nextPart) {
        this.nextPart = nextPart;
        this.isNextPartChanged = true;
    }

    public String getPartName() {
        return partName;
    }

    public boolean isPartNameChanged() {
        return isPartNameChanged;
    }

    public String getLabel() {
        return label;
    }

    public boolean isLabelChanged() {
        return isLabelChanged;
    }

    public String getType() {
        return type;
    }

    public boolean isTypeChanged() {
        return isTypeChanged;
    }

    public String getValue() {
        return value;
    }

    public boolean isValueChanged() {
        return isValueChanged;
    }

    public String getReliesOn() {
        return reliesOn;
    }

    public boolean isRelianceChanged() {
        return isRelianceChanged;
    }

    public String getFirstPart() {
        return firstPart;
    }

    public boolean isFirstPartChanged() {
        return isFirstPartChanged;
    }

    public String getLastPart() {
        return lastPart;
    }

    public boolean isLastPartChanged() {
        return isLastPartChanged;
    }

    public String getNextPart() {
        return nextPart;
    }

    public boolean isNextPartChanged() {
        return isNextPartChanged;
    }
}
