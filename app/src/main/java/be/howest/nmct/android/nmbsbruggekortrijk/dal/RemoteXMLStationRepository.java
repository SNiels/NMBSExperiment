package be.howest.nmct.android.nmbsbruggekortrijk.dal;

import android.util.Xml;
import be.howest.nmct.android.nmbsbruggekortrijk.models.Station;
import be.howest.nmct.android.nmbsbruggekortrijk.uow.UOWParseStationInterface;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niels on 20/02/14.
 */
public class RemoteXMLStationRepository {
    private final static String LOCATIONX="locationX";
    private final static String LOCATIONY="locationY";
    private final static String ID="id";

    public List<Station> getStations(String urlString,UOWParseStationInterface uow)throws XmlPullParserException, IOException{
        InputStream stream = null;
        List<Station> entries = null;
        try {
            stream = downloadUrl(urlString);
            entries = parse(stream,uow);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return  entries;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    public List<Station> parse(InputStream in,UOWParseStationInterface uow) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readStations(parser,uow);
        } finally {
            in.close();
        }
    }

    private List<Station> readStations(XmlPullParser parser,UOWParseStationInterface uow) throws XmlPullParserException, IOException {
        List<Station> entries = new ArrayList<Station>();

        parser.require(XmlPullParser.START_TAG, null, "stations");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("station")) {
                try{
                    Station station=readStation(parser);
                entries.add(station);
                    uow.onParsedStation(station);
                }catch(Exception ex){}
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Station readStation(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "station");
        return  new Station(
                parser.getAttributeValue(null,ID),
                Float.parseFloat(parser.getAttributeValue(null,LOCATIONX)),
                Float.parseFloat(parser.getAttributeValue(null,LOCATIONY)),
                readText(parser));
    }


    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
