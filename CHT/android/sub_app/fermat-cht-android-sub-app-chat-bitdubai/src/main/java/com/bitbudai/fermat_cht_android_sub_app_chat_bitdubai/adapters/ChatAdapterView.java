package com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.models.ChatMessage;
import com.bitbudai.fermat_cht_android_sub_app_chat_bitdubai.sessions.ChatSession;
import com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces.FermatSession;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_cht_android_sub_app_chat_bitdubai.R;
import com.bitdubai.fermat_cht_api.all_definition.enums.ChatStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.MessageStatus;
import com.bitdubai.fermat_cht_api.all_definition.enums.TypeMessage;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetMessageException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantGetNetworkServicePublicKeyException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveChatException;
import com.bitdubai.fermat_cht_api.all_definition.exceptions.CantSaveMessageException;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Chat;
import com.bitdubai.fermat_cht_api.layer.middleware.interfaces.Contact;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.ChatImpl;
import com.bitdubai.fermat_cht_api.layer.middleware.utils.MessageImpl;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatManager;
import com.bitdubai.fermat_cht_api.layer.sup_app_module.interfaces.ChatModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

//import com.bitdubai.fermat_ccp_plugin.layer.network_service.intra_user.developer.bitdubai.version_1.structure.MessageType;

/**
 * Created by miguel on 22/01/16.
 * Updated by Jose Cardozo josejcb (josejcb89@gmail.com) on 09/01/16.
 */

public class ChatAdapterView extends LinearLayout {

    private RecyclerView messagesContainer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private ChatManager chatManager;
    private ChatModuleManager moduleManager;
    private ErrorManager errorManager;
    private ChatSession chatSession;
    private FermatSession appSession;
    private EditText messageET;
    private ViewGroup rootView;
    private String leftName;
    private String rightName;
    private UUID chatid;
    private UUID contactid;
    private int background = -1;
    private String remotepk;
    private PlatformComponentType remotepct;
    private boolean loadDummyData = false;
    private boolean chatwascreate=false;

