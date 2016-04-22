package duan.example.listeningandspeakingtext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    protected static final int RESULT_SPEECH = 1;

    private TextToSpeech textToSpeech;
    private ImageButton imageButtonRecord;
    private ImageButton imageButtonPlay;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instanceElements();
        setListeners();
    }

    private void instanceElements(){
        textToSpeech        = new TextToSpeech(MainActivity.this, MainActivity.this);
        imageButtonRecord   = (ImageButton) findViewById(R.id.imageButtonRecord);
        imageButtonPlay     = (ImageButton) findViewById(R.id.imageButtonPlay);
        editText            = (EditText) findViewById(R.id.editText);
    }

    private void setListeners(){

        imageButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordText();
            }
        });

        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playText();
            }
        });
    }

    private void recordText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        try {
            startActivityForResult(intent, RESULT_SPEECH);
            editText.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(), "Opps! Seu aparelho não suporta essa ação.", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private void playText(){
        if(editText.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Grave ou adicione um texto para ser reproduzido.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!textToSpeech.isSpeaking()){
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "sampletext");
                textToSpeech.speak(editText.getText().toString(), TextToSpeech.QUEUE_ADD, params);
                imageButtonPlay.setVisibility(View.GONE);
            }
            else{
                textToSpeech.stop();
            }
        }
    }

    @Override
    public void onInit(int i) {
        textToSpeech.setOnUtteranceCompletedListener(this);
    }

    @Override
    public void onUtteranceCompleted(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Completo", Toast.LENGTH_SHORT).show();
                imageButtonPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(text.get(0));
                }
                break;
            }

        }
    }
}
