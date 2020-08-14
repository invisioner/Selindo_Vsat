package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selindoalpha.com.selindovsatprod.Model.DetailSpdModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class SpdListAdapter extends RecyclerView.Adapter<SpdListAdapter.ViewHolder> implements Filterable{

    private static final String TAG = "SpdListAdapter";

   private List<DetailSpdModel> OridetailSpdModels;
   private List<DetailSpdModel> filterdetailSpdModels;
   Context mContext;

    private int lastPosition = -1;

    SharedPrefDataSPD sharedPrefDataSPD;
    SharedPrefManager sharedPrefManager;
    private SparseBooleanArray selectedItem;
    private ItemFilter mFilter = new ItemFilter();

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, DetailSpdModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myNamaRemote, myVid, myTotalPengeluaran, myFlagConfirm,jenisBiaya;
        Button myButton;

        ViewHolder(View itemView) {
            super(itemView);
            myNamaRemote = itemView.findViewById(R.id.tvNamaRemoteSpdDetail);
            myVid = itemView.findViewById(R.id.tvVidListSPD);
            myFlagConfirm = itemView.findViewById(R.id.tvFlagConfrim);
            myTotalPengeluaran = itemView.findViewById(R.id.tvTotalPengeluaran);
            myButton = itemView.findViewById(R.id.btnStartSpdDetail);
            jenisBiaya = itemView.findViewById(R.id.tvjenisbiaya);
        }

    }

    public Filter getFilter(){
        return mFilter;
    }

    public SpdListAdapter(Context mContext, List<DetailSpdModel> detailSpdModels) {
        this.OridetailSpdModels = detailSpdModels;
        this.filterdetailSpdModels = detailSpdModels;
        this.mContext = mContext;
        selectedItem = new SparseBooleanArray();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detail_spd_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        sharedPrefDataSPD = new SharedPrefDataSPD(mContext);
       DetailSpdModel detailSpdModel = filterdetailSpdModels.get(position);
        Log.i(TAG, "Flagconfirm: " + detailSpdModel.getFlagconfirm());
        if (detailSpdModel.getFlagconfirm().equals("false")){

            viewHolder.myFlagConfirm.setText("Not Confirmed");
            viewHolder.myButton.setText("Start");
            viewHolder.myFlagConfirm.setBackgroundResource(R.color.super_hot);
        }else if (detailSpdModel.getFlagconfirm().equals("null")){

           viewHolder.myFlagConfirm.setText("Not Confirmed");
           viewHolder.myButton.setText("Start");
           viewHolder.myFlagConfirm.setBackgroundResource(R.color.super_hot);
       }else if (detailSpdModel.getFlagconfirm().equals("true")){

            viewHolder.myFlagConfirm.setText("Confirmed");
            viewHolder.myButton.setText("View");
            viewHolder.myFlagConfirm.setBackgroundResource(R.color.colorGreen500);
        }

        if (detailSpdModel.getTotalPengeluaran().equals("null")){
            viewHolder.myTotalPengeluaran.setText("0");
        }else {
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber = Double.parseDouble(detailSpdModel.getTotalPengeluaran());
            String getTotalPengeluaran = formatter.format(myNumber);
            viewHolder.myTotalPengeluaran.setText(getTotalPengeluaran);
        }

        Log.i(TAG, "onBindViewHolder: " + detailSpdModel.getFlagconfirm());

        viewHolder.myNamaRemote.setText(detailSpdModel.getNamaRemote());
        viewHolder.myVid.setText(detailSpdModel.getVid());
        viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null) {
                    Animation fadein = new AlphaAnimation(0, 1);
                    fadein.setDuration(50);
                    viewHolder.myButton.startAnimation(fadein);
                    monItemClickListener.onItemClick(v,detailSpdModel,position);
                }
            }
        });
        viewHolder.myButton.setActivated(selectedItem.get(position,false));

        sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.VID,detailSpdModel.getVid());

        // call Animation function
        setAnimation(viewHolder.itemView, position);
    }

    // Animation appear with you scroll down only
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.INFINITE, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(1001));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return OridetailSpdModels.size();
    }

    @Override
    public long getItemId(int position){
        return filterdetailSpdModels.get(position).getIdInt();
    }
    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<DetailSpdModel> list = OridetailSpdModels;
            final List<DetailSpdModel> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title = list.get(i).getNoTask();
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
            filterdetailSpdModels = (List<DetailSpdModel>) results.values;
            notifyDataSetChanged();
        }

    }


}
