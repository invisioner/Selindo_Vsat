package selindoalpha.com.selindovsatprod.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataHelperSpdInput extends SQLiteOpenHelper {

    public static final String db = "db_upload";
    public static final String tb_upload = "tb_upload";


    public static final List<String> create = new ArrayList<String>(){{

        add("create table " + tb_upload +
                " (category TEXT, nominal INTEGER PRIMARY KEY ,nameImages TEXT )");
    }};

    public static final List<String> table = new ArrayList<String>(){{
        add(tb_upload);
    }};

    public DataHelperSpdInput(Context context) {
        super(context, db, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        for (int i = 0; i < create.size(); i++){
            sqLiteDatabase.execSQL(create.get(i));
        }

//        String sql = "INSERT INTO tb_upload (category, nominal, nameImages) VALUES " +
//                "('Hotel', '435000', 'image')";
//        sqLiteDatabase.execSQL(sql);
//
//        String sql1 = "INSERT INTO tb_upload (category, nominal, nameImages) VALUES " +
//                "('Transport', '688724', 'image4343')";
//        sqLiteDatabase.execSQL(sql1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        for (int j = 0; j < table.size(); j++){
            sqLiteDatabase.execSQL(table.get(j));
        }
    }
}
