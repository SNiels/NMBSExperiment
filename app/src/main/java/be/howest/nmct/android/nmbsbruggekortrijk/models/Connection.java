package be.howest.nmct.android.nmbsbruggekortrijk.models;

/**
 * Created by Niels on 20/02/14.
 */
public class Connection {
    private String id;
    private int duration;
    private TrainAction departure;
    private TrainAction arrival;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TrainAction getDeparture() {
        return departure;
    }

    public void setDeparture(TrainAction departure) {
        this.departure = departure;
    }

    public TrainAction getArrival() {
        return arrival;
    }

    public void setArrival(TrainAction arrival) {
        this.arrival = arrival;
    }

    public Connection(String id, int duration, TrainAction departure, TrainAction arrival)
    {
        setId(id);
        setDeparture(departure);
        setDuration(duration);
        setArrival(arrival);
    }
}
