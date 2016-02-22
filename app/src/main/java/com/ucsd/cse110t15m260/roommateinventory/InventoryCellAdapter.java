package com.ucsd.cse110t15m260.roommateinventory;

/**
 * Created by saiteja64 on 1/31/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

import Model.InventoryItem;

import static com.ucsd.cse110t15m260.roommateinventory.R.drawable.cow;


public class InventoryCellAdapter<T> extends ArrayAdapter<InventoryItem> {

    Button incButton, decButton;
    Context activity;

    InventoryCellAdapter (Context context, ArrayList<InventoryItem> inventoryitems) {
        super(context, R.layout.custom_row, inventoryitems);
        activity = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater imageInflator = LayoutInflater.from(getContext());
        View customView = imageInflator.inflate(R.layout.custom_row, parent, false);

        String itemNameString = getItem(position).getName();
        TextView itemName = (TextView) customView.findViewById(R.id.itemName);
        TextView itemCount = (TextView) customView.findViewById(R.id.itemCount);
        ImageView imageView = (ImageView) customView.findViewById(R.id.itemPicture);

        itemName.setText(itemNameString);
        itemCount.setText(getItem(position).getQuantity() + "");
        Bitmap image = getItem(position).getImage();
        if(image != null)
            imageView.setImageBitmap(image);
        else
            imageView.setImageResource(R.drawable.cow);

        incButton = (Button) customView.findViewById(R.id.incrementButton);
        decButton = (Button) customView.findViewById(R.id.decrementButton);
        final int pos = position;
        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getItem(pos).increment();
                getItem(pos).saveInBackground();
                notifyDataSetChanged();

            }
        });
        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(pos).getQuantity().intValue() == 0) {
                    return;
                }

                getItem(pos).decrement();
                getItem(pos).saveInBackground();
                notifyDataSetChanged();
            }
        });

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                intent.putExtra("index",position);
                activity.startActivity(intent);
            }
        });

        return customView;
    }
}
