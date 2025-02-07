package com.example.zad5;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "key_extra_task_id";
    public static final String SUBTITLE_VISIBILITY = "subtitle_visibility";
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private boolean subtitleVisible;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();

        return view;
    }

    @Override
    @Deprecated
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    @Deprecated
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch(item.getItemId()){
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        if(item.getItemId() == R.id.new_task) {
            Task task = new Task();
            TaskStorage.getInstance().addTask(task);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
            return true;
        } else if(item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            subtitleVisible = (boolean) savedInstanceState.getSerializable(SUBTITLE_VISIBILITY);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(SUBTITLE_VISIBILITY, subtitleVisible);
    }

    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> taskList = taskStorage.getTasks();
        if(adapter == null) {
            adapter = new TaskAdapter(taskList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    public void updateSubtitle() {
        String subtitle = null;
        if(subtitleVisible) {
            TaskStorage taskStorage = TaskStorage.getInstance();
            List<Task> taskList = taskStorage.getTasks();
            int todoTasksCount = 0;
            for (Task task : taskList) {
                if (!task.isDone()) {
                    todoTasksCount++;
                }
            }
            subtitle = getString(R.string.subtitle_format, todoTasksCount);
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Task task;

        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView categoryImageView;
        private CheckBox statusCheckBox;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            categoryImageView = itemView.findViewById(R.id.task_item_category);
            statusCheckBox = itemView.findViewById(R.id.task_item_status);
        }

        public void bind(Task task) {
            this.task = task;
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());
            if(task.getCategory().equals(Category.HOME)) {
                categoryImageView.setImageResource(R.mipmap.ic_home);
            } else {
                categoryImageView.setImageResource(R.mipmap.ic_studies);
            }
            statusCheckBox.setChecked(task.isDone());
            if(task.isDone()){
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        public CheckBox getCheckBox() {
            return statusCheckBox;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> taskList;
        public TaskAdapter(List<Task> taskList) {
            this.taskList = taskList;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = taskList.get(position);
            holder.bind(task);

            CheckBox checkBox = holder.getCheckBox();
            checkBox.setChecked(taskList.get(position).isDone());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Task task = taskList.get(holder.getBindingAdapterPosition());
                    task.setDone(isChecked);
                    holder.bind(task);
                }
            });
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }
}
