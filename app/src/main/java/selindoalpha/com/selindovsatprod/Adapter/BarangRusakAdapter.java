package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Model.DataRusakModel;
import selindoalpha.com.selindovsatprod.R;

public class BarangRusakAdapter extends RecyclerView.Adapter<BarangRusakAdapter.ViewHolder> {

    private static final String TAG = "BarangRusakAdapter";

    private List<DataRusakModel> dataRusakModelList;
    Context context;

    private SparseBooleanArray selectedItem;

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, DataRusakModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }


    public BarangRusakAdapter(Context context, List<DataRusakModel> dataRusakModelList) {
        this.dataRusakModelList = dataRusakModelList;
        this.context = context;
        selectedItem = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data_rusak, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,  int position) {
        DataRusakModel data = dataRusakModelList.get(position);
//        String localFoto = "vsatphase2/";
        String localFoto = "vsatmonitoring/";

        viewHolder.mDeskripsi.setText(data.getDescription());
        viewHolder.mTitle.setText(data.getNamaBarang());
        viewHolder.mSerialNumber.setText(data.getSn());
        Log.i(TAG, "onBindViewHolderBarangRusak: " + BaseUrl.getPublicIp + localFoto + data.getFileUrl());
        Picasso.with(context)
                .load(BaseUrl.getPublicIp + localFoto + data.getFileUrl())
                .into(viewHolder.mImageView);

        viewHolder.barangrusak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null) {
                    monItemClickListener.onItemClick(v,data,position);
                }
            }
        });
        viewHolder.barangrusak.setActivated(selectedItem.get(position,false));

    }

    @Override
    public int getItemCount() {
        return dataRusakModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mSerialNumber, mDeskripsi;
        ImageView mImageView;
        CardView barangrusak;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageViewBarangRusak);
            mDeskripsi = itemView.findViewById(R.id.deskripsiRusak);
            mTitle = itemView.findViewById(R.id.titleBarangRusak);
            mSerialNumber = itemView.findViewById(R.id.serialNumberRusak);
            barangrusak = itemView.findViewById(R.id.cardbararusak);
        }
    }
}
