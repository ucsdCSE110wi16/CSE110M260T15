package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import Model.Apartment;
import Model.Managers.AccountManager;
import Model.Managers.ApartmentManager;
import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApartmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ApartmentFragment extends Fragment {

    //People in apartment
    List<String> mPeople;
    //Array for holding ppl in apt
    ArrayAdapter<String> mAdapter;

    //For listener (not used)
    private OnFragmentInteractionListener mListener;

    //Buttons for apartment control
    private Button mLeaveApt, mCreateApt, mJoinApt;
    //Textviews to display apartment info.
    private TextView mAptName, mAptID;

    public ApartmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPeople = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mPeople);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get the fragment_apartment view
        View rootView = inflater.inflate(R.layout.fragment_apartment, container, false);

        //For leave, create, and join buttons
        mLeaveApt = (Button) rootView.findViewById(R.id.leave_apt);
        mCreateApt = (Button) rootView.findViewById(R.id.create_apt);
        mJoinApt = (Button) rootView.findViewById(R.id.join_apt);

        //For apt text
        mAptName = (TextView) rootView.findViewById(R.id.apt_name);
        mAptID = (TextView) rootView.findViewById(R.id.apt_id);

        Person person = Person.getCurrentPerson();
        updateUI(person);

        mJoinApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JoinApartmentActivity.class);
                startActivity(intent);
            }
        });
        mCreateApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateApartmentActivity.class);
                startActivity(intent);
            }
        });

        //Actions after pressing leave apartment
        mLeaveApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "onClick: LEAVE APT");
                final Person person = Person.getCurrentPerson();

                ApartmentManager.apartmentManager.removePersonFromCurrentApartment(person, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mPeople.clear();
                        updateUI(person);
                        Log.d("Test", "LEFT APARTMENT");
                    }
                });
            }
        });

        ((ListView) rootView.findViewById(R.id.aptListView)).setAdapter(mAdapter);

        getActivity().setTitle("My Apartment");
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Person person = AccountManager.accountManager.getCurrentUser();
        Apartment currApt = ApartmentManager.apartmentManager.getCurrentApartment();

        if (person != null && person.hasApartment()) {
            for(Person mate: currApt.getMembers()) {
                mPeople.add(mate.getName());
            }
        }
        updateUI(person);
    }

    void updateUI(Person person)
    {
        Log.d("Test", "UPDATING UI");
        if(person.hasApartment())
        {
            mAptID.setText("ID: " + (person.hasApartment() ? person.getApartment().getObjectId() : null));
            mAptName.setText("Apartment Name: " + person.getApartment().getName());

            mLeaveApt.setVisibility(View.VISIBLE);
            mCreateApt.setVisibility(View.GONE);
            mJoinApt.setVisibility(View.GONE);
        }
        else
        {
            mAptID.setText("No Apartment");
            mAptName.setText("Click the buttons below to create/join an apartment!");

            mLeaveApt.setVisibility(View.GONE);
            mCreateApt.setVisibility(View.VISIBLE);
            mJoinApt.setVisibility(View.VISIBLE);
        }
        //Update apartment people list
        mAdapter.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
