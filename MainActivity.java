package com.example.stopwatchapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView timerText;
    private Button startButton, pauseButton, resetButton;

    private final Handler handler = new Handler();
    private long startTime, timeInMilliseconds = 0L;
    private long updatedTime = 0L;
    private boolean isRunning = false;

    private final Runnable updateTimer = new Runnable() {
        public void run() {
            if (isRunning) {
                timeInMilliseconds = System.currentTimeMillis() - startTime;
                updatedTime = timeInMilliseconds;

                int seconds = (int) (updatedTime / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int milliseconds = (int) (updatedTime % 1000);

                timerText.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", minutes, seconds, milliseconds));
                handler.postDelayed(this, 10);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = findViewById(R.id.timer_text);
        startButton = findViewById(R.id.start_button);
        pauseButton = findViewById(R.id.pause_button);
        resetButton = findViewById(R.id.reset_button);

        startButton.setOnClickListener(v -> {
            startTime = System.currentTimeMillis();
            handler.postDelayed(updateTimer, 0);
            isRunning = true;
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resetButton.setEnabled(true);

            // Animate buttons
            animateButton(pauseButton);
            animateButton(resetButton);
        });

        pauseButton.setOnClickListener(v -> {
            isRunning = false;
            handler.removeCallbacks(updateTimer);
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);

            // Animate button
            animateButton(startButton);
        });

        resetButton.setOnClickListener(v -> {
            isRunning = false;
            handler.removeCallbacks(updateTimer);
            timeInMilliseconds = 0L;
            updatedTime = 0L;
            timerText.setText(getString(R.string.timer_initial));
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resetButton.setEnabled(false);

            // Animate button
            animateButton(startButton);
        });
    }

    private void animateButton(Button button) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        button.startAnimation(animation);
    }
}
