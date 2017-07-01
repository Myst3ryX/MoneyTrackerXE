package com.myst3ry.moneytrackerxe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myst3ry.moneytrackerxe.api.AddingResult;
import com.myst3ry.moneytrackerxe.api.LSApi;
import com.myst3ry.moneytrackerxe.api.RemovingResult;

import java.util.List;

public class ItemsFragment extends Fragment {

    public static final String ARG_TYPE = "type";

    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;
    private static final int LOADER_REMOVE = 2;

    private ItemsAdapter adapter = new ItemsAdapter();
    private String type;
    private LSApi api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
        items.setAdapter(adapter);

        type = getArguments().getString(ARG_TYPE);
        api = ((LSApp) getActivity().getApplication()).getApi();

        loadItems();
    }

    public void loadItems() {
        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {

            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {

                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            return api.items(type).execute().body();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> data) {

                if (data == null)
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                else {
                    adapter.clear();
                    adapter.addAll(data);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {
            }

        }).forceLoad();
    }

    public void addItem() {
        getLoaderManager().restartLoader(LOADER_ADD, null, new LoaderManager.LoaderCallbacks<AddingResult>() {

            @Override
            public Loader<AddingResult> onCreateLoader(int id, Bundle args) {
                return null;
            }

            @Override
            public void onLoadFinished(Loader<AddingResult> loader, AddingResult data) {
            }

            @Override
            public void onLoaderReset(Loader<AddingResult> loader) {
            }

        });
    }

    public void removeItem() {
        getLoaderManager().restartLoader(LOADER_REMOVE, null, new LoaderManager.LoaderCallbacks<RemovingResult>() {

            @Override
            public Loader<RemovingResult> onCreateLoader(int id, Bundle args) {
                return null;
            }

            @Override
            public void onLoadFinished(Loader<RemovingResult> loader, RemovingResult data) {
            }

            @Override
            public void onLoaderReset(Loader<RemovingResult> loader) {
            }

        });
    }
}
