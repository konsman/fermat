package com.bitdubai.fermat_cbp_api.layer.network_service.negotiation_transmission.exceptions;

import com.bitdubai.fermat_cbp_api.all_definition.exceptions.CBPException;

/**
 * Created by Yordin Alayn on 05.12.15.
 */
public class CantInitializeCommunicationNetworkServiceConnectionManagerException extends CBPException {
    public static final String DEFAULT_MESSAGE = "NEGOTIATION TRANSMISSION - CAN'T INITIALIZE COMMUNICATION NETWORK SERVICE CONNECTION MANAGER";

    public CantInitializeCommunicationNetworkServiceConnectionManagerException(final String message, final Exception cause, final String context, final String possibleReason) {
        super(message, cause, context, possibleReason);
    }
}
