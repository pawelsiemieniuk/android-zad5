package com.example.zad5;

import java.util.ArrayList;
import java.util.List;

public class TaskStorage {
    private static TaskStorage instance;

    private List<Task> taskList;

    private TaskStorage() {
        taskList = new ArrayList<Task>();
        for(int i=0; i<150; i++) {
            Task task = new Task();
            task.setName("zadanie " + i);
            task.setDone(i % 3 == 0);
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
}
