package chris.portokalis.summonerprofiles_leagueoflegends;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Utils.StringUtils;

public class SummonerProfile extends Activity implements View.OnClickListener {

    public TextView summonerName;
    public TextView summonerLevel;
    public ImageView summonerProfileImage;
    public Button refreshButton;
    public ScrollView profileScrollView;
    public JSONObject totalStatsJson;

    private String profileImageId;
    private String summonerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_profile);
        Intent intentFromLookup = getIntent();
        String summonerJsonString = intentFromLookup.getStringExtra("json");
        String nameString = intentFromLookup.getStringExtra("name");

        System.out.println("SUMMONER JSON = " + summonerJsonString);
        System.out.println("NAME STRING = " + nameString);

        this.profileScrollView = (ScrollView)findViewById(R.id.profileScrollView);

        summonerName = (TextView)findViewById(R.id.foundSummName);
        summonerLevel = (TextView)findViewById(R.id.foundSummLevel);

        summonerProfileImage = (ImageView)findViewById(R.id.summonerIconImage);

        refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);


        JSONObject summonerJson = null;
        try {
            summonerJson = new JSONObject(summonerJsonString);
            summonerName.setText(summonerJson.getJSONObject(nameString.toLowerCase()).get("name").toString());
            summonerLevel.setText(summonerJson.getJSONObject(nameString.toLowerCase()).get("summonerLevel").toString());
            this.profileImageId = summonerJson.getJSONObject(nameString.toLowerCase()).get("profileIconId").toString();
            this.summonerId = summonerJson.getJSONObject(nameString.toLowerCase()).get("id").toString();


            String profileImageURL = "http://ddragon.leagueoflegends.com/cdn/4.21.5/img/profileicon/" + this.profileImageId + ".png";

            ImageAsyncTask imageTask = new ImageAsyncTask(profileImageURL,this.profileImageId);
            imageTask.summProf = this;
            imageTask.execute(this.profileImageId);


        }
        catch(JSONException je) {

            System.out.println("JSON Exception -- SummonerProfile -- OnCreate");
        }

        String statsJsonURL = "https://na.api.pvp.net//api/lol/na/v1.3/stats/by-summoner/" + this.summonerId + "/summary" + "?api_key=" + StringUtils.devKey;
        //JsonAsyncTask defaultSetupTask = new JsonAsyncTask(statsJsonURL,"x","totalStats");
        //defaultSetupTask.summProf = this;
        //defaultSetupTask.execute(statsJsonURL);

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


    @Override
    public void onClick(View v)
    {
        if(v == this.refreshButton)
        {
            finish();
            startActivity(getIntent());

        }

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
