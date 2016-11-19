package chris.portokalis.summonerprofiles_leagueoflegends;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Cache.SummonerCache;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Utils.StringUtils;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.DAO.RiotApiSummonerDao;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Model.SummonerInfo;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Service.RiotApiSummonerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummonerProfile extends Activity implements View.OnClickListener {

    public TextView summonerName;
    public TextView summonerLevel;
    public ImageView summonerProfileImage;
    public ScrollView profileScrollView;

    private String profileImageId;
    private String summonerId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_profile);

        this.profileScrollView = (ScrollView)findViewById(R.id.profileScrollView);

        summonerName = (TextView)findViewById(R.id.foundSummName);
        summonerLevel = (TextView)findViewById(R.id.foundSummLevel);

        summonerProfileImage = (ImageView)findViewById(R.id.summonerIconImage);

        Intent intentFromLookup = getIntent();
        String region = intentFromLookup.getStringExtra("region");
        this.name = intentFromLookup.getStringExtra("name");

        setupSummonerInfo(name, region);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summoner_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void setupSummonerInfo(final String name, final String region)
    {
        RiotApiSummonerDao riotDao = RiotApiSummonerService.createService(RiotApiSummonerDao.class, region);
        Call<Map<String, SummonerInfo>> call = riotDao.getSummonerInfo(name, region, StringUtils.devKey);

        call.enqueue(new Callback<Map<String, SummonerInfo>>(){

            @Override
            public void onResponse(Call<Map<String, SummonerInfo>> call, Response<Map<String, SummonerInfo>> response) {

                Log.d("DEBUG", "in response");

                if(response.isSuccessful())
                {

                    Log.d("DEBUG", "name = " + response.body());

                    if(response.message().toString().equals("Not Found")) {

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.show();
                        finishActivity(0);
                    }
                    else {
                        //convert name to lowercase and remove spaces for Map key
                        String newName = name.replaceAll("\\s+","").toLowerCase();
                        SummonerInfo summoner = response.body().get(newName);
                        summonerName.setText(summoner.getName());
                        summonerLevel.setText(String.valueOf(summoner.getSummonerLevel()));

                        SummonerCache.storeRegion(region, getApplicationContext());
                        SummonerCache.storeSummonerName(name, getApplicationContext());
                    }
                }
                else
                {
                    if(response.message().toString().equals("Not Found")) {

                        Toast.makeText(getApplicationContext(), "Summoner Not Found", Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<String, SummonerInfo>> call, Throwable t ) {
                Log.wtf("FAILURE", "Failed to get response from Riot API");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
