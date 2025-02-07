package com.example.zad5;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(TaskListFragment.KEY_EXTRA_TASK_ID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = TaskFragment.newInstance(taskId);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        return fragment;
    }
}