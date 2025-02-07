package com.example.zad5;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static TaskStorage instance;

    private List<Task> taskList;

    private TaskStorage() {
        taskList = new ArrayList<Task>();
        for(int i=0; i<150; i++) {
            Task task = new Task();
            task.setName("zadanie " + i);
            task.setDone(i % 3 == 0);
            if(i % 3 == 0) {
                task.setCategory(Category.STUDIES);
            } else {
                task.setCategory(Category.HOME);
            }
            taskList.add(task);
        }
    }

    public static TaskStorage getInstance() {
        if(instance == null) {
            synchronized (TaskStorage.class) {
                if(instance == null) {
                    instance = new TaskStorage();
                }
            }
        }
        return instance;
    }

    public List<Task> getTasks() {
        return taskList;
    }

    public Task getTask(UUID taskId) {
        for(Task task : taskList){
            if(task.getId().equals(taskId)){
                return task;
            }
        }
        return null;
    }
}
