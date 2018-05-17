package com.example.branislavnovak.chatapplication;

/**
 * Created by Branislav Novak on 23-Apr-18.
 */

public class Message {
    //private String mMessageId;
    private String mSenderId;
    //private String mReceiverId;
    private String mMessage;

    public Message(String mSenderId, String mMessage) {
        //this.mMessageId = mMessageId;
        this.mSenderId = mSenderId;
        //this.mReceiverId = mReceiverId;
        this.mMessage = mMessage;
    }
    /*public String getmMessageId() {
        return mMessageId;
    }*/

    public String getmSenderId() {
        return mSenderId;
    }

    /*public String getmReceiverId() {
        return mReceiverId;
    }*/

    public String getmMessage() {
        return mMessage;
    }
}
