package Model.Managers;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;
import Model.Person;

/**
 * Created by satre on 2/20/16.
 */
public class AccountManager {

    private AccountManager() {}

    /**
     * Singleton Instance of this manager.
     */
    public static AccountManager accountManager = new AccountManager();

    public Person getCurrentUser() {
        return Person.getCurrentPerson();
    }

    /**
     * Convenience method to sign up a new Person.
     */
    public Person signUpPerson(String name, String email, String password, SignUpCallback callback) {
        Person newUser = Person.createnewPerson(name, email, password);
        newUser.signUpInBackground(callback);

        return newUser;
    }

    /**
     * Convenience method to login a Person with the given username and password.
     *
     * @param username
     * @param password
     */
    public void loginPerson(String username, String password, final LogInCallback callback) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException loginError) {
                if (loginError != null) {
                    callback.done(user, loginError);
                    return;
                }
                PushNotifsManager.getInstance().subscribeToUser(Person.getCurrentPerson());
                ApartmentManager.apartmentManager.fetchApartment(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        InventoryManager.inventoryManager.fetchInventory(new GetCallback<Inventory>() {
                            @Override
                            public void done(Inventory object, ParseException e) {
                                callback.done(user, e);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Convenience method to logout a person
     */
    public void logoutPerson(LogOutCallback callback) {
       Person person = AccountManager.accountManager.getCurrentUser();

        if (person != null) {
            person.logOutInBackground(callback);
            PushNotifsManager.getInstance().unsubscribeFromUser();
        }
    }

    /**
     * This method is responsible for fetching all the data when the app is launched.
     */
    public void fetchAllData() {
        final Person person = Person.getCurrentPerson();
        if(person == null) {
            return;
        }

        person.fetchInBackground(new GetCallback<Person>() {
            @Override
            public void done(Person object, ParseException personError) {
                if (personError != null) {
                    Log.e("Fetch Person Info", personError.getLocalizedMessage());
                    return;
                }
                //Return if no apartment
                if(person.hasApartment() == false)
                {
                    return;
                }
                //refresh the apartment info.
                person.getApartment().fetchInBackground(new GetCallback<Apartment>() {
                    @Override
                    public void done(Apartment object, ParseException aptError) {
                        if(aptError != null ) {
                            Log.e("Fetch Apartment", aptError.getLocalizedMessage());
                            return;
                        }
                        //refresh the inventory info.
                        person.getApartment().getInventory().fetchInBackground(new GetCallback<Inventory>() {
                            @Override
                            public void done(Inventory object, ParseException invError) {

                                if(invError != null) {
                                    Log.e("Fetch Inventory", invError.getLocalizedMessage());
                                }

                                person.getApartment().getInventory().fetchInventoryItems(null);
                            }
                        });
                    }
                });

            }
        });
    }
}
