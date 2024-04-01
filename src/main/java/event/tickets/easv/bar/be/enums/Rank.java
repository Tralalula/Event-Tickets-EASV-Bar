package event.tickets.easv.bar.be.enums;

public enum Rank {
    ADMIN("Admin"),
    EVENT_COORDINATOR("Event coordinator");

    private final String dbValue;

    Rank(String dbValue) {
        this.dbValue = dbValue;
    }

    public String toDbValue() {
        return dbValue;
    }

    public static Rank fromDbValue(String dbValue) {
        for (Rank rank : values()) {
            if (rank.dbValue.equalsIgnoreCase(dbValue)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("No Rank with dbValue " + dbValue + " found");
    }
}