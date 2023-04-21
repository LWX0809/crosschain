package crosschain.fisco.solidity;


import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class Storage extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052348015600f57600080fd5b50603f80601d6000396000f3fe6080604052600080fdfea264697066735822122028f4f8a5c35744e1307bdd1ac04359826257414605ec57753749dfa0accd133664736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052348015600f57600080fd5b50603f80601d6000396000f3fe6080604052600080fdfea2646970667358221220226e1e310997d62e8f494d4379a5134ce7857caba02c0d50708ba0a36ce9631364736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetBurnEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetMintEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approveEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendAckedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"chain_Code\",\"type\":\"string\"}],\"name\":\"setChainCodeEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"gateway_Address\",\"type\":\"address\"}],\"name\":\"setGatewayEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"startTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"}],\"name\":\"takeOutEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferEvent\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

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

    protected Storage(String contractAddress, Client client, CryptoKeyPair credential) {
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

    public static Storage load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new Storage(contractAddress, client, credential);
    }

    public static Storage deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(Storage.class, client, credential, getBinary(client.getCryptoSuite()), "");
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
