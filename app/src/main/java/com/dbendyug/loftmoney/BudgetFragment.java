package com.dbendyug.loftmoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BudgetFragment extends Fragment implements ItemAdapterListener, ActionMode.Callback {

    private static final String PRICE_COLOR = "priceColor";
    private static final String TYPE = "type";

    public static final int REQUEST_CODE = 100;
    private static final String TITLE_KEY = "name";
    private static final String PRICE_KEY = "price";

    private LoftApi loftApi;
    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static ActionMode actionMode;


    public static BudgetFragment newInstance(FragmentType fragmentType) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, fragmentType.getPriceColor());
        args.putString(TYPE, fragmentType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loftApi = ((LoftApp) Objects.requireNonNull(getActivity()).getApplication()).getLoftApi();
    }

    @Override
    public void onStart() {
        super.onStart();

        loadItems();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);

        swipeRefreshLayout = fragmentView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        itemsAdapter = new ItemsAdapter(Objects.requireNonNull(getArguments()).getInt(PRICE_COLOR));
        itemsAdapter.setListener(this);

        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            int price = Integer.parseInt(data.getStringExtra(PRICE_KEY));
            String title = data.getStringExtra(TITLE_KEY);
            String type = Objects.requireNonNull(getArguments()).getString(TYPE);
            SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            String authToken = sharedPreferences.getString(MainActivity.AUTH_TOKEN, "");
            Call<UserId> call = loftApi.addItems(new AddItemRequest(price, title, type), authToken);
            call.enqueue(new Callback<UserId>() {
                @Override
                public void onResponse(Call<UserId> call, Response<UserId> response) {
                    loadItems();
                }

                @Override
                public void onFailure(Call<UserId> call, Throwable t) {

                }
            });
        }
    }

    public void loadItems() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(MainActivity.AUTH_TOKEN, "");
        Call<List<Item>> call = loftApi.getItems(Objects.requireNonNull(getArguments()).getString(TYPE), authToken);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                swipeRefreshLayout.setRefreshing(false);
                itemsAdapter.clearItemList();
                List<Item> itemsList = response.body();

                Comparator<Item> compareByDate = new Comparator<Item>() {
                    @Override
                    public int compare(Item item, Item t1) {
                        return item.getCreatedAt().compareTo(t1.getCreatedAt());
                    }
                };
                Collections.sort(Objects.requireNonNull(itemsList), compareByDate);

                for (Item item : itemsList) {
                    itemsAdapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(Item item, int position) {
        if (ItemsAdapter.selectedItems.size() > 0) {
            itemsAdapter.toggleItem(position);
            itemsAdapter.notifyDataSetChanged();
            actionMode.setTitle(getResources().getString(R.string.items_selected_count) + ItemsAdapter.selectedItems.size());
        }
        if (ItemsAdapter.selectedItems.size() == 0 && actionMode != null) {
            actionMode.setTitle(null);
            actionMode.finish();
        }
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        if (ItemsAdapter.selectedItems.size() == 0) {
            itemsAdapter.toggleItem(position);
            itemsAdapter.notifyDataSetChanged();
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).startSupportActionMode(this);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.item_menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_icon) {
            showDialog();
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        itemsAdapter.clearSelectedItems();
        itemsAdapter.notifyDataSetChanged();
        actionMode = null;
    }

    private void showDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeItems();
                        if (actionMode != null) {
                            actionMode.finish();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void removeItems() {
        List<Integer> selectedItemIds = itemsAdapter.getSelectedItemIds();
        for (int id : selectedItemIds) {
            SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            String authToken = sharedPreferences.getString(MainActivity.AUTH_TOKEN, "");
            Call<UserId> call = loftApi.deleteItem(id, authToken);
            call.enqueue(new Callback<UserId>() {
                @Override
                public void onResponse(Call<UserId> call, Response<UserId> response) {
                    loadItems();
                    itemsAdapter.clearSelectedItems();
                }

                @Override
                public void onFailure(Call<UserId> call, Throwable t) {

                }
            });
        }
    }
}

