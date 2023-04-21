package crosschain.fabric.contract;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class CrossChainContext extends Context {
    public CrossChainTxList crossChainTxList;

    public CrossChainContext(ChaincodeStub stub) {
        super(stub);
        this.crossChainTxList = new CrossChainTxList(this);
    }
}
