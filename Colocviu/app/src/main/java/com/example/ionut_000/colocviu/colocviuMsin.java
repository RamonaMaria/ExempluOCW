package com.example.ionut_000.colocviu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class colocviuMsin extends AppCompatActivity {
    private EditText leftEditText = null;
    private EditText rightEditText = null;
    private Button leftButton = null;
    private Button rightButton = null;
    private Button navigateToSecondaryActivity = null;
    private Boolean serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();



    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;

    private Button navigateToSecondaryActivityButton = null;


    // incrementez numru de apasari
    // creez o clasa asculatator si un obiect instanta al clasei
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int leftNumberOfClicksService = Integer.parseInt(leftEditText.getText().toString());
            int rightNumberOfClicksService = Integer.parseInt(rightEditText.getText().toString());

            switch(view.getId()) {
                case R.id.submit_button_1:
                    int leftNumberOfClicks = Integer.parseInt(leftEditText.getText().toString());
                    leftNumberOfClicks++;
                    leftEditText.setText(String.valueOf(leftNumberOfClicks));
                    break;
                case R.id.submit_button_2:
                    int rightNumberOfClicks = Integer.parseInt(rightEditText.getText().toString());
                    rightNumberOfClicks++;
                    rightEditText.setText(String.valueOf(rightNumberOfClicks));
                    break;
                case R.id.navigate_to_secondary_activity_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int numberOfClicks = Integer.parseInt(leftEditText.getText().toString()) +
                            Integer.parseInt(rightEditText.getText().toString());
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }
            Log.d("MyProcessingThread", "Inainte de if");

            if (leftNumberOfClicksService + rightNumberOfClicksService > Constants.NUMBER_OF_CLICKS_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", leftNumberOfClicksService);
                intent.putExtra("secondNumber", rightNumberOfClicksService);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
                Log.d("MyProcessingThread", "In if");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colocviu_msin);

        leftEditText = (EditText)findViewById(R.id.introduce_yourself_edit_text_1); //iau referinta catre butonul 1
        rightEditText = (EditText)findViewById(R.id.introduce_yourself_edit_text_2);
      //  leftEditText.setText(String.valueOf(0));   // setez 0 initial
     //   rightEditText.setText(String.valueOf(0)); // setez 0 initial

        // iau referintelecatre butoane
        leftButton = (Button)findViewById(R.id.submit_button_1);
        rightButton = (Button)findViewById(R.id.submit_button_2);
        navigateToSecondaryActivity = (Button)findViewById(R.id.navigate_to_secondary_activity_button);



        // incrementez numarul de apasari
        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);


        navigateToSecondaryActivityButton = (Button)findViewById(R.id.navigate_to_secondary_activity_button);
        navigateToSecondaryActivityButton.setOnClickListener(buttonClickListener);

        // metoda 2 B2 - b
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("leftCount")) {
                leftEditText.setText(savedInstanceState.getString("leftCount"));
            } else {
                leftEditText.setText(String.valueOf(0));
            }
            if (savedInstanceState.containsKey("rightCount")) {
                rightEditText.setText(savedInstanceState.getString("rightCount"));
            } else {
                rightEditText.setText(String.valueOf(0));
            }
        } else {
            leftEditText.setText(String.valueOf(0));
            rightEditText.setText(String.valueOf(0));
        }

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Constants.TAG, "OnStop method was invoked");

    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
        Log.d(Constants.TAG, "onResume method was invoked");

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Constants.TAG, "onStart method was invoked");
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        Log.d(Constants.TAG, "onPause method was invoked");

        super.onPause();
    }
        // salvare - pui in obiectul de tip bundle valorile pe care vrei sa le salvezi
    @Override
    public void onSaveInstanceState(Bundle savedInstaceState) {
        super.onSaveInstanceState(savedInstaceState);

        // put -  string definit de utilizator si valoare
        savedInstaceState.putString("leftText", leftEditText.getText().toString());
        savedInstaceState.putString("rightText", rightEditText.getText().toString());
        Log.d(Constants.TAG, "onSaveInstaceState method was invoked");

    }

   // dostrugearea activitati echivalent cu distrugerea serviciuluis
    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        Log.d(Constants.TAG, "onDestroy method was invoked");

        super.onDestroy();
    }



    // restaurarea (refacerea) starii

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d(Constants.TAG, "onRestoreInstanceState method called");

        // butonul stang
        if(savedInstanceState.containsKey("leftText")) {
            // inseamna ca obiectul savedInstaceState contine valoarea si refac starea lui EditText
            // (pe a cui am salvat-o)in caz contra pun starea initiala
            leftEditText.setText(savedInstanceState.getString("leftText"));
        } else {
            // o refac la valoarea initiala
            leftEditText.setText(String.valueOf(0));
        }


        // butonul drept
        if (savedInstanceState.containsKey("rightText")) {
            rightEditText.setText(savedInstanceState.getString("rightText"));
        } else {
            rightEditText.setText(String.valueOf(0));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
}
