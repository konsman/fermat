package com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.enums.DeviceDirectory;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;
import com.bitdubai.fermat_api.layer.all_definition.location_system.DeviceLocation;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantUpdateRecordException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantCreateFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantLoadFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.CantPersistFileException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.exceptions.FileNotFoundException;
import com.bitdubai.fermat_dap_api.layer.all_definition.enums.DAPConnectionState;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantCreateNewDeveloperException;
import com.bitdubai.fermat_dap_api.layer.all_definition.exceptions.CantGetUserDeveloperIdentitiesException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.RedeemPointActorRecord;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.exceptions.CantGetAssetRedeemPointActorsException;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.database.RedeemPointActorDatabaseConstants;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.database.RedeemPointActorDatabaseFactory;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.CantAddPendingRedeemPointException;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.CantGetRedeemPointActorProfileImageException;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.CantGetRedeemPointsListException;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.CantInitializeRedeemPointActorDatabaseException;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.CantPersistProfileImageException;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.CantUpdateRedeemPointException;
import com.bitdubai.fermat_dap_plugin.layer.actor.redeem.point.developer.bitdubai.version_1.exceptions.RedeemPointNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Nerio on 06/10/15.
 *
 * @throws NullPointerException if the constructor failed to initialize the
 * database and you ignored the exception and attempt to execute any method.
 */
public class RedeemPointActorDao implements Serializable {

    String REDEEM_POINT_PROFILE_IMAGE_FILE_NAME = "RedeemPointActorProfileImage";

    /**
     * Represent the Plugin Database.
     */

    private PluginDatabaseSystem pluginDatabaseSystem;

    private PluginFileSystem pluginFileSystem;

    private UUID pluginId;

    /**
     * Constructor with parameters
     * Because all the methods in this class do need a database connection
     * This constructor initialize the database and throws the respective exception
     * if it can't be done you shouldn't continue with the use of this class
     * because every method is going to throw a {@link NullPointerException}
     *
     * @param pluginDatabaseSystem DealsWithPluginDatabaseSystem
     */

    public RedeemPointActorDao(PluginDatabaseSystem pluginDatabaseSystem, PluginFileSystem pluginFileSystem, UUID pluginId) throws CantInitializeRedeemPointActorDatabaseException {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.pluginFileSystem = pluginFileSystem;
        this.pluginId = pluginId;
        initializeDatabase();
    }

    /**
     * Represent de Database where i will be working with
     */
    Database database;

    /**
     * This method open or creates the database i'll be working with     *
     *
     * @throws CantInitializeRedeemPointActorDatabaseException
     */
    private void initializeDatabase() throws CantInitializeRedeemPointActorDatabaseException {
        try {
             /*
              * Open new database connection
              */
            database = this.pluginDatabaseSystem.openDatabase(this.pluginId, RedeemPointActorDatabaseConstants.REDEEM_POINT_DATABASE_NAME);

        } catch (CantOpenDatabaseException cantOpenDatabaseException) {
            throw new CantInitializeRedeemPointActorDatabaseException(CantInitializeRedeemPointActorDatabaseException.DEFAULT_MESSAGE, cantOpenDatabaseException, "", "Exception not handled by the plugin, there is a problem and i cannot open the database.");
        } catch (DatabaseNotFoundException e) {
             /*
              * The database no exist may be the first time the plugin is running on this device,
              * We need to create the new database
              */
            RedeemPointActorDatabaseFactory databaseFactory = new RedeemPointActorDatabaseFactory(pluginDatabaseSystem);
            try {
                  /*
                   * We create the new database
                   */
                database = databaseFactory.createDatabase(this.pluginId, RedeemPointActorDatabaseConstants.REDEEM_POINT_DATABASE_NAME);

            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                  /*
                   * The database cannot be created. I can not handle this situation.
                   */
                throw new CantInitializeRedeemPointActorDatabaseException(CantInitializeRedeemPointActorDatabaseException.DEFAULT_MESSAGE, cantCreateDatabaseException, "", "There is a problem and i cannot create the database.");
            }
        } catch (Exception e) {
            throw new CantInitializeRedeemPointActorDatabaseException(CantInitializeRedeemPointActorDatabaseException.DEFAULT_MESSAGE, e, "", "Exception not handled by the plugin, there is a unknown problem and i cannot open the database.");
        }
    }

    public void createNewRedeemPoint(ActorAssetRedeemPoint redeemPoint) throws CantAddPendingRedeemPointException {
        try {
            /**
             * if Redeem Point exist on table
             * change status
             */
            if (redeemPointExists(redeemPoint.getActorPublicKey())) {
                this.updateRedeemPointDAPConnectionState(redeemPoint.getActorPublicKey(), redeemPoint.getDapConnectionState());
            } else {
                DatabaseTable table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);
                DatabaseTableRecord record = table.getEmptyRecord();

                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME, redeemPoint.getActorPublicKey());
                record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTRATION_DATE_COLUMN_NAME, System.currentTimeMillis());
//                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, redeemPointLoggedInPublicKey);

                setValuesToRecord(record, redeemPoint);

