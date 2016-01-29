package unit.HoldCryptoMoneyTransactionImpl;

import com.bitdubai.fermat_ccp_api.all_definition.enums.CryptoTransactionStatus;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.hold.developer.bitdubai.version_1.utils.HoldCryptoMoneyTransactionImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

/**
 * Created by José Vilchez on 21/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetTimestampConfirmedRejectedTest {

    @Test
    public void setTimestampConfirmedRejected(){
        HoldCryptoMoneyTransactionImpl holdCryptoMoneyTransaction = mock(HoldCryptoMoneyTransactionImpl.class, Mockito.RETURNS_DEEP_STUBS);
        doCallRealMethod().when(holdCryptoMoneyTransaction).setTimestampConfirmedRejected(Mockito.any(long.class));
    }

}
