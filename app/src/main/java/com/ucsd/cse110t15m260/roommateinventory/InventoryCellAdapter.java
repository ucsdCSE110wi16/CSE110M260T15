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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Model.InventoryItem;

//For each "cell" in the inventory
public class InventoryCellAdapter<T> extends ArrayAdapter<InventoryItem> {

    Button incButton, decButton;
    Context activity;

    InventoryCellAdapter(Context context, ArrayList<InventoryItem> inventoryitems) {
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
        itemCount.setText(getItem(position).getQuantity().intValue() + "");
        Bitmap image = getItem(position).getImage();

        //If there's an image, set it, otherwise set it to cow
        if (image != null)
            imageView.setImageBitmap(image);
        else
            imageView.setImageResource(R.drawable.cow);

        incButton = (Button) customView.findViewById(R.id.incrementButton);
        decButton = (Button) customView.findViewById(R.id.decrementButton);
        final int pos = position;

        incButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(pos).incrementQuantity();
                getItem(pos).saveInBackground();
                notifyDataSetChanged();
            }
        });

        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If there's nothing
                getItem(pos).decrementQuantity();
                getItem(pos).saveInBackground();
                notifyDataSetChanged();
            }
        });

        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddItemActivity.class);
                intent.putExtra("index", position);
                activity.startActivity(intent);
            }
        });

        return customView;
    }
}