    public ChatAdapterView(Context context, ArrayList<ChatMessage> chatHistory,
                           ChatManager chatManager, ChatModuleManager moduleManager,
                           ErrorManager errorManager, ChatSession chatSession, FermatSession appSession, int background) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView(inflater.inflate(R.layout.chat, (rootView != null) ? rootView : null));
        this.chatHistory=chatHistory;
        this.chatManager=chatManager;
        this.moduleManager=moduleManager;
        this.errorManager=errorManager;
        this.chatSession=chatSession;
        this.appSession=appSession;
        //this.background=background;
        initControls();
    }

    public ChatAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatAdapterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    void findvalues(Contact contact){ //With contact Id find chatid,pkremote,actortype
        try {
            if (contact != null){
                remotepk = contact.getRemoteActorPublicKey();
                remotepct = contact.getRemoteActorType();
                contactid=contact.getContactId();
                leftName=contact.getAlias();
                for (int i = 0; i < chatManager.getMessages().size(); i++) {
                    if (contactid.equals(chatManager.getMessages().get(i).getContactId())) {
                        chatid = chatManager.getMessages().get(i).getChatId();
                    }
                }
            }
        }catch (CantGetMessageException e) {
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }catch(Exception e){
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    public void whattodo(){
        try {
            System.out.println("WHOCALME NOW:" + chatSession.getData("whocallme"));
            if (chatSession.getData("whocallme").equals("chatlist")) {
                findvalues((Contact)chatSession.getData("contactid"));//if I choose a chat, this will retrieve the chatid
                chatwascreate = true;
            } else if (chatSession.getData("whocallme").equals("contact")) {  //fragment contact call this fragment
                findvalues(chatSession.getSelectedContact());//if I choose a contact, this will search the chat previously created with this contact
                if (chatid != null) {//Here it is define if we need to create a new chat or just add the message to chat created previously
                    chatwascreate = true;
                } else {
                    chatwascreate = false;
                }
            }
        }catch(Exception e){
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    public void findmessage(){
        String message;
        String inorout;
        ChatMessage msg;
        Chat chat;
        int messsize;
        try {
            setChatHistory(null);
            if(chatid!=null){
                chat=chatManager.getChatByChatId(chatid);
            }else{
                chat=chatSession.getSelectedChat();
            }

            if(chat!=null)
                chatid=chat.getChatId();
            if (chatHistory == null) {
                chatHistory = new ArrayList<ChatMessage>();
            }

            if(chatid!=null){
                messsize=chatManager.getMessageByChatId(chatid).size();

                for (int i = 0; i < messsize; i++) {
                    msg = new ChatMessage();
                    message=chatManager.getMessageByChatId(chatid).get(i).getMessage();
                    inorout=chatManager.getMessageByChatId(chatid).get(i).getType().toString();
                    msg.setId(chatManager.getMessageByChatId(chatid).get(i).getMessageId());
                    if (inorout == TypeMessage.OUTGOING.toString()) msg.setMe(true);
                    else msg.setMe(false);
                    msg.setDate(DateFormat.getDateTimeInstance().format(chatManager.getMessageByChatId(chatid).get(i).getMessageDate()));//chatManager.getMessageByChatId(chatid).get(i).getMessageDate().toString()
                    msg.setUserId(chatManager.getMessageByChatId(chatid).get(i).getContactId());
                    msg.setMessage(message);
                    chatHistory.add(msg);
                }
                adapter = new ChatAdapter(this.getContext(), (chatHistory != null) ? chatHistory : new ArrayList<ChatMessage>());
                messagesContainer.setAdapter(adapter);
            }else{
                Toast.makeText(getContext(),"Waiting for chat message", Toast.LENGTH_SHORT).show();
            }
        }catch (CantGetMessageException e) {
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }catch (Exception e){
            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
        }
    }

    public void initControls() {
        messagesContainer = (RecyclerView) findViewById(R.id.messagesContainer);
        messagesContainer.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (Button) findViewById(R.id.chatSendButton);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        TextView meLabel = (TextView) findViewById(R.id.meLbl);
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        if(chatSession!= null){
            whattodo();
            findmessage();
        }
        if (rightName != null) {
            meLabel.setText(rightName);
        } else {
            meLabel.setText("Yo");
        }

        if (leftName != null ) {
            companionLabel.setText(leftName);
        } else {
            companionLabel.setText("Contacto");
        }

        if (background != -1) {
            container.setBackgroundColor(background);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                try {
                    ChatImpl chat = new ChatImpl();
                    MessageImpl message = new MessageImpl();
                    Long dv = System.currentTimeMillis();

                    if (chatwascreate) {
                        chat=(ChatImpl)chatManager.getChatByChatId(chatid);
                        chatManager.saveChat(chat);

                        message.setChatId(chatid);
                        message.setMessageId(UUID.randomUUID());
                        message.setMessage(messageText);
                        message.setMessageDate(new Timestamp(dv));
                        message.setStatus(MessageStatus.CREATED);
                        message.setType(TypeMessage.OUTGOING);
                        message.setContactId(contactid);
                        chatManager.saveMessage(message);
                    } else {
                        UUID newchatid = UUID.randomUUID();
                        chat.setChatId(newchatid);
                        chat.setObjectId(UUID.randomUUID());
                        chat.setStatus(ChatStatus.VISSIBLE);
                        chat.setChatName("DeathNote");
                        chat.setDate(new Timestamp(dv));
                        chat.setLastMessageDate(new Timestamp(dv));
                        chat.setLocalActorPublicKey(chatManager.getNetworkServicePublicKey());
                        chat.setLocalActorType(PlatformComponentType.ACTOR_ASSET_ISSUER);
                        chat.setRemoteActorPublicKey(remotepk);
                        chat.setRemoteActorType(remotepct);
                        chatManager.saveChat(chat);

                        message.setChatId(newchatid);
                        message.setMessageId(UUID.randomUUID());
                        message.setMessage(messageText);
                        message.setMessageDate(new Timestamp(dv));
                        message.setStatus(MessageStatus.CREATED);
                        message.setType(TypeMessage.OUTGOING);
                        message.setContactId(contactid);
                        chatManager.saveMessage(message);
                    }

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setId(UUID.randomUUID());//dummy
                    chatMessage.setMessage(messageText);
                    chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    chatMessage.setMe(true);
                    messageET.setText("");
                    adapter = new ChatAdapter(getContext(), (chatHistory != null) ? chatHistory : new ArrayList<ChatMessage>());
                    messagesContainer.setAdapter(adapter);
                    displayMessage(chatMessage);


                } catch (CantSaveMessageException e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (CantSaveChatException e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (CantGetNetworkServicePublicKeyException e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                } catch (Exception e) {
                    errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        try {
                            findmessage();
                        } catch (Exception e) {
                            //TODO: fix this
                            errorManager.reportUnexpectedSubAppException(SubApps.CHT_CHAT, UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }



    private void loadDummyHistory() {

        if (loadDummyData) {

            chatHistory = new ArrayList<ChatMessage>();

            ChatMessage msg = new ChatMessage();
            msg.setId(UUID.randomUUID());
            msg.setMe(false);
            msg.setMessage("Hola");
            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg);
            ChatMessage msg1 = new ChatMessage();
            msg1.setId(UUID.randomUUID());
            msg1.setMe(true);
            msg1.setMessage("como andas?");
            msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            chatHistory.add(msg1);
        }
        if (chatHistory == null) {
            chatHistory = new ArrayList<ChatMessage>();
        }

        adapter = new ChatAdapter(this.getContext(), (chatHistory != null) ? chatHistory : new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);

        for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }
    }

    public void displayMessage(ChatMessage message) {
        adapter.addItem(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    public void refreshEvents() {
        whattodo();
        findmessage();
        scroll();
    }

    private void scroll() {
        messagesContainer.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void setRootView(ViewGroup rootView) {
        this.rootView = rootView;
    }

    public void addLeftName(String leftName) {
        this.leftName = leftName;
    }

    public void addRightName(String rightName) {
        this.rightName = rightName;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    private void setChatHistory(ArrayList<ChatMessage> chatHistory) {
        this.chatHistory = chatHistory;
    }

    private void setChatManager(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    private void setModuleManager(ChatModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    private void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    private void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    private void setAppSession(FermatSession appSession) {
        this.appSession = appSession;
    }

    private void loadDummyHistory(boolean loadDummyData) {
        this.loadDummyData = loadDummyData;
    }

    public static class Builder {

        private Context context;
        private ViewGroup rootView;
        private ArrayList<ChatMessage> chatHistory;
        private ChatManager chatManager;
        private ChatModuleManager moduleManager;
        private ErrorManager errorManager;
        private ChatSession chatSession;
        private FermatSession appSession;
        private boolean loadDummyData = false;
        private int background = -1;
        private float chatTextSize;
        private int chatTextColor;
        private float dateTextSize;
        private int dateTextColor;
        String rightName;
        String leftName;
        Button customButtom;
        EditText editText;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder insertInto(ViewGroup rootView) {
            this.rootView = rootView;
            return this;
        }

        public Builder addRightName(String rightName) {
            this.rightName = rightName;
            return this;
        }

        public Builder addLeftName(String leftName) {
            this.leftName = leftName;
            return this;
        }

        public void addCustomSendButtom(Button customButtom) {
            this.customButtom = customButtom;
        }

        public void addCustomEditText(EditText customEditText) {
            this.editText = customEditText;
        }

        public Builder setBackground(int background) {
            this.background = background;
            return this;
        }

        public Builder addChatHistory(ArrayList<ChatMessage> chatHistory) {
            this.chatHistory = chatHistory;
            return this;
        }

        public Builder addChatManager(ChatManager chatManager) {
            this.chatManager = chatManager;
            return this;
        }

        public Builder addModuleManager(ChatModuleManager moduleManager) {
            this.moduleManager = moduleManager;
            return this;
        }

        public Builder addErrorManager(ErrorManager errorManager) {
            this.errorManager = errorManager;
            return this;
        }

        public Builder addChatSession(ChatSession chatSession) {
            this.chatSession = chatSession;
            return this;
        }

        public Builder addAppSession(FermatSession appSession) {
            this.appSession = appSession;
            return this;
        }

        public void loadDummyData(boolean loadDummyData) {
            this.loadDummyData = loadDummyData;
        }

        public ChatAdapterView build() {
            ChatAdapterView chatView = new ChatAdapterView(context, chatHistory,
                    chatManager, moduleManager, errorManager, chatSession, appSession, background);
            if (rootView != null) {
                chatView.setRootView(rootView);
            }
            if (chatHistory != null) {
                chatView.setChatHistory(chatHistory);
            }
            if (chatManager != null) {
                chatView.setChatManager(chatManager);
            }
            if (moduleManager != null) {
                chatView.setModuleManager(moduleManager);
            }
            if (errorManager != null) {
                chatView.setErrorManager(errorManager);
            }
            if (chatSession != null) {
                chatView.setChatSession(chatSession);
            }
            if (appSession != null) {
                chatView.setAppSession(appSession);
            }
            if (leftName != null) {
                chatView.addLeftName(leftName);
            }
            if (rightName != null) {
                chatView.addRightName(rightName);
            }
            if (background != -1) {
                chatView.setBackground(background);
            }
            chatView.loadDummyHistory(loadDummyData);
            return chatView;
        }
    }

// extends ArrayAdapter {
//    ArrayList<String> datos=new ArrayList<String>();
//
//public ChatAdapterView(Context context,ArrayList datos) {
//    super(context, R.layout.listviewme, datos);
//    this.datos=datos;
//
//}
//
//            public View getView(int position, View convertView, ViewGroup parent) {
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                View item = inflater.inflate(R.layout.listviewme, null);
//
//                if(!datos.isEmpty()) {
//                    if (datos.get(position).split("@#@#")[0].equals("OUTGOING")) {
//                        item = inflater.inflate(R.layout.listviewme, null);
//                        TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
//                        //           TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblSubTitulo);
//                        System.out.println("*************DATALEFT:" + Arrays.toString(datos.toArray()));
//                        System.out.println("*************PositionLEFT:" + position);
//
//                        try {
//
//                            lblTitulo.setText(datos.get(position).split("@#@#")[1]);
//
//                        } catch (NullPointerException e) {
//                            System.out.print("Null on position:" + position);
//                }
//            } else {
//                item = inflater.inflate(R.layout.listviewyou, null);
//                TextView lblTitulo = (TextView) item.findViewById(R.id.LblTitulo);
//       //         TextView lblSubtitulo = (TextView) item.findViewById(R.id.LblSubTitulo);
//                System.out.println("*************DATARIGHT:" + Arrays.toString(datos.toArray()));
//                System.out.println("*************PositionRIGHT:" + position);
//
//        try {
//
//        lblTitulo.setText(datos.get(position).split("@#@#")[1]);
//
//        } catch (NullPointerException e) {
//        System.out.print("Null on position:" + position);
//        }
//        }
//        }
//        return(item);

//    }
//
//    public void refreshEvents(ArrayList<String> datos) {
//        this.datos=datos;
//
//        notifyDataSetChanged();
//
//    }


}