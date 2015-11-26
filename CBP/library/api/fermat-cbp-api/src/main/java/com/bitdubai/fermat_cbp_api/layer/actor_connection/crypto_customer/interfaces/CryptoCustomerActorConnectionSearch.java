package com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_customer.interfaces;

import com.bitdubai.fermat_api.layer.actor_connection.common.abstract_classes.ActorConnectionSearch;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_customer.utils.CryptoCustomerActorConnection;
import com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_customer.utils.CryptoCustomerActorIdentity;

/**
 * The class <code>com.bitdubai.fermat_cbp_api.layer.actor_connection.crypto_broker.interfaces.CryptoCustomerActorConnectionSearch</code>
 * represents a crypto broker actor connection search.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 20/11/2015.
 */
public abstract class CryptoCustomerActorConnectionSearch extends ActorConnectionSearch<CryptoCustomerActorIdentity, CryptoCustomerActorConnection> {

    public CryptoCustomerActorConnectionSearch(final CryptoCustomerActorIdentity actorIdentity) {
        super(actorIdentity);
    }

}
