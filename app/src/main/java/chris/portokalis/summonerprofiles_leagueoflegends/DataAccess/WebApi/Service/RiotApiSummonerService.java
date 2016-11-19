package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chris on 7/24/2016.
 */
public class RiotApiSummonerService {

    public static final String API_BASE_URL_1 = "https://";
    public static final String API_BASE_URL_2 = ".api.pvp.net/api/lol/";

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass, String region)
    {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getApiBaseUrl(region)).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static String getApiBaseUrl(String region)
    {
        return API_BASE_URL_1 + region + API_BASE_URL_2;
    }
}
