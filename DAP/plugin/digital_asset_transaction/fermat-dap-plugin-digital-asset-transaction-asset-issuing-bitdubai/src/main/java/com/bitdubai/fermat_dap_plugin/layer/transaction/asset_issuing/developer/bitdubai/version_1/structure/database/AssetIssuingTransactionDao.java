package com.bitdubai.fermat_dap_plugin.layer.transaction.asset_issuing.developer.bitdubai.version_1.structure.database;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.transaction_transference_protocol.ProtocolStatus;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantExecuteQueryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.TransactionStatus;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.asset_issuing.exceptions.CantExecuteDatabaseOperationException;
import com.bitdubai.fermat_dap_plugin.layer.transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantCheckAssetIssuingProgressException;
import com.bitdubai.fermat_dap_plugin.layer.transaction.asset_issuing.developer.bitdubai.version_1.exceptions.CantPersistDigitalAssetException;
import com.bitdubai.fermat_dap_plugin.layer.transaction.asset_issuing.developer.bitdubai.version_1.exceptions.UnexpectedResultReturnedFromDatabaseException;

import java.util.List;
import java.util.UUID;

/**
 * Created by Manuel Perez (darkpriestrelative@gmail.com) on 31/08/15.
 */
public class AssetIssuingTransactionDao {

    UUID pluginId;
    Database database;

    PluginDatabaseSystem pluginDatabaseSystem;

    public AssetIssuingTransactionDao(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId) throws CantExecuteDatabaseOperationException {

        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginId = pluginId;

        database = openDatabase();
        database.closeDatabase();
    }

    private DatabaseTable getDatabaseTable(String tableName){
        DatabaseTable assetIssuingDatabaseTable = database.getTable(tableName);
        return assetIssuingDatabaseTable;
    }

    private Database openDatabase() throws CantExecuteDatabaseOperationException {
        try {
            return pluginDatabaseSystem.openDatabase(pluginId, AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DATABASE);
        } catch (CantOpenDatabaseException | DatabaseNotFoundException exception) {
            throw new CantExecuteDatabaseOperationException(exception, "Opening the Asset Issuing Transaction Database", "Error in database plugin.");
        }
    }

    public void updateTransactionProtocolStatus(UUID transactionID, ProtocolStatus protocolStatus) throws CantExecuteQueryException, UnexpectedResultReturnedFromDatabaseException {
        try{
            this.openDatabase();
            DatabaseTable databaseTable=this.database.getTable(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_ASSET_ISSUING_TABLE_NAME);
            databaseTable.setStringFilter(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_FIRST_KEY_COLUMN, transactionID.toString(), DatabaseFilterType.EQUAL);
            databaseTable.loadToMemory();
            DatabaseTransaction databaseTransaction=this.database.newTransaction();
            DatabaseTableRecord databaseTableRecord;
            List<DatabaseTableRecord> databaseTableRecords=databaseTable.getRecords();
            if (databaseTableRecords.size() > 1)
                throw new UnexpectedResultReturnedFromDatabaseException("Unexpected result. More than value returned.",  "Transaction ID:" + transactionID+ " Protocol Status:" + protocolStatus.toString());
            else {
                databaseTableRecord = databaseTableRecords.get(0);
            }
            databaseTableRecord.setStringValue(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_ASSET_ISSUING_PROTOCOL_STATUS, protocolStatus.toString());
            databaseTransaction.addRecordToUpdate(databaseTable, databaseTableRecord);
        } catch (CantExecuteDatabaseOperationException exception) {
            throw new CantExecuteQueryException(CantExecuteDatabaseOperationException.DEFAULT_MESSAGE,exception, "Trying to update "+AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_ASSET_ISSUING_TABLE_NAME,"Check the cause");
        } catch (CantLoadTableToMemoryException exception) {
            throw new CantExecuteQueryException(CantLoadTableToMemoryException.DEFAULT_MESSAGE, exception,"Trying to update "+AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_ASSET_ISSUING_TABLE_NAME,"Check the cause");
        } catch (Exception exception){
            throw new CantExecuteQueryException(CantExecuteQueryException.DEFAULT_MESSAGE, FermatException.wrapException(exception),"Trying to update "+AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_ASSET_ISSUING_TABLE_NAME,"Check the cause");
        }
    }

