package crosschain.fisco.client.entity;


import java.math.BigInteger;

public class BasicInfo {

    private String SrcChainCode;
    private String DestChainCode;
    private String SrcBid;
    private String DestBid;
    private BigInteger TxType;

    public BasicInfo() {
    }

    @Override
    public String toString() {
        return "BasicInfo{" +
                "SrcChainCode='" + SrcChainCode + '\'' +
                ", DestChainCode='" + DestChainCode + '\'' +
                ", SrcBid='" + SrcBid + '\'' +
                ", DestBid='" + DestBid + '\'' +
                ", TxType=" + TxType +
                '}';
    }

    public BasicInfo(String srcChainCode, String destChainCode, String srcBid, String destBid, BigInteger txType) {
        SrcChainCode = srcChainCode;
        DestChainCode = destChainCode;
        SrcBid = srcBid;
        DestBid = destBid;
        TxType = txType;
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

    public BigInteger getTxType() {
        return TxType;
    }

    public void setTxType(BigInteger txType) {
        TxType = txType;
    }


}
