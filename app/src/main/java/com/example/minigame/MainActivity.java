package com.example.minigame;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar horse1, horse2, horse3;
    private Button startButton, addButton;
    private boolean isRacing = false;
    private ValueAnimator animator1, animator2, animator3;
    TextView money, winnerText, rate1, rate2, rate3;
    EditText text1, text2, text3;
    double cash1, cash2, cash3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        money = findViewById(R.id.money);
        money.setText("100");

        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        horse3 = findViewById(R.id.horse3);
        horse1.setEnabled(false);
        horse2.setEnabled(false);
        horse3.setEnabled(false);

        text1 = findViewById(R.id.horse1_check);
        text2 = findViewById(R.id.horse2_check);
        text3 = findViewById(R.id.horse3_check);

        rate1 = findViewById(R.id.horse1_rate);
        rate2 = findViewById(R.id.horse2_rate);
        rate3 = findViewById(R.id.horse3_rate);
        randomizeRates(1, 2);

        startButton = findViewById(R.id.startButton);
        addButton = findViewById(R.id.addButton);
        winnerText = findViewById(R.id.winnerTextView);

        startButton.setOnClickListener(this::startRace);
        addButton.setOnClickListener(v -> {
            if (!isRacing) {
                double cash = Double.parseDouble(money.getText().toString());
                cash += 100;
                money.setText(String.valueOf(cash));
            }
        });
    }

    private boolean checkCash(){
        cash1 = TextUtils.isEmpty(text1.getText().toString()) ?
                0 :
                Double.parseDouble(text1.getText().toString());
        cash2 = TextUtils.isEmpty(text2.getText().toString()) ?
                0 :
                Double.parseDouble(text2.getText().toString());
        cash3 = TextUtils.isEmpty(text3.getText().toString()) ?
                0 :
                Double.parseDouble(text3.getText().toString());

        if (cash1 + cash2 + cash3 > Double.parseDouble(money.getText().toString())){
            winnerText.setText("Not enough money!");
            winnerText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, " Not enough money!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cash1 == 0 && cash2 == 0 && cash3 == 0) {
            winnerText.setText("You must choose at least 1 horse!");
            winnerText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "You must choose at least 1 horse!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            money.setText(String.valueOf(Double.parseDouble(money.getText().toString()) - (cash1 + cash2 + cash3)));
            return true;
        }
    }

    private void startRace(View view) {
        if (isRacing || !checkCash()) {
            return;
        }
        isRacing = true;
        startButton.setEnabled(false);
        horse1.setProgress(0);
        horse2.setProgress(0);
        horse3.setProgress(0);
        horse1.setEnabled(true);
        horse2.setEnabled(true);
        horse3.setEnabled(true);

        Random random = new Random();
        animator1 = createAnimator(horse1, random.nextInt(2000) + 3000);
        animator2 = createAnimator(horse2, random.nextInt(2000) + 3000);
        animator3 = createAnimator(horse3, random.nextInt(2000) + 3000);

        animator1.start();
        animator2.start();
        animator3.start();
    }

    private ValueAnimator createAnimator(final SeekBar horse, int duration) {
        ValueAnimator animator = ValueAnimator.ofInt(0, horse.getMax());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            horse.setProgress(progress);
            winnerText.setVisibility(View.INVISIBLE);
            if (progress == horse.getMax()) {
                checkWinner(horse);
            }
        });
        return animator;
    }

    private void checkWinner(SeekBar winningHorse) {
        if (!isRacing) {
            return;
        }
        double result = 0;

        isRacing = false;
        startButton.setEnabled(true);
        horse1.setEnabled(false);
        horse2.setEnabled(false);
        horse3.setEnabled(false);
        if (winningHorse != horse1) animator1.pause();
        if (winningHorse != horse2) animator2.pause();
        if (winningHorse != horse3) animator3.pause();

        String winner = "";
        if (winningHorse == horse1) {
            result = Double.parseDouble(money.getText().toString()) + cash1 * Double.parseDouble(rate1.getText().toString()) + cash1;
            winner = "Horse 1";
        } else if (winningHorse == horse2) {
            result = Double.parseDouble(money.getText().toString()) + cash2 * Double.parseDouble(rate2.getText().toString()) + cash2;
            winner = "Horse 2";
        } else if (winningHorse == horse3) {
            result = Double.parseDouble(money.getText().toString()) + cash3 * Double.parseDouble(rate3.getText().toString()) + cash3;
            winner = "Horse 3";
        }
        randomizeRates(1, 2);
        money.setText(String.valueOf(result));
        winnerText.setText(winner + " wins!");
        winnerText.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, winner + " wins!", Toast.LENGTH_SHORT).show();
    }

    private void randomizeRates(double min, double max) {
        Random random = new Random();
        rate1.setText(String.format(Locale.ENGLISH, "%.2f", (random.nextDouble() * (max - min) + min)));
        rate2.setText(String.format(Locale.ENGLISH, "%.2f", (random.nextDouble() * (max - min) + min)));
        rate3.setText(String.format(Locale.ENGLISH, "%.2f", (random.nextDouble() * (max - min) + min)));
    }
}

