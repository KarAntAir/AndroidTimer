package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton, stopButton;
    private TextView elapsedTimeTextView;
    private Handler handler;
    private Runnable runnable;
    private long startTime, elapsedTime, totalElapsedTime;

    private static final String KEY_TOTAL_ELAPSED_TIME = "totalElapsedTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);

        handler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        // Восстановление состояния активности при его наличии
        if (savedInstanceState != null) {
            totalElapsedTime = savedInstanceState.getLong(KEY_TOTAL_ELAPSED_TIME);
            updateElapsedTimeTextView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_TOTAL_ELAPSED_TIME, totalElapsedTime);
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                elapsedTime = System.currentTimeMillis() - startTime;
                totalElapsedTime += elapsedTime;
                handler.postDelayed(this, 1000);

                // Обновляем значение в TextView
                updateElapsedTimeTextView();
            }
        }, 1000);

        Toast.makeText(this, "Timer started", Toast.LENGTH_SHORT).show();
    }

    private void stopTimer() {
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "Timer stopped", Toast.LENGTH_SHORT).show();

        // Здесь можно сохранить значение totalElapsedTime в базе данных или файле
        // для последующего использования или отображения
    }

    private void updateElapsedTimeTextView() {
        elapsedTimeTextView.setText("Total Elapsed Time: " + totalElapsedTime / 1000 + " seconds");
    }
}