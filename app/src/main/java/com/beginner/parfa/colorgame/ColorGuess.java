package com.beginner.parfa.colorgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ColorGuess extends AppCompatActivity {

    private TextView t, t2, t3, wCount, lCount;
    private Integer livesLeft = 3;
    private int rnum, tc, bc, wins = 0, loss = 0;
    private boolean vol;
    MediaPlayer track;
    Integer cols[] = {0xffff0000,0xffffa500,0xffffff00,0xff00ff00,0xff0000ff,0xffa020f0};
    String names[] = {"RED","ORANGE","YELLOW","GREEN","BLUE","PURPLE"};
    ConstraintLayout cl;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_guess);

        // Try to load values from old game.
        FileInputStream inputStream;
        try{
            inputStream = openFileInput ( getString ( R.string.filename ));
            wins = inputStream.read();
            loss = inputStream.read ();
            livesLeft = inputStream.read ();
        }catch (Exception e)
        {
            e.printStackTrace ();
        }

        t = (TextView)findViewById(R.id.answer);
        t2 = (TextView)findViewById(R.id.incorrect);
        t3 = (TextView)findViewById(R.id.lives);
        wCount = (TextView)findViewById(R.id.winCount);
        lCount = (TextView)findViewById(R.id.lossCount);

        cl = (ConstraintLayout) findViewById(R.id.game_background);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            tc = extras.getInt("text");
            bc = extras.getInt("back");
            vol = extras.getBoolean ("volume");
            cl.setBackgroundColor(bc);
            t.setTextColor(tc);
            t3.setText(""+livesLeft);
            wCount.setText(""+wins);
            lCount.setText(""+loss);
        }
        playGame();
    }

    public void playGame(){
        rnum = (int) (Math.random()*6);
        t = (TextView)findViewById(R.id.answer);
        t.setText(getString ( R.string.chooseColor ));
        t2.setText("");
        livesLeft = 3;
        t3.setText(""+livesLeft);
        wCount.setText(""+wins);
        lCount.setText(""+loss);
    }

    public void btnClicked(View view) {
        try {
            String name = (view.getResources().getResourceName(view.getId())).substring(32);
            if( name.equals ( names[rnum] ) ) {
                if(vol){
                    track.stop ();
                    track = MediaPlayer.create (ColorGuess.this,R.raw.correct_sound );
                    track.start ();
                }
                wins++;

                new AlertDialog.Builder(this)
                        .setTitle("Victory!")
                        .setMessage("Congratulations! Would you like to play again?")
                        .setCancelable(false)
                        .setNegativeButton("Quit", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                onBackPressed();
                                playGame();
                            }
                        })
                        .setPositiveButton("New Game", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                playGame();
                            }
                        }).create().show();

            }
            else{
                if(vol){
                    track = MediaPlayer.create (ColorGuess.this,R.raw.incorrect_sound );
                    track.start ();
                }
                t2.setText("Incorrect! Please try again.");

                livesLeft--;
                t3.setText(""+livesLeft);
                wCount.setText(""+wins);
                lCount.setText(""+loss);
            }
            if(livesLeft == 0){
                loss++;
                new AlertDialog.Builder(this)
                        .setTitle("Game Over")
                        .setMessage("Too bad! the correct answer was "+ names[rnum])
                        .setCancelable(false)
                        .setNegativeButton("Quit", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                onBackPressed();
                            }
                        })
                        .setPositiveButton("New Game", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                playGame();
                            }
                        }).create().show();
            }
        }
        catch (Exception e){
            e.printStackTrace ();
        }


    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Really exit?")
                .setMessage("Are you sure you want to exit?")
                .setCancelable ( false )
                .setNegativeButton("Nah...",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(livesLeft == 0){
                            playGame();
                        }
                    }
                })
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        i.putExtra("text",tc);
                        i.putExtra("back",bc);
                        i.putExtra ( "volume",vol );
                        setResult(RESULT_OK,i);
                        saveScore ();
                        finish();
                    }
                }).create().show();
    }

    void saveScore()
    {
        // Create or open the HighScore file to save data
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput (getString(R.string.filename), Context.MODE_PRIVATE );
            outputStream.write(wins);
            outputStream.write ( loss );
            outputStream.write ( livesLeft );
        }catch (Exception e)
        {
            e.printStackTrace ();
        }
    }
}
