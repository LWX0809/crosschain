package crosschain.fisco.solidity;

import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.*;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class LWX_CrossChainContractAdditional extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610ebf806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c806390646b4a14610051578063a9059cbb1461006d578063e1f21c6714610089578063eca33be7146100a5575b600080fd5b61006b60048036038101906100669190610795565b6100c1565b005b6100876004803603810190610082919061080d565b6101ce565b005b6100a3600480360381019061009e91906107be565b610481565b005b6100bf60048036038101906100ba9190610849565b6105e7565b005b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610131576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161012890610c4e565b60405180910390fd5b6003819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507f50634bb0a08177296d39814e9009b52e5ada2102a905124a5c94f3b33009445a816040516101c39190610cfc565b60405180910390a150565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141561023e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161023590610c4e565b60405180910390fd5b60008111610281576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161027890610cbc565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156102f0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016102e790610c6e565b60405180910390fd5b6000600660006009543360405160200161030b929190610c02565b604051602081830303815290604052805190602001208152602001908152602001600020549050818111610374576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161036b90610c2e565b60405180910390fd5b6000600660006009548660405160200161038f929190610bd6565b60405160208183030381529060405280519060200120815260200190815260200160002054905082810190508060066000600954876040516020016103d5929190610bd6565b60405160208183030381529060405280519060200120815260200190815260200160002081905550828203915081600660006009543360405160200161041c929190610c02565b604051602081830303815290604052805190602001208152602001908152602001600020819055507f08457ab1ab7a12d0413e675f4079d4497cd761a9ed6f09e1b3280a7a717c87f4836040516104739190610d61565b60405180910390a150505050565b3373ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161480156104e95750600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614155b610528576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161051f90610c4e565b60405180910390fd5b6000811161056b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161056290610cbc565b60405180910390fd5b80600760008585604051602001610583929190610baa565b604051602081830303815290604052805190602001208152602001908152602001600020819055507fed6f0b4c8aa2f7d30871a60708a0e3d94c007a0ffa84adc4ed6f957cc34f3f11816040516105da9190610c8e565b60405180910390a1505050565b6004828290501461062d576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161062490610cdc565b60405180910390fd5b81816002919061063e92919061067c565b507ffc6737f7509cea37de236c12350da440158d0a0a9e1b18597001f6c18d40b9498282604051610670929190610d2a565b60405180910390a15050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106bd57803560ff19168380011785556106eb565b828001600101855582156106eb579182015b828111156106ea5782358255916020019190600101906106cf565b5b5090506106f891906106fc565b5090565b61071e91905b8082111561071a576000816000905550600101610702565b5090565b90565b60008135905061073081610e5b565b92915050565b60008083601f84011261074857600080fd5b8235905067ffffffffffffffff81111561076157600080fd5b60208301915083600182028301111561077957600080fd5b9250929050565b60008135905061078f81610e72565b92915050565b6000602082840312156107a757600080fd5b60006107b584828501610721565b91505092915050565b6000806000606084860312156107d357600080fd5b60006107e186828701610721565b93505060206107f286828701610721565b925050604061080386828701610780565b9150509250925092565b6000806040838503121561082057600080fd5b600061082e85828601610721565b925050602061083f85828601610780565b9150509250929050565b6000806020838503121561085c57600080fd5b600083013567ffffffffffffffff81111561087657600080fd5b61088285828601610736565b92509250509250929050565b61089f61089a82610db2565b610e0f565b82525050565b6108ae81610da0565b82525050565b6108c56108c082610da0565b610dfd565b82525050565b60006108d78385610d8f565b93506108e4838584610dee565b6108ed83610e3d565b840190509392505050565b6000610905603383610d8f565b91507f546865206d61696e2d636861696e20706f696e7473206f66207468652061636360008301527f6f756e7420617265206e6f7420656e6f756768000000000000000000000000006020830152604082019050919050565b600061096b600f83610d8f565b91507f496e76616c6964206164647265737300000000000000000000000000000000006000830152602082019050919050565b60006109ab601b83610d8f565b91507f5472616e736665722061737365747320746f20796f757273656c6600000000006000830152602082019050919050565b60006109eb600783610d8f565b91507f617070726f7665000000000000000000000000000000000000000000000000006000830152602082019050919050565b6000610a2b601c83610d8f565b91507f56616c7565206d7573742062652067726561746572207468616e2030000000006000830152602082019050919050565b6000610a6b602683610d8f565b91507f54686520636861726163746572206c656e677468206f6620636861696e636f6460008301527f65206973203400000000000000000000000000000000000000000000000000006020830152604082019050919050565b6000610ad1600a83610d8f565b91507f73657447617465776179000000000000000000000000000000000000000000006000830152602082019050919050565b6000610b11600c83610d8f565b91507f736574436861696e436f646500000000000000000000000000000000000000006000830152602082019050919050565b6000610b51600883610d8f565b91507f7472616e736665720000000000000000000000000000000000000000000000006000830152602082019050919050565b610b8d81610de4565b82525050565b610ba4610b9f82610de4565b610e33565b82525050565b6000610bb682856108b4565b601482019150610bc682846108b4565b6014820191508190509392505050565b6000610be28285610b93565b602082019150610bf282846108b4565b6014820191508190509392505050565b6000610c0e8285610b93565b602082019150610c1e828461088e565b6014820191508190509392505050565b60006020820190508181036000830152610c47816108f8565b9050919050565b60006020820190508181036000830152610c678161095e565b9050919050565b60006020820190508181036000830152610c878161099e565b9050919050565b60006040820190508181036000830152610ca7816109de565b9050610cb66020830184610b84565b92915050565b60006020820190508181036000830152610cd581610a1e565b9050919050565b60006020820190508181036000830152610cf581610a5e565b9050919050565b60006040820190508181036000830152610d1581610ac4565b9050610d2460208301846108a5565b92915050565b60006040820190508181036000830152610d4381610b04565b90508181036020830152610d588184866108cb565b90509392505050565b60006040820190508181036000830152610d7a81610b44565b9050610d896020830184610b84565b92915050565b600082825260208201905092915050565b6000610dab82610dc4565b9050919050565b6000610dbd82610dc4565b9050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b82818337600083830152505050565b6000610e0882610e21565b9050919050565b6000610e1a82610e21565b9050919050565b6000610e2c82610e4e565b9050919050565b6000819050919050565b6000601f19601f8301169050919050565b60008160601b9050919050565b610e6481610da0565b8114610e6f57600080fd5b50565b610e7b81610de4565b8114610e8657600080fd5b5056fea264697066735822122008ef184cf1c27587ec121d7b4d22e5de100b626179e96f16ff8b38a0d0b3aed664736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610ebf806100206000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c8063220ef8b0146100515780636904e9651461006d578063b386e6a314610089578063dd293063146100a5575b600080fd5b61006b600480360381019061006691906107be565b6100c1565b005b6100876004803603810190610082919061080d565b610227565b005b6100a3600480360381019061009e9190610795565b6104da565b005b6100bf60048036038101906100ba9190610849565b6105e7565b005b3373ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161480156101295750600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614155b610168576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161015f90610c4e565b60405180910390fd5b600081116101ab576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004016101a290610cbc565b60405180910390fd5b806007600085856040516020016101c3929190610baa565b604051602081830303815290604052805190602001208152602001908152602001600020819055507fe5d869baf553a5670bc4d53293bda9ccd6e98b428d8332a7d6e1941587e5f1dd8160405161021a9190610d61565b60405180910390a1505050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610297576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161028e90610c4e565b60405180910390fd5b600081116102da576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004016102d190610cbc565b60405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610349576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161034090610c9c565b60405180910390fd5b60006006600060095433604051602001610364929190610c02565b6040516020818303038152906040528051906020012081526020019081526020016000205490508181116103cd576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004016103c490610cdc565b60405180910390fd5b600060066000600954866040516020016103e8929190610bd6565b604051602081830303815290604052805190602001208152602001908152602001600020549050828101905080600660006009548760405160200161042e929190610bd6565b604051602081830303815290604052805190602001208152602001908152602001600020819055508282039150816006600060095433604051602001610475929190610c02565b604051602081830303815290604052805190602001208152602001908152602001600020819055507fb152fb86e75287b0b851630943ce0c8f518a6e6c5f889307103a84c7273f5a43836040516104cc9190610c6e565b60405180910390a150505050565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561054a576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161054190610c4e565b60405180910390fd5b6003819080600181540180825580915050600190039060005260206000200160009091909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055507fdc969067e6aabb6df7e948266b8aafa0cb001a500e7b4f50ac16022fbda23618816040516105dc9190610cfc565b60405180910390a150565b6004828290501461062d576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040161062490610c2e565b60405180910390fd5b81816002919061063e92919061067c565b507f5dc335ae65118ca26029d68bfd5373ae3111e60215e81a96d095dbe56f4e0da78282604051610670929190610d2a565b60405180910390a15050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106bd57803560ff19168380011785556106eb565b828001600101855582156106eb579182015b828111156106ea5782358255916020019190600101906106cf565b5b5090506106f891906106fc565b5090565b61071e91905b8082111561071a576000816000905550600101610702565b5090565b90565b60008135905061073081610e5b565b92915050565b60008083601f84011261074857600080fd5b8235905067ffffffffffffffff81111561076157600080fd5b60208301915083600182028301111561077957600080fd5b9250929050565b60008135905061078f81610e72565b92915050565b6000602082840312156107a757600080fd5b60006107b584828501610721565b91505092915050565b6000806000606084860312156107d357600080fd5b60006107e186828701610721565b93505060206107f286828701610721565b925050604061080386828701610780565b9150509250925092565b6000806040838503121561082057600080fd5b600061082e85828601610721565b925050602061083f85828601610780565b9150509250929050565b6000806020838503121561085c57600080fd5b600083013567ffffffffffffffff81111561087657600080fd5b61088285828601610736565b92509250509250929050565b61089f61089a82610db2565b610e0f565b82525050565b6108ae81610da0565b82525050565b6108c56108c082610da0565b610dfd565b82525050565b60006108d78385610d8f565b93506108e4838584610dee565b6108ed83610e3d565b840190509392505050565b6000610905602683610d8f565b91507f54686520636861726163746572206c656e677468206f6620636861696e636f6460008301527f65206973203400000000000000000000000000000000000000000000000000006020830152604082019050919050565b600061096b600f83610d8f565b91507f496e76616c6964206164647265737300000000000000000000000000000000006000830152602082019050919050565b60006109ab600883610d8f565b91507f7472616e736665720000000000000000000000000000000000000000000000006000830152602082019050919050565b60006109eb601b83610d8f565b91507f5472616e736665722061737365747320746f20796f757273656c6600000000006000830152602082019050919050565b6000610a2b601c83610d8f565b91507f56616c7565206d7573742062652067726561746572207468616e2030000000006000830152602082019050919050565b6000610a6b603383610d8f565b91507f546865206d61696e2d636861696e20706f696e7473206f66207468652061636360008301527f6f756e7420617265206e6f7420656e6f756768000000000000000000000000006020830152604082019050919050565b6000610ad1600a83610d8f565b91507f73657447617465776179000000000000000000000000000000000000000000006000830152602082019050919050565b6000610b11600c83610d8f565b91507f736574436861696e436f646500000000000000000000000000000000000000006000830152602082019050919050565b6000610b51600783610d8f565b91507f617070726f7665000000000000000000000000000000000000000000000000006000830152602082019050919050565b610b8d81610de4565b82525050565b610ba4610b9f82610de4565b610e33565b82525050565b6000610bb682856108b4565b601482019150610bc682846108b4565b6014820191508190509392505050565b6000610be28285610b93565b602082019150610bf282846108b4565b6014820191508190509392505050565b6000610c0e8285610b93565b602082019150610c1e828461088e565b6014820191508190509392505050565b60006020820190508181036000830152610c47816108f8565b9050919050565b60006020820190508181036000830152610c678161095e565b9050919050565b60006040820190508181036000830152610c878161099e565b9050610c966020830184610b84565b92915050565b60006020820190508181036000830152610cb5816109de565b9050919050565b60006020820190508181036000830152610cd581610a1e565b9050919050565b60006020820190508181036000830152610cf581610a5e565b9050919050565b60006040820190508181036000830152610d1581610ac4565b9050610d2460208301846108a5565b92915050565b60006040820190508181036000830152610d4381610b04565b90508181036020830152610d588184866108cb565b90509392505050565b60006040820190508181036000830152610d7a81610b44565b9050610d896020830184610b84565b92915050565b600082825260208201905092915050565b6000610dab82610dc4565b9050919050565b6000610dbd82610dc4565b9050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b82818337600083830152505050565b6000610e0882610e21565b9050919050565b6000610e1a82610e21565b9050919050565b6000610e2c82610e4e565b9050919050565b6000819050919050565b6000601f19601f8301169050919050565b60008160601b9050919050565b610e6481610da0565b8114610e6f57600080fd5b50565b610e7b81610de4565b8114610e8657600080fd5b5056fea26469706673582212207eba34c0bc5c81969ab2b5805c9b59250a0eeaed4ec6d91e068a0234f52deb3764736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetBurnEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"AssetMintEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approveEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendAckedEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"sendTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"chain_Code\",\"type\":\"string\"}],\"name\":\"setChainCodeEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"gateway_Address\",\"type\":\"address\"}],\"name\":\"setGatewayEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"cross_TxNo\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint8\",\"name\":\"txType\",\"type\":\"uint8\"}],\"name\":\"startTxEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"}],\"name\":\"takeOutEvent\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"operation\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transferEvent\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"approve\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"chainCode\",\"type\":\"string\"}],\"name\":\"setChainCode\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"gatewayAddress\",\"type\":\"address\"}],\"name\":\"setGateway\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"transfer\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_SETCHAINCODE = "setChainCode";

    public static final String FUNC_SETGATEWAY = "setGateway";

    public static final String FUNC_TRANSFER = "transfer";

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

    protected LWX_CrossChainContractAdditional(String contractAddress, Client client, CryptoKeyPair credential) {
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

    public TransactionReceipt approve(String from, String to, BigInteger value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(from),
                new Address(to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void approve(String from, String to, BigInteger value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(from),
                new Address(to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForApprove(String from, String to, BigInteger value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(from),
                new Address(to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<String, String, BigInteger> getApproveInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_APPROVE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue()
                );
    }

    public TransactionReceipt setChainCode(String chainCode) {
        final Function function = new Function(
                FUNC_SETCHAINCODE, 
                Arrays.<Type>asList(new Utf8String(chainCode)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setChainCode(String chainCode, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETCHAINCODE, 
                Arrays.<Type>asList(new Utf8String(chainCode)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetChainCode(String chainCode) {
        final Function function = new Function(
                FUNC_SETCHAINCODE, 
                Arrays.<Type>asList(new Utf8String(chainCode)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetChainCodeInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETCHAINCODE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt setGateway(String gatewayAddress) {
        final Function function = new Function(
                FUNC_SETGATEWAY, 
                Arrays.<Type>asList(new Address(gatewayAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void setGateway(String gatewayAddress, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_SETGATEWAY, 
                Arrays.<Type>asList(new Address(gatewayAddress)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForSetGateway(String gatewayAddress) {
        final Function function = new Function(
                FUNC_SETGATEWAY, 
                Arrays.<Type>asList(new Address(gatewayAddress)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getSetGatewayInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETGATEWAY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public TransactionReceipt transfer(String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public void transfer(String to, BigInteger value, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForTransfer(String to, BigInteger value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new Address(to),
                new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple2<String, BigInteger> getTransferInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_TRANSFER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public static LWX_CrossChainContractAdditional load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new LWX_CrossChainContractAdditional(contractAddress, client, credential);
    }

    public static LWX_CrossChainContractAdditional deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(LWX_CrossChainContractAdditional.class, client, credential, getBinary(client.getCryptoSuite()), "");
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
