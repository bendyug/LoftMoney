package com.dbendyug.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class BudgetFragment extends Fragment {

    private static final String PRICE_COLOR = "priceColor";

    public static final int REQUEST_CODE = 100;
    public static final String TITLE_KEY = "name";
    public static final String PRICE_KEY = "price";

    private ItemsAdapter itemsAdapter;
    private Button openAddScreenButton;


    public BudgetFragment() {

    }

    public static BudgetFragment newInstance(int priceColor) {
        BudgetFragment fragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(PRICE_COLOR, priceColor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        itemsAdapter.addItem(new Item("Молоко", 70));
        itemsAdapter.addItem(new Item("Зубная Щётка", 70));
        itemsAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670)); //example of filling

        openAddScreenButton = fragmentView.findViewById(R.id.open_add_screen_button);
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
            Item item;
            if (data != null) {
                item = new Item(data.getStringExtra(TITLE_KEY), Integer.parseInt(data.getStringExtra(PRICE_KEY)));
                itemsAdapter.addItem(item);
            }
        }
    }
}