                table.insertRecord(record);
                /**
                 * Persist profile image on a file
                 */
                persistNewRedeemPointProfileImage(redeemPoint.getActorPublicKey(), redeemPoint.getProfileImage());
            }
        } catch (CantInsertRecordException e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", e, "", "Cant create new REDEEM POINT, insert database problems.");
        } catch (CantUpdateRedeemPointException e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", FermatException.wrapException(e), "", "Cant update exist REDEEM POINT state, unknown failure.");
        } catch (Exception e) {
            // Failure unknown.
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", FermatException.wrapException(e), "", "Cant create new REDEEM POINT, unknown failure.");
        } finally {

        }
    }

    public void createNewRedeemPointRegisterInNetworkService(ActorAssetRedeemPoint redeemPoint) throws CantAddPendingRedeemPointException {
        try {
            /**
             * if Redeem Point exist on table
             * change status
             */
            if (redeemPointExists(redeemPoint.getActorPublicKey())) {
                updateRedeemPointRegisteredDAPConnectionState(redeemPoint.getActorPublicKey(), DAPConnectionState.REGISTERED_ONLINE);
            } else {

                DatabaseTable table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);
                DatabaseTableRecord record = table.getEmptyRecord();

                record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_REGISTRATION_DATE_COLUMN_NAME, System.currentTimeMillis());
//                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, "-");
//                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, redeemPoint.getActorPublicKey());

                setValuesToRecordRegistered(record, redeemPoint);

                table.insertRecord(record);
                /**
                 * Persist profile image on a file
                 */
                persistNewRedeemPointProfileImage(redeemPoint.getActorPublicKey(), redeemPoint.getProfileImage());
            }
        } catch (CantInsertRecordException e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", e, "", "Cant create new REDEEM POINT, insert database problems.");
        } catch (CantUpdateRedeemPointException e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", FermatException.wrapException(e), "", "Cant update exist REDEEM POINT state, unknown failure.");
        } catch (Exception e) {
            // Failure unknown.
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", FermatException.wrapException(e), "", "Cant create new REDEEM POINT, unknown failure.");
        }
    }

    public void updateRedeemPoint(ActorAssetRedeemPoint redeemPoint) throws CantUpdateRedeemPointException, RedeemPointNotFoundException {

        DatabaseTable table;

        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Redeem Point actor list, table not found.", "Redeem Point Actor", "");
            }

            // 2) Find the Redeem Point , filter by keys.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME, redeemPoint.getActorPublicKey(), DatabaseFilterType.EQUAL);

            table.loadToMemory();

            if (table.getRecords().isEmpty()) {
                throw new RedeemPointNotFoundException("The following public key was not found: " + redeemPoint.getActorPublicKey());
            }

            // 3) Get Redeem Point record and update state.
            for (DatabaseTableRecord record : table.getRecords()) {
                setValuesToRecord(record, redeemPoint);
                updateRedeemPointProfileImage(redeemPoint.getActorPublicKey(), redeemPoint.getProfileImage());
                table.updateRecord(record);
            }

        } catch (CantLoadTableToMemoryException | CantUpdateRecordException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor", "Cant get developer identity list, unknown failure.");
        } finally {

        }
    }

    public void newExtendedPublicKeyRegistered(String redeemPointPublicKey, String extendedPublicKey) throws CantInsertRecordException {
        DatabaseTable table = getRegisteredIssuersTable();
        DatabaseTableRecord newRecord = table.getEmptyRecord();
        newRecord.setStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN, redeemPointPublicKey);
        newRecord.setStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_ISSUER_PUBLICKEY_COLUMN, extendedPublicKey);
        table.insertRecord(newRecord);
    }

    public List<String> getAllExtendedPublicKeyForRedeemPoint(String redeemPointPublicKey) throws CantLoadTableToMemoryException {
        DatabaseTable table = getRegisteredIssuersTable();
        table.addStringFilter(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN, redeemPointPublicKey, DatabaseFilterType.EQUAL);
        table.loadToMemory();
        List<String> toReturn = new ArrayList<>();
        for (DatabaseTableRecord record : table.getRecords()) {
            toReturn.add(record.getStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_ISSUER_PUBLICKEY_COLUMN));
        }
        return toReturn;
    }

    private DatabaseTable getRegisteredIssuersTable() {
        return database.getTable(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_TABLE_NAME);
    }

    public void updateRedeemPointDAPConnectionState(String redeemPointToAddPublicKey, DAPConnectionState connectionState) throws CantUpdateRedeemPointException {

        DatabaseTable table;

        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Redeem Point actor list, table not found.", "Redeem Point Actor", "");
            }

            // 2) Find the Redeem Point , filter by keys.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME, redeemPointToAddPublicKey, DatabaseFilterType.EQUAL);
