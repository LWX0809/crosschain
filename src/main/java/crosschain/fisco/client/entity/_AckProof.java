package crosschain.fisco.client.entity;


public class _AckProof {
    int LedgerSeq;
    String TxHash;
    String verifieraddr;

    public int getLedgerSeq() {
        return LedgerSeq;
    }

    public void setLedgerSeq(int ledgerSeq) {
        LedgerSeq = ledgerSeq;
    }

    public String getTxHash() {
        return TxHash;
    }

    public void setTxHash(String txHash) {
        TxHash = txHash;
    }

    public String getVerifieraddr() {
        return verifieraddr;
    }

    public void setVerifieraddr(String verifieraddr) {
        this.verifieraddr = verifieraddr;
    }
}
