package com.myst3ry.moneytrackerxe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);
        final RecyclerView items = (RecyclerView) findViewById(R.id.items);
        items.setAdapter(new ItemsAdapter());
    }


    private class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        final private List<Item> itemsList = new ArrayList<>();

        private ItemsAdapter() { //fake stub of items

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
            holder.amount.setText(String.format(getString(R.string.rub_sign_format), currentItem.getAmount()));
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView article;
        private final TextView amount;

        private ItemViewHolder(View itemView) {
            super(itemView);
            this.article = (TextView) itemView.findViewById(R.id.item_article);
            this.amount = (TextView) itemView.findViewById(R.id.item_amount);
        }
    }
}
