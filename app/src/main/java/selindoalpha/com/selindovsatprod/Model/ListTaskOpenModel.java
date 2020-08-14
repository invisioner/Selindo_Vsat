package selindoalpha.com.selindovsatprod.Model;

public class ListTaskOpenModel{

    private long idint;
    private String namaRemote;
    private String alamat;
    private String noTask;
    private String vid;
    private String id;
    private String tanggalTask;
    private String provinsi;
    private String idJenisTask;
    private String namaKoordinator;
    private String namaTeknisi;
    private String statusTask;


    public ListTaskOpenModel(long Idint,String namaRemote, String alamat, String noTask, String vid, String id, String tanggalTask, String provinsi, String idJenisTask, String namaKoordinator, String namaTeknisi, String statusTask) {
        this.idint = Idint;
        this.namaRemote = namaRemote;
        this.alamat = alamat;
        this.noTask = noTask;
        this.vid = vid;
        this.id = id;
        this.tanggalTask = tanggalTask;
        this.provinsi = provinsi;
        this.idJenisTask = idJenisTask;
        this.namaKoordinator = namaKoordinator;
        this.namaTeknisi = namaTeknisi;
        this.statusTask = statusTask;
    }

    public long getIdint(){
        return idint;
    }

    public String getNamaRemote() {
        return namaRemote;
    }

    public void setNamaRemote(String namaRemote) {
        this.namaRemote = namaRemote;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTask() {
        return noTask;
    }

    public void setNoTask(String noTask) {
        this.noTask = noTask;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggalTask() {
        return tanggalTask;
    }

    public void setTanggalTask(String tanggalTask) {
        this.tanggalTask = tanggalTask;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getIdJenisTask() {
        return idJenisTask;
    }

    public void setIdJenisTask(String idJenisTask) {
        this.idJenisTask = idJenisTask;
    }

    public String getNamaKoordinator() {
        return namaKoordinator;
    }

    public void setNamaKoordinator(String namaKoordinator) {
        this.namaKoordinator = namaKoordinator;
    }

    public String getNamaTeknisi() {
        return namaTeknisi;
    }

    public void setNamaTeknisi(String namaTeknisi) {
        this.namaTeknisi = namaTeknisi;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }
}
