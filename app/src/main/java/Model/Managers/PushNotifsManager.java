package Model.Managers;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

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
    private static final String replenishedItem = " has been restocked.";

    private PushNotifsManager() {
    }

    public static PushNotifsManager getInstance() {
        return ourInstance;
    }

    public void subscribeToApartment(Apartment apt) {
        this.apartmentChannel = "CSE110" + apt.getObjectId();
        Log.d("Channel","Subscribing to channel: " + this.apartmentChannel);
        ParsePush.subscribeInBackground(this.apartmentChannel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.d("Subscribe error", "Subscription failed: " + e.getMessage());

                return;
            }
        });
    }

    public void unsubscribeFromApartment() {
        ParsePush.unsubscribeInBackground(this.apartmentChannel);
        this.apartmentChannel = null;
    }

    public void subscribeToUser(Person person) {
        this.userChannel = "CSE110" + person.getObjectId();
        ParsePush.subscribeInBackground(this.userChannel, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.d("Subscribe error", "Subscription failed: " + e.getMessage());

                return;
            }
        });
    }

    public void unsubscribeFromUser() {
        ParsePush.unsubscribeInBackground(this.userChannel);
        this.userChannel = null;
    }
    public void sendOutOfItem(InventoryItem item) {
        if (item == null)
            return;

        ParsePush push = new ParsePush();

        Log.d("Channel", "Issuing notif to channel: " + this.apartmentChannel);

        push.setChannel(this.apartmentChannel);
        try {
            push.setMessage(outOfItem + item.getName());
            push.sendInBackground();
        }

        catch(Exception e) {
            Log.d("Push Failed", e.getMessage());
        }
    }

    public void sendReplenishedItem(InventoryItem item) {
        if (item == null)
            return;

        ParsePush push = new ParsePush();
        Log.d("Channel", "Issuing notif to channel: " + this.apartmentChannel);

        push.setChannel(this.apartmentChannel);
        try {
            push.setMessage(item.getName() + replenishedItem);
            push.sendInBackground();
        }

        catch(Exception e) {
            Log.d("Push Failed", e.getMessage());
        }

    }

    public void sendToUser(Person person, String message) {
        if (person == null)
                return;

        ParsePush push = new ParsePush();
        push.setChannel(person.getObjectId());
        push.setMessage(message);
        push.sendInBackground();
    }
}
