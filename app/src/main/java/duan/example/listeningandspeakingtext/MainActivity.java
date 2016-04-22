package duan.example.listeningandspeakingtext;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

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
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playText();
            }
        });
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
}
