package com.dbendyug.loftmoney;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AddItemActivity extends AppCompatActivity {

    private EditText titleEdit;
    private EditText priceEdit;
    private Button addButton;

    private String title;
    private String price;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        titleEdit = findViewById(R.id.title_edit_text);
        priceEdit = findViewById(R.id.price_edit_text);
        addButton = findViewById(R.id.add_button);

        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                title = editable.toString();
                changeButtonTextColor();
            }
        });

        priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                price = editable.toString();
                changeButtonTextColor();
            }
        });


    }

    private void changeButtonTextColor(){
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(price)){
        addButton.setTextColor(ContextCompat.getColor(this, R.color.add_button_text_color));
        } else {
            addButton.setTextColor(ContextCompat.getColor(this, R.color.add_button_color_inactive));
        }
    }
}
