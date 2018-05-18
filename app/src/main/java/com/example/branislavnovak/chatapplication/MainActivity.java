package com.example.branislavnovak.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bRegister;
    private Button bLogin;
    private EditText usernameTypedText, passwordTypedText;
    //private ChatDbHelper chatDbHelper;

    private HttpHelper httpHelper;
    private Handler handler;

    public static final String PREFERENCES_NAME = "PreferenceFile";

    private Context context;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String LOGIN_URL = BASE_URL + "/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final boolean[] usernameEntered = {false};
        final boolean[] passwordEntered = {false};


        // logic for REGISTER button

        bRegister =  findViewById(R.id.registerButton);
        bRegister.setOnClickListener(this);

        // logic for LOGIN button

        bLogin = findViewById(R.id.loginButton);
        bLogin.setOnClickListener(this);
        bLogin.setEnabled(false);

        context = this;

        // logic for enabling

        usernameTypedText = findViewById(R.id.username);
        passwordTypedText = findViewById(R.id.password);

        // instancing new object of DataBase
        //hatDbHelper = new ChatDbHelper(this);

        // instancing httpHelper and holder
        httpHelper = new HttpHelper();
        handler = new Handler();

        // checking if username is typed
        usernameTypedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String lengthCheck = usernameTypedText.getText().toString();
                if (lengthCheck.length() != 0){
                    usernameEntered[0] = true;

                    if (passwordEntered[0]) {
                        bLogin.setEnabled(true);
                    }
                }else{
                    usernameEntered[0] = false;
                    bLogin.setEnabled(false);
                }
            }
        });

        // checking if password is typed
        passwordTypedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String lengthCheck = passwordTypedText.getText().toString();
                if(lengthCheck.length() >= 6){
                    passwordEntered[0] = true;
                    if(usernameEntered[0]){
                        bLogin.setEnabled(true);
                    }
                }else {
                    passwordEntered[0] = false;
                    bLogin.setEnabled(false);
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.registerButton:
                Intent i1 = new Intent(this, RegisterActivity.class);
                startActivity(i1);
                break;

            case R.id.loginButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("username", usernameTypedText.getText().toString());
                            jsonObject.put("password", passwordTypedText.getText().toString());

                            final boolean response = httpHelper.logInUserOnServer(MainActivity.this, LOGIN_URL, jsonObject);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response){
                                        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit();
                                        editor.putString("loggedinUsername", usernameTypedText.getText().toString());
                                        editor.apply();
                                        Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(MainActivity.this, getText(R.string.logIn_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();


                // was linked to database
                /*int doesExist = 0;
                int indexOfLoggedContact = 0;

                Contact[] contacts = chatDbHelper.readContacts();                                   // loading all existing contacts

                SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit();


                if (contacts != null){
                    for(int i = 0; i < contacts.length; i++){
                        if(contacts[i].getmUserName().compareTo(usernameTypedText.getText().toString()) == 0){
                            doesExist = 1;
                            indexOfLoggedContact = i;
                            editor.putString("senderId", contacts[indexOfLoggedContact].getmID());      // saving loggedIn contact into SharedPref
                            editor.apply();
                            break;
                        }
                    }
                }


                if (doesExist == 0){
                    Toast.makeText(this, getText(R.string.sErrorDoesNotExist), Toast.LENGTH_LONG).show();
                }else{
                    Intent i2 = new Intent(this, ContactsActivity.class);
                    startActivity(i2);
                    break;
                }*/
        }
    }
}