    public void persistFormingDigitalAsset(String digitalAssetPublicKey, String digitalAssetLocalStoragePath)throws CantPersistDigitalAssetException{

        try{
            this.database=openDatabase();
            DatabaseTable databaseTable = getDatabaseTable(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_TABLE_NAME);
            DatabaseTableRecord record = databaseTable.getEmptyRecord();
            record.setStringValue(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DIGITAL_ASSET_PUBLIC_KEY_COLUMN_NAME,digitalAssetPublicKey);
            record.setStringValue(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DIGITAL_ASSET_LOCAL_STORAGE_PATH_COLUMN_NAME, digitalAssetLocalStoragePath);
            //record.setStringValue(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DIGITAL_ASSET_TRANSACTION_STATE_COLUMN_NAME, TransactionStatus.FORMING_GENESIS.getCode());
            databaseTable.insertRecord(record);
            this.database.closeDatabase();
        } catch (CantExecuteDatabaseOperationException exception) {
            this.database.closeDatabase();
            throw new CantPersistDigitalAssetException(exception,"Opening the Asset Issuing plugin database","Cannot open the Asset Issuing database");
        } catch (CantInsertRecordException exception) {
            this.database.closeDatabase();
            throw new CantPersistDigitalAssetException(exception,"Persisting a forming genesis digital asset","Cannot insert a record in the Asset Issuing database");
        } catch (Exception exception){
            this.database.closeDatabase();
            throw new CantPersistDigitalAssetException(exception, "Persisting a forming genesis digital asset", "Unexpected exception");
        }

    }

    public List<String> getPendingDigitalAssetPublicKeys(){
        //TODO:implement this method 15/09/2015
        return null;
    }

    public boolean isPendingAsset(String publicKey)throws CantCheckAssetIssuingProgressException, UnexpectedResultReturnedFromDatabaseException {

        try{
            this.database.openDatabase();
            DatabaseTable databaseTable = getDatabaseTable(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_TABLE_NAME);
            databaseTable.setStringFilter(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DIGITAL_ASSET_PUBLIC_KEY_COLUMN_NAME, publicKey, DatabaseFilterType.EQUAL);
            databaseTable.loadToMemory();
            //DatabaseTransaction databaseTransaction=this.database.newTransaction();
            DatabaseTableRecord databaseTableRecord;
            List<DatabaseTableRecord> databaseTableRecords=databaseTable.getRecords();
            if (databaseTableRecords.size() > 1)
                throw new UnexpectedResultReturnedFromDatabaseException("Unexpected result. More than value returned.",  "Public key:" + publicKey);
            else {
                databaseTableRecord = databaseTableRecords.get(0);
            }
            int assetsToGenerate=databaseTableRecord.getIntegerValue(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DIGITAL_ASSET_ASSETS_TO_GENERATE);
            int assetsGenerated=databaseTableRecord.getIntegerValue(AssetIssuingTransactionDatabaseConstants.DIGITAL_ASSET_TRANSACTION_DIGITAL_ASSET_ASSETS_GENERATED);
            this.database.closeDatabase();
            return assetsToGenerate-assetsGenerated>=0;

        } catch (DatabaseNotFoundException exception) {
            this.database.closeDatabase();
            throw new CantCheckAssetIssuingProgressException(exception,"Opening the Asset Issuing plugin database","Cannot found the Asset Issuing database");
        } catch (CantOpenDatabaseException exception) {
            this.database.closeDatabase();
            throw new CantCheckAssetIssuingProgressException(exception,"Opening the Asset Issuing plugin database","Cannot open the Asset Issuing database");
        } catch (CantLoadTableToMemoryException exception) {
            this.database.closeDatabase();
            throw new CantCheckAssetIssuingProgressException(exception,"Loading Asset Issuing plugin database to memory","Cannot load the Asset Issuing database");
        } catch (Exception exception){
            this.database.closeDatabase();
            throw new CantCheckAssetIssuingProgressException(exception,"Checking pending assets to issue","Unexpected exception");
        }
    }

    public boolean isPendingTransactions(/*CryptoStatus cryptoStatus*/) throws CantExecuteQueryException {
        //TODO: implement this method
        return false;
    }

    public int updateTransactionProtocolStatus(boolean occurrence) throws CantExecuteQueryException {
        //TODO: implement this method
        return 0;
    }

}
