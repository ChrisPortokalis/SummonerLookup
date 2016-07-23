package chris.portokalis.summonerprofiles_leagueoflegends;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;


/**
 * Created by Chris on 5/15/2015.
 */
public class LookupFragment extends Fragment implements View.OnClickListener{

    public static String devKey;
    private EditText summonerNameSearchKey;
    private Button findSummoner;
    private Spinner region;


    public static LookupFragment newInstance(int page, String title)
    {
        LookupFragment fragmentFirst = new LookupFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.lookup_fragment,container,false);
        this.devKey = "60234842-7729-42ad-9263-3e5d4a8bfe68";
        this.summonerNameSearchKey = (EditText)view.findViewById(R.id.summSearchKey);

        this.region = (Spinner) view.findViewById(R.id.region);
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),R.array.regionSpinner, android.R.layout.simple_spinner_item);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapt);

        this.findSummoner = (Button) view.findViewById(R.id.findSumm);
        this.findSummoner.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        String selectedRegion = region.getSelectedItem().toString();
        String urlString = "https://na.api.pvp.net/api/lol/" + selectedRegion + "/v1.4/summoner/by-name/" + this.summonerNameSearchKey.getText().toString().replace(" ", "")  + "?api_key=" + this.devKey;
        JsonAsyncTask jsonTask = new JsonAsyncTask(urlString, this.summonerNameSearchKey.getText().toString(),selectedRegion);
        //jsonTask.summ = this;
        jsonTask.execute(urlString);

    }

    private class JsonAsyncTask extends AsyncTask<String, String, JSONObject >
    {
        private JSONObject result;
        private String urlString;
        private String nameString;
        private String region;
        public SummonerLookup summ;
        public SummonerProfile summProf;

        JsonAsyncTask(String url, String name)
        {
            this.urlString = url;
            this.nameString = name.replace(" ", "");
        }

        JsonAsyncTask(String url, String name, String region)
        {
            this.urlString = url;
            this.nameString = name;
            this.region = region;
        }

        @Override
        protected JSONObject doInBackground(String ... urls) {

            JSONObject json = null;

            try
            {
                InputStream inputStream = null;
                ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                HttpClient httpClient = new DefaultHttpClient();

                System.out.println( "ASYNC: " + this.urlString);
                HttpGet httpPost = new HttpGet(this.urlString);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sBuilder = new StringBuilder();
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                String jsonText = sBuilder.toString();
                json = new JSONObject(jsonText);
                this.result = json;
                //System.out.println(this.result.toString());

            }
            catch(MalformedURLException mue)
            {

            }
            catch(IOException ioe)
            {

            }
            catch(JSONException je)
            {

            }


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json)
        {
            Intent passToProfile = new Intent(getActivity(),SummonerProfile.class);
            Log.d("JSON TEST", result.toString());
            passToProfile.putExtra("json", result.toString());
            passToProfile.putExtra("name",nameString);
            passToProfile.putExtra("region",region);
            startActivity(passToProfile);
        }


        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public JSONObject getJsonObject()
        {
            return this.result;
        }
    }


    private static String readAll(Reader rd) throws IOException {

        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
