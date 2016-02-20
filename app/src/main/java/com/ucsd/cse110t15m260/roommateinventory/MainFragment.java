package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Context;
import android.media.AudioRecord;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        updateInfo(rootView);
        return rootView;
    }

    private View updateInfo(View rootView) {

        TextView text = (TextView) rootView.findViewById(R.id.textview_welcome);

        Person person = Person.getCurrentPerson();

        Log.d("ok","ONCREATEVIEW MAINFRAGMENT");

        if(person != null)
        {
            Log.d("ok","USER LOGGED IN");
            text.setText( "Welcome, " + person.getString("name") + "!\n" +
                    "Your User ID is: " + person.getObjectId() + "\n" +
                    "Your Apartment is: " + (person.hasApartment() ? person.getApartment().getObjectId() : null));
        }
        else
        {
            Log.d("ok","NO USER");
            text.setText("No User Logged In");
        }
        getActivity().setTitle("Home Page");
        return rootView;
    }


    //@Override
    //TODO ONRESUME

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
