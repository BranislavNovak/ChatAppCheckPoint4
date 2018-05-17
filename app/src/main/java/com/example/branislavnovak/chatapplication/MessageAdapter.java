package com.example.branislavnovak.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Branislav Novak on 01-Apr-18.
 */

public class MessageAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Message> mMessages;

    public static final String PREFERENCES_NAME = "PreferenceFile";

    public MessageAdapter(Context context) {
        mContext = context;
        mMessages = new ArrayList<Message>();
    }

    // -------------------------------------- not used, but can be helpful for this kind of projects
    /*
    // ---------------------------------------------------------------------------------------------
    public void addMessage(Message model){
         mMessages.add(model);
         notifyDataSetChanged();
    }

    // ---------------------------------------------------------------------------------------------
    public void removeMessage(Message model){
        mMessages.remove(model);
        notifyDataSetChanged();
    }
    */
    // ---------------------------------------------------------------------------------------------
    public void updateMessages(Message[] messages){
        mMessages.clear();
        if (messages != null){
            for(Message message : messages){
                mMessages.add(message);
            }
        }
        notifyDataSetChanged();
    }
    // ---------------------------------------------------------------------------------------------

    /*public void addMessages(Message[] messages) {
        mMessages.clear();
        if (messages != null) {
            Collections.addAll(mM)
        }
        notifyDataSetChanged();
    }*/
    // ---------------------------------------------------------------------------------------------
    @Override
    public int getCount() {                                                                         // returns the size of list
        return mMessages.size();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Object getItem(int position) {                                                           // returns the item on the position
        Object rv = null;
        try{
            rv = mMessages.get(position);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return rv;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public long getItemId(int id) {
        return id;
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_message, null);

            ViewHolder holder = new ViewHolder();

            // linking the holder with inner class ViewHolder to the row item message from .xml file
            holder.inboxMessage = (TextView) convertView.findViewById(R.id.inboxMessage);
            convertView.setTag(holder);
        }

        Message message = (Message) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.inboxMessage.setText(message.getmMessage());


        holder.inboxMessage.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.inboxMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        SharedPreferences prefs = mContext.getSharedPreferences(PREFERENCES_NAME, mContext.MODE_PRIVATE);
        String loggedInUsername = prefs.getString("loggedinUsername", null);

        if((message.getmSenderId().compareTo(loggedInUsername)) == 0){
            holder.inboxMessage.setGravity(Gravity.RIGHT | Gravity.CENTER);
            holder.inboxMessage.setTextColor(Color.rgb(255,64,129));
            holder.inboxMessage.setBackgroundColor(Color.argb(20, 242, 242, 242));
        }else{
            holder.inboxMessage.setGravity(Gravity.LEFT | Gravity.CENTER);
            holder.inboxMessage.setTextColor(Color.rgb(255,64,129));
            holder.inboxMessage.setBackgroundColor(Color.argb(20, 242, 242, 242));
        }

        return convertView;
    }

    public class ViewHolder{
        public TextView inboxMessage = null;
    }
}
