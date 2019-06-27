package com.dbendyug.loftmoney;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> itemList = new ArrayList<>();
    private int priceColor;

    public ItemsAdapter(int priceColor) {
        this.priceColor = priceColor;
    }

    @NonNull
    @Override
    public ItemsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_view, null);
        TextView priceView = itemView.findViewById(R.id.item_price);
        priceView.setTextColor(itemView.getContext().getResources().getColor(priceColor));
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.ItemViewHolder holder, int position) {
        final Item item = itemList.get(position);
        holder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem (final Item item){
        itemList.add(item);
        notifyItemInserted(itemList.size());
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView priceView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.item_name);
            priceView = itemView.findViewById(R.id.item_price);
        }

        public void bindItem(final Item item){
            nameView.setText(item.getName());
            priceView.setText(
                    priceView.getContext().getResources().
                            getString(R.string.price_template,
                                    String.valueOf(item.getPrice())));
        }
    }
}
