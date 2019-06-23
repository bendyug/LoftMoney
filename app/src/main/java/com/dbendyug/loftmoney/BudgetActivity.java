package com.dbendyug.loftmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    public static final String TITLE_KEY = "name";
    public static final String PRICE_KEY = "price";
    private ItemsAdapter itemsAdapter;

    private Button openAddScreenButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        itemsAdapter = new ItemsAdapter();

        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        //itemsAdapter.addItem(new Item("Молоко", 70));
        //itemsAdapter.addItem(new Item("Зубная Щётка", 70));
        //itemsAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670)); //example of filling

        openAddScreenButton = findViewById(R.id.open_add_screen_button);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Item item = new Item(data.getStringExtra(TITLE_KEY), Integer.parseInt(data.getStringExtra(PRICE_KEY)));
            itemsAdapter.addItem(item);
        }
    }
}
