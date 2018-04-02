package com.example.ionut_000.colocviu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {
    private TextView numberOfClicksTextView = null;
    private Button okButton = null;
    private Button cancelButton = null;


    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.ok_button:
                    setResult(RESULT_OK, null); // transmit rezultatul catre activitatea care a invocat intentia
                    break;
                case R.id.cancel_button:
                    setResult(RESULT_CANCELED, null);
                    break;
            }
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        //
        numberOfClicksTextView = (TextView)findViewById(R.id.number_of_clicks_text_view);

        // iau intentia
        Intent intent = getIntent();

        // prin GETextra extrag informatii despre intentie
        if (intent != null && intent.getExtras().containsKey("numberOfClicks")) { // cheia setat de mine in activitatea (colocviuMsin)ce invoca
            int numberOfClicks = intent.getIntExtra("numberOfClicks", -1); // iau numarul de click-uri
            numberOfClicksTextView.setText(String.valueOf(numberOfClicks)); // setez numarul de click-uri in text view
        }

        // butoanele
        okButton = (Button)findViewById(R.id.ok_button);  // ii iau referinta
        okButton.setOnClickListener(buttonClickListener); // ii dau sezultatul activitatii care a invocat intentia
        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(buttonClickListener);
    }

}
