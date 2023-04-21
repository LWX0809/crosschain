package crosschain.fabric.contract.entity;

//import com.owlike.genson.annotation.JsonProperty;

import crosschain.fabric.contract.state.State;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@DataType()
public class CrossChainObj extends State {

    //跨链交易number
    @Property()
    private String CrossTxNo;

    //源AC码
    @Property()
    private String SrcChainCode;

    //目的AC码
    @Property()
    private String DestChainCode;

    //源链地址
    @Property()
    private String SrcBid;

    //目的链地址
    @Property()
    private String DestBid;

    //交易类型：0.1.2.3
    @Property()
    private int TxType;

    //跨链交易描述
    @Property()
    private String[] Payload;

    //tag信息
    @Property()
    private String Remark;

    //跨链交易结果：0.1.2.3
    @Property()
    private String TxResult;

    // withdrawal status of cross-chain transaction assets: 0 ,1 ,2
    @Property()
    private String TxRefunded;

    //用户评论
    @Property()
    private String Extension;

    //senTx proof信息：交易所在区块号、交易哈希、交易验证者（address类型）
    @Property()
    private int  SendProofs_LedgerSeq;
    @Property()
    private String SendProofs_TxHash;
    @Property()
    private String SendProofs_Verifieraddr;

    //sendAcked交易信息
    @Property()
    private int  AckProofs_LedgerSeq;
    @Property()
    private String AckProofs_TxHash;
    @Property()
    private String AckProofs_Verifieraddr;

    //发送链合约版本信息
    @Property()
    private String Version;

    //合约角色：0，1，2
    @Property()
    private String TxOrigin;

    public CrossChainObj(){
        super();
    }

//    public CrossChainObj(@JsonProperty("crossTxNo") String crossTxNo, @JsonProperty("srcChainCode")  String srcChainCode, @JsonProperty("destChainCode")  String destChainCode,  @JsonProperty("srcBid")  String srcBid, @JsonProperty("destBid")  String destBid, @JsonProperty("txType")  int txType,@JsonProperty("payload")  String[] payload,@JsonProperty("remark")  String remark,@JsonProperty("txResult")  String txResult,@JsonProperty("txRefunded")  String txRefunded,@JsonProperty("extension")  String extension,@JsonProperty("sendProofs_LedgerSeq")   int sendProofs_LedgerSeq,@JsonProperty("sendProofs_TxHash")  String sendProofs_TxHash,@JsonProperty("sendProofs_Verifieraddr")  String sendProofs_Verifieraddr, @JsonProperty("ackProofs_LedgerSeq") int ackProofs_LedgerSeq,@JsonProperty("ackProofs_TxHash")  String ackProofs_TxHash,@JsonProperty("ackProofs_Verifieraddr")  String ackProofs_Verifieraddr,@JsonProperty("version")   String version, @JsonProperty("txOrigin") String txOrigin) {
//        CrossTxNo = crossTxNo;
//        SrcChainCode = srcChainCode;
//        DestChainCode = destChainCode;
//        SrcBid = srcBid;
//        DestBid = destBid;
//        TxType = txType;
//        Payload = payload;
//        Remark = remark;
//        TxResult = txResult;
//        TxRefunded = txRefunded;
//        Extension = extension;
//        SendProofs_LedgerSeq = sendProofs_LedgerSeq;
//        SendProofs_TxHash = sendProofs_TxHash;
//        SendProofs_Verifieraddr = sendProofs_Verifieraddr;
//        AckProofs_LedgerSeq = ackProofs_LedgerSeq;
//        AckProofs_TxHash = ackProofs_TxHash;
//        AckProofs_Verifieraddr = ackProofs_Verifieraddr;
//        Version = version;
//        TxOrigin = txOrigin;
//    }


