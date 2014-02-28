package be.howest.nmct.android.nmbsbruggekortrijk.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import be.irail.api.data.Connection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class ConnectionActivity extends ActionBarActivity {
    public static final String CONNECTION="connection";
    private Connection connection;
    private TextView from;
    private TextView to;
    private TextView fromTime;
    private TextView toTime;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.map)
                .contentLayout(R.layout.activity_connection);


        setContentView(helper.createView(this));

        helper.initActionBar(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        connection =(Connection)getIntent().getSerializableExtra(CONNECTION);
        from=(TextView)findViewById(R.id.from);
        to =(TextView)findViewById(R.id.to);
        fromTime=(TextView)findViewById(R.id.fromTime);
        toTime =(TextView)findViewById(R.id.toTime);
        from.setText(connection.getDeparture().getStation().getName());
        to.setText(connection.getArrival().getStation().getName());
        toTime.setText(connection.getArrival().getTime().toString());
        fromTime.setText(connection.getDeparture().getTime().toString());
        try{
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            UiSettings set =googleMap.getUiSettings();
            /*set.setScrollGesturesEnabled(false);
            set.setRotateGesturesEnabled(false);*/
            set.setZoomControlsEnabled(false);
            //set.setZoomGesturesEnabled(false);
            set.setMyLocationButtonEnabled(false);
            set.setAllGesturesEnabled(false);
            set.setCompassEnabled(false);
            LatLng ln1 = new LatLng(connection.getDeparture().getStation().getLocation().getLatitude(),
                    connection.getDeparture().getStation().getLocation().getLongitude());
            LatLng ln2 =new LatLng(connection.getArrival().getStation().getLocation().getLatitude(),
                    connection.getArrival().getStation().getLocation().getLongitude());

            googleMap.addMarker(
                    new MarkerOptions().position(
                            ln1
                    ).title(connection.getDeparture().getStation().getName()));
            googleMap.addMarker(
                    new MarkerOptions().position(
                            ln2
                    ).title(connection.getArrival().getStation().getName()));
            CameraPosition pos = new CameraPosition.Builder().target(new LatLng((ln1.latitude+ln2.latitude)/2,
                    (ln1.longitude+ln2.longitude)/2)).zoom(7.5f).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos));
        }
        }catch(Exception ex){}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            //case android.R.id.home:
                //onBackPressed();
                /*Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }*/
                //return true;
            case R.id.action_settings:
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

}
