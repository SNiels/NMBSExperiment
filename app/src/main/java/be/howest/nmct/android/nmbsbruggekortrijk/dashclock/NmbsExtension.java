package be.howest.nmct.android.nmbsbruggekortrijk.dashclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import be.howest.nmct.android.nmbsbruggekortrijk.app.R;
import be.howest.nmct.android.nmbsbruggekortrijk.app.RouteActivity;
import be.irail.api.IRail;
import be.irail.api.data.Connection;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import java.text.SimpleDateFormat;

/**
 * Created by Niels on 27/02/14.
 */
public class NmbsExtension extends DashClockExtension{
    private static final String TAG = "NmbsExtension";
    private static final String STATIONURL = "http://api.irail.be/";
    public static final String FROM = "from";
    public static final String TO = "to";
    @Override
    protected void onUpdateData(int reason) {
        switch(reason)
        {
            case UPDATE_REASON_MANUAL:
                update();
                break;
            case UPDATE_REASON_PERIODIC:
                update();
                break;
            case UPDATE_REASON_SETTINGS_CHANGED:
                update();
                break;
        }
    }

    private void update() {
        // Get preference value.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String from = sp.getString(FROM,null);
        String to = sp.getString(TO,null);
        if(from==null||to==null)return;
        // Publish the extension data update.
        //task = new LoadConnectionTask();
        //task.execute(from,to);
        Connection connection;
        try{
            IRail rail = new IRail(STATIONURL,"nl",1);
            connection = rail.getConnections(from,to).get(0);
        }catch (Exception ex)
        {
            Log.d(TAG,ex.getMessage());
            return;
        }
        if(connection==null)return;
        String leave =new SimpleDateFormat("H:mm").format(connection.getDeparture().getTime());
        String arrive =new SimpleDateFormat("H:mm").format(connection.getArrival().getTime());
        Intent i = new Intent(getApplicationContext(), RouteActivity.class);
       // i.putExtra(ConnectionActivity.CONNECTION,(Serializable)connection);
        publishUpdate(new ExtensionData()
                .visible(true)
                .icon(R.drawable.ic_launcher)
                .status("Train @ " + leave)
                .expandedTitle(connection.getDeparture().getStation().getName()
                        + " " + leave + " - " + arrive + " " + connection.getArrival().getStation().getName())
                .expandedBody("Trein vertrekt van " + connection.getDeparture().getStation().getName()
                        + " om " + leave + " en komt aan om " + arrive + " in " + connection.getArrival().getStation().getName())
                .clickIntent(i));
    }

    /*LoadConnectionTask task;

    private class LoadConnectionTask extends AsyncTask<String,Void,Connection>
    {

        @Override
        protected Connection doInBackground(String... params) {
            String from = params[0];
            String to = params[1];
            try{
                IRail rail = new IRail(STATIONURL,"nl",1);
                return rail.getConnections(from,to).get(0);
            }catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Connection connection) {
            super.onPostExecute(connection);
            String leave =new SimpleDateFormat("H:mm").format(connection.getDeparture().getTime());
            String arrive =new SimpleDateFormat("H:mm").format(connection.getArrival().getTime());
            Intent i = new Intent(NmbsExtension.this,ConnectionActivity.class);
            i.putExtra(ConnectionActivity.CONNECTION,(Serializable)connection);
            publishUpdate(new ExtensionData()
                    .visible(true)
                    .icon(R.drawable.ic_launcher)
                    .status("Train @ " + leave)
                    .expandedTitle(connection.getDeparture().getStation().getName()
                            + " " + leave + " - " + arrive + " " + connection.getArrival().getStation().getName())
                    .expandedBody("Trein vertrekt van " + connection.getDeparture().getStation().getName()
                            + " om " + leave + " en komt aan om " + arrive + " in " + connection.getArrival().getStation().getName())
                    .clickIntent(i));
            task=null;
        }
    }*/
}
