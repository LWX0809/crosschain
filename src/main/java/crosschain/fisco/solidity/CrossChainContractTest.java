package crosschain.fisco.solidity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CrossChainContractTest extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610541806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80633033413b14610046578063c7a9b74c14610064578063cec65dca14610080575b600080fd5b61004e61009c565b60405161005b91906103e8565b60405180910390f35b61007e600480360381019061007991906101fd565b6100a2565b005b61009a60048036038101906100959190610191565b6100ea565b005b600b5481565b8060ff16600b819055507fa708820a3dffc653e7997895bbd91de60498807398b93ef30dfddeda42d2bea482826040516100dd929190610362565b60405180910390a1505050565b7fa708820a3dffc653e7997895bbd91de60498807398b93ef30dfddeda42d2bea481606460405161011c9291906103a5565b60405180910390a15050565b600082601f83011261013957600080fd5b813561014c61014782610430565b610403565b9150808252602083016020830185838301111561016857600080fd5b6101738382846104a1565b50505092915050565b60008135905061018b816104f4565b92915050565b600080604083850312156101a457600080fd5b600083013567ffffffffffffffff8111156101be57600080fd5b6101ca85828601610128565b925050602083013567ffffffffffffffff8111156101e757600080fd5b6101f385828601610128565b9150509250929050565b60008060006060848603121561021257600080fd5b600084013567ffffffffffffffff81111561022c57600080fd5b61023886828701610128565b935050602084013567ffffffffffffffff81111561025557600080fd5b61026186828701610128565b92505060406102728682870161017c565b9150509250925092565b6102858161048f565b82525050565b60006102968261045c565b6102a08185610467565b93506102b08185602086016104b0565b6102b9816104e3565b840191505092915050565b60006102d1601583610467565b91507f63726561746543726f7373436861696e577269746500000000000000000000006000830152602082019050919050565b6000610311601483610467565b91507f63726561746543726f7373436861696e526561640000000000000000000000006000830152602082019050919050565b61034d81610478565b82525050565b61035c81610482565b82525050565b6000606082019050818103600083015261037b816102c4565b9050818103602083015261038f818561028b565b905061039e6040830184610353565b9392505050565b600060608201905081810360008301526103be81610304565b905081810360208301526103d2818561028b565b90506103e1604083018461027c565b9392505050565b60006020820190506103fd6000830184610344565b92915050565b6000604051905081810181811067ffffffffffffffff8211171561042657600080fd5b8060405250919050565b600067ffffffffffffffff82111561044757600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b6000819050919050565b600060ff82169050919050565b600061049a82610482565b9050919050565b82818337600083830152505050565b60005b838110156104ce5780820151818401526020810190506104b3565b838111156104dd576000848401525b50505050565b6000601f19601f8301169050919050565b6104fd81610482565b811461050857600080fd5b5056fea264697066735822122056222d475e9b6700502877dc36a3d3f3e6cce0638b53075f17be09c28bf0b1d864736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610541806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063499c747d14610046578063642ee38b146100625780639d1c9d6f14610080575b600080fd5b610060600480360381019061005b91906101fd565b61009c565b005b61006a6100e4565b60405161007791906103e8565b60405180910390f35b61009a60048036038101906100959190610191565b6100ea565b005b8060ff16600b819055507fe0fa8d95c65e8febcaebac5aa006bee0ea499a7bece703317cef9842b226eebf82826040516100d79291906103a5565b60405180910390a1505050565b600b5481565b7fe0fa8d95c65e8febcaebac5aa006bee0ea499a7bece703317cef9842b226eebf81606460405161011c929190610362565b60405180910390a15050565b600082601f83011261013957600080fd5b813561014c61014782610430565b610403565b9150808252602083016020830185838301111561016857600080fd5b6101738382846104a1565b50505092915050565b60008135905061018b816104f4565b92915050565b600080604083850312156101a457600080fd5b600083013567ffffffffffffffff8111156101be57600080fd5b6101ca85828601610128565b925050602083013567ffffffffffffffff8111156101e757600080fd5b6101f385828601610128565b9150509250929050565b60008060006060848603121561021257600080fd5b600084013567ffffffffffffffff81111561022c57600080fd5b61023886828701610128565b935050602084013567ffffffffffffffff81111561025557600080fd5b61026186828701610128565b92505060406102728682870161017c565b9150509250925092565b6102858161048f565b82525050565b60006102968261045c565b6102a08185610467565b93506102b08185602086016104b0565b6102b9816104e3565b840191505092915050565b60006102d1601483610467565b91507f63726561746543726f7373436861696e526561640000000000000000000000006000830152602082019050919050565b6000610311601583610467565b91507f63726561746543726f7373436861696e577269746500000000000000000000006000830152602082019050919050565b61034d81610478565b82525050565b61035c81610482565b82525050565b6000606082019050818103600083015261037b816102c4565b9050818103602083015261038f818561028b565b905061039e604083018461027c565b9392505050565b600060608201905081810360008301526103be81610304565b905081810360208301526103d2818561028b565b90506103e16040830184610353565b9392505050565b60006020820190506103fd6000830184610344565b92915050565b6000604051905081810181811067ffffffffffffffff8211171561042657600080fd5b8060405250919050565b600067ffffffffffffffff82111561044757600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b600082825260208201905092915050565b6000819050919050565b600060ff82169050919050565b600061049a82610482565b9050919050565b82818337600083830152505050565b60005b838110156104ce5780820151818401526020810190506104b3565b838111156104dd576000848401525b50505050565b6000601f19601f8301169050919050565b6104fd81610482565b811461050857600080fd5b5056fea2646970667358221220c7df21450d0c89840fa52a2117625a0590b0895ef365b7a8caa4cb2dfd13c55b64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetBurnEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetMintEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approveEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendAckedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"chain_Code\",\"type\":\"string\"}],\"name\":\"setChainCodeEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"gateway_Address\",\"type\":\"address\"}],\"name\":\"setGatewayEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"startTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"}],\"name\":\"takeOutEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferEvent\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"funcn\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"}],\"name\":\"createCrossChainRead\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"funcn\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"uint8\",\"name\":\"value\",\"type\":\"uint8\"}],\"name\":\"createCrossChainWrite\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"value1\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CREATECROSSCHAINREAD = "createCrossChainRead";

    public static final String FUNC_CREATECROSSCHAINWRITE = "createCrossChainWrite";

    public static final String FUNC_VALUE1 = "value1";

    public static final Event ASSETBURNEVENT_EVENT = new Event("AssetBurnEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event ASSETMINTEVENT_EVENT = new Event("AssetMintEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVEEVENT_EVENT = new Event("approveEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SENDACKEDEVENT_EVENT = new Event("sendAckedEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event SENDTXEVENT_EVENT = new Event("sendTxEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event SETCHAINCODEEVENT_EVENT = new Event("setChainCodeEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event SETGATEWAYEVENT_EVENT = new Event("setGatewayEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event STARTTXEVENT_EVENT = new Event("startTxEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event TAKEOUTEVENT_EVENT = new Event("takeOutEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFEREVENT_EVENT = new Event("transferEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    protected CrossChainContractTest(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public List<AssetBurnEventEventResponse> getAssetBurnEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ASSETBURNEVENT_EVENT, transactionReceipt);
        ArrayList<AssetBurnEventEventResponse> responses = new ArrayList<AssetBurnEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AssetBurnEventEventResponse typedResponse = new AssetBurnEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeAssetBurnEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ASSETBURNEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeAssetBurnEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ASSETBURNEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<AssetMintEventEventResponse> getAssetMintEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ASSETMINTEVENT_EVENT, transactionReceipt);
        ArrayList<AssetMintEventEventResponse> responses = new ArrayList<AssetMintEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AssetMintEventEventResponse typedResponse = new AssetMintEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.from = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeAssetMintEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ASSETMINTEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeAssetMintEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ASSETMINTEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<ApproveEventEventResponse> getApproveEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVEEVENT_EVENT, transactionReceipt);
        ArrayList<ApproveEventEventResponse> responses = new ArrayList<ApproveEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApproveEventEventResponse typedResponse = new ApproveEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeApproveEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(APPROVEEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeApproveEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(APPROVEEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<SendAckedEventEventResponse> getSendAckedEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SENDACKEDEVENT_EVENT, transactionReceipt);
        ArrayList<SendAckedEventEventResponse> responses = new ArrayList<SendAckedEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SendAckedEventEventResponse typedResponse = new SendAckedEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.cross_TxNo = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.txType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeSendAckedEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(SENDACKEDEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeSendAckedEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(SENDACKEDEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<SendTxEventEventResponse> getSendTxEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SENDTXEVENT_EVENT, transactionReceipt);
        ArrayList<SendTxEventEventResponse> responses = new ArrayList<SendTxEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SendTxEventEventResponse typedResponse = new SendTxEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.cross_TxNo = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.txType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeSendTxEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(SENDTXEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeSendTxEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(SENDTXEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<SetChainCodeEventEventResponse> getSetChainCodeEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SETCHAINCODEEVENT_EVENT, transactionReceipt);
        ArrayList<SetChainCodeEventEventResponse> responses = new ArrayList<SetChainCodeEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SetChainCodeEventEventResponse typedResponse = new SetChainCodeEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.chain_Code = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeSetChainCodeEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(SETCHAINCODEEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeSetChainCodeEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(SETCHAINCODEEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<SetGatewayEventEventResponse> getSetGatewayEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(SETGATEWAYEVENT_EVENT, transactionReceipt);
        ArrayList<SetGatewayEventEventResponse> responses = new ArrayList<SetGatewayEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            SetGatewayEventEventResponse typedResponse = new SetGatewayEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.gateway_Address = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeSetGatewayEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(SETGATEWAYEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeSetGatewayEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(SETGATEWAYEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<StartTxEventEventResponse> getStartTxEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(STARTTXEVENT_EVENT, transactionReceipt);
        ArrayList<StartTxEventEventResponse> responses = new ArrayList<StartTxEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            StartTxEventEventResponse typedResponse = new StartTxEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.cross_TxNo = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.txType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeStartTxEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(STARTTXEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeStartTxEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(STARTTXEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<TakeOutEventEventResponse> getTakeOutEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TAKEOUTEVENT_EVENT, transactionReceipt);
        ArrayList<TakeOutEventEventResponse> responses = new ArrayList<TakeOutEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TakeOutEventEventResponse typedResponse = new TakeOutEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTakeOutEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(TAKEOUTEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeTakeOutEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(TAKEOUTEVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public List<TransferEventEventResponse> getTransferEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFEREVENT_EVENT, transactionReceipt);
        ArrayList<TransferEventEventResponse> responses = new ArrayList<TransferEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventEventResponse typedResponse = new TransferEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.operation = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeTransferEventEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(TRANSFEREVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeTransferEventEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(TRANSFEREVENT_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public TransactionReceipt createCrossChainRead(String funcn, String key) {
        final Function function = new Function(
                FUNC_CREATECROSSCHAINREAD, 
                Arrays.<Type>asList(new Utf8String(funcn),
                new Utf8String(key)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void createCrossChainRead(String funcn, String key, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATECROSSCHAINREAD, 
                Arrays.<Type>asList(new Utf8String(funcn),
                new Utf8String(key)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreateCrossChainRead(String funcn, String key) {
        final Function function = new Function(
                FUNC_CREATECROSSCHAINREAD, 
                Arrays.<Type>asList(new Utf8String(funcn),
                new Utf8String(key)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, String> getCreateCrossChainReadInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATECROSSCHAINREAD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public TransactionReceipt createCrossChainWrite(String funcn, String key, BigInteger value) {
        final Function function = new Function(
                FUNC_CREATECROSSCHAINWRITE, 
                Arrays.<Type>asList(new Utf8String(funcn),
                new Utf8String(key),
                new Uint8(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void createCrossChainWrite(String funcn, String key, BigInteger value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATECROSSCHAINWRITE, 
                Arrays.<Type>asList(new Utf8String(funcn),
                new Utf8String(key),
                new Uint8(value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForCreateCrossChainWrite(String funcn, String key, BigInteger value) {
        final Function function = new Function(
                FUNC_CREATECROSSCHAINWRITE, 
                Arrays.<Type>asList(new Utf8String(funcn),
                new Utf8String(key),
                new Uint8(value)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, String, BigInteger> getCreateCrossChainWriteInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATECROSSCHAINWRITE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }

    public BigInteger value1() throws ContractException {
        final Function function = new Function(FUNC_VALUE1, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public static CrossChainContractTest load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new CrossChainContractTest(contractAddress, client, credential);
    }

    public static CrossChainContractTest deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(CrossChainContractTest.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class AssetBurnEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String from;

        public BigInteger value;
    }

    public static class AssetMintEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class ApproveEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public BigInteger value;
    }

    public static class SendAckedEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String cross_TxNo;

        public BigInteger txType;
    }

    public static class SendTxEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String cross_TxNo;

        public BigInteger txType;
    }

    public static class SetChainCodeEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String chain_Code;
    }

    public static class SetGatewayEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String gateway_Address;
    }

    public static class StartTxEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String cross_TxNo;

        public BigInteger txType;
    }

    public static class TakeOutEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public String to;
    }

    public static class TransferEventEventResponse {
        public TransactionReceipt.Logs log;

        public String operation;

        public BigInteger value;
    }
}
