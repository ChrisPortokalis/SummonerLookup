package chris.portokalis.summonerprofiles_leagueoflegends;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Cache.SummonerCache;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.Utils.StringUtils;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.DAO.RiotApiSummonerDao;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Model.Summoner.SummonerInfo;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Service.RiotApiImageService;
import chris.portokalis.summonerprofiles_leagueoflegends.DataAccess.WebApi.Service.RiotApiSummonerService;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummonerProfile extends Activity implements View.OnClickListener {

    @BindView(R.id.foundSummName)
    public TextView summonerName;
    @BindView(R.id.foundSummLevel)
    public TextView summonerLevel;
    @BindView(R.id.summonerIconImage)
    public ImageView summonerProfileImage;
    @BindView(R.id.profileScrollView)
    public ScrollView profileScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_profile);
        ButterKnife.bind(this);

        setupSummonerInfo();

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



    private void setupSummonerInfo()
    {

        Intent intentFromLookup = getIntent();
        final String region = intentFromLookup.getStringExtra("region");
        final String name = intentFromLookup.getStringExtra("name");

        Log.d("DEBUG", "name = " + name);
        Log.d("DEUBG", "region = " + region);

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
                        String newName = name;
                        newName = newName.replaceAll("\\s+","").toLowerCase();

                        Log.d("DEBUG", "newName = " + newName);

                        SummonerInfo summoner = response.body().get(newName);
                        summonerName.setText(response.body().get(newName).getName());
                        summonerLevel.setText(String.valueOf(response.body().get(newName).getSummonerLevel()));

                        SummonerCache.storeRegion(region, getApplicationContext());
                        SummonerCache.storeSummonerName(name, getApplicationContext());

                        OkHttpClient client = new OkHttpClient();
                        client.newCall(RiotApiImageService.getSummonerIconRequest(SummonerCache.getVersion(getApplicationContext()), String.valueOf(response.body().get(newName).getProfileIconId())))
                                .enqueue(new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(okhttp3.Call call, IOException e) {
                                        Log.wtf("WTF", "FAILED TO GET IMAGE FROM DDRAGON");

                                    }

                                    @Override
                                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                                        if(response.isSuccessful())
                                        {
                                            InputStream is = response.body().byteStream();
                                            final Bitmap bitmap = BitmapFactory.decodeStream(is);
                                            
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    summonerProfileImage.setImageBitmap(bitmap);
                                                }
                                            });
                                        }
                                        else{
                                            Log.wtf("WTF", "wtf happened");
                                        }
                                    }
                                });
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
