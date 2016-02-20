package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import Model.Person;
import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;
import Model.Managers.ApartmentManager;
import Model.Managers.InventoryManager;
import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class InventoryFragment extends Fragment {

    Button incButton, decButton;
    public static Inventory currentInventory;
    public static ListView theListView;

    private ListAdapter inventoryFoodAdapter;

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_inventory, container, false);

        FloatingActionButton b = (FloatingActionButton) view.findViewById(R.id.addfab);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        Apartment apartment = ApartmentManager.apartmentManager.getCurrentApartment();

        Log.i("Current User:", Person.getCurrentPerson().toString());
        currentInventory = apartment.getInventory();

        //getActionBar().setTitle(this.currentInventory.getName());
        ArrayList<InventoryItem> inventoryItems = currentInventory.getItems();


        inventoryFoodAdapter = new InventoryCellAdapter<InventoryItem>(view.getContext(), inventoryItems);
        theListView = (ListView) view.findViewById(R.id.inventoryListView);
        theListView.setAdapter(inventoryFoodAdapter);
        getActivity().setTitle("Apartment Inventory");

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
