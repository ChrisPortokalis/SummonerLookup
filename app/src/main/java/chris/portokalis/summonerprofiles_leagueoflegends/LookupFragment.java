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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chris on 5/15/2015.
 */
public class LookupFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.summSearchKey)
    public EditText summonerNameSearchKey;
    @BindView(R.id.findSumm)
    public Button findSummoner;
    @BindView(R.id.region)
    public Spinner region;


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
        ButterKnife.bind(this, view);

        setupSpinnerAdapter();

        this.findSummoner.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(getActivity(),SummonerProfile.class);
        intent.putExtra("name", this.summonerNameSearchKey.getText().toString());
        intent.putExtra("region", region.getSelectedItem().toString());
        startActivity(intent);
    }

    public void setupSpinnerAdapter()
    {
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),R.array.regionSpinner, android.R.layout.simple_spinner_item);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapt);
    }


}
