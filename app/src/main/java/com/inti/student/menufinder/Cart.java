package com.inti.student.menufinder;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Bundle itemData = getIntent().getExtras();
        String itemSelected = itemData.getString("items");
        String amount = itemData.getString("prices");

        final TextView cartText = (TextView) findViewById(R.id.cartText);
        final TextView amountText = (TextView) findViewById(R.id.amountText);
        cartText.setText(itemSelected);
        amountText.setText(amount);
    }
}
