package com.myst3ry.moneytrackerxe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {

    public static final int RCODE_ADD_ITEM = 66;
    public static final String EXTRA_TYPE = "type";
    public static final String RESULT_ITEM = "item";

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        final TextView addButton = (TextView) findViewById(R.id.add_button);
        final EditText articleName = (EditText) findViewById(R.id.article_name);
        final EditText amountField = (EditText) findViewById(R.id.amount_field);

        type = getIntent().getStringExtra(EXTRA_TYPE);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addButton.setEnabled(!TextUtils.isEmpty(articleName.getText()) && !TextUtils.isEmpty(amountField.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        articleName.addTextChangedListener(watcher);
        amountField.addTextChangedListener(watcher);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adding = new Intent();
                adding.putExtra(RESULT_ITEM, new Item(articleName.getText().toString(), Integer.parseInt(amountField.getText().toString()), type));
                setResult(RESULT_OK, adding);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
