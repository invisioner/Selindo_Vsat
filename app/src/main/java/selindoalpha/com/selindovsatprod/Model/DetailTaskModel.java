package selindoalpha.com.selindovsatprod.Model;

public class DetailTaskModel {

    private String vid;
    private String provinsi;
    private String statusTask;
    private String idStatusPerbaikan;

    public DetailTaskModel(String vid, String provinsi, String statusTask, String idStatusPerbaikan) {
        this.vid = vid;
        this.provinsi = provinsi;
        this.statusTask = statusTask;
        this.idStatusPerbaikan = idStatusPerbaikan;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }

    public String getIdStatusPerbaikan() {
        return idStatusPerbaikan;
    }

    public void setIdStatusPerbaikan(String idStatusPerbaikan) {
        this.idStatusPerbaikan = idStatusPerbaikan;
    }
}
