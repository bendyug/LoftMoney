package com.dbendyug.loftmoney;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> itemList = new ArrayList<>();

    public static SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int priceColor;

    private ItemAdapterListener itemAdapterListener;

    public ItemsAdapter(int priceColor) {
        this.priceColor = priceColor;
    }


    public void setListener(ItemAdapterListener itemAdapterListener) {
        this.itemAdapterListener = itemAdapterListener;
    }

    public void toggleItem(int position) {
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, !selectedItems.get(position));
        }
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
        holder.bindItem(item, selectedItems.get(position));
        holder.setListener(item, itemAdapterListener, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(final Item item) {
        itemList.add(item);
        notifyItemInserted(itemList.size());
    }

    public void clearItemList() {
        itemList.clear();
        notifyDataSetChanged();
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> selectedItemIds = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (selectedItems.get(i)) {
                selectedItemIds.add(itemList.get(i).getId());
            }
        }
        return selectedItemIds;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView priceView;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            nameView = itemView.findViewById(R.id.item_name);
            priceView = itemView.findViewById(R.id.item_price);
        }

        public void bindItem(final Item item, boolean selected) {
            itemView.setSelected(selected);
            nameView.setText(item.getName());
            priceView.setText(
                    priceView.getContext().getResources().
                            getString(R.string.price_template,
                                    String.valueOf(item.getPrice())));
        }

        public void setListener(final Item item, final ItemAdapterListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item, position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });
        }
    }
}
