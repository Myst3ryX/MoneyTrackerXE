package com.myst3ry.moneytrackerxe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView addButton = (TextView) findViewById(R.id.add_button);
        final EditText articleName = (EditText) findViewById(R.id.article_name);
        final EditText amountField = (EditText) findViewById(R.id.amount_field);
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
