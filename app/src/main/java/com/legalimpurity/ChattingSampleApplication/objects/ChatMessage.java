package com.legalimpurity.ChattingSampleApplication.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@IgnoreExtraProperties
public class ChatMessage  implements Parcelable {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String guid;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
        this.guid = UUID.randomUUID().toString().replace("-", "");
    }

    // Required for FireBase
    public ChatMessage(){
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("messageText", messageText);
        result.put("messageUser", messageUser);
        result.put("messageTime", messageTime);
        result.put("guid", guid);
        return result;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.messageText,
                this.messageUser,
                this.guid
        });

        dest.writeLongArray(new long[] {
                this.messageTime
        });
    }

    public ChatMessage(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.messageText = data[0];
        this.messageUser = data[1];
        this.guid = data[2];

        long[] data2 = new long[1];

        in.readLongArray(data2);
        messageTime = data2[0];
    }

}