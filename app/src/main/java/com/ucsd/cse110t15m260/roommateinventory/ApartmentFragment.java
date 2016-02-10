package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;

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
