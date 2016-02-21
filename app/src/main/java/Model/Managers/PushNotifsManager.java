package Model.Managers;

import com.parse.ParsePush;

import Model.Apartment;
import Model.InventoryItem;
import Model.Person;

/**
 * Created by Isaac on 2/13/2016.
 */
public class PushNotifsManager {
    private static PushNotifsManager ourInstance = new PushNotifsManager();

    private String apartmentChannel;
    private String userChannel;

    private static final String outOfItem = "Your apartment has run out of ";

    private PushNotifsManager() {
    }

    public static PushNotifsManager getInstance() {
        return ourInstance;
    }

    public void subscribeToApartment(Apartment apt) {
        this.apartmentChannel = apt.getObjectId();
        ParsePush.subscribeInBackground(apt.getObjectId());
    }

    public void unsubscribeFromApartment() {
        ParsePush.unsubscribeInBackground(this.apartmentChannel);
        this.apartmentChannel = null;
    }

    public void subscribeToUser(Person person) {
        this.userChannel = person.getObjectId();
        ParsePush.subscribeInBackground(person.getObjectId());
    }

    public void unsubscribeFromUser() {
        ParsePush.unsubscribeInBackground(this.userChannel);
        this.userChannel = null;
    }
    public void sendOutOfItem(InventoryItem item) {
        if (item == null)
            return;

        ParsePush push = new ParsePush();
        push.setChannel(this.apartmentChannel);
        push.setMessage(outOfItem + item.getName());
        push.sendInBackground();
    }

    public void sendPushToUser(Person person, String message) {
        if (person == null)
                return;

        ParsePush push = new ParsePush();
        push.setChannel(person.getObjectId());
        push.setMessage(message);
        push.sendInBackground();
    }
}
