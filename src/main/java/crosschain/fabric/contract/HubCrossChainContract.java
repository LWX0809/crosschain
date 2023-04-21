package crosschain.fabric.contract;

import crosschain.fabric.contract.entity.CrossChainObj;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Contract(
        name = "HubCrossChainContract",
        info = @Info(
                title = "HubCrossChain contract",
                description = "The HubCrossChain contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "178366679@qq.com",
                        name = "HubCrossChain",
                        url = "https://HubCrossChain.com")))
@Default
public class HubCrossChainContract implements ContractInterface {
    private final static Logger logger = Logger.getLogger(HubCrossChainContract.class.getName());

    private final String Version="v1.0.0";
    private int crossTxNum;

    @Override
    public Context createContext(ChaincodeStub stub) {
        return new CrossChainContext(stub);
    }
    public HubCrossChainContract(){

    }

    //can only be called once
    @Transaction
    public void init(CrossChainContext ctx){
        this.crossTxNum =0;
        logger.info("cross contract init");
    }

    //Internal call
    @Transaction
    public String createCrossTxNo(CrossChainContext ctx, String srcChainCode, String destChainCode){
        this.crossTxNum++;
        String crossTxNo=srcChainCode+":"+destChainCode+":"+this.crossTxNum;
        return crossTxNo;
    }

    @Transaction
    public CrossChainObj createCrossTx(CrossChainContext ctx, String  srcBid, String  destBid, String  srcChainCode, String  destChainCode, int txType, String []  payload, String  txOrigin){
        CrossChainObj crossChainObj=new CrossChainObj();

        crossChainObj.setSrcChainCode(srcChainCode);
        crossChainObj.setDestChainCode(destChainCode);
        crossChainObj.setSrcBid(srcBid);
        crossChainObj.setDestBid(destBid);
        crossChainObj.setTxType(txType);
        crossChainObj.setPayload(payload);
        crossChainObj.setTxOrigin(txOrigin);
        String crossTxNo=createCrossTxNo(ctx,srcChainCode,destChainCode);
        crossChainObj.setCrossTxNo(crossTxNo);

        return crossChainObj;
    }

    @Transaction
    public void startTx(CrossChainContext ctx, String srcAddress, String  destBid, String  srcChainCode, String  destChainCode, int txType, String []  payload, String  extension, String  remark, String  version){
        if (txType == 0) {
            //
        } else if (txType == 1) {
          ////
            //
        } else {
            //contract call
//            subStartTxCall(srcAddress, srcChainCode, payload);
        }

        CrossChainObj crossChainObj = createCrossTx(ctx,srcAddress,destBid, srcChainCode, destChainCode, txType, payload, "SRC");
        crossChainObj.setExtension(  extension);
        crossChainObj.setRemark(remark);
        crossChainObj.setVersion(version);

        //Store crossTxObj into List
        ctx.crossChainTxList.addCrossChainTx(crossChainObj);
        logger.info("startTx...:crosschain data:"+crossChainObj.toString());

        //create a cross-chain event  startTxEvent("startTx", crossTxObj.CrossTxNo, txType);
        ChaincodeStub stub=ctx.getStub();
        String crossTxNo_txType=crossChainObj.getCrossTxNo()+" "+txType;
        byte [] bytes=crossTxNo_txType.getBytes(StandardCharsets.UTF_8);
        stub.setEvent("startTx",bytes);

    }

    @Transaction
    public void sendTx(CrossChainContext ctx){



    }

    //When calling the user contract (or checking data on the chain), an event is initiated and returned to the gateway
    //Called by the call method of the cross-chain gateway
    @Transaction
    public String  sendAcked(CrossChainContext ctx, String crossChainObjString, String  txResult, String version, String  proof){

        byte[] _crossChainObjByte=crossChainObjString.getBytes(StandardCharsets.UTF_8);
        logger.info("sendAcked:"+"crossChainObj bytes ："+new String(_crossChainObjByte,StandardCharsets.UTF_8));
        CrossChainObj crossChainObj= CrossChainObj.deserialize(_crossChainObjByte);
        logger.info("sendAcked:"+"crossChainObj deserialize ："+crossChainObj.toString());

        //Cross-chain contract call
        if(txResult.equals("2")){
            String [] payload=crossChainObj.getPayload();
            String userContractName=payload[0];
            String funcName=payload[1];
            String key=payload[2];
            String value=payload[3];

            //On-chain or data processing , Call user contract
            //Return it as the result of the event
            ChaincodeStub stub = ctx.getStub();
            value=stub.getStringState(key);
            stub.setEvent("sendAcked",value.getBytes(StandardCharsets.UTF_8));
            return value;
        }
        else{
            //
            return null;
        }


    }

    @Transaction
    public String  setKey(CrossChainContext ctx, String key, String  value){
        ChaincodeStub stub = ctx.getStub();
        stub.putState(key,value.getBytes(StandardCharsets.UTF_8));
        value=stub.getStringState(key);
        stub.setEvent("setKey",value.getBytes(StandardCharsets.UTF_8));
        return value;
    }

    @Transaction
    public String  crossChainWrite(CrossChainContext ctx,String key, String  value){
        ChaincodeStub stub = ctx.getStub();
        stub.putState(key,value.getBytes(StandardCharsets.UTF_8));
        stub.setEvent("crossChainWrite",value.getBytes(StandardCharsets.UTF_8));
        return value;
    }

    @Transaction
    public String  crossChainRead(CrossChainContext ctx, String key){
        ChaincodeStub stub = ctx.getStub();

        String value=stub.getStringState(key);
        stub.setEvent("crossChainRead",value.getBytes(StandardCharsets.UTF_8));
        return value;
    }


}
