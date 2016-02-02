package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.BaseAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

import Model.Inventory;
import Model.InventoryItem;
import Model.Apartment;
import Model.Managers.ApartmentManager;
import Model.Managers.InventoryManager;

public class InventoryActivity extends AppCompatActivity {

    public static Inventory currentInventory;
    public static ListView theListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Request for custom title bar
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //set to your layout file
        setContentView(R.layout.activity_inventory);
        //Set the titlebar layout
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_titlebar);
        super.onCreate(savedInstanceState);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
         //   }
       // });

        InventoryManager.inventoryManager.fetchInventory(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> objects, ParseException e) {
                if (e != null) {
                    System.out.println("error" + e);
                }
            }
        });

        Apartment sampleApartment = (Apartment )ParseUser.getCurrentUser().get("apartment");
        this.currentInventory = (Inventory)sampleApartment.getInventory();

        //getActionBar().setTitle(this.currentInventory.getName());
        InventoryItem[] inventoryItems = new InventoryItem[currentInventory.items.size()];


        for(int i = 0; i < this.currentInventory.items.size();i++)
        {
            inventoryItems[i] = (InventoryItem) this.currentInventory.items.get(i);
        }


        ListAdapter inventoryFoodAdapter = new InventoryCellAdapter<InventoryItem>(this, inventoryItems);
        theListView = (ListView) findViewById(R.id.inventoryListView);
        theListView.setAdapter(inventoryFoodAdapter);

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
