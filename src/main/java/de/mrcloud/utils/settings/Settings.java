package de.mrcloud.utils.settings;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mrcloud.utils.NewJoinedMember;
import de.mrcloud.utils.sql.SqlUtils;
import net.dv8tion.jda.api.entities.Member;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Settings {
    public static final String VERSION = "1.4.3-alpha";
    public static final String TOKEN = "NzA5Mjg4ODc3OTU3MDU0NDY1.Xrjuvg._3jN_sBLsc2FPzPcW0QBR7Yyq10";
    public static final String TESTING_TOKEN = "Njg4NDExMjQzNDMzNjg5MjM5.Xmz67g.Dn8P-NU0P8ltuucNLRuNQ5Pcvhg";
    public static final String CLOUD_ID_STRING = "364475349700050944";
    public static final long CLOUD_ID_LONG = 364475349700050944L;
    public static final String DB_CONNECT_URL_RASP = "jdbc:mariadb://localhost:3306/CloudCity";
    public static final String DB_CONNECT_URL_PC = "jdbc:mariadb://192.168.178.47:3306/CloudCityTesting";
    public static final String DB_PW = "ReisMinerDB";
    public static final String CLOUD_CITY_ID = "514511396491231233";
    public static final long JOIN_CATEGORY = 717017605575278673L;
    public static final List<String> emotesToAdd = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
    public static final String CLIENT_ID = "gp762nuuoqcoxypju8c569th9wz7q5";
    public static final String CLIENT_SECRET = "vibomnsu7ay1sje2l7f42scxbnzuxz";
    public static final String CLIENT_ACCESS_TOKEN = "6aalhf40g5954pr230n2y2w0fb3j1x";

    public static LinkedHashMap<String, NewJoinedMember> newJoinedMembers = new LinkedHashMap<>();
    public static long ROLE_ID_EVERYONE = 0L;
    public static Languages languages;



    public static void loadSettings() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        System.out.println("loading");
        try {
            InputStream resourceAsStream = Settings.class.getResourceAsStream("/texts.json");
            languages = objectMapper.readValue(resourceAsStream, Languages.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getLanguageText(String language, String toGet) {
        if (language.equals("eng")) return languages.english.get(toGet);
        else return languages.german.get(toGet);
    }

    public static String getLanguageTextByMember(Member member, String toGet) {
        String language = SqlUtils.getMemberPreferredLanguage(member);

        if (language.equals("eng")) return languages.english.get(toGet);
        else return languages.german.get(toGet);
    }

    public static String getAsFormatted(Member member, String toGet, List<String> toFormat) {
        return String.format(getLanguageTextByMember(member, toGet), toFormat);
    }


}
