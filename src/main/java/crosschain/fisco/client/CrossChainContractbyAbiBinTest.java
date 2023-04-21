package crosschain.fisco.client;


import crosschain.fabric.gateway_connect.HubCrossChainGatewayTest;

import org.fisco.bcos.sdk.BcosSDK;

import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;

import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionBaseException;

import org.hyperledger.fabric.gateway.ContractException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.TimeoutException;

//实现对合约的部署调用
//abi以及合约部署调用已经更新成最新的
public class CrossChainContractbyAbiBinTest {

    static Logger logger= LoggerFactory.getLogger(CrossChainContractbyAbiBinTest.class);

    private BcosSDK bcosSDK;
    private Client client;
    private CryptoKeyPair cryptoKeyPair;
    private String abi= "[{\"inputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetBurnEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetMintEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approveEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendAckedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"chain_Code\",\"type\":\"string\"}],\"name\":\"setChainCodeEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"gateway_Address\",\"type\":\"address\"}],\"name\":\"setGatewayEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"startTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"}],\"name\":\"takeOutEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferEvent\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"funcn\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"}],\"name\":\"createCrossChainRead\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"funcn\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"uint8\",\"name\":\"value\",\"type\":\"uint8\"}],\"name\":\"createCrossChainWrite\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"value1\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"}]";
    //console :"0x05f2b7ddbd8db80351b38965cbf538c9669e205c"
    private  String crossChainContractAddress="0x1c1c86def5350aaa3691af7a1be0dd123f584d37";

    AssembleTransactionProcessor transactionProcessor;
    private static HubCrossChainGatewayTest hubCrossChainGateway;

    public static void main(String[] args) throws Exception {
        CrossChainContractbyAbiBinTest crossChainContractbyAbiBin=new CrossChainContractbyAbiBinTest();
        crossChainContractbyAbiBin.initialize();
        hubCrossChainGateway=new HubCrossChainGatewayTest();
        hubCrossChainGateway.connect("crossChainContract");

        crossChainContractbyAbiBin.crossChainWriteTest();

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

    public void crossChainWriteTest() throws ABICodecException, TransactionBaseException, InterruptedException, TimeoutException, ContractException {
        //调用之后，直接调用hub
        //调用fisco合约
       // 创建调用交易函数的参数，此处为传入一个参数
        List<Object> params = new ArrayList<>();
        params.add("crossChainWrite");
        params.add("a");
        params.add("100");
        // 调用HelloWorld合约，合约地址为helloWorldAddress， 调用函数名为『set』，函数参数类型为params
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("CrossChainContractTest", crossChainContractAddress, "createCrossChainWrite", params);
        //调用hub
        String result = hubCrossChainGateway.testCrossChainWrite("a","100");
        System.out.println("===================fiscoHub======================");
        System.out.println(result);
        System.out.println("===================fiscoHub======================");
    }
    public void crossChainReadTest() throws ABICodecException, TransactionBaseException, InterruptedException, TimeoutException, ContractException {
        //调用之后，直接调用hub
        //调用fisco合约
        // 创建调用交易函数的参数，此处为传入一个参数
        List<Object> params = new ArrayList<>();
        params.add("crossChainRead");
        params.add("a");

        // 调用HelloWorld合约，合约地址为helloWorldAddress， 调用函数名为『set』，函数参数类型为params
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("CrossChainContractTest", crossChainContractAddress, "createCrossChaiRead", params);
        //调用hub
        String result = hubCrossChainGateway.testCrossChainRead("a");
        System.out.println("===================fiscoHub======================");
        System.out.println(result);
        System.out.println("===================fiscoHub======================");
    }

}
