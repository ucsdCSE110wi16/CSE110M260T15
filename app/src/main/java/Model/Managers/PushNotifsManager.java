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
    private static final String outOfItem = "Your apartment has run out of %s";
    private static final String replenishedItem = "%s has been restocked.";
    private static final String itemRequest = "%s has requested that you purchase %s";
    private static final String channelPrefix = "CSE110";
    private static PushNotifsManager ourInstance = new PushNotifsManager();
    private String apartmentChannel;
    private String userChannel;

    private PushNotifsManager() {
    }

    public static PushNotifsManager getInstance() {
        return ourInstance;
    }

    public void subscribeToApartment(Apartment apt) {
        this.apartmentChannel = channelPrefix + apt.getObjectId();
        Log.d("Channel", "Subscribing to channel: " + this.apartmentChannel);
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
        this.userChannel = channelPrefix + person.getObjectId();
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
            push.setMessage(String.format(outOfItem, item.getName()));
            push.sendInBackground();
        } catch (Exception e) {
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
            push.setMessage(String.format(replenishedItem, item.getName()));
            push.sendInBackground();
        } catch (Exception e) {
            Log.d("Push Failed", e.getMessage());
        }

    }

    public void sendToUser(Person person, InventoryItem item) {
        //TODO: Display alert to user to notify of success/fail
        if (person == null || item == null)
            return;

        ParsePush push = new ParsePush();
        Log.d("Channel", "Issuing notif to channel: " + person.getObjectId());

        push.setChannel(channelPrefix + person.getObjectId());
        try {
            push.setMessage(String.format(
                    itemRequest,
                    AccountManager.accountManager.getCurrentUser(),
                    item.getName()
            ));

            push.sendInBackground();
        } catch (Exception e) {
            Log.d("Push Failed", e.getMessage());
        }
    }
}
