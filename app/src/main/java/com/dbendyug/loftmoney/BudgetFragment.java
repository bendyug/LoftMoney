package com.dbendyug.loftmoney;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BudgetFragment extends Fragment {

    private static final String PRICE_COLOR = "priceColor";
    private static final String TYPE = "type";

    private static final int REQUEST_CODE = 100;
    private static final String TITLE_KEY = "name";
    private static final String PRICE_KEY = "price";

    private LoftApi loftApi;
    private ItemsAdapter itemsAdapter;

    public BudgetFragment() {
    }

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

        loftApi = ((LoftApp) getActivity().getApplication()).getLoftApi();
    }

    @Override
    public void onStart() {
        super.onStart();

        loadItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);

        RecyclerView recyclerView = fragmentView.findViewById(R.id.recycler_view);

        itemsAdapter = new ItemsAdapter(getArguments().getInt(PRICE_COLOR));

        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Button openAddScreenButton = fragmentView.findViewById(R.id.open_add_screen_button);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), AddItemActivity.class), REQUEST_CODE);
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){

            int price = Integer.parseInt(data.getStringExtra(PRICE_KEY));
            String title = data.getStringExtra(TITLE_KEY);
            String type = getArguments().getString(TYPE);
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
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

    public void loadItems(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(MainActivity.AUTH_TOKEN, "");
        Call<List<Item>> call = loftApi.getItems(getArguments().getString(TYPE), authToken);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {

                itemsAdapter.clear();
                List<Item> itemsList = response.body();
                for (Item item: itemsList) {
                    itemsAdapter.addItem(item);
                }
            }
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }
}

