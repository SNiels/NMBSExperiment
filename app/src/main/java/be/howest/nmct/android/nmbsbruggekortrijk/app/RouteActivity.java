package be.howest.nmct.android.nmbsbruggekortrijk.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import be.irail.api.data.Station;

import java.util.List;

public class RouteActivity extends ActionBarActivity implements PickStationsFragment.OnPickStationsFragmentInteractionListener {

    ConnectionsFragment conFrag ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_layout);
        conFrag = (ConnectionsFragment)getSupportFragmentManager().findFragmentById(R.id.connectionsList);
        Log.d("RouteActivity","onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("RouteActivity","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("RouteActivity","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("RouteActivity","onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("RouteActivity","onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("RouteActivity","onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStationsPicked(List<Station> pointsA, List<Station> pointsB) {
        if(pointsA==null||pointsB==null||pointsA.size()<1||pointsB.size()<1)
            return;
        conFrag.onStationsPicked( pointsA, pointsB);
    }
}
