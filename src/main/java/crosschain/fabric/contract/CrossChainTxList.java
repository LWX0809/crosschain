package crosschain.fabric.contract;

import crosschain.fabric.contract.entity.CrossChainObj;
import crosschain.fabric.contract.state.StateList;
import org.hyperledger.fabric.contract.Context;

public class CrossChainTxList {
    private StateList stateList;

    public CrossChainTxList(Context ctx) {
        this.stateList = StateList.getStateList(ctx, CrossChainTxList.class.getSimpleName(), CrossChainObj::deserialize);
    }
    public CrossChainTxList addCrossChainTx(CrossChainObj crossChainObj) {
        stateList.addState(crossChainObj);
        return this;
    }
    public CrossChainObj getCrossChainTx(String crossChainNo) {
        return (CrossChainObj) this.stateList.getState(crossChainNo);
    }

    public CrossChainTxList updateCrossChainTx(CrossChainObj crossChainObj) {
        this.stateList.updateState(crossChainObj);
        return this;
    }

}
