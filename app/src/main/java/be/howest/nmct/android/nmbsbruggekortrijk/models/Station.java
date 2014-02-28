package be.howest.nmct.android.nmbsbruggekortrijk.models;

/**
 * Created by Niels on 20/02/14.
 */
public class Station {
    private String id;
    private float locationX;
    private float locationY;
    private String name;

    public Station(){}
    public Station(String id, float locationX, float locationY, String standardName){
        setId(id);
        setLocationX(locationX);
        setLocationY(locationY);
        setName(standardName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLocationX() {
        return locationX;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
//<station id="BE.NMBS.008895000" locationX="4.038586" locationY="50.943053" standardname="Aalst">Aalst</station>