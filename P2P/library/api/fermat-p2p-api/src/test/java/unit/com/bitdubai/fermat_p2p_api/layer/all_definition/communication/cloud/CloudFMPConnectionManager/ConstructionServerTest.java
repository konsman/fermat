package unit.com.bitdubai.fermat_p2p_api.layer.all_definition.communication.cloud.CloudFMPConnectionManager;

import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.cloud.CloudFMPConnectionManager;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;


public class ConstructionServerTest extends
		CloudFMPConnectionManagerUnitTest {
	
	private CloudFMPConnectionManager testManager;
	
	@Before
	public void setUpTest() throws Exception{
		setUpParameters(TCP_BASE_TEST_PORT);
	}
	
	@Test
	public void Construction_ValidParameter_NotNull() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(testAddress, testExecutor, testPrivateKey, testPublicKey);
		assertThat(testManager).isNotNull();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Construction_NullAddress_InvalidParameterException() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(null, testExecutor, testPrivateKey, testPublicKey);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Construction_NullExecutor_InvalidParameterException() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(testAddress, null, testPrivateKey, testPublicKey);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Construction_NullPrivateKey_InvalidParameterException() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(testAddress, testExecutor, null, testPublicKey);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Construction_EmptyPrivateKey_InvalidParameterException() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(testAddress, testExecutor, "", testPublicKey);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Construction_NullPublicKey_InvalidParameterException() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(testAddress, testExecutor, testPrivateKey, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Construction_EmptyPublicKey_InvalidParameterException() throws Exception{
		testManager = new MockCloudFMPConnectionManagerServer(testAddress, testExecutor, testPrivateKey, null);
	}

}