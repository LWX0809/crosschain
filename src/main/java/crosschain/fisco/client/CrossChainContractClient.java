package crosschain.fisco.client;


import crosschain.fisco.solidity.CrossChainContract;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

//实现对合约的部署调用
public class CrossChainContractClient {
    static Logger logger= LoggerFactory.getLogger(CrossChainContractClient.class);

    private BcosSDK bcosSDK;
    private Client client;
    private CryptoKeyPair cryptoKeyPair;

    public void initialize() throws Exception{
        @SuppressWarnings("resource")
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        // 初始化BcosSDK对象
        bcosSDK=context.getBean(BcosSDK.class);
        // 获取Client对象，此处传入的群组ID为1
        client=bcosSDK.getClient(1);
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        cryptoKeyPair=client.getCryptoSuite().getCryptoKeyPair();

        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);
        logger.debug("create client for group1, account address is "+cryptoKeyPair.getAddress());
    }

    public void deployCrossChainContractAndRecordAddr(){
        try{
            CrossChainContract crossChainContract = CrossChainContract.deploy(client, cryptoKeyPair);

            System.out.println(" deploy crossChainContract success, contract address is " + crossChainContract.getContractAddress());

            recordCrossChainContractAddr(crossChainContract.getContractAddress());
        }catch (Exception e){
            System.out.println(" deploy CrossChainContract contract failed, error message is  " + e.getMessage());
        }
    }
    public void recordCrossChainContractAddr(String address) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("address", address);
        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
    }

    public String loadCrossChainContractAddr() throws Exception {
        // load Asset contact address from contract.properties
        Properties prop = new Properties();
        final Resource contractResource = new ClassPathResource("contract.properties");
        prop.load(contractResource.getInputStream());

        String contractAddress = prop.getProperty("address");
        if (contractAddress == null || contractAddress.trim().equals("")) {
            throw new Exception(" load CrossChainContract contract address failed, please deploy it first. ");
        }
        logger.info(" load CrossChainContract address from contract.properties, address is {}", contractAddress);
        return contractAddress;
    }








}
