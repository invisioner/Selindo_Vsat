package selindoalpha.com.selindovsatprod.Model;

public class DetailSpdModel {

    long idInt;
    private String noTask;
    private String flagconfirm;
    private String namaRemote;
    private String vid;
    private String totalPengeluaran;

    public DetailSpdModel(long idInt,String noTask, String flagconfirm, String namaRemote,
                          String vid, String totalPengeluaran) {
        this.idInt = idInt;
        this.noTask = noTask;
        this.flagconfirm = flagconfirm;
        this.namaRemote = namaRemote;
        this.vid = vid;
        this.totalPengeluaran = totalPengeluaran;
    }

    public long getIdInt(){
        return idInt;
    }

    public String getFlagconfirm() {
        return flagconfirm;
    }

    public String getNoTask() {
        return noTask;
    }

    public String getNamaRemote() {
        return namaRemote;
    }

    public String getVid() {
        return vid;
    }

    public String getTotalPengeluaran() {
        return totalPengeluaran;
    }

}


