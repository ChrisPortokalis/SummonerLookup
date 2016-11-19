package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.DAO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Chris on 11/19/2016.
 */
public interface RiotApiStaticDataDao {

    @GET("{region}/v1.2/versions")
    public Call<ArrayList<String>> getVersions(@Path("region") String region, @Query("api_key") String apiKey);
}
