package com.beginner.parfa.colorgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private Integer tc = 0xff000000, bc = 0xff55b2de;
    private boolean vol = true;
    TextView t;
    ConstraintLayout cl;
    int request_code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView vers = (TextView)findViewById ( R.id.credits );
        vers.setText ( "version: " + BuildConfig.VERSION_NAME );

        t = (TextView)findViewById(R.id.greeting);
        cl = (ConstraintLayout)findViewById(R.id.mainCl);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getInt("text") != 0){
            tc = extras.getInt("text");
            bc = extras.getInt("back");
            vol = extras.getBoolean ( "volume" );
            cl.setBackgroundColor(bc);
            t.setTextColor(tc);
            Toast.makeText(getBaseContext(),"Not null "+tc,Toast.LENGTH_SHORT).show();
        }else {
            cl.setBackgroundColor ( bc );
            t.setTextColor(tc);
        }

    }
    public void gotoOldGame(View view){
        Intent intent = new Intent(this, ColorGuess.class);
        Bundle extras = new Bundle();
        extras.putInt("text",tc);
        extras.putInt("back",bc);
        extras.putBoolean ( "volume",vol );
        intent.putExtras(extras);
        startActivityForResult(intent,request_code);
    }

    public void gotoGame(View view){
        final Intent intent = new Intent(this, ColorGuess.class);
        new AlertDialog.Builder(this)
                .setTitle ( "Start New Game?" )
                .setMessage ( "Are you sure you want to start a new Game? Doing so will " +
                        "delete your current progress." )
                .setNegativeButton ( "Cancel", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                } )
                .setPositiveButton ( "Yes", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFile(getString(R.string.filename));
                        Bundle extras = new Bundle();
                        extras.putInt("text",tc);
                        extras.putInt("back",bc);
                        extras.putBoolean ( "volume",vol );
                        intent.putExtras(extras);
                        startActivityForResult(intent,request_code);
                    }
                } ).create ().show ();
    }

    public void gotoSettings(View view){
        try{
            Intent intent = new Intent(this, Settings.class);
            intent.putExtra("text",tc);
            intent.putExtra("back",bc);
            intent.putExtra ( "volume",vol );
            startActivityForResult(intent,request_code);
        }
        catch (Exception e)
        {
            t.setText(e.getMessage());
        }
    }

    public void showInfo(View view){
        new AlertDialog.Builder ( this )
                .setTitle ( "About Color Guess "+ BuildConfig.VERSION_NAME )
                .setMessage ( R.string.info_text )
                .setIcon ( R.drawable.launcher)
                .setPositiveButton ( "OK", new DialogInterface.OnClickListener (){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which){
                        dialogInterface.dismiss ();
                    }
                } )
                .create ().show ();
    }

    public void showInstruct(View view){
        new AlertDialog.Builder(this)
                .setTitle ( getString( R.string.instruct_title) )
                .setMessage ( getString(R.string.instructions) )
                .setIcon ( R.drawable.launcher )
                .setPositiveButton ( "OK", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss ();
                    }
                } )
                .create ().show ();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == request_code){
            if(resultCode == RESULT_OK){
                tc = data.getExtras().getInt("text");
                bc = data.getExtras().getInt("back");
                vol = data.getExtras ().getBoolean ( "volume", vol );
                t.setTextColor(tc);
                cl.setBackgroundColor(bc);
            }
        }
    }


}
