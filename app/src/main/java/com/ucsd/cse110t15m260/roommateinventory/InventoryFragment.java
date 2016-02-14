package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;
import Model.Managers.InventoryManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InventoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static Inventory currentInventory;
    public static ListView theListView;

    private OnFragmentInteractionListener mListener;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        InventoryManager.inventoryManager.fetchInventory(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> objects, ParseException e) {
                if (e != null) {
                    System.out.println("error" + e);
                }
            }
        });

        Apartment sampleApartment = (Apartment ) ParseUser.getCurrentUser().get("apartment");
        this.currentInventory = (Inventory)sampleApartment.getInventory();

        //getActionBar().setTitle(this.currentInventory.getName());
        InventoryItem[] inventoryItems = new InventoryItem[currentInventory.items.size()];


        for(int i = 0; i < this.currentInventory.items.size();i++)
        {
            inventoryItems[i] = (InventoryItem) this.currentInventory.items.get(i);
        }


        ListAdapter inventoryFoodAdapter = new InventoryCellAdapter<InventoryItem>(getView().getContext(), inventoryItems);
        theListView = (ListView) getView().findViewById(R.id.inventoryListView);
        theListView.setAdapter(inventoryFoodAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_inventory, container, false);
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


    public void incrementInventoryItem(View v)
    {
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        int position = theListView.getPositionForView((View)v.getParent());
        this.currentInventory.items.get(position).setQuantity((int) this.currentInventory.items.get(position).getQuantity() + 1);
        this.currentInventory.items.get(position).saveInBackground();
        theListView.refreshDrawableState();
        ((BaseAdapter)theListView.getAdapter()).notifyDataSetChanged();

    }

    public void decrementInventoryItem(View v)
    {
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        int position = theListView.getPositionForView((View)v.getParent());
        this.currentInventory.items.get(position).setQuantity((int) this.currentInventory.items.get(position).getQuantity() - 1);
        this.currentInventory.items.get(position).saveInBackground();
        theListView.refreshDrawableState();
        ((BaseAdapter)theListView.getAdapter()).notifyDataSetChanged();
    }

    public void addInventoryItem(InventoryItem item)
    {
        this.currentInventory.items.add(item);
        theListView.refreshDrawableState();
        ((BaseAdapter)theListView.getAdapter()).notifyDataSetChanged();
    }

}
