package crosschain.fabric.gateway_connect;

import org.hyperledger.fabric.gateway.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Connect {
    private static String channelName="mychannel";


    /**
     * connect gateway
     */
    public Gateway connectGateway() throws IOException {

        // 载入一个存在的持有用户身份的钱包，用于访问fabric网络
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        //Path为fabric网络配置文件的路径
        Path networkConfigFile = Paths.get("src/main/resources/connection1.json");

        // 配置gateway连接用于访问fabric网络（用户和网络配置文件）
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "user1")
                .networkConfig(networkConfigFile);

        // 创建一个gateway连接
//        try (Gateway gateway = builder.connect()) {
        Gateway gateway = builder.connect();
        return gateway;
    }

    /**
     * 获取通道
     */
    public Network getNetwork(Gateway gateway) {
        return gateway.getNetwork(this.channelName);
    }

    /**
     * 获取合约
     */
    public Contract getContract(Network network,String contractName) {
        return network.getContract(contractName);
    }
}
