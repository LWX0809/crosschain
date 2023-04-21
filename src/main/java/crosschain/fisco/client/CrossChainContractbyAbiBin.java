package crosschain.fisco.client;


import crosschain.fabric.contract.HubCrossChainContract;
import crosschain.fabric.contract.entity.CrossChainObj;
import crosschain.fabric.gateway_connect.HubCrossChainGateway;
import crosschain.fisco.client.entity.*;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.ABICodec;
import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.*;
import org.fisco.bcos.sdk.model.EventLog;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionBaseException;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionException;
import org.hyperledger.fabric.gateway.ContractException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

//实现对合约的部署调用
//abi以及合约部署调用已经更新成最新的
public class CrossChainContractbyAbiBin {

    static Logger logger= LoggerFactory.getLogger(CrossChainContractbyAbiBin.class);

    private BcosSDK bcosSDK;
    private Client client;
    private CryptoKeyPair cryptoKeyPair;
    private String abi= "[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetBurnEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetMintEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approveEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendAckedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"chain_Code\",\"type\":\"string\"}],\"name\":\"setChainCodeEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"gateway_Address\",\"type\":\"address\"}],\"name\":\"setGatewayEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"startTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"}],\"name\":\"takeOutEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferEvent\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approve\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"addr\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"spender\",\"type\":\"address\"}],\"name\":\"getAllowance\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"crossTxNo\",\"type\":\"string\"}],\"name\":\"getCrossTx\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"bytes\",\"name\":\"\",\"type\":\"bytes\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"crossTxNo\",\"type\":\"string\"}],\"name\":\"getCrossTxSendAndAckProof\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getVersion\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"crossChainAdd\",\"type\":\"address\"}],\"name\":\"initialize\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"crossTxNo\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"txResult\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"version\",\"type\":\"string\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"sendAcked\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"srcBid\",\"type\":\"string\"},{\"internalType\":\"address payable\",\"name\":\"destAddress\",\"type\":\"address\"},{\"internalType\":\"string\",\"name\":\"srcChainCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"destChainCode\",\"type\":\"string\"},{\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"},{\"internalType\":\"string\",\"name\":\"crossTxNo\",\"type\":\"string\"},{\"internalType\":\"bytes\",\"name\":\"payload\",\"type\":\"bytes\"},{\"internalType\":\"string\",\"name\":\"extension\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"remark\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"version\",\"type\":\"string\"},{\"internalType\":\"bytes\",\"name\":\"proof\",\"type\":\"bytes\"}],\"name\":\"sendTx\",\"outputs\":[],\"stateMutability\":\"payable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"chaincode\",\"type\":\"string\"}],\"name\":\"setChainCode\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"gatewayAddress\",\"type\":\"address\"}],\"name\":\"setGateway\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"srcAddress\",\"type\":\"address\"},{\"internalType\":\"string\",\"name\":\"destBid\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"srcChainCode\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"destChainCode\",\"type\":\"string\"},{\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"},{\"internalType\":\"bytes\",\"name\":\"payload\",\"type\":\"bytes\"},{\"internalType\":\"string\",\"name\":\"extension\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"remark\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"version\",\"type\":\"string\"}],\"name\":\"startTx\",\"outputs\":[],\"stateMutability\":\"payable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"crossTxNo\",\"type\":\"string\"},{\"internalType\":\"address payable\",\"name\":\"to\",\"type\":\"address\"}],\"name\":\"takeOut\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transfer\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";
    //console :"0x05f2b7ddbd8db80351b38965cbf538c9669e205c"
    private  String crossChainContractAddress="0xb04b655e78fed67925c18d0705796ceb58ff28bb";

    AssembleTransactionProcessor transactionProcessor;
    private static HubCrossChainGateway hubCrossChainGateway;

    public static void main(String[] args) throws Exception {
        CrossChainContractbyAbiBin crossChainContractbyAbiBin=new CrossChainContractbyAbiBin();
        crossChainContractbyAbiBin.initialize();
        crossChainContractbyAbiBin.getEvents("1","latest");
        hubCrossChainGateway=new HubCrossChainGateway();

    }
    public void initialize() throws Exception{
        @SuppressWarnings("resource")
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//         初始化BcosSDK对象
        bcosSDK=context.getBean(BcosSDK.class);
//         获取Client对象，此处传入的群组ID为1
        client=bcosSDK.getClient(1);
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        cryptoKeyPair=client.getCryptoSuite().getCryptoKeyPair();

        //基于abi和binary文件 部署、交易、查询合约   部署需要bin文件，仅交易查询的话，后边bin为“”
        transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, cryptoKeyPair, "src/main/resources/abi/", "src/main/resources/bin/");

    }

    //只需要部署一次；
    public TransactionResponse deployCrossChainContract(String contractName) throws Exception {
        //基于abi和binary文件 部署、交易、查询合约   部署需要bin文件，仅交易查询的话，后边bin为“”
        transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, cryptoKeyPair, "src/main/resources/abi/", "src/main/resources/bin/");

        // 部署HelloWorld合约。第一个参数为合约名称，第二个参数为合约构造函数的列表，是List<Object>类型。
        TransactionResponse response = transactionProcessor.deployByContractLoader(contractName, new ArrayList<>());
         crossChainContractAddress=response.getContractAddress();
        //<<<<<<<<<<<<CrossChainContractAddress>>>>>>>>>>::::0xc0f18d06c4b7b34ff64b2abf84a6754139107ed9
        System.out.println("<<<<<<<<<<<<contractAddress>>>>>>>>>>::::"+crossChainContractAddress);
        return response;
    }

    //账户生成
