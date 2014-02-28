package be.howest.nmct.android.nmbsbruggekortrijk.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import be.irail.api.IRail;
import be.irail.api.data.Station;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link be.howest.nmct.android.nmbsbruggekortrijk.app.PickStationsFragment.OnPickStationsFragmentInteractionListener} interface
 * to handle interaction events.
 *
 */
public class PickStationsFragment extends Fragment {
    private List<Station> stations = new ArrayList<Station>();
    private static final String STATIONURL = "http://api.irail.be";
    private ArrayAdapter<Station> stationAdapter;

    private OnPickStationsFragmentInteractionListener mListener;
    private DownloadXmlTask task;

    private AutoCompleteTextView stationEditText1;
    private AutoCompleteTextView stationEditText2;

    private IRail irail ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("PickStationsFragment","onCreate");
        super.onCreate(savedInstanceState);
        irail = new IRail(STATIONURL,"nl");
        this.task=new DownloadXmlTask();
        task.execute(STATIONURL+"?lang=nl");
        stationAdapter =new ArrayAdapter<Station>(getActivity(),
        android.R.layout.simple_dropdown_item_1line, android.R.id.text1, stations);
        setRetainInstance(true);
    }

    private TextWatcher watcher ;

    private void checkInput() {
        String stationA = stationEditText1.getText().toString();
        if(TextUtils.isEmpty(stationA)||stationA.length()<2)return;
        String stationB = stationEditText2.getText().toString();
        if(TextUtils.isEmpty(stationB)||stationB.length()<2)return;
        List<Station> stationsA = new ArrayList<Station>();
        List<Station> stationsB = new ArrayList<Station>();

        for(Station station:stations)
        {
           if(station.getName().contains(stationA))
               stationsA.add(station);
            else if(station.getName().contains(stationB))
               stationsB.add(station);
        }
        if(stationsA.size()!=1||stationsB.size()!=1)return;
        mListener.onStationsPicked(stationsA,stationsB);
    }


    public PickStationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("PickStationsFragment","onCreateView");
        View view =inflater.inflate(R.layout.fragment_pick_stations_layout, container, false);
        //voorlopig hardcoded
        stationEditText1=(AutoCompleteTextView)view.findViewById(R.id.stationEditText1);
        stationEditText2=(AutoCompleteTextView)view.findViewById(R.id.stationEditText2);
        // Inflate the layout for this fragment

        stationEditText1.setAdapter(stationAdapter);
        stationEditText2.setAdapter(stationAdapter);
        watcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInput();
            }
        };
        stationEditText1.addTextChangedListener(watcher);
        stationEditText2.addTextChangedListener(watcher);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPickStationsFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnPickStationsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        stationEditText1.removeTextChangedListener(watcher);
        stationEditText2.removeTextChangedListener(watcher);
        //Log.d("PickStationsFragment","onStop");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPickStationsFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onStationsPicked(List<Station> pointsA,List<Station> pointsB);
    }

    private class DownloadXmlTask extends AsyncTask<String, be.irail.api.data.Station, List<be.irail.api.data.Station>> {

        @Override
        protected List<Station> doInBackground(String... urls) {
            /*try {
                new RemoteXMLStationRepository().getStations(urls[0],this);
            } catch (IOException e) {
                //return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                //return getResources().getString(R.string.xml_error);
            }catch(Exception e){
                Log.d("error", e.getMessage());
            }*/
            try{
                return irail.getStations();
                //Station station = new Station();
            }catch (Exception ex)
            {

            }
            return  null;

        }

        @Override
        protected void onPostExecute(List<be.irail.api.data.Station> newstations) {
            super.onPostExecute(newstations);
            if(newstations==null)return;
            stations.addAll(newstations);
            stationAdapter.notifyDataSetChanged();
        }


        /*@Override
        public void onParsedStation(Station station) {
            publishProgress(station);
        }

        @Override
        protected void onProgressUpdate(Station... values) {
            super.onProgressUpdate(values);
            for(Station station:values)
                stations.add(station);
            stationAdapter.notifyDataSetChanged();
        }*/
    }


}
