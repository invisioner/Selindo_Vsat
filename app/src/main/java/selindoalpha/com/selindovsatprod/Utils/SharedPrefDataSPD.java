package selindoalpha.com.selindovsatprod.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefDataSPD {


    private static final String SP_SELINDO_APP = "SP_SELINDO_APP";
    public static final String NoTask = "NoTask";
    public static final String NamaTeknisi = "NamaTeknisi";
    public static final String VID = "VID";
    public static final String ID = "ID";
    public static final String JenisBiaya = "JenisBiaya";
    public static final String Nominal = "Nominal";
    public static final String TglInputBiaya = "TglInputBiaya";
    public static final String CatatanTransaksi = "CatatanTransaksi";
    public static final String file_urlFoto = "CatatanTransaksi";
    public static final String file_id = "file_id";


    public static final String NOTASKSAVE = "NOTASKSAVE";
    public static final String VIDSAVE = "VIDSAVE";
    public static final String NAMATASKSAVE = "NAMATASKSAVE";
    public static final String NAMAREMOTESAVE = "NAMAREMOTESAVE";
    public static final String IPLANSAVE = "IPLANSAVE";
    public static final String LOKASISAVE = "LOKASISAVE";


    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefDataSPD(Context context) {
        sp = context.getSharedPreferences(SP_SELINDO_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value) {
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getFile_urlFoto() {
        return sp.getString(file_urlFoto,"");
    }

    public String getNominal() {
        return sp.getString(Nominal,"");
    }

    public String getTglInputBiaya() {
        return sp.getString(TglInputBiaya,"");
    }

    public String getCatatanTransaksi() {
        return sp.getString(CatatanTransaksi,"");
    }

    public String getNoTask() {
        return sp.getString(NoTask,"");
    }

    public String getVID() {
        return sp.getString(VID,"");
    }

    public String getID() {
        return sp.getString(ID,"");
    }

    public String getJenisBiaya() {
        return sp.getString(JenisBiaya, "");
    }

    public String getFile_id() {
        return sp.getString(file_id, "");
    }

    public String getNOTASKSAVE() {
        return sp.getString(NOTASKSAVE,"");
    }

    public String getVIDSAVE() {
        return sp.getString(VIDSAVE,"");
    }

    public String getNAMATASKSAVE() {
        return sp.getString(NAMATASKSAVE,"");
    }

    public String getNAMAREMOTESAVE() {
        return sp.getString(NAMAREMOTESAVE,"");
    }

    public String getIPLANSAVE() {
        return sp.getString(IPLANSAVE,"");
    }

    public String getLOKASISAVE() {
        return sp.getString(LOKASISAVE,"");
    }
}