//    public void creatAccount(){
//        // 创建非国密类型的CryptoSuite
//        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
//       // 随机生成非国密公私钥对
//        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
//       // 获取账户地址
//        String accountAddress = cryptoKeyPair.getAddress();
//    }

    public void getEvents(String fromBlock,String toBlock){
        //合约事件推送
        //1.注册请求2.节点回复3.event log数据推送
        //1、将链上所有/最新的事件回调至客户端

        // 参数设置
        EventLogParams params1=new EventLogParams();
        // 参数设置
        params1.setFromBlock(fromBlock);
        // toBlock设置为"latest"，处理至最新区块继续等待新的区块
        params1.setToBlock(toBlock);

        // addresses设置为空数组，匹配所有的合约地址 ，将链上所有事件回调至客户端
        params1.setAddresses(new ArrayList<String>());
        // topics设置为空数组，匹配所有的Event
        params1.setTopics(new ArrayList<Object>());

        //回调函数
        class SubscribeCallback implements EventCallback {
            public transient Semaphore semaphore = new Semaphore(1, true);

            SubscribeCallback() {
                try {
                    semaphore.acquire(1);
                } catch (InterruptedException e) {
                    logger.error("error :", e);
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public void onReceiveLog(int status, List<EventLog> logs) {
                System.out.println("+++++++++++++++++++++++++++++开始执行onReceiveLog函数+++++++++++++++++++++++++++++++");
                Assert.assertEquals(status, 0);
                String str = "status in onReceiveLog : " + status;
                logger.debug(str);
                System.out.println("+++++++++++++++++++++++++++++"+str+"+++++++++++++++++++++++++++++++");
                semaphore.release();
                // decode event
                //就没执行这个方法
                if (logs != null) {
                    for (EventLog log : logs) {
                        logger.debug(
                                " blockNumber:"
                                        + log.getBlockNumber()
                                        + ",txIndex:"
                                        + log.getTransactionIndex()
                                        + " data:"
                                        + log.getData()
                                        + " topics: "+log.getTopics());
                        ABICodec abiCodec = new ABICodec(client.getCryptoSuite());

                        System.out.println("__________________________");
                        System.out.println(  " --blockNumber:"
                                + log.getBlockNumber()
                                + ",--txIndex:"
                                + log.getTransactionIndex()
                                + "--- data:"
                                + log.getData()
                                + ",--topics: "+log.getTopics());

                        try {
                            //todo：需要监听所有类型的跨链事件
                            List<Object> list = abiCodec.decodeEvent(abi, "startTxEvent", log);  //
//                            List<Object> list = abiCodec.decodeEventByTopic(abi,log.getTopics().get(0), log);  //
                            logger.debug("decode event log content, " + list);
                            System.out.println("===========list："+list);

                            //合约里：emit sendTxEvent("sendTx", crossTxNo, txType);   emit触发合约事件
                            //_crossTxNo：是经过拼接的   srcChainCodestChainCodede：未知（但是是同一个数据）
                            String _crossTxNo= (String) list.get(1);
                            BigInteger _crossTxType = (BigInteger) list.get(2);

                            BigInteger _ledgerSeq=log.getBlockNumber();
                            String _txHash =log.getTransactionHash();

                            if(list.get(0).equals("startTx")){
                                //用户发送跨链交易事件，网关节点转发跨链交易
                                System.out.println("startTx事件");
                                _startTx(_crossTxNo,_crossTxType,_ledgerSeq,_txHash);
                            }else if(list.get(0).equals("sendTx")){
                                //网关节点转发的跨链交易成功上链，需要回Ack给源链
                                System.out.println("sendTx事件");
                                _sendTx(_crossTxNo,_ledgerSeq,_txHash);
                            }
                            else if(list.get(0).equals("sendAcked")){
                                //网关节点回的Ack上链成功，需要给目标连回复Ack
                                System.out.println("sendAcked事件");
                                _sendAcked(_crossTxNo,_ledgerSeq,_txHash);
                            }
                            else{
                                System.out.println("其他事件，不予捕捉");
                            }
                            System.out.println();
                            // list = [Alice, Bob]
//                            Assert.assertEquals(2, list.size());
                        } catch (ABICodecException | TransactionBaseException e) {
                            logger.error("decode event log error, " + e.getMessage());
                            System.out.println("decode event log error, " + e.getMessage());
                        } catch (TransactionException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (ContractException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        SubscribeCallback subscribeEventCallback= new SubscribeCallback();
        EventSubscribe eventSubscribe=new EventSubscribeImp( client.getGroupManagerService(), client.getEventResource(), client.getGroupId());

        String registerId=eventSubscribe.subscribeEvent(params1,subscribeEventCallback);

        System.out.println("++++++++++++++++++:"+registerId+"+++++++++++++++++++++++++++");

    }

    //_ledgerSeq  是event.blockNumber
    public void _startTx(String _crossTxNo,BigInteger _crossTxType,BigInteger _ledgerSeq,String _txHash) throws ABICodecException, TransactionBaseException, IOException, TransactionException, InterruptedException, TimeoutException, ContractException {

        System.out.println("开始调用11111 ==========注意查看");
        List<Object> params=new ArrayList<>();
        params.add(_crossTxNo);
        //同步方式发送交易
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("CrossChainContract", crossChainContractAddress, "getCrossTx", params);
        //根据transactionResponse判断是否发送成功
        System.out.println("开始调用发送交易   ==========注意查看");

        TransactionReceipt transactionReceipt=transactionResponse.getTransactionReceipt();
        String output=transactionReceipt.getOutput();
        //解析交易返回值
        ABICodec abiCodec = new ABICodec(client.getCryptoSuite());
        List<Object> list= abiCodec.decodeMethod(abi, "getCrossTx", output);

        System.out.println("====================================");
        System.out.println("交易回执解析，解析带返回值的交易 ==========="+list);
        System.out.println("====================================");

        //跨链基础信息：源AC、目标AC、源地址、目标地址、跨链交易类型
        BasicInfo basicInfo= new BasicInfo();
        //源链AC码
        basicInfo.setSrcChainCode(String.valueOf(list.get(0)));
        basicInfo.setDestChainCode(String.valueOf(list.get(1)));
        //源地址
        basicInfo.setSrcBid(String.valueOf(list.get(2)));
        basicInfo.setDestBid(String.valueOf(list.get(3)));
        //跨链交易类型
        basicInfo.setTxType((BigInteger) list.get(4));

        System.out.println("开始展示basicinfo  ==========注意查看");
        System.out.println(basicInfo.toString());

        //跨链交易处理状态  TxResult
        String _txResult= (String) list.get(6);
        String _txRefunded=(String)list.get(7);
        String _txOrigin=(String)list.get(8);

        System.out.println("开始展示跨链交易处理状态 ==========注意查看");
        System.out.println(_txResult);

        //附属信息：remark extension version
        ExtensionInfo _extensionInfo = new ExtensionInfo();
        _extensionInfo.setRemark(String.valueOf(list.get(9)));
        _extensionInfo.setExtension(String.valueOf(list.get(10)));
        _extensionInfo.setVersion(String.valueOf(list.get(11)));


        TransactionResponse transactionResponse1 = transactionProcessor.sendTransactionAndGetResponseByContractLoader("CrossChainContract", crossChainContractAddress, "getCrossTxSendAndAckProof", params);
        //根据transactionResponse判断是否发送成功

        TransactionReceipt transactionReceipt1=transactionResponse1.getTransactionReceipt();
        String output1=transactionReceipt1.getOutput();
        //解析交易返回值
        ABICodec abiCodec1 = new ABICodec(client.getCryptoSuite());
        List<Object> list1= abiCodec1.decodeMethod(abi, "getCrossTxSendAndAckProof", output1);
        System.out.println("开始展示跨链交易sendProof信息 ==========注意查看");
        _SendProof _sendProof=new _SendProof();
        _sendProof.setLedgerSeq((Integer) list1.get(0));
        _sendProof.setTxHash((String) list1.get(1));
        _sendProof.setVerifieraddr((String) list1.get(2));

        _AckProof _ackProof=new _AckProof();
        _ackProof.setLedgerSeq((Integer) list1.get(3));
        _ackProof.setTxHash((String) list1.get(4));
        _ackProof.setVerifieraddr((String) list1.get(5));


        //startTX交易为init未处理状态
        if(_txResult.equals("INIT")){
            //主链网关节点构建sendTx
            if(_crossTxType.equals(0)){
                //主链积分转移  此时payload类型是int类型的转移数字
                int  payload= (int) list.get(5);
                System.out.println("===============主链积分转移数："+payload);
            }
            else if(_crossTxType.equals(1)){
                //子链积分兑换
                //解析payload


                //发送给另一方的方法进行跨链
            }
            else if(_crossTxType.equals(2)){
                //发送给另一方的方法进行跨链

                //合约互操作 数据交互
                //解析payload,解析destChainCode位置

                _CrossTxObj _crossTxObj=new _CrossTxObj();
                _crossTxObj.setCrossTxNo(_crossTxNo);
                _crossTxObj.setSrcChainCode(basicInfo.getSrcChainCode());
                _crossTxObj.setDestChainCode(basicInfo.getDestChainCode());
                _crossTxObj.setSrcBid(basicInfo.getSrcBid());
                _crossTxObj.setDestBid(basicInfo.getDestBid());
                _crossTxObj.setTxType(basicInfo.getTxType().intValue());

                //解析payload[]
//                _crossTxObj.setPayload();

                _crossTxObj.setRemark(_extensionInfo.getRemark());
                _crossTxObj.setTxResult(_txResult);
                _crossTxObj.setTxRefunded(_txRefunded);
                _crossTxObj.setExtension(_extensionInfo.getExtension());
                _crossTxObj.setSendProofs(_sendProof);
                _crossTxObj.setAckProofs(_ackProof);
                _crossTxObj.setVersion(_extensionInfo.getVersion());
                _crossTxObj.setTxOrigin(_txOrigin);

                CrossChainObj crossChainObj=hubCrossChainGateway.fiscoCrossChainObjToFbaricCrossChaonObj(_crossTxObj);

                //调用另一个hub 获取账本数据
                byte[] proof = new String("proof").getBytes(StandardCharsets.UTF_8);
                String value=hubCrossChainGateway.call(crossChainObj,crossChainObj.getTxResult(),crossChainObj.getVersion(),proof);
                logger.info("跨链调用后，得到的结果为："+value);
                //调用己方合约返回值


            }
        }





        //调用合约查询接口
        //CrossChainContract『name』函数，合约地址为helloWorldAddress，参数为空
//        CallResponse callResponse = transactionProcessor.sendCallByContractLoader("CrossChainContract", crossChainContractAddress, "", new ArrayList<>());
//        String values=callResponse.getValues();




        //
    }
    public void _sendTx(String _crossTxNo,BigInteger _ledgerSeq,String _txHash){

    }
    public void _sendAcked(String _crossTxNo,BigInteger _ledgerSeq,String  _txHash){
        //调用自己的跨链合约

    }


}
