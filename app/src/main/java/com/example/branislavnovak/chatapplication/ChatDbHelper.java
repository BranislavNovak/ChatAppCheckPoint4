package com.example.branislavnovak.chatapplication;

/**
 * Created by Branislav Novak on 23-Apr-18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ChatDbHelper /*extends SQLiteOpenHelper */{
/*
    public static final String DATABASE_NAME = "chat.db";
    public static final int DATABASE_VERSION = 1;

    // --------------------------------------------------------------------------------------------- contacts DataBase
    public static final String TABLE_NAME_CONTACTS = "contacts";
    public static final String COLUMN_CONTACT_ID = "ContactId";                                     // can also be an intiger
    public static final String COLUMN_USER_NAME = "UserName";
    public static final String COLUMN_FIRST_NAME = "FirstName";
    public static final String COLUMN_LAST_NAME = "LastName";

    // --------------------------------------------------------------------------------------------- messages DataBase
    public static final String TABLE_NAME_MESSAGES = "messages";
    public static final String COLUMN_MESSAGE_ID = "MessageId";
    public static final String COLUMN_SENDER_ID = "SenderId";
    public static final String COLUMN_RECEIVER_ID = "ReceiverId";
    public static final String COLUMN_MESSAGE = "Message";

    // private SQLiteOpenHelper mDb = null;                                                            // creating a SQLiteOpenHelper object

    public ChatDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
                COLUMN_CONTACT_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_NAME_MESSAGES + " (" +
                COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_SENDER_ID + " TEXT, " +
                COLUMN_RECEIVER_ID + " TEXT, " +
                COLUMN_MESSAGE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // --------------------------------------------------------------------------------------------- insert CONTACT
    public void insertContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();                                                  // enabling overwriting the DataBase

        ContentValues values = new ContentValues();                                                 // creating new ContentValues object
        values.put(COLUMN_CONTACT_ID, contact.getmID());
        values.put(COLUMN_USER_NAME, contact.getmUserName());
        values.put(COLUMN_FIRST_NAME, contact.getmFirstName());
        values.put(COLUMN_LAST_NAME, contact.getmLastName());

        db.insert(TABLE_NAME_CONTACTS, null, values);                                 // inserting the element of database
        close();

    }

    // --------------------------------------------------------------------------------------------- insert MESSAGE
    public void insertMessage(Message message){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE_ID, message.getmMessageId());
        values.put(COLUMN_SENDER_ID, message.getmSenderId());
        values.put(COLUMN_RECEIVER_ID, message.getmReceiverId());
        values.put(COLUMN_MESSAGE, message.getmMessage());

        db.insert(TABLE_NAME_MESSAGES, null, values);
    }

    // --------------------------------------------------------------------------------------------- read CONTACTS
    public Contact[] readContacts(){
        SQLiteDatabase db = getReadableDatabase();                                                  // enabling reading from DataBase
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, null, null);

        if(cursor.getCount() <= 0){
            return null;
        }

        Contact[] contacts = new Contact[cursor.getCount()];
        int i = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            contacts[i++] = createContact(cursor);
        }

        close();
        return contacts;
    }

    // --------------------------------------------------------------------------------------------- read MESSAGES
    public Message[] readMessages(String sender, String receiver){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGES, null, "(SenderId =? AND ReceiverId =?) OR (SenderId =? AND ReceiverId =?)", new String[] {sender,receiver,receiver,sender}, null, null, null, null);

        if(cursor.getCount() <= 0){
            return null;
        }


        Message[] messages = new Message[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            messages[i++] = createMessage(cursor);
        }

        close();
        return messages;
    }

    // -------------------------------------- not used, but can be helpful for this kind of projects

    // --------------------------------------------------------------------------------------------- read CONTACT
    public Contact readContact(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, COLUMN_CONTACT_ID + "=?", new String[] {id}, null, null, null);
        cursor.moveToFirst();

        Contact contact = createContact(cursor);

        close();
        return contact;
    }

    // --------------------------------------------------------------------------------------------- read MESSAGE
    public Message readMessage(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MESSAGES, null, COLUMN_MESSAGE_ID + "=?", new String[] {id}, null, null, null);
        cursor.moveToFirst();

        Message message = createMessage(cursor);

        close();
        return message;
    }

    // -------------------------------------- not used, but can be helpful for this kind of projects

    // --------------------------------------------------------------------------------------------- delete CONTACT
    public void deleteContact(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_CONTACTS, COLUMN_CONTACT_ID + "=?", new String[] {id});
        close();
    }

    // --------------------------------------------------------------------------------------------- delete MESSAGE
    public void deleteMessage(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_MESSAGES, COLUMN_MESSAGE_ID + "=?", new String[] {id});
        close();
    }

    // --------------------------------------------------------------------------------------------- create CONTACT
    private static Contact createContact(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID));
        String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
        String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));

        //return new Contact(id, userName, firstName, lastName);
        return new Contact(userName);
    }


    // --------------------------------------------------------------------------------------------- create MESSAGE
    public Message createMessage(Cursor cursor){
        String messageId = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_ID));
        String senderId = cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_ID));
        String receiverId = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_ID));
        String messages = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));

        return new Message(messageId, senderId, receiverId, messages);
    }
    */
}

