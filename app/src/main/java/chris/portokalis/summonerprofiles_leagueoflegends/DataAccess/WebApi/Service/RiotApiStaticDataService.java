package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chris on 11/19/2016.
 */
public class RiotApiStaticDataService {

    private static final String API_BASE_URL = "https://global.api.pvp.net/api/lol/static-data/";

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass)
    {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

}
