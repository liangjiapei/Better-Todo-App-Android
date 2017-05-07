package com.liangjiapei.bettertodoapp;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by liangjiapei on 5/6/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoAdapterViewHolder> {



    private ArrayList<TodoItem> mTodos;

    private final TodoAdapterOnClickHandler mClickHandler;

    public interface TodoAdapterOnClickHandler {
        void onClick(int position);
    }

    public TodoAdapter(TodoAdapterOnClickHandler clickHandler) {
        mTodos = new ArrayList<>();
        mClickHandler = clickHandler;
    }

    public class TodoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTodoDescriptionTextView;
        public final TextView mTodoCreatedAtTextView;
        public final TextView mTodoDueAtTextView;
        public final TextView mTodoDueAtTextViewLabel;
        public final TextView mTodoCreatedAtTextViewLabel;
        public final CheckBox mTodoCheckBox;


        private Context context;

        public TodoAdapterViewHolder(View view) {
            super(view);
            mTodoDescriptionTextView = (TextView) view.findViewById(R.id.tv_todo_list_item_description);
            mTodoCreatedAtTextView = (TextView) view.findViewById(R.id.tv_todo_list_item_created_at);
            mTodoCheckBox = (CheckBox) view.findViewById(R.id.cb_todo_list_item);
            mTodoCreatedAtTextViewLabel = (TextView) view.findViewById(R.id.tv_created_at_label);
            mTodoDueAtTextView = (TextView) view.findViewById(R.id.tv_due_date);
            mTodoDueAtTextViewLabel = (TextView) view.findViewById(R.id.tv_due_at_label);

            context = view.getContext();

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position);
        }

    }

    @Override
    public TodoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.todo_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        Boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TodoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodoAdapterViewHolder todoAdapterViewHolder, int position) {

        final TodoItem todo = mTodos.get(position);

        String description = todo.getDescription();
        String createdAt = todo.getCreatedAt();
        Boolean isCompleted = todo.isCompleted();

        todoAdapterViewHolder.mTodoDescriptionTextView.setText(description);
        todoAdapterViewHolder.mTodoCreatedAtTextView.setText(createdAt);
        todoAdapterViewHolder.mTodoCheckBox.setChecked(isCompleted);

        final TextView descriptionTextView = todoAdapterViewHolder.mTodoDescriptionTextView;
        final TextView createdAtTextView = todoAdapterViewHolder.mTodoCreatedAtTextView;
        final TextView createdAtTextViewLabel = todoAdapterViewHolder.mTodoCreatedAtTextViewLabel;
        final TextView dueAtTextView = todoAdapterViewHolder.mTodoDueAtTextView;
        final TextView dueAtTextViewLabel = todoAdapterViewHolder.mTodoDueAtTextViewLabel;

        if (isCompleted) {
            descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            createdAtTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            createdAtTextViewLabel.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dueAtTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            dueAtTextViewLabel.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            createdAtTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            createdAtTextViewLabel.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dueAtTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            dueAtTextViewLabel.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }



        todoAdapterViewHolder.mTodoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todo.setCompleted(isChecked);

                if (isChecked) {
                    descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    createdAtTextView.setPaintFlags(descriptionTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    descriptionTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    createdAtTextView.setPaintFlags(descriptionTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }

                writeToFile(todoAdapterViewHolder.context);

            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == mTodos) return 0;
        return mTodos.size();
    }

    public void setTodoData(ArrayList<TodoItem> todos) {
        mTodos = todos;
        notifyDataSetChanged();
    }

    public ArrayList<TodoItem> getmTodos() {
        return mTodos;
    }

    public void addTodoItem(TodoItem todo) {

        mTodos.add(todo);
        notifyDataSetChanged();
    }

    public void deleteTodoItem(int position) {
        mTodos.remove(position);
        notifyDataSetChanged();
    }

    public void deleteAllTodos() {
        mTodos.clear();
        notifyDataSetChanged();
    }

    public void writeToFile(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("todos", context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mTodos);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
