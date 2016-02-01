package com.ucsd.cse110t15m260.roommateinventory;

/**
 * Created by saiteja64 on 1/31/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Model.InventoryItem;


public class InventoryCellAdapter<I> extends ArrayAdapter<InventoryItem>{

    InventoryCellAdapter (Context context, InventoryItem[] inventoryitems){
        super(context, R.layout.custom_row,inventoryitems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater imageInflator = LayoutInflater.from(getContext());
        View customView = imageInflator.inflate(R.layout.custom_row, parent, false);

        String itemNameString = getItem(position).getName();
        TextView itemName = (TextView) customView.findViewById(R.id.inventoryItemName);
        TextView itemCount = (TextView) customView.findViewById(R.id.itemCount);
        ImageView imageView = (ImageView) customView.findViewById(R.id.itemPicture);

        itemName.setText(itemNameString);
        itemCount.setText(getItem(position).getQuantity() + "");
        imageView.setImageBitmap(getItem(position).getImage());
        return customView;
    }
}