    public CrossChainObj(String crossTxNo, String srcChainCode, String destChainCode, String srcBid, String destBid, int txType, String[] payload, String remark, String txResult, String txRefunded, String extension, int sendProofs_LedgerSeq, String sendProofs_TxHash, String sendProofs_Verifieraddr, int ackProofs_LedgerSeq, String ackProofs_TxHash, String ackProofs_Verifieraddr, String version, String txOrigin) {
        CrossTxNo = crossTxNo;
        SrcChainCode = srcChainCode;
        DestChainCode = destChainCode;
        SrcBid = srcBid;
        DestBid = destBid;
        TxType = txType;
        Payload = payload;
        Remark = remark;
        TxResult = txResult;
        TxRefunded = txRefunded;
        Extension = extension;
        SendProofs_LedgerSeq = sendProofs_LedgerSeq;
        SendProofs_TxHash = sendProofs_TxHash;
        SendProofs_Verifieraddr = sendProofs_Verifieraddr;
        AckProofs_LedgerSeq = ackProofs_LedgerSeq;
        AckProofs_TxHash = ackProofs_TxHash;
        AckProofs_Verifieraddr = ackProofs_Verifieraddr;
        Version = version;
        TxOrigin = txOrigin;
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

    public String[] getPayload() {
        return Payload;
    }

    public void setPayload(String[] payload) {
        Payload = payload;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getTxResult() {
        return TxResult;
    }

    public void setTxResult(String txResult) {
        TxResult = txResult;
    }

    public String getTxRefunded() {
        return TxRefunded;
    }

    public void setTxRefunded(String txRefunded) {
        TxRefunded = txRefunded;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public int getSendProofs_LedgerSeq() {
        return SendProofs_LedgerSeq;
    }

    public void setSendProofs_LedgerSeq(int sendProofs_LedgerSeq) {
        SendProofs_LedgerSeq = sendProofs_LedgerSeq;
    }

    public String getSendProofs_TxHash() {
        return SendProofs_TxHash;
    }

    public void setSendProofs_TxHash(String sendProofs_TxHash) {
        SendProofs_TxHash = sendProofs_TxHash;
    }

    public String getSendProofs_Verifieraddr() {
        return SendProofs_Verifieraddr;
    }

    public void setSendProofs_Verifieraddr(String sendProofs_Verifieraddr) {
        SendProofs_Verifieraddr = sendProofs_Verifieraddr;
    }

    public int getAckProofs_LedgerSeq() {
        return AckProofs_LedgerSeq;
    }

    public void setAckProofs_LedgerSeq(int ackProofs_LedgerSeq) {
        AckProofs_LedgerSeq = ackProofs_LedgerSeq;
    }

    public String getAckProofs_TxHash() {
        return AckProofs_TxHash;
    }

    public void setAckProofs_TxHash(String ackProofs_TxHash) {
        AckProofs_TxHash = ackProofs_TxHash;
    }

    public String getAckProofs_Verifieraddr() {
        return AckProofs_Verifieraddr;
    }

    public void setAckProofs_Verifieraddr(String ackProofs_Verifieraddr) {
        AckProofs_Verifieraddr = ackProofs_Verifieraddr;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getTxOrigin() {
        return TxOrigin;
    }

    public void setTxOrigin(String txOrigin) {
        TxOrigin = txOrigin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CrossChainObj that = (CrossChainObj) o;
        return TxType == that.TxType && SendProofs_LedgerSeq == that.SendProofs_LedgerSeq && AckProofs_LedgerSeq == that.AckProofs_LedgerSeq && Objects.equals(CrossTxNo, that.CrossTxNo) && Objects.equals(SrcChainCode, that.SrcChainCode) && Objects.equals(DestChainCode, that.DestChainCode) && Objects.equals(SrcBid, that.SrcBid) && Objects.equals(DestBid, that.DestBid) && Arrays.equals(Payload, that.Payload) && Objects.equals(Remark, that.Remark) && Objects.equals(TxResult, that.TxResult) && Objects.equals(TxRefunded, that.TxRefunded) && Objects.equals(Extension, that.Extension) && Objects.equals(SendProofs_TxHash, that.SendProofs_TxHash) && Objects.equals(SendProofs_Verifieraddr, that.SendProofs_Verifieraddr) && Objects.equals(AckProofs_TxHash, that.AckProofs_TxHash) && Objects.equals(AckProofs_Verifieraddr, that.AckProofs_Verifieraddr) && Objects.equals(Version, that.Version) && Objects.equals(TxOrigin, that.TxOrigin);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(CrossTxNo, SrcChainCode, DestChainCode, SrcBid, DestBid, TxType, Remark, TxResult, TxRefunded, Extension, SendProofs_LedgerSeq, SendProofs_TxHash, SendProofs_Verifieraddr, AckProofs_LedgerSeq, AckProofs_TxHash, AckProofs_Verifieraddr, Version, TxOrigin);
        result = 31 * result + Arrays.hashCode(Payload);
        return result;
    }

    @Override
    public String toString() {
        return "CrossChainObj{" +
                "CrossTxNo='" + CrossTxNo + '\'' +
                ", SrcChainCode='" + SrcChainCode + '\'' +
                ", DestChainCode='" + DestChainCode + '\'' +
                ", SrcBid='" + SrcBid + '\'' +
                ", DestBid='" + DestBid + '\'' +
                ", TxType=" + TxType +
                ", Payload=" + Arrays.toString(Payload) +
                ", Remark='" + Remark + '\'' +
                ", TxResult='" + TxResult + '\'' +
                ", TxRefunded='" + TxRefunded + '\'' +
                ", Extension='" + Extension + '\'' +
                ", SendProofs_LedgerSeq=" + SendProofs_LedgerSeq +
                ", SendProofs_TxHash='" + SendProofs_TxHash + '\'' +
                ", SendProofs_Verifieraddr='" + SendProofs_Verifieraddr + '\'' +
                ", AckProofs_LedgerSeq=" + AckProofs_LedgerSeq +
                ", AckProofs_TxHash='" + AckProofs_TxHash + '\'' +
                ", AckProofs_Verifieraddr='" + AckProofs_Verifieraddr + '\'' +
                ", Version='" + Version + '\'' +
                ", TxOrigin='" + TxOrigin + '\'' +
                '}';
    }


    public static CrossChainObj deserialize(byte[] data) {
        JSONObject json = new JSONObject(new String(data, UTF_8));

        String crossTxNo = json.getString("crossTxNo");
        String srcChainCode= json.getString("srcChainCode");
        String destChainCode = json.getString("destChainCode");
        String srcBid = json.getString("srcBid");
        String destBid = json.getString("destBid");
        int txType = json.getInt("txType");
//        System.out.println(json.get("payload"));
        JSONArray payload= (JSONArray) json.get("payload");
        String [] payload1=convertJsonArrayToString(payload);
        String remark= json.getString("remark");
        String txResult=json.getString("txResult");
        String txRefunded=json.getString("txRefunded");
        String extension=json.getString("extension");
        int  sendProofs_LedgerSeq=json.getInt("sendProofs_LedgerSeq");
        String sendProofs_TxHash=json.getString("sendProofs_TxHash");
        String sendProofs_Verifieraddr =json.getString("sendProofs_Verifieraddr");
        int ackProofs_LedgerSeq=json.getInt("ackProofs_LedgerSeq");
        String ackProofs_TxHash=json.getString("ackProofs_TxHash");
        String ackProofs_Verifieraddr=json.getString("ackProofs_Verifieraddr");
        String version=json.getString("version");
        String txOrigin=json.getString("txOrigin");

        return new CrossChainObj(crossTxNo, srcChainCode, destChainCode, srcBid, destBid,txType,payload1,remark,txResult,txRefunded,extension,sendProofs_LedgerSeq,sendProofs_TxHash,sendProofs_Verifieraddr,ackProofs_LedgerSeq,ackProofs_TxHash,ackProofs_Verifieraddr,version,txOrigin);
    }


    public static String[] convertJsonArrayToString(JSONArray jsonArray) {
        String[] stringArray = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            stringArray[i] = jsonArray.getString(i);
        }
       return stringArray;
    }

    public static byte[] serialize(CrossChainObj crossChainObj) {
        return State.serialize(crossChainObj);
    }
}
