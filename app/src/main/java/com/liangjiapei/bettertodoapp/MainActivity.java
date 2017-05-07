package com.liangjiapei.bettertodoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoAdapter.TodoAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private TodoAdapter mTodoAdapter;

    private final int EDIT_ITEM = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_todo_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(false);

        mTodoAdapter = new TodoAdapter(this);

        mRecyclerView.setAdapter(mTodoAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getLayoutPosition();
                Log.d("Swiped: ", "" + position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to delete this todo?");

                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("On Delete: ", "Delete");
                        mTodoAdapter.deleteTodoItem(position);
                        writeToFile();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("On CANCEL Delete: ", "CANCEL");
                        mTodoAdapter.notifyItemChanged(position);
                    }
                }).show();



            }

        }).attachToRecyclerView(mRecyclerView);

        readFromFile();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            mTodoAdapter.deleteAllTodos();
            writeToFile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v) {
        // Get editText
        EditText newTodoEditText = (EditText) findViewById(R.id.et_new_todo);
        // Get text from editText
        String description = newTodoEditText.getText().toString();

        TodoItem newTodo = new TodoItem(description);

        Log.d("ADD TODO:", newTodo.getDescription());

        // add new item to adapter
        mTodoAdapter.addTodoItem(newTodo);
        newTodoEditText.setText("");

        writeToFile();

    }

    protected void writeToFile() {
        try {
            FileOutputStream fos = openFileOutput("todos", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mTodoAdapter.getmTodos());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void readFromFile() {
        try {
            FileInputStream fis = openFileInput("todos");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<TodoItem> todos = (ArrayList<TodoItem>) ois.readObject();
            ois.close();
            mTodoAdapter.setTodoData(todos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(int position) {
        Log.d("Tap viewHolder: ", "Tap");

        Context context = MainActivity.this;
        Intent intent = new Intent(context, EditItemActivity.class);

        TodoItem todo = mTodoAdapter.getmTodos().get(position);

        intent.putExtra("com.liangjiapei.bettertodoapp.description", todo.getDescription());
        intent.putExtra("com.liangjiapei.bettertodoapp.pos", position);
        startActivityForResult(intent, EDIT_ITEM);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM) {
            int pos = data.getIntExtra("com.liangjiapei.bettertodoapp.editedPos", 0);
            String editedTodoText = data.getStringExtra("com.liangjiapei.bettertodoapp.editedTodoText");

            ArrayList<TodoItem> newTodos = mTodoAdapter.getmTodos();
            TodoItem editedTodo = new TodoItem(editedTodoText);
            newTodos.set(pos, editedTodo);

            mTodoAdapter.setTodoData(newTodos);

            writeToFile();
        }
    }

    public void onCheckboxClicked(View v) {
        // Is the view now checked?
        boolean checked = ((CheckBox) v).isChecked();

        Log.d("Checkbox clicked: ", "" + checked);
    }






}
