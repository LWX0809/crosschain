package crosschain.fisco.solidity;

//solidity/;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class ContractCallTest extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052348015600f57600080fd5b50603f80601d6000396000f3fe6080604052600080fdfea264697066735822122083e6a7656966b1eeb462606abc685acc2c692f102beb5a25c5bdd707a281a9eb64736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052348015600f57600080fd5b50603f80601d6000396000f3fe6080604052600080fdfea2646970667358221220667374ecc473e838617e8d66b34430097c32d38d422456eb56a24ea7dc0f215e64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    protected ContractCallTest(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static ContractCallTest load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new ContractCallTest(contractAddress, client, credential);
    }

    public static ContractCallTest deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(ContractCallTest.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
