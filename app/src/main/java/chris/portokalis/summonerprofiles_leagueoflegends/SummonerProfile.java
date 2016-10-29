package chris.portokalis.summonerprofiles_leagueoflegends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public JSONObject totalStatsJson;

    private String profileImageId;
    private String summonerId;

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
        String name = intentFromLookup.getStringExtra("name");

        setupUi(name, region);
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



    private void setupUi(String name, String region)
    {
        RiotApiSummonerDao riotDao = RiotApiSummonerService.createService(RiotApiSummonerDao.class);
        Call<SummonerInfo> call = riotDao.getSummonerInfo(name, region, StringUtils.devKey);

        Log.d("DEBUG", "name = " + name + " " + region);

        call.enqueue(new Callback<SummonerInfo>(){

            @Override
            public void onResponse(Call<SummonerInfo> call, Response<SummonerInfo> response) {

                Log.d("DEBUG", "in response");

                if(response.isSuccessful())
                {
                    Log.d("DEBUG", "name = " + response.body().getName());

                    summonerName.setText(response.body().getName());
                    summonerLevel.setText(String.valueOf(response.body().getSummonerLevel()));
                }
                else
                {
                    Log.d("DEBUG", response.errorBody().toString());
                    Log.d("DEBUG", response.message().toString());
                }
            }
            @Override
            public void onFailure(Call<SummonerInfo> call, Throwable t ) {
                Log.wtf("FAILURE", "Failed to get response from Riot API");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    public void setupStatScreen()
    {
        String numberOfKills = "";
        try {
            JSONArray totalStatsJsonArr = this.totalStatsJson.getJSONArray("playerStatSummaries");
            JSONObject unrankedStatsJson = totalStatsJsonArr.getJSONObject(12).getJSONObject("aggregatedStats");
            System.out.println(unrankedStatsJson.toString());
            numberOfKills = unrankedStatsJson.getString("totalChampionKills");
            System.out.println(numberOfKills);
        }
        catch(JSONException je)
        {
            System.out.println("SummProf setupStatScreen JSON EXCEPTION");
        }

        LinearLayout verticalLL = new LinearLayout(getApplicationContext());
        verticalLL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        verticalLL.setGravity(LinearLayout.VERTICAL);

        LinearLayout horizontalLL1 = new LinearLayout(getApplicationContext());
        horizontalLL1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        horizontalLL1.setGravity(LinearLayout.HORIZONTAL);

        TextView totalKills = new TextView(getApplicationContext());
        totalKills.setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        totalKills.setText(numberOfKills);

        horizontalLL1.addView(totalKills);
        verticalLL.addView(horizontalLL1);
        profileScrollView.addView(verticalLL);

    }

}
