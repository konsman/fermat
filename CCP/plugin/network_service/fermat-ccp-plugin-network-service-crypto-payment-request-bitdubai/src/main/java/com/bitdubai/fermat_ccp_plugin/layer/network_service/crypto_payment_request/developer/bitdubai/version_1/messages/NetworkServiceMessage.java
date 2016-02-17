package com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.messages;

import com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.enums.MessageTypes;
import com.google.gson.Gson;

import java.util.UUID;

/**
 * The interface <code>com.bitdubai.fermat_ccp_plugin.layer.network_service.crypto_payment_request.developer.bitdubai.version_1.messages.NetworkServiceMessage</code>
 * indicates all the basic functionality of a network service message,
 * <p>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 08/10/2015.
 */
public class NetworkServiceMessage {

    private MessageTypes messageType;
    private String identitySender;
    private String actorDestination;

    public NetworkServiceMessage(final MessageTypes messageType,String identitySender,String actorDestination) {
        this.messageType = messageType;
        this.identitySender = identitySender;
        this.actorDestination = actorDestination;
    }



    public NetworkServiceMessage(final MessageTypes messageType) {
        this.messageType = messageType;
    }

    public String toJson() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public MessageTypes getMessageType() {
        return messageType;
    }

    public String getIdentitySender() {
        return identitySender;
    }

    public String getActorDestination() {
        return actorDestination;
    }

}
