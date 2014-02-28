package be.howest.nmct.android.nmbsbruggekortrijk.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import be.irail.api.IRail;
import be.irail.api.data.Connection;
import be.irail.api.data.Station;
import com.nhaarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {link Callbacks}
 * interface.
 */
public class ConnectionsFragment extends Fragment implements AbsListView.OnItemClickListener, PickStationsFragment.OnPickStationsFragmentInteractionListener {

    private static final String STATIONURL = "http://api.irail.be/";


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter;
    private List<Connection> connections;
    private static final String API_URL="http://api.irail.be";
    private IRail irail;
    private DownloadXmlTask task;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConnectionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connections=new ArrayList<Connection>();
        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<Connection>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, connections);
        irail = new IRail(API_URL,"nl");
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connections, container, false);

        mListView = (AbsListView) view.findViewById(android.R.id.list);


        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks


        ScaleInAnimationAdapter animAdapter = new ScaleInAnimationAdapter((BaseAdapter)mAdapter);
        animAdapter.setAbsListView(mListView);
        ((AdapterView<ListAdapter>) mListView).setAdapter(animAdapter);
        mListView.setOnItemClickListener(this);
        return view;
    }



    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Connection con = connections.get(position);
        Intent i = new Intent(getActivity(),ConnectionActivity.class);
        i.putExtra(ConnectionActivity.CONNECTION,(Serializable)con);
        startActivity(i);
    }

    @Override
    public void onStationsPicked(List<Station> pointsA, List<Station> pointsB) {
        if(pointsA==null||pointsB==null||pointsA.size()<1||pointsB.size()<1)
            return;
        task= new DownloadXmlTask();
        task.execute(pointsA,pointsB);
    }

    private class DownloadXmlTask extends AsyncTask<List<Station>, Station, List<Connection>> {

        @Override
        protected List<Connection> doInBackground(List<Station>... stationsArray) {
            List<Connection> res = new ArrayList<Connection>();
            for(Station station1:stationsArray[0])
            {
                for(Station station2:stationsArray[1])
                {
                    try{
                    res.addAll(irail.getConnections(station1.getName(),station2.getName()));
                    }catch(Exception ex){
                        Log.d("irailapi",ex.getMessage());
                    }
                }
            }
            return  res;

        }

        @Override
        protected void onPostExecute(List<Connection> newcons) {
            super.onPostExecute(newcons);
            if(newcons==null||newcons.size()<1)return;
            connections.clear();
            connections.addAll(newcons);
            mAdapter.notifyDataSetChanged();
        }
    }
}
