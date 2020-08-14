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

import selindoalpha.com.selindovsatprod.Model.ListSpdModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class SpdRecyclerViewAdapter extends RecyclerView.Adapter<SpdRecyclerViewAdapter.ViewHolder> implements Filterable{

    private static final String TAG = "SpdRecyclerViewAdapter";
    private List<ListSpdModel> OrilistSpdModels;
    private List<ListSpdModel> filterlistSpdModels;
    Context context;

    SharedPrefDataSPD sharedPrefDataSPD;

    private int lastPosition = -1;
    SharedPrefManager sharedPrefManager;
    private SparseBooleanArray selectedItem;
    private ItemFilter mFilter = new ItemFilter();

    // data is passed into the constructor

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, ListSpdModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView myTask, myDate, myDiterima, myDigunakan, myDisetujui, mySisa, myNamaTask;
        Button myBtnStart;

        ViewHolder(View itemView) {
            super(itemView);
            myTask = itemView.findViewById(R.id.tvTaskSpd);
            myDate = itemView.findViewById(R.id.tvDateSpd);
            myDiterima = itemView.findViewById(R.id.uang_diterima);
            myDigunakan = itemView.findViewById(R.id.uang_digunakan);
            myDisetujui = itemView.findViewById(R.id.uang_disetujui);
            mySisa = itemView.findViewById(R.id.uang_sisa);
            myNamaTask = itemView.findViewById(R.id.tvNamaTask);
            myBtnStart = itemView.findViewById(R.id.btnStartSPD);
        }

    }

    public Filter getFilter(){
        return mFilter;
    }

    public SpdRecyclerViewAdapter(Context context, List<ListSpdModel> listSpdModels) {
        this.OrilistSpdModels = listSpdModels;
        this.filterlistSpdModels = listSpdModels;
        this.context = context;
        selectedItem = new SparseBooleanArray();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_spd_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ListSpdModel listSpdModel = filterlistSpdModels.get(position);

//        String rupiah = "Rp. ";

        sharedPrefDataSPD = new SharedPrefDataSPD(context);
        if (listSpdModel.getJumlahTrf().equals("null")){
            viewHolder.myDiterima.setText("0");
        }else {
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber1 = Double.parseDouble(listSpdModel.getJumlahTrf());
            String getTotal = formatter.format(myNumber1);
            viewHolder.myDiterima.setText(getTotal);
        }
        if (listSpdModel.getPenggunaan().equals("null")){
            viewHolder.myDigunakan.setText("0");
        }else {
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber2 = Double.parseDouble(listSpdModel.getPenggunaan());
            String getTotalsuk = formatter.format(myNumber2);
            viewHolder.myDigunakan.setText(getTotalsuk);
        }
        if (listSpdModel.getApprovalNominal().equals("null")){
            viewHolder.myDisetujui.setText("0");
        }else {
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber3 = Double.parseDouble(listSpdModel.getApprovalNominal());
            String getApprove = formatter.format(myNumber3);
            viewHolder.myDisetujui.setText(getApprove);
        }
        if (listSpdModel.getSisa().equals("null")){
            viewHolder.mySisa.setText("0");
        }else {
            NumberFormat formatter = new DecimalFormat("#,###");
            double myNumber4 = Double.parseDouble(listSpdModel.getSisa());
            String getSisa = formatter.format(myNumber4);
            viewHolder.mySisa.setText(getSisa);
        }
        if (listSpdModel.getNamaTask().equals("null")){
            viewHolder.myNamaTask.setVisibility(View.GONE);
        }else {
            viewHolder.myNamaTask.setText(listSpdModel.getNamaTask());
        }
        if (listSpdModel.getNoTask().equals("null")){
            viewHolder.myTask.setText("N/A");
        }else {
            viewHolder.myTask.setText(listSpdModel.getNoTask());
        }
        if (listSpdModel.getTanggalTask().equals("null")){
            viewHolder.myDate.setText("N/A");
        }else {
            viewHolder.myDate.setText(listSpdModel.getTanggalTask());
        }

        Log.i(TAG, "SPD idTeknisi: " + listSpdModel.getIdTeknisi());
        viewHolder.myBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null){
                    Animation fadein = new AlphaAnimation(0,1);
                    fadein.setDuration(50);
                    viewHolder.myBtnStart.startAnimation(fadein);
                    monItemClickListener.onItemClick(v,listSpdModel,position);
                }
            }
        });
        viewHolder.myBtnStart.setActivated(selectedItem.get(position,false));

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
        return filterlistSpdModels.size();
    }

    @Override
    public long getItemId(int position){
        return filterlistSpdModels.get(position).getIdInt();
    }
    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<ListSpdModel> list = OrilistSpdModels;
            final List<ListSpdModel> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title1 = list.get(i).getNoTask();
                String str_title2 = list.get(i).getNamaTask();
                String str_title3 = list.get(i).getNamaTeknisi();
                String str_title4 = list.get(i).getIdTeknisi();
                String str_title8 = list.get(i).getTanggalTask();
                String str_title10 = list.get(i).getSisa();
                if (str_title1.toLowerCase().contains(query)||
                        str_title2.toLowerCase().contains(query)||
                        str_title3.toLowerCase().contains(query)||
                        str_title4.toLowerCase().contains(query)||
                        str_title8.toLowerCase().contains(query)||
                        str_title10.toLowerCase().contains(query)){
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
            filterlistSpdModels = (List<ListSpdModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
