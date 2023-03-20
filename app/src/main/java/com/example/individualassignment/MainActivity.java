package com.example.individualassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private TextView timerTextView;
    private TextView scoreTextView, scoreView;
    private TableLayout tableLayout;
    private int previousHighlightedColumn = -1;
    private int score;
    private CountDownTimer countDownTimer;
    private ArrayList<Button> viewItems;

    private int currentLevel = 1;
    private int successfulTouches = 0;
    private int highlightedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        tableLayout = findViewById(R.id.table_layout);
        scoreView = findViewById(R.id.end_of_game_text);


        updateTableLayout(2, 2);
        startGame();
    }

    private void highlightRandomColumn() {
        // Get the number of columns in the TableLayout
        int columnCount = ((TableRow) tableLayout.getChildAt(0)).getChildCount();
        int rowCount = tableLayout.getChildCount();

        // Generate a random column index and row index
        Random random = new Random();
        int randomColumn = random.nextInt(columnCount);
        int randomRow = random.nextInt(rowCount);

        // Reset the background color of all cells in the TableLayout
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < columnCount; j++) {
                tableRow.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // Change the background color of the random column in the random row
        TableRow tableRow = (TableRow) tableLayout.getChildAt(randomRow);
        View cell = tableRow.getChildAt(randomColumn);
        cell.setBackgroundColor(Color.RED); // Set the desired background color here
    }

    public void onCellClicked(View view) {
        // Check if the clicked cell is highlighted
        ColorDrawable cellColor = (ColorDrawable) view.getBackground();
        int colorId = cellColor.getColor();

        if (colorId == Color.RED) { // Change this to match the highlight color
            // Highlight the next random column, except for the one just clicked
            score += 100;
            scoreTextView.setText("Score: " + score);
            highlightRandomColumnExcept(view);
        }
    }

    private void highlightRandomColumnExcept(View viewToExclude) {
        // Get the number of columns and rows in the TableLayout
        int columnCount = ((TableRow) tableLayout.getChildAt(0)).getChildCount();
        int rowCount = tableLayout.getChildCount();

        // Get the row and column index of the view to exclude
        TableRow tableRowToExclude = (TableRow) viewToExclude.getParent();
        int rowIndexToExclude = tableLayout.indexOfChild(tableRowToExclude);
        int columnIndexToExclude = tableRowToExclude.indexOfChild(viewToExclude);

        // Generate random row and column indices, excluding the given view
        Random random = new Random();
        int randomColumn, randomRow;
        do {
            randomColumn = random.nextInt(columnCount);
            randomRow = random.nextInt(rowCount);
        } while (randomColumn == columnIndexToExclude && randomRow == rowIndexToExclude);

        // Reset the background color of all cells in the TableLayout
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < columnCount; j++) {
                tableRow.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
            }
        }

        // Change the background color of the random column in the random row
        TableRow tableRow = (TableRow) tableLayout.getChildAt(randomRow);
        View cell = tableRow.getChildAt(randomColumn);
        cell.setBackgroundColor(Color.RED); // Set the desired background color here
    }

    private void startGame() {
        // Highlight the first random column in a random row
        highlightRandomColumn();


        // Start the countdown timer
        countDownTimer = new CountDownTimer(5000, 1000) { // 5000 milliseconds (5 seconds) total time, 1000 milliseconds (1 second) interval
            public void onTick(long millisUntilFinished) {
                // You can update a TextView with the remaining time here, if you want
                timerTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {


                // Disable all cells in the TableLayout when the time is up
                int rowCount = tableLayout.getChildCount();
                int columnCount = ((TableRow) tableLayout.getChildAt(0)).getChildCount();
                for (int i = 0; i < rowCount; i++) {
                    TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                    for (int j = 0; j < columnCount; j++) {
                        tableRow.getChildAt(j).setEnabled(false);
                    }
                }
                // Show a message that the time is up, if you want
                View endOfGameScreen = getLayoutInflater().inflate(R.layout.end_of_game_screen, null);
                setContentView(endOfGameScreen);

                TextView endOfGameScoreText = endOfGameScreen.findViewById(R.id.end_of_game_text);
                endOfGameScoreText.setText("Score: " + score);

                // Set up the continue button
                Button continueButton = endOfGameScreen.findViewById(R.id.continue_button);
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int nextRowCount = rowCount + 1;
                        int nextColumnCount = columnCount + 1;
                        setContentView(R.layout.activity_main);
                        tableLayout = findViewById(R.id.table_layout);
                        timerTextView = findViewById(R.id.timerTextView);
                        scoreTextView = findViewById(R.id.scoreTextView);
                        updateTableLayout(nextRowCount, nextColumnCount);
                        startGame();
                    }
                });

                // Set up the not continue button
                Button notContinueButton = endOfGameScreen.findViewById(R.id.not_continue_button);
                notContinueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Leave this code block empty or add your desired action when the user chooses not to continue
                    }
                });
            }
        }.start();
    }

    private void updateTableLayout(int rows, int columns) {
        // Remove all existing rows in the TableLayout
        tableLayout.removeAllViews();

        // Add the new rows and columns
        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < columns; j++) {
                TextView cell = new TextView(this);
                cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                cell.setGravity(Gravity.CENTER);
                cell.setPadding(8, 8, 8, 8);
                cell.setText("Cell"); // Set the text or any other content for the cell
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCellClicked(v);
                    }
                });
                tableRow.addView(cell);
            }

            tableLayout.addView(tableRow);
        }
    }
}




