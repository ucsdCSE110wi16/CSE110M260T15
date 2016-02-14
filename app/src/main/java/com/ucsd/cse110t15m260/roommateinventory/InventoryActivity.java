package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import Model.Inventory;
import Model.InventoryItem;
import Model.Managers.ApartmentManager;
import Model.Managers.InventoryManager;

public class InventoryActivity extends AbstractActivity {

    public static Inventory currentInventory;
    public static ListView theListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        this.currentInventory = ApartmentManager.apartmentManager.getCurrentApartment().getInventory();

        getActionBar().setTitle(this.currentInventory.getName());
        String[] items = new String[currentInventory.items.size()];


        InventoryItem[] inventoryItems = (InventoryItem[]) this.currentInventory.items.toArray();

        ListAdapter inventoryFoodAdapter = new InventoryCellAdapter<InventoryItem>(this, inventoryItems);
        theListView = (ListView) findViewById(R.id.inventoryListView);
        theListView.setAdapter(inventoryFoodAdapter);

    }

    public void incrementInventoryItem(View v)
    {
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        int position = theListView.getPositionForView((View)v.getParent());
        this.currentInventory.items.get(position).setQuantity((int)this.currentInventory.items.get(position).getQuantity() + 1);

        theListView.refreshDrawableState();


    }

    public void decrementInventoryItem(View v)
    {
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        int position = theListView.getPositionForView((View)v.getParent());
        this.currentInventory.items.get(position).setQuantity((int)this.currentInventory.items.get(position).getQuantity() + 1);

        theListView.refreshDrawableState();
    }

    public void addInventoryItem(InventoryItem item)
    {
        this.currentInventory.items.add(item);
        theListView.refreshDrawableState();
    }



}
