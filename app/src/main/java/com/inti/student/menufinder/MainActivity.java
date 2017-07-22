package com.inti.student.menufinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ItemAdapter adapter;
    private List<Item> itemList;
    private static ArrayList<Item> checkedItem = new ArrayList<>();
    FloatingActionButton floating_confirm;

    StringBuffer sb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(this, itemList);

        Spinner dropdown = (Spinner) findViewById(R.id.dropDown);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.category));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        dropdown.setAdapter(myAdapter);

        floating_confirm = (FloatingActionButton) findViewById(R.id.floating_confirm);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (position != 0) {
                    SetFragment f2 = new SetFragment();
                    pushFragments("SET", f2);
                } else {
                    AlacarteFragment f1 = new AlacarteFragment();
                    pushFragments("ALA_CARTE", f1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        floating_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                double total = 0;
                sb = new StringBuffer();

                for (Item i : checkedItem) {
                    sb.append(i.getItemName());
                    sb.append(" Price: " + String.format("RM %.2f", i.getPrice()*i.getQuantity()));
                    sb.append("\n");

                    total += i.getPrice()*i.getQuantity();
                }

                if (checkedItem.size() > 0) {
                    Intent i = new Intent(MainActivity.this, Cart.class);
                    String amount = String.format("RM %.2f", total);
                    String itemSelected = sb.toString();

                    i.putExtra("prices", "Total: " + amount);
                    i.putExtra("items", itemSelected);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "No item selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        //hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("McDonald's");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onCreate(savedInstanceState);
    }

    public void pushFragments(String tag, Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();


        if (manager.findFragmentByTag(tag) == null) {
            ft.add(R.id.fragment_container, fragment, tag);
        }

        Fragment fragmentAlacarte = manager.findFragmentByTag("ALA_CARTE");
        Fragment fragmentSet = manager.findFragmentByTag("SET");

        // Hide all Fragment
        if (fragmentAlacarte != null) {
            ft.hide(fragmentAlacarte);
        }
        if (fragmentSet != null) {
            ft.hide(fragmentSet);
        }

        // Show  current Fragment
        if (tag == "ALA_CARTE") {
            if (fragmentAlacarte != null) {
                ft.show(fragmentAlacarte);
            }
        }
        if (tag == "SET") {
            if (fragmentSet != null) {
                ft.show(fragmentSet);
            }
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commitAllowingStateLoss();
    }

    public static void updateList(Item i) {
        checkedItem.add(i);
    }

    public static void removeList(Item i) {
        checkedItem.remove(i);
    }

    public static ArrayList<Item> getList() {
        return checkedItem;
    }

    @Override
    public void onBackPressed() {
        if (!checkedItem.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage("Item selected will not be saved. Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            MainActivity.this.finish();
        }
    }
}
