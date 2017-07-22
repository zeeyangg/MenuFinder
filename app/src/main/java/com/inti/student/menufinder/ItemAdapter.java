package com.inti.student.menufinder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, price, quantityCount;
        public ImageView thumbnail;
        public CheckBox checkBox;
        public Button plus, minus;

        public ItemClickListener itemClickListener;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            plus = (Button) view.findViewById(R.id.plusQuantity);
            minus = (Button) view.findViewById(R.id.minusQuantity);
            quantityCount = (TextView) view.findViewById(R.id.textQuantity);

            checkBox.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClick) {

            this.itemClickListener = itemClick;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }

    public ItemAdapter(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Item item = itemList.get(position);
        holder.title.setText(item.getItemName());
        holder.price.setText(String.format("RM %.2f", item.getPrice()));
        holder.thumbnail.setImageResource(item.getThumbnail());

        // Card clicked listener
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "Not Selected";

                for (Item i : MainActivity.getList()) {
                    text = "Not Selected";
                    if (item.getItemName().equals(i.getItemName())) {
                        text = "Selected";
                        break;
                    }
                }

                Intent i = new Intent(mContext, ItemDetails.class);
                i.putExtra("check", text);
                i.putExtra("name", item.getItemName());
                i.putExtra("price", item.getPrice());
                i.putExtra("thumbnail", item.getThumbnail());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox checkBox = (CheckBox) v;

                //Check if item checked
                if (checkBox.isChecked()) {
                    if (item.getQuantity() == 0) {
                        item.setQuantity(1);
                    }
                    Toast.makeText(mContext, itemList.get(pos).getItemName() + " selected", Toast.LENGTH_SHORT).show();
                    MainActivity.updateList(itemList.get(pos));
                } else if (!checkBox.isChecked()) {
                    holder.price.setText(String.format("RM %.2f", item.getPrice()));
                    item.setQuantity(0);
                    MainActivity.removeList(itemList.get(pos));
                }
                // Set quantity after checking the checkbox
                holder.quantityCount.setText(String.valueOf(item.getQuantity()));
            }
        });

        // Increasing item quantity when user press the '+' button
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getQuantity() == 0) {
                    holder.checkBox.setChecked(true);
                    MainActivity.updateList(item);
                }
                item.setQuantity(item.getQuantity()+1);
                holder.quantityCount.setText(String.valueOf(item.getQuantity()));
                holder.price.setText(String.format("RM %.2f", item.getQuantity() * item.getPrice()));
            }
        });

        // Decreasing item quantity when user press the '-' button
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.getQuantity() > 0) {
                    item.setQuantity(item.getQuantity() - 1);

                    if (item.getQuantity() == 0) {
                        holder.price.setText(String.format("RM %.2f", item.getPrice()));
                        holder.checkBox.setChecked(false);
                        MainActivity.removeList(item);
                    } else {
                        holder.price.setText(String.format("RM %.2f", item.getQuantity() * item.getPrice()));
                    }
                }
                holder.quantityCount.setText(String.valueOf(item.getQuantity()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
