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
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private ItemAdapter adapter;
    private List<Item> itemList;

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_set, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_set);

        itemList = new ArrayList<>();
        adapter = new ItemAdapter(getActivity(), itemList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        //RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(4), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareItems();

        return rootView;
    }

    //Adding items to the menu
    private void prepareItems() {
        int counter = 0;

        int[] covers = new int[]{
                R.drawable.mcd10};

        databaseHelper = new DatabaseHelper(getActivity());
        try {
            databaseHelper.checkAndCopyDatabase();
            databaseHelper.openDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            cursor = databaseHelper.QueryData("SELECT * FROM set_category");

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
