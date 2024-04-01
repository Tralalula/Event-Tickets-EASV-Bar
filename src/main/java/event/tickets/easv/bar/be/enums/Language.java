package event.tickets.easv.bar.be.enums;

public enum Language {
    EN_GB("en-GB"),
    DA_DK("da-DK");

    private final String dbValue;

    Language(String dbValue) {
        this.dbValue = dbValue;
    }

    public String toDbValue() {
        return dbValue;
    }

    public static Language fromDbValue(String dbValue) {
        for (Language language : values()) {
            if (language.dbValue.equalsIgnoreCase(dbValue)) {
                return language;
            }
        }
        throw new IllegalArgumentException("No Language with dbValue " + dbValue + " found");
    }
}