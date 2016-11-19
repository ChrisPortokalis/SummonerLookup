package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chris on 11/19/2016.
 */
public class RiotApiImageService {

    private static final String API_BASE_URL = "http://ddragon.leagueoflegends.com/cdn";

    public static Request getSummonerIconRequest(String version, int imageId)
    {
        return new Request.Builder().url(buildSummonerIconUrl(version, imageId)).build();
    }

    public static String buildSummonerIconUrl(String version, int imageId)
    {
        return String.format("%1s/%2s/img/profileicon/%d.png", API_BASE_URL, version, imageId);
    }
}
