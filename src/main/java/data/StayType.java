package data;

public enum StayType {
    OVERNIGHT("Overnight Stays"),
    DAY_USE("Day Use Stays");

    private final String text;
    StayType(String text) {
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
}
