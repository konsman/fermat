package com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.ReferenceWallet;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletTransaction;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.DealsWithBitcoinWallet;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetWalletContactRegistryException;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactsManager;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetBalanceException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetTransactionsException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantRequestCryptoAddressException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantSendCryptoException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWalletTransaction;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.DealsWithOutgoingExtraUser;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.OutgoingExtraUserManager;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.CantGetTransactionManagerException;
import com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.CantSendFundsException;
import com.bitdubai.fermat_api.layer.dmp_actor.Actor;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.exceptions.CantGetActorAddressBookRegistryException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.exceptions.CantRegisterActorAddressBookException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.ActorAddressBookRegistry;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.DealsWithWalletContacts;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactRecord;
import com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.interfaces.WalletContactsRegistry;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_api.layer.dmp_actor.extra_user.DealsWithExtraUsers;
import com.bitdubai.fermat_api.layer.dmp_actor.extra_user.ExtraUserManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.actor_address_book.interfaces.DealsWithActorAddressBook;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.exceptions.CantGetWalletAddressBookRegistryException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.exceptions.CantRegisterWalletAddressBookException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.interfaces.DealsWithWalletAddressBook;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.interfaces.WalletAddressBookManager;
import com.bitdubai.fermat_cry_api.layer.crypto_module.wallet_address_book.interfaces.WalletAddressBookRegistry;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.CryptoVaultManager;
import com.bitdubai.fermat_cry_api.layer.crypto_vault.DealsWithCryptoVault;
import com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.exceptions.CantCreateOrRegisterActorException;
import com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.exceptions.CantInitializeCryptoWalletManagerException;
import com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.exceptions.CantRequestOrRegisterCryptoAddressException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Class <code>com.bitdubai.fermat_dmp_plugin.layer.niche_wallet_type.crypto_wallet.developer.bitdubai.version_1.structure.NicheWalletTypeCryptoWallet</code>
 * haves all methods for the contacts activity of a bitcoin wallet
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 11/06/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class NicheWalletTypeCryptoWallet implements CryptoWallet, DealsWithActorAddressBook, DealsWithBitcoinWallet, DealsWithCryptoVault, DealsWithErrors, DealsWithExtraUsers, DealsWithOutgoingExtraUser, DealsWithWalletContacts, DealsWithWalletAddressBook {

    /**
     * DealsWithActorAddressBook Interface member variable
     */
    private ActorAddressBookManager actorAddressBookManager;
    private ActorAddressBookRegistry actorAddressBookRegistry;

    /**
     * DealsWithBitcoinWallet Interface member variables.
     */
    private BitcoinWalletManager bitcoinWalletManager;

    /**
     * DealsWithCryptoVault Interface member variables.
     */
    private CryptoVaultManager cryptoVaultManager;

    /**
     * DealsWithErrors Interface member variables.
     */
    private ErrorManager errorManager;

    /**
     * DealsWithExtraUsers Interface member variables.
     */
    private ExtraUserManager extraUserManager;

    /**
     * DealsWithOutgoingExtraUser Interface member variables.
     */
    private OutgoingExtraUserManager outgoingExtraUserManager;

    /**
     * DealsWithWalletContacts Interface member variable
     */
    private WalletContactsManager walletContactsManager;
    private WalletContactsRegistry walletContactsRegistry;

    /**
     * DealsWithWalletAddressBook Interface member variable
     */
    private WalletAddressBookManager walletAddressBookManager;
    private WalletAddressBookRegistry walletAddressBookRegistry;


    public void initialize() throws CantInitializeCryptoWalletManagerException {
        try {
            actorAddressBookRegistry = actorAddressBookManager.getActorAddressBookRegistry();
            walletAddressBookRegistry = walletAddressBookManager.getWalletAddressBookRegistry();
            walletContactsRegistry = walletContactsManager.getWalletContactsRegistry();
        } catch (CantGetActorAddressBookRegistryException|CantGetWalletContactRegistryException|CantGetWalletAddressBookRegistryException e){
            throw new CantInitializeCryptoWalletManagerException(CantInitializeCryptoWalletManagerException.DEFAULT_MESSAGE, e);
        }  catch (Exception e){
            throw new CantInitializeCryptoWalletManagerException(CantInitializeCryptoWalletManagerException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public List<WalletContactRecord> listWalletContacts(UUID walletId) throws CantGetAllWalletContactsException {
        try {
            return walletContactsRegistry.listWalletContacts(walletId);
        } catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetAllWalletContactsException e) {
            throw new CantGetAllWalletContactsException(CantGetAllWalletContactsException.DEFAULT_MESSAGE, e);
        }  catch (Exception e) {
            throw new CantGetAllWalletContactsException(CantGetAllWalletContactsException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public List<WalletContactRecord> listWalletContactsScrolling(UUID walletId, Integer max, Integer offset) throws CantGetAllWalletContactsException {
        try {
            return walletContactsRegistry.listWalletContactsScrolling(walletId, max, offset);
        } catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetAllWalletContactsException e) {
            throw new CantGetAllWalletContactsException(CantGetAllWalletContactsException.DEFAULT_MESSAGE, e);
        } catch (Exception e) {
            throw new CantGetAllWalletContactsException(CantGetAllWalletContactsException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public WalletContactRecord createWalletContact(CryptoAddress receivedCryptoAddress, String actorName, Actors actorType, ReferenceWallet referenceWallet, UUID walletId) throws CantCreateWalletContactException {
        try{
            WalletContactRecord walletContactRecord;
            walletContactRecord = walletContactsRegistry.getWalletContactByNameAndWalletId(actorName, walletId);
            UUID actorId = createActor(actorName, actorType);

            if (walletContactRecord == null)
                return walletContactsRegistry.createWalletContact(actorId, actorName, actorType, receivedCryptoAddress, walletId);
            else if(!(receivedCryptoAddress.getAddress().equals(walletContactRecord.getReceivedCryptoAddress().getAddress())))
                this.updateWalletContact(walletContactRecord.getContactId(), walletContactRecord.getReceivedCryptoAddress(), walletContactRecord.getActorName());

            return  walletContactRecord;
        } catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetWalletContactException e) {
            throw new CantCreateWalletContactException(CantCreateWalletContactException.DEFAULT_MESSAGE, e);
        } catch (CantCreateOrRegisterActorException e) {
            throw new CantCreateWalletContactException(CantCreateWalletContactException.DEFAULT_MESSAGE, e);
        }  catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantCreateWalletContactException e) {
            throw new CantCreateWalletContactException(CantCreateWalletContactException.DEFAULT_MESSAGE, e);
        } catch (CantUpdateWalletContactException e) {
            throw new CantCreateWalletContactException(CantCreateWalletContactException.DEFAULT_MESSAGE, e);
        } catch (Exception e) {
            throw new CantCreateWalletContactException(CantCreateWalletContactException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }

    }

    @Override
    public CryptoAddress requestAddress(UUID deliveredByActorId, Actors deliveredByActorType, String deliveredToActorName, Actors deliveredToActorType, ReferenceWallet referenceWallet, UUID walletId) throws CantRequestCryptoAddressException {
        try {
            CryptoAddress deliveredCryptoAddress;
            deliveredCryptoAddress = requestAndRegisterCryptoAddress(walletId, referenceWallet);
            createAndRegisterActor(deliveredByActorId, deliveredByActorType, deliveredToActorName, deliveredToActorType, deliveredCryptoAddress);
            return deliveredCryptoAddress;
        } catch (CantCreateOrRegisterActorException e) {
            throw new CantRequestCryptoAddressException(CantRequestCryptoAddressException.DEFAULT_MESSAGE, e);
        } catch (CantRequestOrRegisterCryptoAddressException e) {
            throw new CantRequestCryptoAddressException(CantRequestCryptoAddressException.DEFAULT_MESSAGE, e);
        } catch (Exception exception){
            throw new CantRequestCryptoAddressException(CantRequestCryptoAddressException.DEFAULT_MESSAGE, FermatException.wrapException(exception));
        }
    }

    @Override
    public CryptoAddress requestAddress(UUID deliveredByActorId, Actors deliveredByActorType, UUID deliveredToActorId, Actors deliveredToActorType, ReferenceWallet referenceWallet, UUID walletId) throws CantRequestCryptoAddressException {
        try {
            CryptoAddress deliveredCryptoAddress;
            deliveredCryptoAddress = requestAndRegisterCryptoAddress(walletId, referenceWallet);
            actorAddressBookRegistry.registerActorAddressBook(deliveredByActorId, deliveredByActorType, deliveredToActorId, deliveredToActorType, deliveredCryptoAddress);
            return deliveredCryptoAddress;
        } catch (CantRequestOrRegisterCryptoAddressException e) {
            throw new CantRequestCryptoAddressException(CantRequestCryptoAddressException.DEFAULT_MESSAGE, e);
        } catch (CantRegisterActorAddressBookException e) {
            throw new CantRequestCryptoAddressException(CantRequestCryptoAddressException.DEFAULT_MESSAGE, e, "", "Check if all the params are sended.");
        } catch(Exception exception){
            throw new CantRequestCryptoAddressException(CantRequestCryptoAddressException.DEFAULT_MESSAGE, FermatException.wrapException(exception));
        }
    }

    @Override
    public void updateWalletContact(UUID contactId, CryptoAddress receivedCryptoAddress, String actorName) throws CantUpdateWalletContactException {
        try {
            walletContactsRegistry.updateWalletContact(contactId, receivedCryptoAddress, actorName);
        } catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantUpdateWalletContactException e) {
            throw new CantUpdateWalletContactException(CantUpdateWalletContactException.DEFAULT_MESSAGE, e);
        }  catch (Exception e) {
            throw new CantUpdateWalletContactException(CantUpdateWalletContactException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public void deleteWalletContact(UUID contactId) throws CantDeleteWalletContactException {
        try {
            walletContactsRegistry.deleteWalletContact(contactId);
        } catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantDeleteWalletContactException e) {
            throw new CantDeleteWalletContactException(CantDeleteWalletContactException.DEFAULT_MESSAGE, e);
        } catch (Exception e) {
            throw new CantDeleteWalletContactException(CantDeleteWalletContactException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public List<WalletContactRecord> getWalletContactByNameContainsAndWalletId(String actorName, UUID walletId) throws CantGetWalletContactException {
        try {
            return walletContactsRegistry.getWalletContactByNameContainsAndWalletId(actorName, walletId);
        } catch (com.bitdubai.fermat_api.layer.dmp_middleware.wallet_contacts.exceptions.CantGetWalletContactException e) {
            throw new CantGetWalletContactException(CantGetWalletContactException.DEFAULT_MESSAGE, e);
        } catch(Exception exception){
            throw new CantGetWalletContactException(CantGetWalletContactException.DEFAULT_MESSAGE, FermatException.wrapException(exception));
        }
    }

    // TODO:  ESTOS DOS METODOS SON LOS QUE NATALIA/EZE ME TIENEN QUE PASAR PARA YO HACER CORRER LA APP
    @Override
    public long getAvailableBalance(UUID walletId) throws CantGetBalanceException {
        try {
            BitcoinWalletWallet bitcoinWalletWallet = bitcoinWalletManager.loadWallet(walletId);
            return bitcoinWalletWallet.getAvailableBalance().getBalance();
        } catch (com.bitdubai.fermat_api.layer.dmp_basic_wallet.basic_wallet_common_exceptions.CantLoadWalletException e) {
            throw new CantGetBalanceException(CantGetBalanceException.DEFAULT_MESSAGE, e);
        }  catch (com.bitdubai.fermat_api.layer.dmp_basic_wallet.basic_wallet_common_exceptions.CantCalculateBalanceException e) {
            throw new CantGetBalanceException(CantGetBalanceException.DEFAULT_MESSAGE, e);
        } catch(Exception e){
            throw new CantGetBalanceException(CantGetBalanceException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public long getBookBalance(UUID walletId) throws CantGetBalanceException {
        try {
            BitcoinWalletWallet bitcoinWalletWallet = bitcoinWalletManager.loadWallet(walletId);
            return bitcoinWalletWallet.getBookBalance().getBalance();
        } catch (com.bitdubai.fermat_api.layer.dmp_basic_wallet.basic_wallet_common_exceptions.CantLoadWalletException e) {
            throw new CantGetBalanceException(CantGetBalanceException.DEFAULT_MESSAGE, e);
        } catch (com.bitdubai.fermat_api.layer.dmp_basic_wallet.basic_wallet_common_exceptions.CantCalculateBalanceException e) {
            throw new CantGetBalanceException(CantGetBalanceException.DEFAULT_MESSAGE, e);
        } catch (Exception e){
            throw new CantGetBalanceException(CantGetBalanceException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public List<CryptoWalletTransaction> getTransactions(int max, int offset, UUID walletId) throws CantGetTransactionsException {
        try {
            BitcoinWalletWallet bitcoinWalletWallet = bitcoinWalletManager.loadWallet(walletId);
            List<CryptoWalletTransaction> cryptoWalletTransactionList = new ArrayList<>();
            List<BitcoinWalletTransaction> bitcoinWalletTransactionList = bitcoinWalletWallet.getTransactions(max, offset);

            for (BitcoinWalletTransaction bwt : bitcoinWalletTransactionList) {
                cryptoWalletTransactionList.add(enrichTransaction(bwt));
            }

            return cryptoWalletTransactionList;
        } catch (com.bitdubai.fermat_api.layer.dmp_basic_wallet.basic_wallet_common_exceptions.CantLoadWalletException | com.bitdubai.fermat_api.layer.dmp_basic_wallet.basic_wallet_common_exceptions.CantGetTransactionsException e) {
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, e);
        } catch(Exception e){
            throw new CantGetTransactionsException(CantGetTransactionsException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public void send(long cryptoAmount, CryptoAddress destinationAddress, String notes, UUID walletID, UUID deliveredByActorId, Actors deliveredByActorType, UUID deliveredToActorId, Actors deliveredToActorType) throws CantSendCryptoException, InsufficientFundsException {
        try {
            outgoingExtraUserManager.getTransactionManager().send(walletID, destinationAddress, cryptoAmount, notes, deliveredByActorId, deliveredByActorType, deliveredToActorId, deliveredToActorType);
        } catch (com.bitdubai.fermat_api.layer.dmp_transaction.outgoing_extrauser.exceptions.InsufficientFundsException e) {
            throw new InsufficientFundsException(InsufficientFundsException.DEFAULT_MESSAGE, e);
        } catch (CantSendFundsException e) {
            throw new CantSendCryptoException(CantSendCryptoException.DEFAULT_MESSAGE, e);
        } catch (CantGetTransactionManagerException e) {
            throw new CantSendCryptoException(CantSendCryptoException.DEFAULT_MESSAGE, e);
        }  catch (Exception e) {
            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_CRYPTO_WALLET_NICHE_WALLET_TYPE,UnexpectedPluginExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_PLUGIN,e);
            throw new CantSendCryptoException(CantSendCryptoException.DEFAULT_MESSAGE, FermatException.wrapException(e));
        }
    }

    @Override
    public boolean isValidAddress(CryptoAddress cryptoAddress) {
        return cryptoVaultManager.isValidAddress(cryptoAddress);
    }

    /**
     * DealsWithActorAddressBook Interface implementation.
     */
    @Override
    public void setActorAddressBookManager(ActorAddressBookManager actorAddressBookManager) {
        this.actorAddressBookManager = actorAddressBookManager;
    }

    /**
     * DealsWithBitcoinWallet Interface implementation.
     */
    @Override
    public void setBitcoinWalletManager(BitcoinWalletManager bitcoinWalletManager) {
        this.bitcoinWalletManager = bitcoinWalletManager;
    }

    /**
     * DealsWithCryptoVault Interface implementation.
     */
    @Override
    public void setCryptoVaultManager(CryptoVaultManager cryptoVaultManager) {
        this.cryptoVaultManager = cryptoVaultManager;
    }

    /**
     * DealsWithErrors Interface implementation.
     */
    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithExtraUsers Interface implementation.
     */
    @Override
    public void setExtraUserManager(ExtraUserManager extraUserManager) {
        this.extraUserManager = extraUserManager;
    }

    /**
     * DealsWithOutgoingExtraUser Interface implementation.
     */
    @Override
    public void setOutgoingExtraUserManager(OutgoingExtraUserManager outgoingExtraUserManager) {
        this.outgoingExtraUserManager = outgoingExtraUserManager;
    }

    /**
     * DealsWithWalletContacts Interface implementation.
     */
    @Override
    public void setWalletContactsManager(WalletContactsManager walletContactsManager) {
        this.walletContactsManager = walletContactsManager;
    }

    /**
     * DealsWithWalletAddressBook Interface implementation.
     */
    @Override
    public void setWalletAddressBookManager(WalletAddressBookManager walletAddressBookManager) {
        this.walletAddressBookManager = walletAddressBookManager;
    }



    private UUID createAndRegisterActor(UUID deliveredByActorId, Actors deliveredByActorType, String deliveredToActorName, Actors deliveredToActorType, CryptoAddress cryptoAddress) throws CantCreateOrRegisterActorException {
        try {
            UUID deliveredToActorId = createActor(deliveredToActorName, deliveredToActorType);
            actorAddressBookRegistry.registerActorAddressBook(deliveredByActorId, deliveredByActorType, deliveredToActorId, deliveredToActorType, cryptoAddress);
            return deliveredToActorId;
        } catch (CantRegisterActorAddressBookException e) {
            throw new CantCreateOrRegisterActorException(CantCreateOrRegisterActorException.DEFAULT_MESSAGE, e, "", "Check if all the params are sended.");
        }
    }

    private UUID createActor(String actorName, Actors actorType) throws CantCreateOrRegisterActorException {
        switch (actorType){
            case EXTRA_USER:
                Actor actor = extraUserManager.createActor(actorName);
                return actor.getId();
            default:
                throw new CantCreateOrRegisterActorException(CantCreateOrRegisterActorException.DEFAULT_MESSAGE, null, "", "ActorType is not Compatible.");
        }
        // TODO ADD CANTCREATEXTRAUSER EXCEPTION IN PLUGIN EXTRA USER
        //      throw new CantCreateOrRegisterActorException(CantCreateOrRegisterActorException.DEFAULT_MESSAGE, e, "", "Check if all the params are sended.");
    }

    private CryptoAddress requestAndRegisterCryptoAddress(UUID walletId, ReferenceWallet referenceWallet) throws CantRequestOrRegisterCryptoAddressException {
        CryptoAddress cryptoAddress;
        switch (referenceWallet){
            case BASIC_WALLET_BITCOIN_WALLET:
                cryptoAddress = cryptoVaultManager.getAddress();
                break;
            default:
                throw new CantRequestOrRegisterCryptoAddressException(CantRequestOrRegisterCryptoAddressException.DEFAULT_MESSAGE, null, "", "ReferenceWallet is not Compatible.");
        }
        // TODO ADD CANT GET ADDRESS EXCEPTION IN PLUGIN CRYPTO VAULT
        try {
            walletAddressBookRegistry.registerWalletCryptoAddressBook(cryptoAddress, referenceWallet, walletId);
            return cryptoAddress;
        } catch (CantRegisterWalletAddressBookException e) {
            throw new CantRequestOrRegisterCryptoAddressException(CantRequestOrRegisterCryptoAddressException.DEFAULT_MESSAGE, e, "", "Check if all the params are sended.");
        }
    }

    private CryptoWalletTransaction enrichTransaction(BitcoinWalletTransaction bitcoinWalletTransaction) {
        String involvedActorName = null;
        switch(bitcoinWalletTransaction.getTransactionType()) {
            case CREDIT:
                involvedActorName = getActorNameByActorIdAndType(bitcoinWalletTransaction.getActorFrom(), bitcoinWalletTransaction.getActorFromType());
                break;
            case DEBIT:
                try {
                    WalletContactRecord walletContactRecord = walletContactsRegistry.getWalletContactByActorId(bitcoinWalletTransaction.getActorTo());
                    involvedActorName = walletContactRecord.getActorName();
                } catch(Exception e) {
                    involvedActorName = getActorNameByActorIdAndType(bitcoinWalletTransaction.getActorTo(), bitcoinWalletTransaction.getActorToType());
                }
                break;
        }
        return new CryptoWalletNicheWalletTypeTransaction(bitcoinWalletTransaction, involvedActorName);
    }

    private String getActorNameByActorIdAndType(UUID actorId, Actors actorType) {
        switch (actorType) {
            case EXTRA_USER:
                Actor actor = extraUserManager.getActor(actorId);
                return actor.getName();
            default:
                return null;
        }
        // TODO ADD cant get extrauser EXCEPTION IN PLUGIN EXTRA USER
    }
}
