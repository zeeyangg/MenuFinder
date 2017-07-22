package com.inti.student.menufinder;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);
        TextView checkSelected = (TextView) findViewById(R.id.checkSelected);
        TextView title = (TextView) findViewById(R.id.title);
        TextView price = (TextView) findViewById(R.id.price);

        Bundle itemData = getIntent().getExtras();
        checkSelected.setText(itemData.getString("check"));

        if (itemData.getString("check").equals("Selected")) {
            checkSelected.setBackgroundColor(Color.parseColor("#FF41BF1B"));
        } else {
            checkSelected.setBackgroundColor(Color.parseColor("#F52424"));
        }

        title.setText(itemData.getString("name"));
        price.setText(String.format("RM %.2f", itemData.getDouble("price")));
        thumbnail.setImageResource(itemData.getInt("thumbnail"));
    }
}
