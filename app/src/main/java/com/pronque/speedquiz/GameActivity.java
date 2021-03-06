package com.pronque.speedquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.pronque.speedquiz.Models.Question;
import com.pronque.speedquiz.Controllers.QuestionManager;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Classe de l'activité de jeu
 */
public class GameActivity extends AppCompatActivity {
    private TextView TV_name_player1;
    private TextView TV_name_player2;
    private TextView TV_question_player1;
    private TextView TV_question_player2;
    private TextView TV_score_player1;
    private TextView TV_score_player2;
    private Button BT_player1;
    private Button BT_player2;
    private Button BT_game_menu;
    private Button BT_game_replay;
    private ConstraintLayout CL_game;
    private FrameLayout FL_game_end;

    private ArrayList<Question> questionsList;
    private QuestionManager questionManager;

    Runnable questionRunnable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TV_name_player1 = findViewById(R.id.name_player1);
        TV_name_player2 = findViewById(R.id.name_player2);
        TV_question_player1 = findViewById(R.id.question_player_1);
        TV_question_player2 = findViewById(R.id.question_player_2);
        TV_score_player1 = findViewById(R.id.score_player1);
        TV_score_player2 = findViewById(R.id.score_player2);
        BT_player1 = findViewById(R.id.bt_player1);
        BT_player2 = findViewById(R.id.bt_player2);
        BT_game_menu = findViewById(R.id.bt_game_menu);
        BT_game_replay = findViewById(R.id.bt_game_replay);
        CL_game = findViewById(R.id.constraintlayout_game);
        FL_game_end = findViewById(R.id.framelayout_end);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Permet de récupérer les extras de l'intent
        Bundle extras = getIntent().getExtras();

        // Récupère le nom des joueurs
        String namePlayer1 = extras.getString("namePlayer1");
        String namePlayer2 = extras.getString("namePlayer2");
        long lengthQuestions = extras.getLong("lengthQuestions");
        questionsList = (ArrayList<Question>) extras.get("questionsList");

        // Défini le texte pour les TextViews
        TV_name_player1.setText(namePlayer1);
        TV_name_player2.setText(namePlayer2);

        questionManager = new QuestionManager(questionsList);

        Question questionPlayer1 = questionManager.getRandomQuestion(questionsList);
        Question questionPlayer2 = questionManager.getRandomQuestion(questionsList);

        Handler handler = new Handler();
        questionRunnable = new Runnable() {
            @Override
            public void run() {
                if (questionManager.isLastQuestion(questionsList)) {
                    CL_game.setVisibility(View.INVISIBLE);
                    FL_game_end.setVisibility(View.VISIBLE);
                    handler.removeCallbacks(this);
                    // DO_OTHER_EXIT_CODE
                } else {
                    // DO_CODE_QUESTION_ITERATION

                    BT_player1.setOnClickListener(v -> {
                        BT_player2.setEnabled(false);
                        Toast.makeText(GameActivity.this, "Appui bt player 1", Toast.LENGTH_SHORT).show();
                        BT_player2.setEnabled(true);
                    });

                    BT_player2.setOnClickListener(v -> {
                        BT_player1.setEnabled(false);
                        Toast.makeText(GameActivity.this, "Appui bt player 2", Toast.LENGTH_SHORT).show();
                        BT_player1.setEnabled(true);
                    });

                    TV_question_player1.setText(questionManager.getRandomQuestion(questionsList).getQuestion());
                    TV_question_player2.setText(questionManager.getRandomQuestion(questionsList).getQuestion());
                    handler.postDelayed(this, lengthQuestions);
                }
            }
        };
        handler.postDelayed(questionRunnable,5000);

        BT_game_menu.setOnClickListener(v -> {
            // Permet d'appeller une activité
            Intent i = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(i);
        });

        BT_game_replay.setOnClickListener(v -> {
        });
    }

    /*
    public int scorePlayer(Question question) {
        int scorePlayer = 0;
        if (question.getAnswer() == 1) {
            scorePlayer++;
        }
        return scorePlayer;
    }
     */
}
