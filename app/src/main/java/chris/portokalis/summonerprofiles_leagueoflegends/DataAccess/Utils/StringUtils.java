package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Utils;

/**
 * Created by Chris on 5/15/2015.
 */
public class StringUtils {

    public static final String devKey = "RGAPI-F019138C-FAE7-4F8E-9614-6A599691E95F";

    public boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
