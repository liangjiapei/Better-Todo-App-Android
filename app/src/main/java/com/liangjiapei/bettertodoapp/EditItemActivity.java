package com.liangjiapei.bettertodoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText et_todo;
    Button btn_save;

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        setTitle("Edit Item");

        et_todo = (EditText) findViewById(R.id.et_todo);
        btn_save = (Button) findViewById(R.id.btn_save);

        Intent intent = getIntent();

        String todoText = "";

        if (intent.hasExtra("com.liangjiapei.bettertodoapp.description")) {
            todoText = intent.getStringExtra("com.liangjiapei.bettertodoapp.description");

            et_todo.setText(todoText);
        }

        if (intent.hasExtra("com.liangjiapei.bettertodoapp.pos")) {
            pos = intent.getIntExtra("com.liangjiapei.bettertodoapp.pos", 0);
        }
    }

    public void onSave(View v) {
        Log.d("Save Button Clicked: ", "Tap");

        Intent data = new Intent();

        data.putExtra("com.liangjiapei.bettertodoapp.editedTodoText", et_todo.getText().toString());
        data.putExtra("com.liangjiapei.bettertodoapp.editedPos", pos);

        setResult(RESULT_OK, data);

        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
        builder.setMessage("Do you want to discard this change?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditItemActivity.super.onBackPressed();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("On discard edit: ", "CANCEL");
            }
        }).show();
    }
}
