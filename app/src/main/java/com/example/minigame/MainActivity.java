package com.example.minigame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar horse1, horse2, horse3;
    private Button startButton, addButton;
    private boolean isRacing = false;
    private ValueAnimator animator1, animator2, animator3;
    TextView money, winnerText, rate1, rate2, rate3;
    EditText text1,text2,text3;
    double cash1, cash2, cash3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        money = findViewById(R.id.money);
        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        horse3 = findViewById(R.id.horse3);
        text1 = findViewById(R.id.horse1_check);
        text2 = findViewById(R.id.horse2_check);
        text3 = findViewById(R.id.horse3_check);

        rate1 = findViewById(R.id.horse1_rate);
        rate2 = findViewById(R.id.horse2_rate);
        rate3 = findViewById(R.id.horse3_rate);
        Random random = new Random();
        rate1.setText(String.valueOf ((random.nextInt(100) + 1) / 100.0));
        rate2.setText(String.valueOf ((random.nextInt(100) + 1) / 100.0));
        rate3.setText(String.valueOf ((random.nextInt(100) + 1) / 100.0));

        startButton = findViewById(R.id.startButton);
        winnerText = findViewById(R.id.winnerTextView);
        money.setText("100");
        addButton = findViewById(R.id.addButton);
        horse1.setEnabled(false);
        horse2.setEnabled(false);
        horse3.setEnabled(false);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRacing && chechCash()) {
                    startRace();
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRacing) {
                    double cash = Double.parseDouble(money.getText().toString());
                    cash += 100;
                    money.setText(String.valueOf(cash));
                }
            }
        });
    }
    private boolean chechCash(){
        if (TextUtils.isEmpty(text1.getText().toString())){
            cash1 = 0;
        }
        else cash1 = Double.parseDouble(text1.getText().toString());
        if (TextUtils.isEmpty(text2.getText().toString())){
            cash2 = 0;
        }
        else cash2 = Double.parseDouble(text2.getText().toString());
        if (TextUtils.isEmpty(text3.getText().toString())){
            cash3 = 0;
        }
        else cash3 = Double.parseDouble(text3.getText().toString());

        if (cash1 + cash2 + cash3 > Double.parseDouble(money.getText().toString())){

            winnerText.setText(" Not enough money!");
            winnerText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, " Not enough money!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cash1 == 0 && cash2 == 0 && cash3 == 0) {
            winnerText.setText(" You must choose at least 1 horse!");
            winnerText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, " You must choose at least 1 horse!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            money.setText(String.valueOf(Double.parseDouble(money.getText().toString()) - (cash1 + cash2 + cash3)));
            return true;
        }
    }
    private void startRace() {
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
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                horse.setProgress(progress);
                winnerText.setVisibility(View.INVISIBLE);
                if (progress == horse.getMax()) {
                    checkWinner(horse);
                }
            }
        });
        return animator;
    }

    private void checkWinner(SeekBar winningHorse) {
        if (!isRacing) return;
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
        Random random = new Random();
        rate1.setText(String.valueOf ((random.nextInt(100) + 1) / 100.0));
        rate2.setText(String.valueOf ((random.nextInt(100) + 1) / 100.0));
        rate3.setText(String.valueOf ((random.nextInt(100) + 1) / 100.0));
        money.setText(String.valueOf(result));
        winnerText.setText(winner + " wins!");
        winnerText.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, winner + " wins!", Toast.LENGTH_SHORT).show();
    }
}

