package com.example.branislavnovak.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Branislav Novak on 28-Mar-18.
 */

public class ContactsAdapter extends BaseAdapter {


    private Context mContext;
    private ArrayList<Contact> mContacts;         // list of contacts (Contact.java)
    public static final String PREFERENCES_NAME = "PreferenceFile";

    public ContactsAdapter(Context context) {
        mContext = context;
        mContacts = new ArrayList<Contact>();
    }

    // --------------------------------------------------------------------------------------------- returns the size of list
    @Override
    public int getCount() {
        return mContacts.size();
    }

    // --------------------------------------------------------------------------------------------- returns the item on position
    @Override
    public Object getItem(int position) {
        Object rv = null;
        try{
            rv = mContacts.get(position);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return rv;
    }

    // -------------------------------------- not used, but can be helpful for this kind of projects
    /*
    // --------------------------------------------------------------------------------------------- add one of the elements of list
    public void addContact(Contact contact){
        mContacts.add(contact);
        notifyDataSetChanged();
    }
    */

    // --------------------------------------------------------------------------------------------- removes one of the elements of list
    public void removeContact(int position){
        mContacts.remove(position);
        notifyDataSetChanged();
    }

    // --------------------------------------------------------------------------------------------- updates the list of Contacts
    public void updateContacts(Contact[] contacts){
        mContacts.clear();
        if (contacts != null){
            for (Contact contact : contacts){
                mContacts.add(contact);
            }
        }
        notifyDataSetChanged();
    }

    // --------------------------------------------------------------------------------------------- returns the items ID
    @Override
    public long getItemId(int id) {
        return id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);

            final View finalConvertView = convertView;                                              // needs to be like this, cause of getting a ID for TextView
            final ImageView nextButton = (ImageView) convertView.findViewById(R.id.nextButton);

            ViewHolder holder = new ViewHolder();                                                   // linking the holder with inner class ViewHolder to the row item form .xml file
            holder.firstLetter = (TextView) convertView.findViewById(R.id.firstLetter);
            holder.name = (TextView) convertView.findViewById(R.id.contactName);
            holder.image = (ImageView) convertView.findViewById(R.id.nextButton);

            // sating a random background color on TextView
            holder.firstLetter.setBackgroundColor(getRandomColor());

            convertView.setTag(holder);

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {                                                    // bundle is for saving string of current name and sanding it into other activity
                    Bundle b = new Bundle();
                    TextView tv = finalConvertView.findViewById(R.id.contactName);
                    String name = tv.getText().toString();
                    b.putString("contactName", name);

                    // opening a shared preference to get put the receivers id for the message database linking with sender
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
                    editor.putString("receiverUsername", view.getTag().toString());
                    editor.apply();

                    Intent i = new Intent (mContext, MessageActivity.class);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
            });


        }

        Contact contact = (Contact) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        if(contact != null) {
            if(contact.getmUserName() != null) {
                holder.firstLetter.setText(contact.getmUserName().substring(0, 1).toUpperCase());      // first name letter
            }else{

            }
        }
        holder.firstLetter.setBackgroundColor(getRandomColor());

        //String fullContactName = contact.getmFirstName() + " " + contact.getmLastName();            // making full contact name on contact list

        String full_name = contact.getmUserName();
        holder.name.setText(full_name);
        //holder.image.setTag(contact.getmID());                                                      // setting contact id on button tag

        holder.image.setTag(contact.getmUserName());
        return convertView;
    }

    private class ViewHolder{
        public TextView firstLetter = null;
        public TextView name = null;
        public ImageView image = null;
    }


    private int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
