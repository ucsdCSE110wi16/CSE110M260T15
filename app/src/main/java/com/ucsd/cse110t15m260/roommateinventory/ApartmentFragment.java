package com.ucsd.cse110t15m260.roommateinventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import Model.Apartment;
import Model.Managers.AccountManager;
import Model.Managers.ApartmentManager;
import Model.InventoryItem;
import Model.Managers.PushNotifsManager;

import Model.Managers.InventoryManager;
import Model.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApartmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ApartmentFragment extends Fragment {

    List<String> mPeople;
    ArrayAdapter<String> mAdapter;

    private OnFragmentInteractionListener mListener;
    private static final int REQUEST_ITEM_CODE = 5;

    //Buttons for apartment control
    private Button mLeaveApt, mCreateApt, mJoinApt;
    //Textviews to display apartment info.
    private TextView mAptName, mAptID;
    private ListView mAptMates;

    /**
     * Used to signify the position of the selected person in the list.
     */
    int personPosition = -1;
    public ApartmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPeople = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                mPeople
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apartment, container, false);

        mLeaveApt = (Button) rootView.findViewById(R.id.leave_apt);
        mCreateApt = (Button) rootView.findViewById(R.id.create_apt);
        mJoinApt = (Button) rootView.findViewById(R.id.join_apt);
        mAptName = (TextView) rootView.findViewById(R.id.apt_name);
        mAptID = (TextView) rootView.findViewById(R.id.apt_id);
        mAptMates = (ListView) rootView.findViewById(R.id.aptListView);

        Person person = Person.getCurrentPerson();
        if(person.hasApartment()) {
            mAptID.setText("ID: " + (person.hasApartment() ? person.getApartment().getObjectId() : null));
            mAptName.setText("Apartment Name: " + person.getApartment().getName());
        }
        else {
            mAptID.setText("No Apartment");
            mAptName.setText("Click the buttons below to create/join an apartment!");
        }

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

        mLeaveApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", "onClick: LEAVE APT");
                final Person person = Person.getCurrentPerson();

                ApartmentManager.apartmentManager.removePersonFromCurrentApartment(person, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mPeople.clear();
                        mAdapter.notifyDataSetChanged();

                        mJoinApt.setVisibility(View.VISIBLE);
                        mCreateApt.setVisibility(View.VISIBLE);
                        mLeaveApt.setVisibility(View.GONE);
                    }
                });
            }
        });

        mAptMates.setAdapter(mAdapter);
        mAptMates.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public final String [] options = {"Select Item", "Create Item"};
            public final String dialogTitle = "Request item from user";

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String aptMate = (String) mAdapter.getItem(position);
                personPosition = position;

                ListAdapter optionsListAdapter = new ArrayAdapter<>(
                        view.getContext(),
                        android.R.layout.simple_selectable_list_item,
                        options
                );

                final Person person = (Person) ApartmentManager
                        .apartmentManager
                        .getCurrentApartment()
                        .getMembers()
                        .get(position);

                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());

                b.setTitle(dialogTitle);
                b.setSingleChoiceItems(optionsListAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context c = ((Dialog) dialog).getContext();

                        switch (which) {
                            case 1:
                                Intent i = new Intent(c, AddItemActivity.class);
                                startActivityForResult(i, REQUEST_ITEM_CODE);
                                break;

                            case 0:
                                AlertDialog.Builder b = new AlertDialog.Builder(c);

                                final ListAdapter itemAdapter = new ArrayAdapter<InventoryItem>(
                                        c,
                                        android.R.layout.simple_selectable_list_item,
                                        InventoryManager.inventoryManager.getInventory().getItems()
                                );

                                if (itemAdapter.isEmpty()) {
                                    b.setMessage("Your inventory has no items");
                                    b.show();
                                    break;
                                }

                                b.setTitle(dialogTitle);
                                b.setAdapter(itemAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        InventoryItem item
                                                = (InventoryItem) itemAdapter.getItem(which);

                                        PushNotifsManager
                                                .getInstance()
                                                .sendToUser(
                                                        person,
                                                        item
                                                );

                                        showCompletedNotif(person);

                                        dialog.dismiss();
                                    }
                                });

                                b.show();
                                break;
                            default:
                                throw new IllegalStateException();
                        }

                        dialog.dismiss();
                    }
                });

                b.show();
            }
        });

        getActivity().setTitle("My Apartment");
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_ITEM_CODE == requestCode) {
            if(resultCode == AddItemActivity.RESULT_OK); {
                //get the item
                String itemId = (String) data.getCharSequenceExtra("item");
                InventoryItem item = InventoryItem.getInventoryItemById(itemId);

                if (item == null) {
                    Log.d("Failed to send notif", "Item creation failed");
                    return;
                }

                Person person = ApartmentManager.apartmentManager.getCurrentApartment().getMembers().get(personPosition);
                PushNotifsManager.getInstance().sendToUser(person, item);
                showCompletedNotif(person);
            }
        }
    }

    public void showCompletedNotif(Person person) {
        Toast.makeText(
                this.getContext(),
                "Notification sent to " + person.getName(),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        Person person = Person.getCurrentPerson();
        Apartment currApt = person.getApartment();
        mPeople.clear();
        if (person != null && person.hasApartment()) {
            for(Person mate: currApt.getMembers()) {
                mPeople.add(mate.getName());
            }

            updateUI(person);
        }
    }

    void updateUI(Person person) {
        Apartment currApt = ApartmentManager.apartmentManager.getCurrentApartment();
        currApt.fetchMembersOfApartment(null);
        Log.d("Test", "UPDATING UI");
            person.getApartment().findMembers(new FindCallback<Person>() {
                @Override
                public void done(List<Person> objects, ParseException e) {
                    if (e == null) {
                        for (Person p : objects) {
                            mPeople.add(p.toString());
                        }
                        Log.d("PEOPLE_LIST", mPeople.toString());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("PEOPLE_LIST", e.toString());
                    }
                    Log.d("PEOPLE_LIST", mPeople.toString());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.d("PEOPLE_LIST", e.toString());
                }
            }
        });
        //Manage UI for Apartment Page
        if(person.hasApartment())
        {
            mLeaveApt.setVisibility(View.VISIBLE);
            mCreateApt.setVisibility(View.GONE);
            mJoinApt.setVisibility(View.GONE);
            mAptMates.setVisibility(View.VISIBLE);
        }
        else
        {
            mLeaveApt.setVisibility(View.GONE);
            mCreateApt.setVisibility(View.VISIBLE);
            mJoinApt.setVisibility(View.VISIBLE);
            mAptMates.setVisibility(View.GONE);
        }
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
