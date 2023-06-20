package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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

        totalElapsedTime = readElapsedTimeFromFile();
        updateElapsedTimeTextView();

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

        // Сохраняем значение totalElapsedTime в файл
        saveElapsedTimeToFile();
    }

    private void saveElapsedTimeToFile() {
        String filename = "elapsed_time.txt";
        String elapsedTimeString = String.valueOf(totalElapsedTime);

        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(elapsedTimeString.getBytes());
            fos.close();
            Toast.makeText(this, "Elapsed time saved to file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save elapsed time to file", Toast.LENGTH_SHORT).show();
        }
    }

    private long readElapsedTimeFromFile() {
        String filename = "elapsed_time.txt";
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fis = null;
        BufferedReader bufferedReader = null;

        try {
            fis = openFileInput(filename);
            bufferedReader = new BufferedReader(new InputStreamReader(fis));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            return Long.parseLong(stringBuilder.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private void updateElapsedTimeTextView() {
        elapsedTimeTextView.setText("Total Elapsed Time: " + totalElapsedTime / 1000 + " seconds");
    }
}