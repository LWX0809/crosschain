package crosschain.fabric.gateway_connect;

import com.google.protobuf.InvalidProtocolBufferException;
import crosschain.fabric.contract.entity.CrossChainObj;
import crosschain.fabric.gateway_connect.chaincodeEvent.ChaincodeEventCapture;
import crosschain.fisco.client.entity._CrossTxObj;
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

public class HubCrossChainGatewayTest {
    private Connect connect;
    private Gateway gateway;
    private Network network;
    private Contract contract;
//    private static HubCrossChainGatewayTest hubCrossChainGateway;

    private final static Logger logger = Logger.getLogger(HubCrossChainGatewayTest.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException, ContractException, InvalidArgumentException {
//        hubCrossChainGateway = new HubCrossChainGatewayTest();
//        //install合约时设置的这个名字
//        hubCrossChainGateway.connect("crossChainContract");

    }

    public HubCrossChainGatewayTest() throws IOException {
//        hubCrossChainGateway.connect("crossChainContract");
    }

    //调用fabric合约
    public  String  testCrossChainWrite(String key,String value) throws InterruptedException, TimeoutException, ContractException {
        System.out.println("_crossChaintx data:" + key +" value: "+value);
        byte[] result = contract.submitTransaction("crossChainWrite", key,value);
        String value1=new String(result);
        System.out.println("value:"+value1);
        return "write success";
    }
    //调用fabric合约
    public  String  testCrossChainRead(String key) throws InterruptedException, TimeoutException, ContractException {
        System.out.println("_crossChaintx data:" + key );
        byte[] result = contract.submitTransaction("crossChainRead", key);
        String value=new String(result);
        System.out.println("value:"+value);
        return value;
    }


    //连接gateway
    public void connect(String contractName) throws IOException {
        connect = new Connect();
        this.gateway = connect.connectGateway();
        this.network = connect.getNetwork(gateway);
        System.out.println(network.toString());
        this.contract = connect.getContract(network, contractName);
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


}
