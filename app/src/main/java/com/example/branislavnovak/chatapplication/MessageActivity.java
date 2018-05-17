package com.example.branislavnovak.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bSend;
    private Button bLogout;
    private Button bRefresh;
    private EditText enteredMessage;
    private Message[] messagesC;

    private String receiverUsername;
    private String senderId;
    private String name;

    public static final String PREFERENCES_NAME = "PreferenceFile";
    private MessageAdapter adapter = new MessageAdapter(this);
    public ChatDbHelper chatDbHelper;

    private static String BASE_URL = "http://18.205.194.168:80";
    private static String POST_MESSAGE_URL = BASE_URL + "/message";
    private static String GET_MESSAGE_URL = BASE_URL + "/message/";
    private static String LOGOUT_URL = BASE_URL + "/logout";

    private HttpHelper httpHelper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // getting all IDs from .xml
        bSend = findViewById(R.id.sendButton);
        bLogout = findViewById(R.id.logoutButton1);
        bRefresh = findViewById(R.id.message_refresh);
        enteredMessage = findViewById(R.id.eMessage);

        // opening shared preference and taking sender nad receiver ID from it with their own keys
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        senderId = preferences.getString("senderId", null);
        receiverUsername = preferences.getString("receiverUsername", null);

        //chatDbHelper = new ChatDbHelper(this);

        ListView listOfMessages = findViewById(R.id.listOfMessages);
        TextView nameOfContact = findViewById(R.id.contactNameInMessage);                           // nameOfContact taking a ID of a TextView field that needs to be changed


        bSend.setEnabled(false);
        bSend.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bRefresh.setOnClickListener(this);

        // getting all extras (exactly the nameOfContact)
        Intent intent = getIntent();
        Bundle b = intent.getExtras();                                                              // getting a values from the first made Bundle in ContactsAdapter
        name = b.getString("contactName");                                              // then set a value on a String through a key value
        nameOfContact.setText(receiverUsername);                                                                // making a TextView in MessageActivity

        listOfMessages.setAdapter(adapter);

        httpHelper = new HttpHelper();
        handler = new Handler();

        updateMessagesList();
        // setting item of a list on LongClickListener for removing it from list and database
        /*listOfMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Message message = (Message) adapter.getItem(position);

                if (messages != null){
                    for(int i = 0; i < messages.length; i++){
                        if(messages[i].getmMessageId().compareTo(message.getmMessageId()) == 0){
                            chatDbHelper.deleteMessage(message.getmMessageId());
                            break;
                        }
                    }
                }

                updateMessagesList();
                return true;
            }
        });*/

        enteredMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = enteredMessage.getText().toString();
                if (s.length() != 0){
                    bSend.setEnabled(true);
                }else{
                    bSend.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sendButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("receiver", receiverUsername);
                            jsonObject.put("data", enteredMessage.getText().toString());

                            final boolean success = httpHelper.sendMessageToServer(MessageActivity.this, POST_MESSAGE_URL, jsonObject);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(success){
                                        Toast.makeText(MessageActivity.this, getText(R.string.message_sent), Toast.LENGTH_SHORT).show();
                                        enteredMessage.getText().clear();
                                        updateMessagesList();
                                    }else{
                                        Toast.makeText(MessageActivity.this, getText(R.string.message_send_error), Toast.LENGTH_SHORT).show();
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
                /*String message = enteredMessage.getText().toString();
                String messageSent = "Message is sent";
                Toast.makeText(this, messageSent, Toast.LENGTH_LONG).show();

                // inserting Message into database
                Message messageToSend = new Message(null, senderId, receiverId, message);
                chatDbHelper.insertMessage(messageToSend);
                updateMessagesList();

                enteredMessage.getText().clear();
                enteredMessage.setHint(R.string.sMessage);*/
                break;

            case R.id.logoutButton1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            final boolean response = httpHelper.logOutUserFromServer(MessageActivity.this, LOGOUT_URL);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(response){
                                        Intent i = new Intent(MessageActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(MessageActivity.this, getString(R.string.cannot_logout_error).toString(), Toast.LENGTH_SHORT).show();

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
                break;
            case R.id.message_refresh:
                updateMessagesList();
                break;
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        updateMessagesList();
    }*/

    public void updateMessagesList(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    final JSONArray all_messages = httpHelper.getMessagesFromServer(MessageActivity.this, (GET_MESSAGE_URL+receiverUsername));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(all_messages != null){
                                JSONObject json_message;
                                messagesC = new Message[all_messages.length()];

                                for(int i = 0; i < all_messages.length(); i++){
                                    try{
                                        json_message = all_messages.getJSONObject(i);
                                        messagesC[i] = new Message(json_message.getString("sender"), json_message.getString("data"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.updateMessages(messagesC);

                            }else{
                                Toast.makeText(MessageActivity.this, getText(R.string.get_message_error), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //messages = chatDbHelper.readMessages(senderId, receiverId);
        //adapter.addMessages(messages);
    }
}
