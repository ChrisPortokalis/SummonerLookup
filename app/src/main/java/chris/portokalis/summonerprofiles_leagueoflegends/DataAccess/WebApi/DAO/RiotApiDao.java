package chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.DAO;

import org.json.JSONObject;

/**
 * Created by Chris on 7/23/2016.
 */
public interface RiotApiDao {

    public JSONObject getSummonerInfo(String summonerName, String region);
}
