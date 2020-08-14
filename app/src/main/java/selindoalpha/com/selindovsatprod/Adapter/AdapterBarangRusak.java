package selindoalpha.com.selindovsatprod.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import selindoalpha.com.selindovsatprod.Model.NamaBarang;
import selindoalpha.com.selindovsatprod.R;

public class AdapterBarangRusak extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<NamaBarang> item;

    public AdapterBarangRusak(Activity activity, List<NamaBarang> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_list_nama_barang, null);

        TextView NamaBarang = (TextView) convertView.findViewById(R.id.namabarang);

        NamaBarang data;
        data = item.get(position);

        NamaBarang.setText(data.getNamaBarang());

        return convertView;
    }

}
