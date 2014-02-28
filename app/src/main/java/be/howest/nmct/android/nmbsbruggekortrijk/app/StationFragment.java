package be.howest.nmct.android.nmbsbruggekortrijk.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import be.irail.api.IRail;
import be.irail.api.data.Station;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

//import be.howest.nmct.android.nmbsbruggekortrijk.models.Station;

public class StationFragment extends ListFragment {
    public static final String API_URL="http://api.irail.be";
    private IRail irail ;
    private OnFragmentInteractionListener mListener;
    private List<Station> stations = new ArrayList<Station>();
    private static final String STATIONURL = "http://api.irail.be/stations/?lang=nl";
    private ArrayAdapter<Station> stationAdapter;

    private DownloadXmlTask task;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationFragment() {
        irail=new IRail(API_URL,"nl");//temp
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.task=new DownloadXmlTask();
        task.execute(STATIONURL);
        // TODO: Change Adapter to display your content
        stationAdapter =new ArrayAdapter<Station>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, stations);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stations_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ScaleInAnimationAdapter animAdapter = new ScaleInAnimationAdapter(stationAdapter);
        animAdapter.setAbsListView(getListView());
        setListAdapter(animAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(stations.get(position).getId());
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private class DownloadXmlTask extends AsyncTask<String, Station, List<Station>> {

        @Override
        protected List<Station> doInBackground(String... urls) {
           /* try {
                 new RemoteXMLStationRepository().getStations(urls[0],this);
            } catch (IOException e) {
                //return getResources().getString(R.string.connection_error);
            } catch (XmlPullParserException e) {
                //return getResources().getString(R.string.xml_error);
            }catch(Exception e){
                Log.d("error",e.getMessage());
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
        protected void onPostExecute(List<Station> newstations) {
            super.onPostExecute(newstations);
            if(newstations==null)return;
            stations.addAll(newstations);
            stationAdapter.notifyDataSetChanged();
        }

/*@Override
        protected void onProgressUpdate(Station... values) {
            super.onProgressUpdate(values);
            for(Station station:values)
                stations.add(station);
            stationAdapter.notifyDataSetChanged();
        }*/
    }

}
