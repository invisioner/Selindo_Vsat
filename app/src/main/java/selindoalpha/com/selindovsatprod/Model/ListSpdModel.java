package selindoalpha.com.selindovsatprod.Model;

public class ListSpdModel {

    long idInt;
    private String NoTask;
    private String NamaTeknisi;
    private String IdTeknisi;
    private String JumlahTrf;
    private String ApprovalNominal;
    private String TanggalTask;
    private String Penggunaan;
    private String sisa;
    private String NamaTask;

    public ListSpdModel(long idInt,String noTask, String namaTeknisi, String idTeknisi, String jumlahTrf,
                        String approvalNominal, String tanggalTask, String penggunaan, String sisa,String namaTask) {
        this.idInt = idInt;
        this.NoTask = noTask;
        this.NamaTeknisi = namaTeknisi;
        this.IdTeknisi = idTeknisi;
        this.JumlahTrf = jumlahTrf;
        this.ApprovalNominal = approvalNominal;
        this.TanggalTask = tanggalTask;
        this.Penggunaan = penggunaan;
        this.sisa = sisa;
        this.NamaTask = namaTask;
    }

    public long getIdInt(){
        return idInt;
    }

    public String getNoTask() {
        return NoTask;
    }

    public String getNamaTeknisi() {
        return NamaTeknisi;
    }

    public String getIdTeknisi() {
        return IdTeknisi;
    }

    public String getJumlahTrf() {
        return JumlahTrf;
    }

    public String getApprovalNominal() {
        return ApprovalNominal;
    }

    public String getTanggalTask() {
        return TanggalTask;
    }

    public String getPenggunaan() {
        return Penggunaan;
    }

    public String getSisa() {
        return sisa;
    }

    public String getNamaTask(){
        return NamaTask;
    }
}


