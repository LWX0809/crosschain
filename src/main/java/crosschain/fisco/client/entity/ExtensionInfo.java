package crosschain.fisco.client.entity;


public class ExtensionInfo {
   private String  Remark;
   private String  Extension;
   private  String Version;

    public ExtensionInfo() {
    }

    public ExtensionInfo(String remark, String extension, String version) {
        Remark = remark;
        Extension = extension;
        Version = version;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    @Override
    public String toString() {
        return "ExtensionInfo{" +
                "Remark='" + Remark + '\'' +
                ", Extension='" + Extension + '\'' +
                ", Version='" + Version + '\'' +
                '}';
    }

}