//            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, redeemPointLoggedInPublicKey, DatabaseFilterType.EQUAL);

            table.loadToMemory();


            // 3) Get Redeem Point record and update state.
            for (DatabaseTableRecord record : table.getRecords()) {
                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONNECTION_STATE_COLUMN_NAME, connectionState.getCode());
                record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
                table.updateRecord(record);
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (CantUpdateRecordException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor", "Cant get developer identity list, unknown failure.");
        } finally {

        }
    }

    public void updateRedeemPointRegisteredDAPConnectionState(String redeemPointToAddPublicKey, DAPConnectionState connectionState) throws CantUpdateRedeemPointException {

        DatabaseTable table;

        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Redeem Point actor list, table not found.", "Redeem Point Actor", "");
            }

            // 2) Find the Redeem Point , filter by keys.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, redeemPointToAddPublicKey, DatabaseFilterType.EQUAL);

            table.loadToMemory();


            // 3) Get Redeem Point record and update state.
            for (DatabaseTableRecord record : table.getRecords()) {
                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, connectionState.getCode());
                record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
                table.updateRecord(record);
            }
        } catch (CantLoadTableToMemoryException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (CantUpdateRecordException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor", "Cant get developer identity list, unknown failure.");
        } finally {

        }
    }

    public int createNewAssetRedeemPointRegisterInNetworkServiceByList(List<ActorAssetRedeemPoint> actorAssetIssuerRecord) throws CantAddPendingRedeemPointException {
        int recordInsert = 0;
        try {
            /**
             * if Asset User exist on table
             * change status
             */

            for (ActorAssetRedeemPoint actorAssetRedeemPoint : actorAssetIssuerRecord) {
                if (redeemPointRegisteredExists(actorAssetRedeemPoint.getActorPublicKey())) {
                    this.updateAssetRedeemPointDAPConnectionStateActorNetworkService(actorAssetRedeemPoint, null, actorAssetRedeemPoint.getCryptoAddress());
                } else {
                    DatabaseTable table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);
                    DatabaseTableRecord record = table.getEmptyRecord();

                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, "-");
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, actorAssetRedeemPoint.getActorPublicKey());
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_NAME_COLUMN_NAME, actorAssetRedeemPoint.getName());

                    if (actorAssetRedeemPoint.getLocationLatitude() != null) {
                        record.setDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LATITUDE_COLUMN_NAME, actorAssetRedeemPoint.getLocationLatitude());
                    }
                    if (actorAssetRedeemPoint.getLocationLongitude() != null) {
                        record.setDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LONGITUDE_COLUMN_NAME, actorAssetRedeemPoint.getLocationLongitude());
                    }

                    if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME) != null) {
                        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, DAPConnectionState.REGISTERED_ONLINE.getCode());//actorAssetUser.getDAPConnectionState().getCode());
                    } else {
                        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, DAPConnectionState.CONNECTED_ONLINE.getCode());//actorAssetUser.getDAPConnectionState().getCode());
                    }

                    if (actorAssetRedeemPoint.getCryptoAddress() != null) {
                        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME, actorAssetRedeemPoint.getCryptoAddress().getAddress());
                        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME, actorAssetRedeemPoint.getCryptoAddress().getCryptoCurrency().getCode());
                    }

                    record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_REGISTRATION_DATE_COLUMN_NAME, System.currentTimeMillis());
                    record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());

                    table.insertRecord(record);
                    DatabaseTable issuersRegisteredTable = getRegisteredIssuersTable();
                    for (String registeredIssuer : actorAssetRedeemPoint.getRegisteredIssuers()) {
                        DatabaseTableRecord issuerRegisteredRecord = issuersRegisteredTable.getEmptyRecord();
                        issuerRegisteredRecord.setStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_ISSUER_PUBLICKEY_COLUMN, registeredIssuer);
                        issuerRegisteredRecord.setStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN, actorAssetRedeemPoint.getActorPublicKey());
                        issuersRegisteredTable.insertRecord(issuerRegisteredRecord);
                    }
                    recordInsert = recordInsert + 1;
                    /**
                     * Persist profile image on a file
                     */
                    persistNewRedeemPointProfileImage(actorAssetRedeemPoint.getActorPublicKey(), actorAssetRedeemPoint.getProfileImage());
                }
            }
            if (actorAssetIssuerRecord.isEmpty()) {
                this.updateAssetRedeemPointDAPConnectionStateActorNetworkService(DAPConnectionState.REGISTERED_OFFLINE);
            }

        } catch (CantInsertRecordException e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT REGISTERED IN ACTOR NETWORK SERVICE", e, "", "Cant create new ASSET ISSUER REGISTERED IN ACTOR NETWORK SERVICE, insert database problems.");
        } catch (CantUpdateRedeemPointException e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT REGISTERED IN ACTOR NETWORK SERVICE", FermatException.wrapException(e), "", "Cant update exist ASSET USER REGISTERED IN ACTOR NETWORK SERVICE state, unknown failure.");
        } catch (Exception e) {
            throw new CantAddPendingRedeemPointException("CAN'T INSERT REDEEM POINT", FermatException.wrapException(e), "", "Cant create new ASSET USER, unknown failure.");
        } finally {

        }
        return recordInsert;
    }

    public void updateAssetRedeemPointDAPConnectionStateActorNetworkService(DAPConnectionState dapConnectionState) throws CantUpdateRedeemPointException {
        DatabaseTable table;

        try {
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                throw new CantGetUserDeveloperIdentitiesException("Cant get Actor Asset Redeem Point Registered list, table not found.", "Asset Issuer Actor", "");
            }

            // 2) Find the Asset User , filter by keys.
//            table.addStringFilter(AssetUserActorDatabaseConstants.ASSET_USER_REGISTERED_PUBLIC_KEY_COLUMN_NAME, assetUserPublicKey, DatabaseFilterType.EQUAL);

            table.loadToMemory();

            if (table.getRecords().size() > 0) {
                // 3) Get Asset User record and update state.
                for (DatabaseTableRecord record : table.getRecords()) {
                    if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME) != null) {
                        dapConnectionState = DAPConnectionState.CONNECTED_OFFLINE;
                    }

                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, dapConnectionState.getCode());

                    record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
                    table.updateRecord(record);
                }
            }

        } catch (CantLoadTableToMemoryException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Actor Asset Redeem Point Registered", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (CantUpdateRecordException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "Actor Asset Redeem Point Registered", "Cant Update " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), FermatException.wrapException(e), "Asset User REGISTERED Actor", "Cant get developer identity list, unknown failure.");
        }
    }

    public void updateAssetRedeemPointDAPConnectionStateActorNetworkService(ActorAssetRedeemPoint actorAssetRedeemPoint, DAPConnectionState dapConnectionState, CryptoAddress cryptoAddress) throws CantUpdateRedeemPointException {

        DatabaseTable table;

        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Actor Asset Redeem Point Registered list, table not found.", "Asset Issuer Actor", "");
            }

            // 2) Find the Asset Issuer , filter by keys.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, actorAssetRedeemPoint.getActorPublicKey(), DatabaseFilterType.EQUAL);

            table.loadToMemory();

            // 3) Get Asset Issuer record and update state.
            for (DatabaseTableRecord record : table.getRecords()) {
                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, "-");
                record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_NAME_COLUMN_NAME, actorAssetRedeemPoint.getName());

                if (cryptoAddress != null) {
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME, cryptoAddress.getAddress());
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME, cryptoAddress.getCryptoCurrency().getCode());
                }

                if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME) == null) {
                    if (Objects.equals(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME), DAPConnectionState.REGISTERED_OFFLINE.getCode())) {
                        dapConnectionState = DAPConnectionState.REGISTERED_ONLINE;
                    }
                } else {
                    if (Objects.equals(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME), DAPConnectionState.CONNECTED_OFFLINE.getCode())) {
                        dapConnectionState = DAPConnectionState.CONNECTED_ONLINE;
                    }
                }

                if (dapConnectionState != null)
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, dapConnectionState.getCode());

                record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
                table.updateRecord(record);

                updateRegisteredIssuers(actorAssetRedeemPoint);
            }

            updateRedeemPointProfileImage(actorAssetRedeemPoint.getActorPublicKey(), actorAssetRedeemPoint.getProfileImage());

        } catch (CantLoadTableToMemoryException | CantUpdateRecordException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "ACTOR ASSET REDEEM POINT REGISTERED", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), FermatException.wrapException(e), "ACTOR ASSET REDEEM POINT REGISTERED", "Cant get developer identity list, unknown failure.");
        }
    }

    private void updateRegisteredIssuers(ActorAssetRedeemPoint redeemPoint) throws CantUpdateRecordException{
        try {
            DatabaseTable issuers = getRegisteredIssuersTable();
            issuers.addStringFilter(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN, redeemPoint.getActorPublicKey(), DatabaseFilterType.EQUAL);
            issuers.loadToMemory();

            List<DatabaseTableRecord> records = issuers.getRecords();
            for (DatabaseTableRecord record : records) {
                issuers.deleteRecord(record);
            }
            for (String issuerPk : redeemPoint.getRegisteredIssuers()) {
                DatabaseTableRecord newRecord = issuers.getEmptyRecord();
                newRecord.setStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN, redeemPoint.getActorPublicKey());
                newRecord.setStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_ISSUER_PUBLICKEY_COLUMN, issuerPk);
                issuers.insertRecord(newRecord);
            }
        } catch(Exception e){
            throw new CantUpdateRecordException(e);
        }
    }

    public void updateAssetRedeemPointPConnectionStateCryptoAddress(String assetRedeemPointPublicKey, DAPConnectionState dapConnectionState, CryptoAddress cryptoAddress) throws CantUpdateRedeemPointException {

        DatabaseTable table;

        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Actor Asset Redeem Point Registered list, table not found.", "Asset Issuer Actor", "");
            }

            // 2) Find the Asset Issuer , filter by keys.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, assetRedeemPointPublicKey, DatabaseFilterType.EQUAL);

            table.loadToMemory();

            // 3) Get Asset Issuer record and update state.
            for (DatabaseTableRecord record : table.getRecords()) {
                if (cryptoAddress != null) {
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME, cryptoAddress.getAddress());
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME, cryptoAddress.getCryptoCurrency().getCode());
                }

                if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME) == null) {
                    if (Objects.equals(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME), DAPConnectionState.REGISTERED_OFFLINE.getCode())) {
                        dapConnectionState = DAPConnectionState.REGISTERED_ONLINE;
                    }
                } else {
                    if (Objects.equals(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME), DAPConnectionState.CONNECTED_OFFLINE.getCode())) {
                        dapConnectionState = DAPConnectionState.CONNECTED_ONLINE;
                    }
                }

                if (dapConnectionState != null)
                    record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, dapConnectionState.getCode());

                record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
                table.updateRecord(record);
            }

        } catch (CantLoadTableToMemoryException | CantUpdateRecordException e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), e, "ACTOR ASSET REDEEM POINT REGISTERED", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantUpdateRedeemPointException(e.getMessage(), FermatException.wrapException(e), "ACTOR ASSET REDEEM POINT REGISTERED", "Cant get developer identity list, unknown failure.");
        }
    }

    public List<ActorAssetRedeemPoint> getAllARedeemPoints(String redeemPointLoggedInPublicKey, int max, int offset) throws CantGetRedeemPointsListException {

        // Setup method.
        List<ActorAssetRedeemPoint> list = new ArrayList<ActorAssetRedeemPoint>(); // Redeem Point Actor list.
        DatabaseTable table;

        // Get Redeem Points identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Redeem Point identity list, table not found.", "Plugin Identity", "Cant get Redeem Point identity list, table not found.");
            }

            // 2) Find all Redeem Points.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, redeemPointLoggedInPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONNECTION_STATE_COLUMN_NAME, DAPConnectionState.REGISTERED_ONLINE.getCode(), DatabaseFilterType.EQUAL);
            table.setFilterOffSet(String.valueOf(offset));
            table.setFilterTop(String.valueOf(max));
            table.loadToMemory();

            // 3) Get Redeem Points Recorod.

            addRecordsToList(list, table.getRecords());
        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_DATABASE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {
            // Failure unknown.
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor", "Cant get Redeem Point Actor list, unknown failure.");
        } finally {

        }
        // Return the list values.
        return list;
    }

    public List<ActorAssetRedeemPoint> getRedeemPoints(String redeemPointLoggedInPublicKey, DAPConnectionState connectionState, int max, int offset) throws CantGetRedeemPointsListException {

        // Setup method.
        List<ActorAssetRedeemPoint> list = new ArrayList<ActorAssetRedeemPoint>(); // Redeem Point Actor list.
        DatabaseTable table;

        // Get Redeem Points identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Redeem Point identity list, table not found.", "Plugin Identity", "Cant get Redeem Point identity list, table not found.");
            }
            // 2) Find  Redeem Points by state.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, redeemPointLoggedInPublicKey, DatabaseFilterType.EQUAL);
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONNECTION_STATE_COLUMN_NAME, connectionState.getCode(), DatabaseFilterType.EQUAL);
            table.setFilterOffSet(String.valueOf(offset));
            table.setFilterTop(String.valueOf(max));
            table.loadToMemory();

            // 3) Get Redeem Points Recorod.

            addRecordsToList(list, table.getRecords());

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_DATABASE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {
            // Failure unknown.
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), FermatException.wrapException(e), "Redeem Points Actor", "Cant get Redeem Point Actor list, unknown failure.");
        } finally {

        }
        // Return the list values.
        return list;
    }

    public ActorAssetRedeemPoint getActorRegisteredByPublicKey(String actorPublicKey) throws CantGetAssetRedeemPointActorsException {

        ActorAssetRedeemPoint actorAssetRedeemPoint = null;
        DatabaseTable table;

        // Get Asset Users identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                throw new CantGetUserDeveloperIdentitiesException("Cant get asset User identity list, table not found.", "Plugin Identity", "Cant get asset user identity list, table not found.");
            }
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, actorPublicKey, DatabaseFilterType.EQUAL);

            table.loadToMemory();
            // 3) Get Asset Users Record.
