package Model;

import com.parse.ParsePush;

/**
 * Created by Isaac on 2/13/2016.
 */
public class PushNotifsManager {
    private static PushNotifsManager ourInstance = new PushNotifsManager();

    private String apartmentChannel;
    private String userChannel;

    private static final String outOfItem = "Your apartment has run out of "

    private PushNotifsManager() {
    }

    public static PushNotifsManager getInstance() {
        return ourInstance;
    }

    public void subscribeToApartment(Apartment apt) {
        this.apartmentChannel = apt.getObjectId();
        ParsePush.subscribeInBackground(apt.getObjectId());
    }

    public void subscribeToUser(Person person) {
        this.userChannel = person.getObjectId();
        ParsePush.subscribeInBackground(person.getObjectId());
    }

    public void sendOutOfItem(String message) {
        ParsePush push = new ParsePush();
        push.setChannel(this.apartmentChannel);
        push.setMessage(outOfItem + message);
        push.sendInBackground();
    }

    public void sendPushToUser(Person person, String message) {
        ParsePush push = new ParsePush(q);
        push.setChannel(person.getObjectId());
        push.setMessage(message);
        push.sendInBackground();
    }
}
