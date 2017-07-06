package com.myst3ry.moneytrackerxe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.myst3ry.moneytrackerxe.api.AddingResult;
import com.myst3ry.moneytrackerxe.api.LSApi;
import com.myst3ry.moneytrackerxe.api.Result;

import java.io.Serializable;
import java.util.List;

public class ItemsFragment extends Fragment {

    public static final String ARG_TYPE = "type";

    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;
    private static final int LOADER_REMOVE = 2;

    private ItemsAdapter adapter = new ItemsAdapter();
    private String type;
    private LSApi api;
    private RecyclerView items;
    private FloatingActionButton addItemButton;
    private SwipeRefreshLayout refresh;

    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.items_menu, menu);
            addItemButton.setVisibility(View.GONE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.trash_can:
                    new AlertDialog.Builder(getContext())
                            .setCancelable(false)
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.deleting_confirm)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int count = adapter.getSelectedItems().size();
                                    for (int i = count - 1; i >= 0; i--)
                                        removeItem(adapter.remove(adapter.getSelectedItems().get(i)));

                                    Toast.makeText(getContext(), getString(R.string.item_removed_successful, count), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {

                                    actionMode.finish();
                                }
                            }).show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelectedItems();
            addItemButton.setVisibility(View.VISIBLE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = (RecyclerView) view.findViewById(R.id.items);
        items.setAdapter(adapter);

        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (actionMode != null) return;
                actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                toggleSelection(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (actionMode != null) toggleSelection(e);
                return super.onSingleTapConfirmed(e);
            }
        });

        items.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        addItemButton = (FloatingActionButton) view.findViewById(R.id.floating_add_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, AddItemActivity.RCODE_ADD_ITEM);
            }
        });

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresher);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        type = getArguments().getString(ARG_TYPE);
        api = ((LSApp) getActivity().getApplication()).getApi();

        loadItems();
    }

    public void toggleSelection(MotionEvent e) {
        adapter.toggleSelection(items.getChildLayoutPosition(items.findChildViewUnder(e.getX(), e.getY())));
        actionMode.setTitle(getString(R.string.selected_count_title, adapter.getSelectedItemsCount()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddItemActivity.RCODE_ADD_ITEM && resultCode == Activity.RESULT_OK) {
            Serializable obj = data.getSerializableExtra(AddItemActivity.RESULT_ITEM);

            if (obj instanceof Item) addItem((Item) obj);
            Toast.makeText(getContext(), R.string.item_added_successful, Toast.LENGTH_SHORT).show();
        }
    }

    // --------------- INTERNET:

    public void loadItems() { //load items from the server
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
                    refresh.setRefreshing(false);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {
            }
        }).forceLoad();
    }

    public void addItem(final Item item) { //add item to the server
        getLoaderManager().restartLoader(LOADER_ADD, null, new LoaderManager.LoaderCallbacks<AddingResult>() {

            @Override
            public Loader<AddingResult> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<AddingResult>(getContext()) {

                    @Override
                    public AddingResult loadInBackground() {
                        try {
                            return api.add(item.getArticle(), item.getAmount(), item.getType()).execute().body();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<AddingResult> loader, AddingResult data) {

                if (data == null)
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                else adapter.addItemToAdapter(item, data.getId());
            }

            @Override
            public void onLoaderReset(Loader<AddingResult> loader) {
            }
        }).forceLoad();
    }

    public void removeItem(final Item item) { //remove item from the server
        getLoaderManager().restartLoader(LOADER_REMOVE, null, new LoaderManager.LoaderCallbacks<Result>() {

            @Override
            public Loader<Result> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Result>(getContext()) {

                    @Override
                    public Result loadInBackground() {
                        try {
                            return api.remove(item.getId()).execute().body();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Result> loader, Result data) {
            }

            @Override
            public void onLoaderReset(Loader<Result> loader) {
            }
        }).forceLoad();
    }
}
