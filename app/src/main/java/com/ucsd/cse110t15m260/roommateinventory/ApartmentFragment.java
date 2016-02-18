package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApartmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ApartmentFragment extends Fragment {

    List<String> people;
    ArrayAdapter<String> adapter;

    private OnFragmentInteractionListener mListener;

    public ApartmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apartment, container, false);

        Person person = Person.getCurrentPerson();

        people = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, people);


        ((ListView) rootView.findViewById(R.id.aptListView)).setAdapter(adapter);
        final Button leaveApt = (Button) rootView.findViewById(R.id.leave_apt);
        final Button createApt = (Button) rootView.findViewById(R.id.create_apt);
        final Button joinApt = (Button) rootView.findViewById(R.id.join_apt);

        if (person != null && person.hasApartment()) {
            person.getApartment().findMembers(new FindCallback<Person>() {
                @Override
                public void done(List<Person> objects, ParseException e) {
                    if (e == null) {
                        for (Person p: objects) {
                            people.add(p.toString());
                        }
                        Log.d("PEOPLE_LIST", people.toString());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("PEOPLE_LIST", e.toString());
                    }
                }
            });
        };
        //Manage UI for Apartment Page
        if(person.hasApartment())
        {
            leaveApt.setVisibility(View.VISIBLE);
            createApt.setVisibility(View.GONE);
            joinApt.setVisibility(View.GONE);

            leaveApt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Person person = Person.getCurrentPerson();
                    person.leaveApartment(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            joinApt.setVisibility(View.VISIBLE);
                            createApt.setVisibility(View.VISIBLE);
                            leaveApt.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
        else
        {
            leaveApt.setVisibility(View.GONE);
            createApt.setVisibility(View.VISIBLE);
            joinApt.setVisibility(View.VISIBLE);
            joinApt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), JoinApartmentActivity.class);
                    startActivity(intent);
                }
            });
            createApt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CreateApartmentActivity.class);
                    startActivity(intent);
                }
            });
        }
        // Inflate the layout for this fragment
        return rootView;
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
