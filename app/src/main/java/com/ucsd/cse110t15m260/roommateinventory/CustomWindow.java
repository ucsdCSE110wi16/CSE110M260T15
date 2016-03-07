package com.ucsd.cse110t15m260.roommateinventory;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


/**
 * Created by saiteja64 on 2/1/16.
 */
public class CustomWindow extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Request for custom title bar
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //set to your layout file
        setContentView(R.layout.activity_inventory);
        //Set the titlebar layout
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_titlebar);

    }
}