//            actorAssetRedeemPoint = this.addRecords(table.getRecords());
            actorAssetRedeemPoint = this.addRecordsRegistered(table.getRecords());

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), e, "Asset User Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), e, "Asset User Actor", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), FermatException.wrapException(e), "Asset User Actor", "Cant get Asset User Actor list, unknown failure.");
        } finally {

        }
        // Return the values.
        return actorAssetRedeemPoint;
    }

    public ActorAssetRedeemPoint getActorAssetRedeemPoint() throws CantGetAssetRedeemPointActorsException {

        ActorAssetRedeemPoint actorAssetRedeemPoint = null;//new RedeemPointActorRecord();
        DatabaseTable table;

        // Get Asset Users identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get asset User identity list, table not found.", "Plugin Identity", "Cant get asset user identity list, table not found.");
            }
            table.loadToMemory();
            // 3) Get Asset Users Record.
            actorAssetRedeemPoint = this.addRecords(table.getRecords());

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), e, "Asset User Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), e, "Asset User Actor", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), FermatException.wrapException(e), "Asset User Actor", "Cant get Asset User Actor list, unknown failure.");
        } finally {

        }
        // Return the values.
        return actorAssetRedeemPoint;
    }

    public List<ActorAssetRedeemPoint> getAllAssetRedeemPointActorRegistered() throws CantGetRedeemPointsListException {
        List<ActorAssetRedeemPoint> list = new ArrayList<>(); // Asset Issuer Actor list.

        DatabaseTable table;

        // Get Asset Issuer identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get asset Issuer identity list, table not found.", "Plugin Identity", "Cant get asset user identity list, table not found.");
            }

            table.loadToMemory();
            // 3) Get Asset Users Record.
            this.addRecordsTableRegisteredToList(list, table.getRecords());

        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor Registered", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor Registered", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {
            throw new CantGetRedeemPointsListException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor Registered", "Cant get Redeem Point Actor Registered list, unknown failure.");
        } finally {

        }
        // Return the list values.
        return list;
    }

    public List<ActorAssetRedeemPoint> getAssetRedeemPointRegistered(String actorPublicKey) throws CantGetAssetRedeemPointActorsException {
        // Setup method.
        List<ActorAssetRedeemPoint> list = new ArrayList<ActorAssetRedeemPoint>(); // Asset User Actor list.
        DatabaseTable table;

        // Get Asset Users identities list.
        try {

            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get Asset User identity list, table not found.", "Plugin Identity", "Cant get Asset Usuer identity list, table not found.");
            }

            // 2) Find all Asset Users.
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, actorPublicKey, DatabaseFilterType.EQUAL);
//            table.setFilterOffSet(String.valueOf(offset));
//            table.setFilterTop(String.valueOf(max));
            table.loadToMemory();

            // 3) Get Asset Users Record.
            this.addRecordsTableRegisteredToList(list, table.getRecords());


        } catch (CantLoadTableToMemoryException e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), e, "Asset User Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), e, "Asset User Actor", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {
            throw new CantGetAssetRedeemPointActorsException(e.getMessage(), FermatException.wrapException(e), "Asset User Actor", "Cant get Asset User Actor list, unknown failure.");
        }
        // Return the list values.
        return list;
    }

    public List<ActorAssetRedeemPoint> getRedeemPointsConnectedForIssuer(String issuerPk) throws CantGetRedeemPointsListException {
        List<ActorAssetRedeemPoint> list = new ArrayList<>(); // Asset Issuer Actor list.

        DatabaseTable table;

        // Get Asset Issuer identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);
            DatabaseTable issuerRegisteredTable = getRegisteredIssuersTable();
            if (table == null || issuerRegisteredTable == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get asset Issuer identity list, table not found.", "Plugin Identity", "Cant get asset user identity list, table not found.");
            }


            issuerRegisteredTable.addStringFilter(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_ISSUER_PUBLICKEY_COLUMN, issuerPk, DatabaseFilterType.EQUAL);
            issuerRegisteredTable.loadToMemory();

            for (DatabaseTableRecord record : issuerRegisteredTable.getRecords()) {
                table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, DAPConnectionState.CONNECTED_ONLINE.getCode(), DatabaseFilterType.EQUAL);
                table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, record.getStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN), DatabaseFilterType.EQUAL);
                table.loadToMemory();
                this.addRecordsTableRegisteredToList(list, table.getRecords());
            }
        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor Registered", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {

            // Failure unknown.
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor Registered", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {

            throw new CantGetRedeemPointsListException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor Registered", "Cant get Redeem Point Actor Registered list, unknown failure.");
        }
        // Return the list values.
        return list;
    }

    public List<ActorAssetRedeemPoint> getAllAssetRedeemPointActorConnected() throws CantGetRedeemPointsListException {
        List<ActorAssetRedeemPoint> list = new ArrayList<>(); // Asset Issuer Actor list.

        DatabaseTable table;

        // Get Asset Issuer identities list.
        try {
            /**
             * 1) Get the table.
             */
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                /**
                 * Table not found.
                 */
                throw new CantGetUserDeveloperIdentitiesException("Cant get asset Issuer identity list, table not found.", "Plugin Identity", "Cant get asset user identity list, table not found.");
            }
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, DAPConnectionState.CONNECTED_ONLINE.getCode(), DatabaseFilterType.EQUAL);


            table.loadToMemory();

            this.addRecordsTableRegisteredToList(list, table.getRecords());
        } catch (CantLoadTableToMemoryException e) {

            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor Registered", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME + " table in memory.");
        } catch (CantGetRedeemPointActorProfileImageException e) {

            // Failure unknown.
            throw new CantGetRedeemPointsListException(e.getMessage(), e, "Redeem Point Actor Registered", "Can't get profile ImageMiddleware.");
        } catch (Exception e) {

            throw new CantGetRedeemPointsListException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor Registered", "Cant get Redeem Point Actor Registered list, unknown failure.");
        }
        // Return the list values.
        return list;
    }

    private void addRecordsToList(List<ActorAssetRedeemPoint> list, List<DatabaseTableRecord> records) throws InvalidParameterException, CantGetRedeemPointActorProfileImageException {

        for (DatabaseTableRecord record : records) {
            // Add records to list.

            //INICIALIZAR, VALORES OBLIGATORIOS: Nombre y PublicKey
            RedeemPointActorRecord redeemPointActorRecord = new RedeemPointActorRecord(
                    record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME),
                    record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_NAME_COLUMN_NAME));

            //SETEAR EL ADDRESS
            RedeemPointActorAddress address = new RedeemPointActorAddress();
            address.setCountryName(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_COUNTRY_NAME_COLUMN_NAME));
            address.setProvinceName(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_PROVINCE_NAME_COLUMN_NAME));
            address.setProvinceName(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_CITY_NAME_COLUMN_NAME));
            address.setStreetName(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_STREET_NAME_COLUMN_NAME));
            address.setPostalCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_POSTAL_CODE_COLUMN_NAME));
            address.setHouseNumber(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_HOUSE_NUMBER_COLUMN_NAME));
            redeemPointActorRecord.setAddress(address);

            //SETEAR EL CRYPTOADDRESS
            CryptoAddress cryptoAddress = new CryptoAddress();
            cryptoAddress.setAddress(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CRYPTO_ADDRESS_COLUMN_NAME));
            cryptoAddress.setCryptoCurrency(CryptoCurrency.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CRYPTO_CURRENCY_COLUMN_NAME)));
            redeemPointActorRecord.setCryptoAddress(cryptoAddress);

            //SETEAR LOCATION
            DeviceLocation location = new DeviceLocation();
            try {
                location.setLatitude(record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LOCATION_LATITUDE_COLUMN_NAME));
                location.setLongitude(record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LOCATION_LONGITUDE_COLUMN_NAME));
            } catch (NumberFormatException e) {
                //If the saved location cannot be parsed to double ("-", "null") then I'll keep it as the default null value
            }
            redeemPointActorRecord.setLocation(location);
            //SETEAR EL CONECTIONSTATE, éste se registra en la BBDD con su código.
            redeemPointActorRecord.setDapConnectionState(DAPConnectionState.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONNECTION_STATE_COLUMN_NAME)));

            //SETEAR LA IMAGEN
            redeemPointActorRecord.setProfileImage(getRedeemPointProfileImagePrivateKey(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME)));

            //SETEAR LOS OTROS ATRIBUTOS
            redeemPointActorRecord.setHoursOfOperation(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_HOURS_OF_OPERATION_COLUMN_NAME));
            redeemPointActorRecord.setContactInformation(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONTACT_INFORMATION_COLUMN_NAME));

            list.add(redeemPointActorRecord);
        }
    }

    /**
     * Private Methods
     */
    private void setValuesToRecord(DatabaseTableRecord record, ActorAssetRedeemPoint redeemPoint) {
        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_NAME_COLUMN_NAME, redeemPoint.getName());
        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONNECTION_STATE_COLUMN_NAME, redeemPoint.getDapConnectionState().getCode());
        record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());

        //LOCATION
