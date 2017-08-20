package com.legalimpurity.ChattingSampleApplication.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.legalimpurity.ChattingSampleApplication.R;
import com.legalimpurity.ChattingSampleApplication.objects.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.text.DateFormat;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatItemHolder>{

    private LinkedHashMap<String,ChatMessage> chatMessages = new LinkedHashMap<String,ChatMessage>();
    List<String> indexes = new ArrayList<String>(chatMessages.keySet());
    private Activity act;

    public ChatAdapter(Activity act)
    {
        this.act = act;
    }

    public void addChatMessage(ChatMessage chatObj)
    {
        this.chatMessages.put(chatObj.getGuid(),chatObj);
        indexes = new ArrayList<String>(chatMessages.keySet());
        notifyDataSetChanged();
    }

    public void changeChatMessage(ChatMessage chatObj)
    {
        this.chatMessages.put(chatObj.getGuid(),chatObj);
        indexes = new ArrayList<String>(chatMessages.keySet());
        notifyDataSetChanged();
    }

    public void removeChatMessage(ChatMessage chatObj)
    {
        this.chatMessages.remove(chatObj.getGuid());
        indexes = new ArrayList<String>(chatMessages.keySet());
        notifyDataSetChanged();
    }

    @Override
    public ChatItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(act).inflate(R.layout.message_list_item, parent,false);
        return new ChatItemHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ChatItemHolder holder, int position) {
        ChatMessage ro = chatMessages.get(indexes.get(position));
        holder.bind(ro);
    }

    @Override
    public int getItemCount() {
        if(chatMessages == null)
            return 0;
        return chatMessages.size();
    }

    public class ChatItemHolder extends RecyclerView.ViewHolder
    {

        private TextView message_user;
        private TextView message_time;
        private TextView message_text;
        private View root_view;

        private ChatItemHolder(View itemView) {
            super(itemView);
            root_view = (View) itemView.findViewById(R.id.root_view);
            message_user = (TextView) itemView.findViewById(R.id.message_user);
            message_time = (TextView) itemView.findViewById(R.id.message_time);
            message_text = (TextView) itemView.findViewById(R.id.message_text);
        }

        void bind(final ChatMessage cm)
        {
            message_user.setText(cm.getMessageUser());
            message_time.setText(DateFormat.getDateTimeInstance().format(cm.getMessageTime()));
            message_text.setText(cm.getMessageText());
        }
    }

}
