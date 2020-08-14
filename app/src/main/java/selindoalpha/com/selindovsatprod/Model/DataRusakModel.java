package selindoalpha.com.selindovsatprod.Model;

public class DataRusakModel {

      String vid;
      String namaBarang;
      String type;
      String sn;
      String esnModem;
      String status;
      String fileUrl;
      String description;

    public DataRusakModel(String vid, String namaBarang, String type, String sn, String esnModem, String status, String fileUrl, String description) {
        this.vid = vid;
        this.namaBarang = namaBarang;
        this.type = type;
        this.sn = sn;
        this.esnModem = esnModem;
        this.status = status;
        this.fileUrl = fileUrl;
        this.description = description;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getEsnModem() {
        return esnModem;
    }

    public void setEsnModem(String esnModem) {
        this.esnModem = esnModem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
