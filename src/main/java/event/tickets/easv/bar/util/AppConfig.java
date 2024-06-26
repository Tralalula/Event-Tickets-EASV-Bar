package event.tickets.easv.bar.util;

public class AppConfig {
    public static final String CONFIG_FILE = "config/config.properties";

    public static final String DB_SERVER = "db.server";
    public static final String DB_DATABASE = "db.database";
    public static final String DB_PORT = "db.port";
    public static final String DB_USERNAME = "db.username";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_USE_INTEGRATED_SECURITY = "db.use_integrated_security";

    public static final String RESEND_API = "resend.api";

    public static final String EVENT_IMAGES_DIR = "/data/event_images/";
    public static final String EVENT_IMAGES_DIR2 = EVENT_IMAGES_DIR.substring(1, EVENT_IMAGES_DIR.length() - 1);
    public static final String EVENT_TEMP_IMAGE_DIR = EVENT_IMAGES_DIR2 + "/temp";
    public static final String PROFILE_IMAGES_DIR = "/data/profile_images/";
    public static final String PROFILE_IMAGES_DIR2 = PROFILE_IMAGES_DIR.substring(1, PROFILE_IMAGES_DIR.length() - 1);
    public static final String PROFILE_TEMP_IMAGE_DIR = PROFILE_IMAGES_DIR2 + "/temp";
}
