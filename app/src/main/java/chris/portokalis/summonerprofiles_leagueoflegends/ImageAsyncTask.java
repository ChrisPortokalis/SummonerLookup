package chris.portokalis.summonerprofiles_leagueoflegends;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.*;
import org.apache.http.HttpEntity;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Chris on 1/11/2015.
 */
public class ImageAsyncTask extends AsyncTask<String, Bitmap, String> {

    private Drawable iconDrawable;
    private JSONObject result;
    private String urlString;
    private String nameString;
    private String icondId;
    private String type;
    public SummonerLookup summ;
    public SummonerProfile summProf;

    ImageAsyncTask(String url, String id )
    {
        this.urlString = url;
        this.icondId = id;
    }

    @Override
    protected String doInBackground(String ... urls) {

        JSONObject json = null;

        try
        {
            InputStream is = (InputStream) new URL(this.urlString).getContent();
            this.iconDrawable = Drawable.createFromStream(is, this.icondId + ".png");


            //System.out.println(this.result.toString());

        }
        catch(MalformedURLException mue)
        {

        }
        catch(IOException ioe)
        {

        }


        return urlString;
    }


    @Override
    protected void onPostExecute(String string)
    {
        this.summProf.summonerProfileImage.setImageDrawable(this.iconDrawable);

    }


}
