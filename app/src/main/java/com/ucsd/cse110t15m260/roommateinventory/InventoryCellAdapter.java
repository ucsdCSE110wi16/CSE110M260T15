package com.ucsd.cse110t15m260.roommateinventory;

/**
 * Created by saiteja64 on 1/31/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Model.InventoryItem;


public class InventoryCellAdapter<T> extends ArrayAdapter<InventoryItem> {

    Button incButton, decButton;

    InventoryCellAdapter (Context context, ArrayList<InventoryItem> inventoryitems){
        super(context, R.layout.custom_row,inventoryitems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater imageInflator = LayoutInflater.from(getContext());
        View customView = imageInflator.inflate(R.layout.custom_row, parent, false);

        String itemNameString = getItem(position).getName();
        TextView itemName = (TextView) customView.findViewById(R.id.itemName);
        TextView itemCount = (TextView) customView.findViewById(R.id.itemCount);
        //ImageView imageView = (ImageView) customView.findViewById(R.id.itemPicture);

        itemName.setText(itemNameString);
        itemCount.setText(getItem(position).getQuantity() + "");

        incButton = (Button) customView.findViewById(R.id.incrementButton);
        decButton = (Button) customView.findViewById(R.id.decrementButton);
        final int pos = position;
        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getItem(pos).setQuantity((int) getItem(pos).getQuantity() + 1);
                getItem(pos).saveInBackground();
                notifyDataSetChanged();

            }
        });
        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(pos).setQuantity((int) getItem(pos).getQuantity() - 1);
                getItem(pos).saveInBackground();
                notifyDataSetChanged();
            }
        });


        //if(getItem(position).getImage() != null)
            //imageView.setImageBitmap(getItem(position).getImage());
        //else
            //imageView.setImageResource(R.drawable.carrot);
        return customView;
    }
}
