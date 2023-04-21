package crosschain.fisco.client.entity;

public class _CrossTxObj {
    String CrossTxNo;
    String SrcChainCode;
    String DestChainCode;
    String SrcBid;
    String DestBid;
    int TxType;
    byte [] Payload;
    String Remark;
    String txResult;
    String txRefunded;
    String Extension;
    _SendProof SendProofs;
    _AckProof AckProofs;
    String Version;
    String txOrigin;

    public _CrossTxObj() {
    }

    public _CrossTxObj(String crossTxNo, String srcChainCode, String destChainCode, String srcBid, String destBid, int txType, byte[] payload, String remark, String txResult, String txRefunded, String extension, _SendProof sendProofs, _AckProof ackProofs, String version, String txOrigin) {
        CrossTxNo = crossTxNo;
        SrcChainCode = srcChainCode;
        DestChainCode = destChainCode;
        SrcBid = srcBid;
        DestBid = destBid;
        TxType = txType;
        Payload = payload;
        Remark = remark;
        this.txResult = txResult;
        this.txRefunded = txRefunded;
        Extension = extension;
        SendProofs = sendProofs;
        AckProofs = ackProofs;
        Version = version;
        this.txOrigin = txOrigin;
    }

    public String getCrossTxNo() {
        return CrossTxNo;
    }

    public void setCrossTxNo(String crossTxNo) {
        CrossTxNo = crossTxNo;
    }

    public String getSrcChainCode() {
        return SrcChainCode;
    }

    public void setSrcChainCode(String srcChainCode) {
        SrcChainCode = srcChainCode;
    }

    public String getDestChainCode() {
        return DestChainCode;
    }

    public void setDestChainCode(String destChainCode) {
        DestChainCode = destChainCode;
    }

    public String getSrcBid() {
        return SrcBid;
    }

    public void setSrcBid(String srcBid) {
        SrcBid = srcBid;
    }

    public String getDestBid() {
        return DestBid;
    }

    public void setDestBid(String destBid) {
        DestBid = destBid;
    }

    public int getTxType() {
        return TxType;
    }

    public void setTxType(int txType) {
        TxType = txType;
    }

    public byte[] getPayload() {
        return Payload;
    }

    public void setPayload(byte[] payload) {
        Payload = payload;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getTxResult() {
        return txResult;
    }

    public void setTxResult(String txResult) {
        this.txResult = txResult;
    }

    public String getTxRefunded() {
        return txRefunded;
    }

    public void setTxRefunded(String txRefunded) {
        this.txRefunded = txRefunded;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public _SendProof getSendProofs() {
        return SendProofs;
    }

    public void setSendProofs(_SendProof sendProofs) {
        SendProofs = sendProofs;
    }

    public _AckProof getAckProofs() {
        return AckProofs;
    }

    public void setAckProofs(_AckProof ackProofs) {
        AckProofs = ackProofs;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getTxOrigin() {
        return txOrigin;
    }

    public void setTxOrigin(String txOrigin) {
        this.txOrigin = txOrigin;
    }
}


