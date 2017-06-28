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

    final private List<Item> itemsList = new ArrayList<>();

    ItemsAdapter() { //fake stub of items

        itemsList.add(new Item("Молоко волшебной коровы", 125));
        itemsList.add(new Item("Зубная фея КолКейт", 1500));
        itemsList.add(new Item("Сковородка с пригарно-угарным покрытием", 150000));
        itemsList.add(new Item("Шоколадка из ада, возможно немножко б/у", 75));

        for (int i = 17; i < 67; i++) {
            itemsList.add(new Item("Какая-то ненужная шмотка...", i + 5155));
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Item currentItem = itemsList.get(position);
        holder.article.setText(currentItem.getArticle());
        Resources res = holder.itemView.getContext().getResources();
        holder.amount.setText(String.format(res.getString(R.string.rub_sign_format), currentItem.getAmount()));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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



