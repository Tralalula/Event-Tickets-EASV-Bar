package event.tickets.easv.bar.be.enums;

public enum Theme {
    LIGHT("Light"),
    DARK("Dark");

    private final String dbValue;

    Theme(String dbValue) {
        this.dbValue = dbValue;
    }

    public String toDbValue() {
        return dbValue;
    }

    public static Theme fromDbValue(String dbValue) {
        for (Theme theme : values()) {
            if (theme.dbValue.equalsIgnoreCase(dbValue)) {
                return theme;
            }
        }
        throw new IllegalArgumentException("No Theme with dbValue " + dbValue + " found");
    }
}