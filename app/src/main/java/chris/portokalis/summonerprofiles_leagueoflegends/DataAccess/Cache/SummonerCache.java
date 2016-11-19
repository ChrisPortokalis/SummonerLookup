package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Chris on 11/19/2016.
 */
public class SummonerCache {

    private static final String APPLICATION_PREFERENCES = "SUMMONER_PREFERENCES";

    private static final String SUMMONER_NAME_KEY = "SUMMONER_NAME";
    private static final String REGION_KEY = "REGION_NAME";

    private static final String SUMMONER_NAME_DEFAULT = "";
    private static final String REGION_DEFAULT = "NA";

    public static void storeSummonerName(String name, Context context)
    {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SUMMONER_NAME_KEY, name);
        editor.commit();
    }

    public static void storeRegion(String region, Context context)
    {
        SharedPreferences sharedPreferences =  context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REGION_KEY, region);
        editor.commit();
    }

    public static String getSummonerName(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(SUMMONER_NAME_KEY, SUMMONER_NAME_DEFAULT);
    }

    public static String getRegion(Context context)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPref.getString(REGION_KEY, REGION_DEFAULT);
    }

}
