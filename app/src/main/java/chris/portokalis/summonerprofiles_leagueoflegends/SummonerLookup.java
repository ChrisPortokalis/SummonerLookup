package chris.portokalis.summonerprofiles_leagueoflegends;

//android imports
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.lang.*;

public class SummonerLookup extends AppCompatActivity {
   // StrictMode.setThreadPolicy(policy);
    //private member variables
    public FrameLayout frame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Start", "did Start");
        setContentView(R.layout.activity_summoner_lookup);
        //find id for frame layout
        frame = (FrameLayout) findViewById(R.id.lookup_fragment_container);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(frame.getId(),new LookupFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summoner_lookup, menu);
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

}
