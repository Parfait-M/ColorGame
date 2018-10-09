package com.beginner.parfa.colorgame;

import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class Settings extends AppCompatActivity {

    Integer cols[] = {0xffffffff, 0xffff0000,0xffffa500,0xffffff00,0xff00ff00,0xff0000ff,0xffa020f0,0xffa52a2a,0xff000000};
    String names[] = {"WHITE","RED","ORANGE","YELLOW","GREEN","BLUE","PURPLE","BROWN","BLACK"};
    Integer backCols[] = {0xff4dacf2,0xff9a1f1f, 0xfffd6e04, 0xfff6c611, 0xff2a9b14, 0xff7a1ad0, 0xffa71383, 0xffc5bfc4,0xff55b2de};
    String backNames[] = {"SKY BLUE","BURGUNDY", "ORANGE", "YELLOW OCHRE", "GRASS GREEN", "VIOLET", "MAGENTA", "GRAY","DEF_BACK"};
    SeekBar seekBar1, seekBar2;
    TextView textView1, textView2;
    ConstraintLayout cl;
    Button sound;
    int prog1,prog2;
    private int defText = 0xff000000;
    private int defBack = 0xff55b2de;
    private static boolean vol1 = true;
    private boolean vol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        int bc = 0xff55b2de, tc = 0xff000000;
        sound = (Button)findViewById ( R.id.soundOn );
        seekBar1 = (SeekBar)findViewById(R.id.textSeek);
        seekBar2 = (SeekBar)findViewById(R.id.backSeek);
        textView1= (TextView)findViewById(R.id.text_color);
        textView2= (TextView)findViewById(R.id.back_color);
        cl = (ConstraintLayout)findViewById(R.id.lay_background);

        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                tc = extras.getInt("text");
                bc = extras.getInt("back");
                vol = extras.getBoolean ( "volume" );
                textView1.setTextColor(tc);
                cl.setBackgroundColor(bc);
            }
        }
        catch (Exception e){
            Toast.makeText(getBaseContext(),"Failed to load colors",Toast.LENGTH_LONG).show();
            vol = true;
        }
        checkVolume(vol);
        prog1 = Arrays.asList(cols).indexOf(tc);
        prog2 = Arrays.asList(backCols).indexOf(bc);
        seekBar1.setProgress(prog1);
        seekBar2.setProgress(prog2);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView1.setTextColor(cols[progress]);
                textView1.setText("The text is "+names[progress]);
                prog1 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cl.setBackgroundColor(backCols[progress]);
                prog2 = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void restoreDef(View v){
        Intent i = new Intent();
        i.putExtra("text",defText);
        i.putExtra("back",defBack);
        i.putExtra ( "volume",true );
        setResult(RESULT_OK,i);
        finish();
    }

    public void cancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void close(View view){
        Intent i = new Intent();
        i.putExtra("text",cols[prog1]);
        i.putExtra("back",backCols[prog2]);
        i.putExtra ( "volume",vol );
        setResult(RESULT_OK,i);
        finish();
    }

    public void toggleSound(View view){
        if(vol)
            sound.setBackgroundResource ( R.drawable.vol_off );
        else
            sound.setBackgroundResource ( R.drawable.vol_on );
        vol = !vol;
    }

    public void checkVolume(boolean vol){
        if(!vol)
            sound.setBackgroundResource ( R.drawable.vol_off );
        else
            sound.setBackgroundResource ( R.drawable.vol_on );
    }
}
