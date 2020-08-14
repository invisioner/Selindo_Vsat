package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Model.ListSpdGambarModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class ListSpdGambarAdapter extends RecyclerView.Adapter<ListSpdGambarAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "ListSpdGambarAdapter";

    private List<ListSpdGambarModel> OrilistSpdGambarModels;
    private List<ListSpdGambarModel> filterlistSpdGambarModels;
    Context mContext;
    String replaceAll;

//    SharedPrefDataTask sharedPrefDataTask;

    SharedPrefDataSPD sharedPrefDataSPD;

    SharedPrefManager sharedPrefManager;
    private SparseBooleanArray selectedItem;
    private ItemFilter mFilter = new ItemFilter();

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, ListSpdGambarModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    private OnMoreButtonClickListener onMoreButtonClickListener;

    public interface OnMoreButtonClickListener{
        void onItemClick(View view, ListSpdGambarModel obj, int position);
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener){
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemClick(View view, ListSpdGambarModel obj, int position);
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView mPengunaan, mNominal, mDate, mNote;
        LinearLayout myDetailCard,ly_delete_item;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewSpd);
            mPengunaan = itemView.findViewById(R.id.tvPengunaan);
            mNominal = itemView.findViewById(R.id.tvNominal);
            mDate = itemView.findViewById(R.id.tvDateSpdCard);
            mNote = itemView.findViewById(R.id.tvNote);
            myDetailCard = itemView.findViewById(R.id.CardDetailSpd);
            ly_delete_item = itemView.findViewById(R.id.ly_delete_item);

        }
    }

    public Filter getFilter(){
        return mFilter;
    }

    public ListSpdGambarAdapter(Context mContext, List<ListSpdGambarModel> listSpdGambarModels) {
        this.OrilistSpdGambarModels = listSpdGambarModels;
        this.filterlistSpdGambarModels = listSpdGambarModels;
        this.mContext = mContext;
        selectedItem = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data_spd_gambar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        sharedPrefDataSPD = new SharedPrefDataSPD(mContext);

        ListSpdGambarModel data = filterlistSpdGambarModels.get(position);
        viewHolder.mPengunaan.setText(data.getJenisBiaya());
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = Double.parseDouble(data.getNominal());
        String getNominal = formatter.format(myNumber);
        viewHolder.mNominal.setText(getNominal);
        viewHolder.mDate.setText(data.getTglInputBiaya());
        viewHolder.mNote.setText(data.getCatatanTransaksi());
//        String localFoto = "vsatphase2/";
        String localFoto = "vsatmonitoring/";
        String getUrl = data.getFile_url();
        replaceAll = getUrl.replaceAll(" ", "");
        Log.i(TAG, "onBindViewHolder: " + BaseUrl.getPublicIp + localFoto + replaceAll);
        Picasso.with(mContext)
                .load(BaseUrl.getPublicIp + localFoto + replaceAll)
                .into(viewHolder.imageView);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMoreButtonClickListener != null){
                    onMoreButtonClickListener.onItemClick(v,data,position);
                }
            }
        });
        viewHolder.imageView.setActivated(selectedItem.get(position,false));

        viewHolder.myDetailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null){
                    monItemClickListener.onItemClick(v,data,position);
                }
            }
        });
        viewHolder.myDetailCard.setActivated(selectedItem.get(position,false));

        if (data.getFlagConfirm().equals("false")) {
            viewHolder.ly_delete_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemClick(v, data, position);
                    }
                    return false;
                }
            });
        }

    }



    @Override
    public int getItemCount() {
        return filterlistSpdGambarModels.size();
    }

    @Override
    public long getItemId(int position){
        return filterlistSpdGambarModels.get(position).getIdInt();
    }
    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<ListSpdGambarModel> list = OrilistSpdGambarModels;
            final List<ListSpdGambarModel> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title = list.get(i).getId();
                if (str_title.toLowerCase().contains(query)){
                    result_list.add(list.get(i));
                }
            }

            results.values = result_list;
            results.count = result_list.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterlistSpdGambarModels = (List<ListSpdGambarModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
