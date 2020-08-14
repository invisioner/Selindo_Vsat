package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;
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
import selindoalpha.com.selindovsatprod.Model.DataTerpasangModel;
import selindoalpha.com.selindovsatprod.R;

public class BarangTerpasangAdapter extends RecyclerView.Adapter<BarangTerpasangAdapter.ViewHolder> {

    private List<DataTerpasangModel> dataTerpasangModelList;
    Context context;
    private SparseBooleanArray selectedItem;

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, DataTerpasangModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }


    public BarangTerpasangAdapter(Context context, List<DataTerpasangModel> dataTerpasangModelList) {
        this.dataTerpasangModelList = dataTerpasangModelList;
        this.context = context;
        selectedItem = new SparseBooleanArray();
    }

    @Override
    public BarangTerpasangAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data_terpasang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BarangTerpasangAdapter.ViewHolder viewHolder, int position) {
        DataTerpasangModel data = dataTerpasangModelList.get(position);
//        String localFoto = "vsatphase2/";
        String localFoto = "vsatmonitoring/";


        viewHolder.mTitle.setText(data.getNamaBarang());
        viewHolder.mVid.setText(data.getVid());
        viewHolder.mSerialNumber.setText(data.getSn());
        viewHolder.mDeskripsi.setText(data.getDescription());
        Picasso.with(context)
                .load(BaseUrl.getPublicIp + localFoto + data.getFileUrl())
                .into(viewHolder.mImageView);
        Log.i("Foto",BaseUrl.getPublicIp + localFoto + data.getFileUrl());

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null) {
                    monItemClickListener.onItemClick(v,data,position);
                }
            }
        });
        viewHolder.card.setActivated(selectedItem.get(position,false));
    }

    @Override
    public int getItemCount() {
        return dataTerpasangModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mVid, mSerialNumber, mDeskripsi;
        ImageView mImageView;
        CardView card;
        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageViewBarang);
            mDeskripsi = itemView.findViewById(R.id.deskripsi);
            mTitle = itemView.findViewById(R.id.titleBarangTerpasang);
            mSerialNumber = itemView.findViewById(R.id.serial_number);
            mVid = itemView.findViewById(R.id.tvVidCard);
            card = itemView.findViewById(R.id.cardbarangterpasang);
        }
    }
}
