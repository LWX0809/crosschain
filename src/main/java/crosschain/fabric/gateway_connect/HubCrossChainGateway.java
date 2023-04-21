package crosschain.fabric.gateway_connect;

import com.google.protobuf.InvalidProtocolBufferException;
import crosschain.fabric.contract.entity.CrossChainObj;
import crosschain.fabric.gateway_connect.chaincodeEvent.ChaincodeEventCapture;
import crosschain.fisco.client.entity._AckProof;
import crosschain.fisco.client.entity._CrossTxObj;
import crosschain.fisco.client.entity._SendProof;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class  HubCrossChainGateway {
    private Connect connect;
    private Gateway gateway;
    private Network network;
    private Contract contract;
    private static HubCrossChainGateway hubCrossChainGateway;

    private final static Logger logger = Logger.getLogger(HubCrossChainGateway.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, ContractException, InvalidArgumentException {
        hubCrossChainGateway = new HubCrossChainGateway();
        //install合约时设置的这个名字
        hubCrossChainGateway.connect("crossChainContract");

//
//       hubCrossChainGateway.getEvents();

        //invoke fisco gateway function


    }

    public HubCrossChainGateway() throws IOException {
        hubCrossChainGateway.connect("crossChainContract");
    }

    public void test() throws InterruptedException, TimeoutException, ContractException {
        CrossChainObj crossChainObj = new CrossChainObj();
        String[] payload = new String[]{"userContractName", "set", "a", ""};
        //不能为空，反序列化解析会出问题
        crossChainObj.setCrossTxNo("1");
        crossChainObj.setSrcChainCode("1234");
        crossChainObj.setDestChainCode("1111");
        crossChainObj.setExtension("Extension");
        crossChainObj.setRemark("remark");
        crossChainObj.setDestBid("DestBid");
        crossChainObj.setSrcBid("SrcBid");
        crossChainObj.setTxOrigin("TxOrigin");
        crossChainObj.setTxType(2);
        crossChainObj.setAckProofs_LedgerSeq(0);
        crossChainObj.setAckProofs_TxHash("");
        crossChainObj.setAckProofs_Verifieraddr("");
        crossChainObj.setSendProofs_LedgerSeq(0);
        crossChainObj.setSendProofs_TxHash("");
        crossChainObj.setSendProofs_Verifieraddr("");
        crossChainObj.setPayload(payload);
        crossChainObj.setTxResult("result");
        crossChainObj.setTxRefunded("txRefunded");
        crossChainObj.setVersion("1.0");
        byte[] proof = new String("proof").getBytes(StandardCharsets.UTF_8);
        hubCrossChainGateway.call(crossChainObj, "2", "1.0", proof);
    }

    //连接gateway
    public void connect(String contractName) throws IOException {
        connect = new Connect();
        this.gateway = connect.connectGateway();
        this.network = connect.getNetwork(gateway);
        System.out.println(network.toString());
        this.contract = connect.getContract(network, contractName);
    }

    public CrossChainObj fiscoCrossChainObjToFbaricCrossChaonObj(_CrossTxObj _crossTxObj){
        CrossChainObj crossChainObj=new CrossChainObj();
        crossChainObj.setCrossTxNo(_crossTxObj.getCrossTxNo());
        crossChainObj.setSrcChainCode(_crossTxObj.getSrcChainCode());
        crossChainObj.setDestChainCode(_crossTxObj.getDestChainCode());
        crossChainObj.setSrcBid(_crossTxObj.getSrcBid());
        crossChainObj.setDestBid(_crossTxObj.getDestBid());
        crossChainObj.setTxType(_crossTxObj.getTxType());
        String[] payload = new String[]{"userContractName", "set", "a", ""};
        crossChainObj.setPayload(payload);
        crossChainObj.setRemark(_crossTxObj.getRemark());
        crossChainObj.setTxResult(_crossTxObj.getTxResult());
        crossChainObj.setTxRefunded(_crossTxObj.getTxRefunded());
        crossChainObj.setExtension(_crossTxObj.getExtension());
        crossChainObj.setSendProofs_LedgerSeq(_crossTxObj.getSendProofs().getLedgerSeq());
        crossChainObj.setSendProofs_TxHash(_crossTxObj.getSendProofs().getTxHash());
        crossChainObj.setSendProofs_Verifieraddr(_crossTxObj.getSendProofs().getVerifieraddr());
        crossChainObj.setAckProofs_LedgerSeq(_crossTxObj.getAckProofs().getLedgerSeq());
        crossChainObj.setAckProofs_TxHash(_crossTxObj.getAckProofs().getTxHash());
        crossChainObj.setAckProofs_Verifieraddr(_crossTxObj.getAckProofs().getVerifieraddr());
        crossChainObj.setVersion(_crossTxObj.getVersion());
        crossChainObj.setTxOrigin(_crossTxObj.getTxOrigin());
        logger.info(crossChainObj.toString());
        return crossChainObj;
    }

    //调用合约
    public String  call(CrossChainObj crossChainObj, String txResult, String version, byte[] proof) throws InterruptedException, TimeoutException, ContractException {
        byte[] _crossChainObjByte = CrossChainObj.serialize(crossChainObj);
//        byte[] result1 = contract.submitTransaction("init");
        String _crossChainObjString = new String(_crossChainObjByte, StandardCharsets.UTF_8);
        System.out.println("_crossChainObjByte to string" + _crossChainObjString);
        byte[] result = contract.submitTransaction("sendAcked", _crossChainObjString, txResult, version, new String(proof, StandardCharsets.UTF_8));
        String value=new String(result);
        System.out.println("value:"+value);
        return value;
    }




    //监听合约事件
    public void getEvents() throws InvalidArgumentException, InterruptedException {
//        Consumer<ContractEvent> listener = event -> {
//            byte[] bytes = event.getPayload().get();
//            String s = new String(bytes);
//            System.out.println("1111111" + s);
//
//        };
//        String chaincodeEventListenerHandle = network.getChannel().registerChaincodeEventListener(
//                Pattern.compile(".*"), Pattern.compile(Pattern.quote("sendAcked")),
//                (handle, blockEvent, chaincodeEvent) -> {
////                    final String es = blockEvent.getPeer() != null
////                            ? blockEvent.getPeer().getName()
////                            : blockEvent.getEventHub().getName();
//                    System.out.format(
//                            "Chaincode event with handle: %s"
//                                    + ", thread name: %s"
//                                    + ", chaincode Id: %s"
//                                    + ", chaincode event name: %s"
//                                    + ", transaction id: %s"
//                                    + ", event payload: \"%s\""
//                                    + ", from eventhub: %s\n",
//                            Thread.currentThread().getName(),
//                            handle,
//                            chaincodeEvent.getChaincodeId(),
//                            chaincodeEvent.getEventName(),
//                            chaincodeEvent.getTxId(),
//                            new String(chaincodeEvent.getPayload()));
//
//                });

//        Consumer<ContractEvent> contractEventConsumer=contract.addContractListener(listener,Pattern.compile(".*"));
//        System.out.println(contractEventConsumer);


        Vector<ChaincodeEventCapture> chaincodeEvents = new Vector<>(); // Test list to capture
        String chaincodeEventListenerHandle = setChaincodeEventListener(network.getChannel(), "sendAcked", chaincodeEvents);


        boolean eventDone = waitForChaincodeEvent(150, network.getChannel(), chaincodeEvents, chaincodeEventListenerHandle);
        logger.info("eventDone: " + eventDone);


    }

    public static String setChaincodeEventListener(Channel channel, String expectedEventName, Vector<ChaincodeEventCapture> chaincodeEvents) throws InvalidArgumentException {

        ChaincodeEventListener chaincodeEventListener = new ChaincodeEventListener() {
            @Override
            public void received(String handle, BlockEvent blockEvent, ChaincodeEvent chaincodeEvent) {
                chaincodeEvents.add(new ChaincodeEventCapture(handle, blockEvent, chaincodeEvent));

                Peer eventHub = blockEvent.getPeer();
                String eventHubName="";
                if(eventHub != null){
                    eventHubName = blockEvent.getPeer().getName();
                }
//                else {
//                    eventHubName = blockEvent.getEventHub().getName();
//                }
                // Here put what you want to do when receive chaincode event
                System.out.println("RECEIVED CHAINCODE EVENT with handle: " + handle + ", chaincodeId: " + chaincodeEvent.getChaincodeId() + ", chaincode event name: " + chaincodeEvent.getEventName() + ", transactionId: " + chaincodeEvent.getTxId() +", event Payload: " + new String(chaincodeEvent.getPayload()) + ", from eventHub: " + eventHub);
            }
        };

//        String eventListenerHandle = channel.registerChaincodeEventListener(Pattern.compile(".*"), Pattern.compile(Pattern.quote(expectedEventName)), chaincodeEventListener);
        String eventListenerHandle = channel.registerChaincodeEventListener(Pattern.compile("d5a1157ad72f42ca7e5bddf3e6c6d6d35bd61a8ba7e0326c5a08e05d4b098a0c"), Pattern.compile(Pattern.quote(expectedEventName)), chaincodeEventListener);

        return eventListenerHandle;
    }

    public static boolean waitForChaincodeEvent(Integer timeout, Channel channel, Vector<ChaincodeEventCapture> chaincodeEvents, String chaincodeEventListenerHandle) throws InvalidArgumentException, InterruptedException {
        boolean eventDone = false;
        if (chaincodeEventListenerHandle != null) {
//            int numberEventsExpected =channel.getEventHubs().size() + channel.getPeers(EnumSet.of(Peer.PeerRole.EVENT_SOURCE)).size();
//            logger.info("numberEventsExpected: " +                                                                                                                                                        numberEventsExpected);
//            //just make sure we get the notifications
//            if (timeout.equals(0)) {
//                // get event without timer
//                while (chaincodeEvents.size() != numberEventsExpected) {
//                    // do nothing
//                }
//                eventDone = true;
//            } else {
//                // get event with timer
//                for (int i = 0; i < timeout; i++) {
//                    if (chaincodeEvents.size() == numberEventsExpected) {
//                        eventDone = true;
//                        break;
//                    } else {
//                        try {
//                            double j = i;
//                            j = j / 10;
//                            logger.info(j + " second");
//                            Thread.sleep(100); // wait for the events for one tenth of second.
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
            Thread.sleep(10000);

            logger.info("chaincodeEvents.size(): " + chaincodeEvents.size());

            // unregister event listener
            channel.unregisterChaincodeEventListener(chaincodeEventListenerHandle);
            int i = 1;
            // arrived event handling
            for (ChaincodeEventCapture chaincodeEventCapture : chaincodeEvents) {
                logger.info("Event number. " + i);
                logger.info("event capture object: " + chaincodeEventCapture.toString());
                logger.info("Event Handle: " + chaincodeEventCapture.getHandle());
                logger.info("Event TxId: " + chaincodeEventCapture.getChaincodeEvent().getTxId());
                logger.info("Event Name: " + chaincodeEventCapture.getChaincodeEvent().getEventName());
                logger.info("Event Payload: " + chaincodeEventCapture.getChaincodeEvent().getPayload()); // byte
                logger.info("Event ChaincodeId: " + chaincodeEventCapture.getChaincodeEvent()
                        .getChaincodeId());
                BlockEvent blockEvent = chaincodeEventCapture.getBlockEvent();

                try {
                    logger.info("Event Channel: " + blockEvent.getChannelId());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                logger.info("Event Hub: ....." );
//                logger.info("Event Hub: " + blockEvent.getEventHub());
                i++;
            }

        } else {
            logger.info("chaincodeEvents.isEmpty(): " + chaincodeEvents.isEmpty());
        }
        logger.info("eventDone: " + eventDone);
        return eventDone;
    }

}
