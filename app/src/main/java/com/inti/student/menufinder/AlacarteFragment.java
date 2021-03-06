package com.inti.student.menufinder;


import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlacarteFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private ItemAdapter adapter;
    private List<Item> itemList;

    public AlacarteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_alacarte, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_alacarte);

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(getActivity(), itemList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareItems();

        return rootView;
    }

    // Adding items to the menu for Ala Carte
    private void prepareItems() {
        int counter = 0;

        int[] covers = new int[]{
                R.drawable.mcd1,
                R.drawable.mcd2,
                R.drawable.mcd3,
                R.drawable.mcd4,
                R.drawable.mcd5,
                R.drawable.mcd6,
                R.drawable.mcd7,
                R.drawable.mcd8,
                R.drawable.mcd9};

        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.checkAndCopyDatabase();
            databaseHelper.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            cursor = databaseHelper.QueryData("SELECT * FROM ala_carte");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Item i = new Item(cursor.getString(1), cursor.getDouble(2), covers[counter]);
                    itemList.add(i);
                    counter++;
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        adapter.notifyDataSetChanged();
    }

}
