package com.dbendyug.loftmoney;

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

    private Button openAddScreenButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        final ItemsAdapter itemsAdapter = new ItemsAdapter();

        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        itemsAdapter.addItem(new Item("Молоко", 70));
        itemsAdapter.addItem(new Item("Зубная Щётка", 70));
        itemsAdapter.addItem(new Item("Сковородка с антипригарным покрытием", 1670));

        openAddScreenButton = findViewById(R.id.open_add_screen_button);
        openAddScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BudgetActivity.this, AddItemActivity.class ));
            }
        });
    }
}
