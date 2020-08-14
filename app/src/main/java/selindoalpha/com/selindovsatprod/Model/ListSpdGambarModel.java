package selindoalpha.com.selindovsatprod.Model;

public class ListSpdGambarModel {

    long idInt;
    String id;
    String file_url;
    String Description;
    String VID;
    String noTask;
    String CatatanTransaksi;
    String JenisBiaya;
    String Nominal;
    String TglInputBiaya;
    String file_id;
    String FlagConfirm;

    public ListSpdGambarModel(long idInt,String id, String file_url, String description, String VID,
                              String NoTask, String catatanTransaksi, String jenisBiaya, String nominal,
                              String tglInputBiaya, String file_id,String flagConfirm) {
        this.idInt = idInt;
        this.id = id;
        this.file_url = file_url;
        Description = description;
        this.VID = VID;
        this.noTask = NoTask;
        CatatanTransaksi = catatanTransaksi;
        JenisBiaya = jenisBiaya;
        Nominal = nominal;
        TglInputBiaya = tglInputBiaya;
        this.file_id = file_id;
        this.FlagConfirm = flagConfirm;
    }

    public long getIdInt(){
        return idInt;
    }

    public String getNoTask() {
        return noTask;
    }

    public void setNoTask(String noTask) {
        this.noTask = noTask;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getVID() {
        return VID;
    }

    public void setVID(String VID) {
        this.VID = VID;
    }

    public String getCatatanTransaksi() {
        return CatatanTransaksi;
    }

    public void setCatatanTransaksi(String catatanTransaksi) {
        CatatanTransaksi = catatanTransaksi;
    }

    public String getJenisBiaya() {
        return JenisBiaya;
    }

    public void setJenisBiaya(String jenisBiaya) {
        JenisBiaya = jenisBiaya;
    }

    public String getNominal() {
        return Nominal;
    }

    public void setNominal(String nominal) {
        Nominal = nominal;
    }

    public String getTglInputBiaya() {
        return TglInputBiaya;
    }

    public void setTglInputBiaya(String tglInputBiaya) {
        TglInputBiaya = tglInputBiaya;
    }

    public String getFlagConfirm(){
        return FlagConfirm;
    }


}
