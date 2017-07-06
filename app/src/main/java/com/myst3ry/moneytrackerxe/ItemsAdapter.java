package com.myst3ry.moneytrackerxe;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> itemsList = new ArrayList<>();
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        final Item currentItem = itemsList.get(position);
        Resources res = holder.itemView.getContext().getResources();

        holder.article.setText(currentItem.getArticle());
        holder.amount.setText(String.format(res.getString(R.string.rub_sign_format), currentItem.getAmount()));
        holder.itemContainer.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    int getSelectedItemsCount() {
        return selectedItems.size();
    }

    List<Integer> getSelectedItems() {
        List<Integer> selectedList = new ArrayList<>(getSelectedItemsCount());
        for (int i = 0; i < getSelectedItemsCount(); i++) {
            selectedList.add(selectedItems.keyAt(i));
        }
        return selectedList;
    }

    void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) selectedItems.delete(pos);
        else selectedItems.put(pos, true);
        notifyItemChanged(pos);
    }

    void clear() {
        itemsList.clear();
    }

    void clearSelectedItems() {
        for (Integer position : getSelectedItems()) {
            selectedItems.delete(position);
        }
        notifyDataSetChanged();
    }

    Item remove(int pos) {
        final Item item = itemsList.remove(pos);
        notifyItemRemoved(pos);
        return item;
    }

    void addAll(List<Item> data) {
        itemsList.addAll(data);
        notifyDataSetChanged();
    }

    void addItemToAdapter(Item item, int id) {
        item.setId(id);
        itemsList.add(0, item);
        notifyItemInserted(0);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView article;
        private final TextView amount;
        private final View itemContainer;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.article = (TextView) itemView.findViewById(R.id.item_article);
            this.amount = (TextView) itemView.findViewById(R.id.item_amount);
            this.itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}



