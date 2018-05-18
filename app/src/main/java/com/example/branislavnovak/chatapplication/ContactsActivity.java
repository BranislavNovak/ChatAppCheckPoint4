package com.example.branislavnovak.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bLogout, bRefresh;
    private ListView list;
    private ChatDbHelper chatDbHelper;
    private Contact[] contacts;
    public static final String PREFERENCES_NAME = "PreferenceFile";
    public String userId;
    public ContactsAdapter adapter;

    private String contact_to_delete;
    private HttpHelper httphelper;
    private Handler handler;
    private static String BASE_URL = "http://18.205.194.168:80";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String LOGOUT_URL = BASE_URL + "/logout";
    private static String DELETE_URL = BASE_URL + "/contacts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        bLogout = findViewById(R.id.logoutButton);
        bRefresh = findViewById(R.id.refreshButton);
        list = findViewById(R.id.listOfContacts);
        bLogout.setOnClickListener(this);
        bRefresh.setOnClickListener(this);

        // new chatDataBase instance and reading contacts from database
        // chatDbHelper = new ChatDbHelper();
        // contacts = chatDbHelper.readContacts();

        // Getting logged user userid, from SharedPreference file
        SharedPreferences sharedPref = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        userId = sharedPref.getString("senderId", null);
        adapter = new ContactsAdapter(this);

        list.setAdapter(adapter);
        adapter.updateContacts(contacts);

        httphelper = new HttpHelper();
        handler = new Handler();


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = new JSONObject();
                        Contact contact_model = (Contact) adapter.getItem(i);
                        contact_to_delete = contact_model.getmUserName();

                        try{
                            jsonObject.put("username", contact_to_delete);

                            final boolean success = httphelper.httpDeleteContact(ContactsActivity.this, (DELETE_URL+contact_to_delete), jsonObject);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(success){
                                        adapter.removeContact(i);
                                        updateContactList();
                                    }else{
                                        Toast.makeText(ContactsActivity.this, "Cannot delete user", Toast.LENGTH_SHORT).show();
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

                return true;
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logoutButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final boolean success = httphelper.logOutUserFromServer(ContactsActivity.this, LOGOUT_URL);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        Intent i = new Intent(ContactsActivity.this, MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(ContactsActivity.this, getText(R.string.cannot_logout_error), Toast.LENGTH_SHORT).show();
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
            case R.id.nextButton:
                Intent i2 = new Intent(this, MessageActivity.class);
                startActivity(i2);
                break;
            case R.id.refreshButton:
                updateContactList();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Deleting logged user from contacts list
        //deleteLoggedUserFromList();
        //updateContactList();
    }

    // Updating contacts list
    public void updateContactList(){
        new Thread(new Runnable() {
            Contact[] allContacts;
            @Override
            public void run() {
                try{
                    final JSONArray contacts = httphelper.getContactsFromServer(ContactsActivity.this, CONTACTS_URL);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(contacts != null){
                                JSONObject jsonContact;
                                allContacts = new Contact[contacts.length()];

                                for (int i = 0; i < contacts.length(); i++){
                                    try{
                                        jsonContact = contacts.getJSONObject(i);
                                        allContacts[i] = new Contact(jsonContact.getString("username"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.updateContacts(allContacts);
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

    }


    /*public void deleteLoggedUserFromList() {
        contacts = chatDbHelper.readContacts();
        adapter.updateContacts(contacts);

        if (contacts != null) {
            for (int i = 0; i < contacts.length; i++) {
                if (contacts[i].getmID().compareTo(userId) == 0) {
                    adapter.removeContact(i);
                    break;
                }
            }
        }
    }*/
}