//        if (redeemPoint.getLocation() != null){
        record.setDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LOCATION_LONGITUDE_COLUMN_NAME, redeemPoint.getLocationLatitude());
        record.setDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LOCATION_LATITUDE_COLUMN_NAME, redeemPoint.getLocationLongitude());
//        }

        //ADDRESS
        if (redeemPoint.getAddress() != null) {
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_COUNTRY_NAME_COLUMN_NAME, redeemPoint.getAddress().getCountryName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_STREET_NAME_COLUMN_NAME, redeemPoint.getAddress().getStreetName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_PROVINCE_NAME_COLUMN_NAME, redeemPoint.getAddress().getProvinceName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_CITY_NAME_COLUMN_NAME, redeemPoint.getAddress().getCityName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_POSTAL_CODE_COLUMN_NAME, redeemPoint.getAddress().getPostalCode());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_ADDRESS_HOUSE_NUMBER_COLUMN_NAME, redeemPoint.getAddress().getHouseNumber());
        }
        //CRYPTOADDRESS
        if (redeemPoint.getCryptoAddress() != null) {
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CRYPTO_CURRENCY_COLUMN_NAME, redeemPoint.getCryptoAddress().getCryptoCurrency().getCode());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CRYPTO_ADDRESS_COLUMN_NAME, redeemPoint.getCryptoAddress().getAddress());
        }

        if (redeemPoint.getContactInformation() != null)
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONTACT_INFORMATION_COLUMN_NAME, redeemPoint.getContactInformation());

        if (redeemPoint.getHoursOfOperation() != null)
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_HOURS_OF_OPERATION_COLUMN_NAME, redeemPoint.getHoursOfOperation());

    }

    private void setValuesToRecordRegistered(DatabaseTableRecord record, ActorAssetRedeemPoint redeemPoint) {
        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LINKED_IDENTITY_PUBLIC_KEY_COLUMN_NAME, "-");

        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_NAME_COLUMN_NAME, redeemPoint.getName());
        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, redeemPoint.getActorPublicKey());

        record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME, redeemPoint.getDapConnectionState().getCode());

        record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_REGISTRATION_DATE_COLUMN_NAME, System.currentTimeMillis());
        record.setLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME, System.currentTimeMillis());
        //LOCATION
