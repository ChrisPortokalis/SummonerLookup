package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.DAO;

import java.util.Map;

import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Model.SummonerInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Chris on 7/23/2016.
 */
public interface RiotApiSummonerDao {

   @GET("{region}/v1.4/summoner/by-name/{summonerName}/")
   Call<Map<String, SummonerInfo>> getSummonerInfo(@Path("summonerName") String summonerName, @Path("region") String region, @Query("api_key") String apiKey);

   String getCurrentVersion();
}
