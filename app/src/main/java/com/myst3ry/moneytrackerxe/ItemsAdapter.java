package com.myst3ry.moneytrackerxe;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> itemsList = new ArrayList<>();

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
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    void clear() {
        itemsList.clear();
    }

    void addAll(List<Item> data) {
        itemsList.addAll(data);
        notifyDataSetChanged();
    }

    void add(Item item) { //for testing only
        itemsList.add(item);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView article;
        private final TextView amount;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.article = (TextView) itemView.findViewById(R.id.item_article);
            this.amount = (TextView) itemView.findViewById(R.id.item_amount);
        }
    }
}