//        record.setDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LONGITUDE_COLUMN_NAME, redeemPoint.getLocation().getLongitude());
//        record.setDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LATITUDE_COLUMN_NAME, redeemPoint.getLocation().getLatitude());

        //ADDRESS
        if (redeemPoint.getAddress() != null) {

            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_ADDRESS_COUNTRY_NAME_COLUMN_NAME, redeemPoint.getAddress().getCountryName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_ADDRESS_STREET_NAME_COLUMN_NAME, redeemPoint.getAddress().getStreetName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_ADDRESS_PROVINCE_NAME_COLUMN_NAME, redeemPoint.getAddress().getProvinceName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_ADDRESS_CITY_NAME_COLUMN_NAME, redeemPoint.getAddress().getCityName());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_ADDRESS_POSTAL_CODE_COLUMN_NAME, redeemPoint.getAddress().getPostalCode());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_ADDRESS_HOUSE_NUMBER_COLUMN_NAME, redeemPoint.getAddress().getHouseNumber());
        }

        //CRYPTOADDRESS
        if (redeemPoint.getCryptoAddress() != null) {
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME, redeemPoint.getCryptoAddress().getCryptoCurrency().getCode());
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME, redeemPoint.getCryptoAddress().getAddress());
        }

        if (redeemPoint.getContactInformation() != null)
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONTACT_INFORMATION_COLUMN_NAME, redeemPoint.getContactInformation());

        if (redeemPoint.getHoursOfOperation() != null)
            record.setStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_HOURS_OF_OPERATION_COLUMN_NAME, redeemPoint.getHoursOfOperation());
    }

    private void persistNewRedeemPointProfileImage(String publicKey, byte[] profileImage) throws CantPersistProfileImageException {
        try {
            PluginBinaryFile file = this.pluginFileSystem.createBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    REDEEM_POINT_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.setContent(profileImage);

            file.persistToMedia();
        } catch (CantPersistFileException e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", e, "Error persist file.", null);

        } catch (CantCreateFileException e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", e, "Error creating file.", null);
        } catch (Exception e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", FermatException.wrapException(e), "", "");
        }
    }

    private void updateRedeemPointProfileImage(String publicKey, byte[] profileImage) throws CantPersistProfileImageException {
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    REDEEM_POINT_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );
            //If the profileImage hasn't been update don't modify it.
            if (file.getContent().equals(profileImage)) {
                return;
            }
            file.delete();
            file.setContent(profileImage);
            file.persistToMedia();
        } catch (CantPersistFileException | CantCreateFileException e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", e, "Error persist file.", null);
        } catch (Exception e) {
            throw new CantPersistProfileImageException("CAN'T PERSIST PROFILE IMAGE ", FermatException.wrapException(e), "", "");
        }
    }

    private byte[] getRedeemPointProfileImagePrivateKey(String publicKey) throws CantGetRedeemPointActorProfileImageException {
        byte[] profileImage;
        try {
            PluginBinaryFile file = this.pluginFileSystem.getBinaryFile(pluginId,
                    DeviceDirectory.LOCAL_USERS.getName(),
                    REDEEM_POINT_PROFILE_IMAGE_FILE_NAME + "_" + publicKey,
                    FilePrivacy.PRIVATE,
                    FileLifeSpan.PERMANENT
            );

            file.loadFromMedia();

            profileImage = file.getContent();

        } catch (CantLoadFileException e) {
            throw new CantGetRedeemPointActorProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error loaded file.", null);

        } catch (FileNotFoundException | CantCreateFileException e) {
            throw new CantGetRedeemPointActorProfileImageException("CAN'T GET IMAGE PROFILE ", e, "Error getting Redeem Point Actor private keys file.", null);
        } catch (Exception e) {
            throw new CantGetRedeemPointActorProfileImageException("CAN'T GET IMAGE PROFILE ", FermatException.wrapException(e), "", "");
        }
        return profileImage;
    }

    private boolean redeemPointExists(String redeemPointToAddPublicKey) throws CantCreateNewDeveloperException {

        DatabaseTable table;
        /**
         * Get developers identities list.
         * I select records on table
         */

        try {
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME);

            if (table == null) {
                throw new CantGetUserDeveloperIdentitiesException("Cant check if alias exists, table not  found.", "Redeem Point Actor", "Cant check if alias exists, table not found.");
            }
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME, redeemPointToAddPublicKey, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            return !table.getRecords().isEmpty();

        } catch (CantLoadTableToMemoryException em) {
            throw new CantCreateNewDeveloperException(em.getMessage(), em, "Redeem Point Actor", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_DATABASE_NAME + " table in memory.");

        } catch (Exception e) {
            throw new CantCreateNewDeveloperException(e.getMessage(), FermatException.wrapException(e), "Redeem Point Actor", "Cant check if alias exists, unknown failure.");
        }
    }

    private boolean redeemPointRegisteredExists(String assetRedeemPointExistsPublicKey) throws CantCreateNewDeveloperException {

        DatabaseTable table;
        /**
         * Get developers identities list.
         * I select records on table
         */
        try {
            table = this.database.getTable(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_TABLE_NAME);

            if (table == null) {
                throw new CantGetUserDeveloperIdentitiesException("Cant check if alias exists, table not  found.", "Actor Redeem Point", "Cant check if alias exists, table not found.");
            }
            table.addStringFilter(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME, assetRedeemPointExistsPublicKey, DatabaseFilterType.EQUAL);
            table.loadToMemory();

            return table.getRecords().size() > 0;

        } catch (CantLoadTableToMemoryException em) {
            throw new CantCreateNewDeveloperException(em.getMessage(), em, "Actor Redeem Point", "Cant load " + RedeemPointActorDatabaseConstants.REDEEM_POINT_TABLE_NAME + " table in memory.");
        } catch (Exception e) {
            throw new CantCreateNewDeveloperException(e.getMessage(), FermatException.wrapException(e), "Actor Redeem Point", "Cant check if alias exists, unknown failure.");
        }
    }

    private ActorAssetRedeemPoint addRecords(List<DatabaseTableRecord> records) throws InvalidParameterException, CantGetRedeemPointActorProfileImageException {
        RedeemPointActorRecord redeemPointActor = null;
        for (DatabaseTableRecord record : records) {
            CryptoAddress cryptoAddress = null;
            if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CRYPTO_ADDRESS_COLUMN_NAME) != null) {
                cryptoAddress = new CryptoAddress(
                        record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CRYPTO_ADDRESS_COLUMN_NAME),
                        CryptoCurrency.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME)));
            }

            String publicKey = record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME);

            redeemPointActor = new RedeemPointActorRecord(
                    publicKey,
                    record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_NAME_COLUMN_NAME),
                    DAPConnectionState.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_CONNECTION_STATE_COLUMN_NAME)),
                    record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LOCATION_LATITUDE_COLUMN_NAME),
                    record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LOCATION_LONGITUDE_COLUMN_NAME),
                    cryptoAddress,
                    record.getLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTRATION_DATE_COLUMN_NAME),
                    record.getLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_LAST_CONNECTION_DATE_COLUMN_NAME),
                    getRedeemPointProfileImagePrivateKey(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_PUBLIC_KEY_COLUMN_NAME)),
                    getRegisteredIssuersListByRepoPublicKey(publicKey));
        }
        return redeemPointActor;
    }

    private ActorAssetRedeemPoint addRecordsRegistered(List<DatabaseTableRecord> records) throws InvalidParameterException, CantGetRedeemPointActorProfileImageException {
        RedeemPointActorRecord redeemPointActor = null;
        for (DatabaseTableRecord record : records) {
            CryptoAddress cryptoAddress = null;
            if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME) != null) {
                cryptoAddress = new CryptoAddress(
                        record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME),
                        CryptoCurrency.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME)));
            }

            String publicKey = record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME);

            double latitude;
            double longitude;
            try {
                latitude = record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LATITUDE_COLUMN_NAME);
                longitude = record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LONGITUDE_COLUMN_NAME);
            } catch (NullPointerException e) {
                latitude = 0.0;
                longitude = 0.0;
            }

            redeemPointActor = new RedeemPointActorRecord(
                    publicKey,
                    record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_NAME_COLUMN_NAME),
                    DAPConnectionState.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME)),
                    latitude,
                    longitude,
                    cryptoAddress,
                    record.getLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_REGISTRATION_DATE_COLUMN_NAME),
                    record.getLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME),
                    getRedeemPointProfileImagePrivateKey(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME)));
        }
        return redeemPointActor;
    }

    private void addRecordsTableRegisteredToList(List<ActorAssetRedeemPoint> list, List<DatabaseTableRecord> records) throws InvalidParameterException, CantGetRedeemPointActorProfileImageException {

        for (DatabaseTableRecord record : records) {
            CryptoAddress cryptoAddress = null;
            if (record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME) != null) {
                cryptoAddress = new CryptoAddress(
                        record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_ADDRESS_COLUMN_NAME),
                        CryptoCurrency.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CRYPTO_CURRENCY_COLUMN_NAME)));
            }

            String publicKey = record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME);

            list.add(new RedeemPointActorRecord(
                    publicKey,
                    record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_NAME_COLUMN_NAME),
                    DAPConnectionState.getByCode(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_CONNECTION_STATE_COLUMN_NAME)),
                    record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LATITUDE_COLUMN_NAME),
                    record.getDoubleValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LOCATION_LONGITUDE_COLUMN_NAME),
                    cryptoAddress,
                    record.getLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_REGISTRATION_DATE_COLUMN_NAME),
                    record.getLongValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_LAST_CONNECTION_DATE_COLUMN_NAME),
                    getRedeemPointProfileImagePrivateKey(record.getStringValue(RedeemPointActorDatabaseConstants.REDEEM_POINT_REGISTERED_PUBLIC_KEY_COLUMN_NAME)),
                    getRegisteredIssuersListByRepoPublicKey(publicKey)));
        }
    }

    private List<String> getRegisteredIssuersListByRepoPublicKey(String repoPublicKey) {
        List<String> registeredIssuers = new ArrayList<>();
        try {
            DatabaseTable registeredIssuerTable = getRegisteredIssuersTable();
            registeredIssuerTable.addStringFilter(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_REDEEM_POINT_PUBLICKEY_COLUMN, repoPublicKey, DatabaseFilterType.EQUAL);
            registeredIssuerTable.loadToMemory();
            for (DatabaseTableRecord registeredIssuerRecord : registeredIssuerTable.getRecords()) {
                registeredIssuers.add(registeredIssuerRecord.getStringValue(RedeemPointActorDatabaseConstants.REGISTERED_ASSET_ISSUERS_ISSUER_PUBLICKEY_COLUMN));
            }
        } catch (CantLoadTableToMemoryException e) {
            //we'll keep it as an empty list
        }
        return registeredIssuers;
    }
}
