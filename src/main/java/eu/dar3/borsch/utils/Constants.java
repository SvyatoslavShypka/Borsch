package eu.dar3.borsch.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    private Constants() {
    }

    public static final int FAMILY_CODE_LENGTH = 10;

    public static final String REDIRECT_URL_404 = "redirect:error/404";
    public static final String URL_ACCOUNT = "/account";

    public static final Map<Integer, String> GENDERS = new HashMap<>();
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_OTHER = 3;

    static {
        GENDERS.put(GENDER_UNKNOWN, "Не відома");
        GENDERS.put(GENDER_MALE, "Чоловіча");
        GENDERS.put(GENDER_FEMALE, "Жіноча");
        GENDERS.put(GENDER_OTHER, "Інше");
    }
}
