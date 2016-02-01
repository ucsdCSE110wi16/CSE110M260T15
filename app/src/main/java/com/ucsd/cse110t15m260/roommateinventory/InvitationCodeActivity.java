package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.parse.ParseObject;

import Model.Person;

public class InvitationCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_code);

        Person person = Person.getCurrentPerson();

        ParseObject apartment = person.getApartment();

        TextView apartmentInvitationId = (TextView) findViewById(R.id.apartment_invitation_id);

        Log.d("InvitationCode", "Invitation Code is: " + apartment.getObjectId());
        apartmentInvitationId.setText(apartment.getObjectId());

    }
}
