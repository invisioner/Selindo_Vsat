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
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selindoalpha.com.selindovsatprod.Model.ListTaskOpenModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> implements Filterable{

    private List<ListTaskOpenModel> OrilistTaskOpenModels;
    private List<ListTaskOpenModel> filterlistTaskOpenModels;
    Context context;

    private static final String TAG = "TaskRecyclerViewAdapter";

    private int lastPosition = -1;
    SharedPrefManager sharedPrefManager;
    private SparseBooleanArray selectedItem;
    private ItemFilter mFilter = new ItemFilter();

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, ListTaskOpenModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTitle, myTask, myDate, mAlamat, myStatus, myProvinsi, mOrder, myButton,VID;

        ViewHolder(View itemView) {
            super(itemView);
            myTitle = itemView.findViewById(R.id.tvTitleTask);
            myTask = itemView.findViewById(R.id.tvListTask);
            myDate = itemView.findViewById(R.id.tvDateTask);
            mAlamat = itemView.findViewById(R.id.tvAlamatTask);
            myStatus = itemView.findViewById(R.id.tvStatusTask);
            myProvinsi = itemView.findViewById(R.id.tvLocationTask);
            mOrder = itemView.findViewById(R.id.tvOrderTask);
            myButton = itemView.findViewById(R.id.btnStartTask);
            VID = itemView.findViewById(R.id.vidopentask);
        }
    }

    public Filter getFilter(){
        return mFilter;
    }

    public TaskRecyclerViewAdapter(Context context, List<ListTaskOpenModel> listTaskOpenModels) {
        this.context = context;
        OrilistTaskOpenModels = listTaskOpenModels;
        filterlistTaskOpenModels = listTaskOpenModels;
        selectedItem = new SparseBooleanArray();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        ListTaskOpenModel mListTaskOpenModel = filterlistTaskOpenModels.get(position);
        sharedPrefManager = new SharedPrefManager(context);

        Log.i(TAG, "OrderBy: " + mListTaskOpenModel.getIdJenisTask());

        if (mListTaskOpenModel.getIdJenisTask().equals("null") || mListTaskOpenModel.getIdJenisTask().equals("")){
            viewHolder.mOrder.setVisibility(View.GONE);
        }else {
            viewHolder.mOrder.setText(mListTaskOpenModel.getIdJenisTask());
        }
        if (mListTaskOpenModel.getNamaRemote().equals("null")){
            viewHolder.myTitle.setText("N/A");
        }else {
            viewHolder.myTitle.setText(mListTaskOpenModel.getNamaRemote());
        }
        if (mListTaskOpenModel.getNoTask().equals("null")){
            viewHolder.myTask.setText("N/A");
        }else {
            viewHolder.myTask.setText(mListTaskOpenModel.getNoTask());
        }
        if (mListTaskOpenModel.getTanggalTask().equals("null")){
            viewHolder.myDate.setText("N/A");
        }else {
            viewHolder.myDate.setText(mListTaskOpenModel.getTanggalTask());
        }
        if (mListTaskOpenModel.getStatusTask().equals("null")){
            viewHolder.myStatus.setText("N/A");
        }else {
            viewHolder.myStatus.setText(mListTaskOpenModel.getStatusTask());
        }
        if (mListTaskOpenModel.getProvinsi().equals("null")){
            viewHolder.myProvinsi.setText("N/A");
        }else {
            viewHolder.myProvinsi.setText(mListTaskOpenModel.getProvinsi());
        }
        if (mListTaskOpenModel.getAlamat().equals("null")){
            viewHolder.mAlamat.setText("N/A");
        }else {
            viewHolder.mAlamat.setText(mListTaskOpenModel.getAlamat());
        }
        if (mListTaskOpenModel.getVid().equals("null")){
            viewHolder.VID.setText("N/A");
        }else {
            viewHolder.VID.setText(mListTaskOpenModel.getVid());
        }
        Log.i(TAG, "VID per Card: " + mListTaskOpenModel.getId());

        viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null){
                    Animation fadein = new AlphaAnimation(0,1);
                    fadein.setDuration(50);
                    viewHolder.myButton.startAnimation(fadein);
                    monItemClickListener.onItemClick(v,mListTaskOpenModel,position);
                }
            }
        });
        viewHolder.myButton.setActivated(selectedItem.get(position,false));

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
        return filterlistTaskOpenModels.size();
    }

    public void remove(int position){
        filterlistTaskOpenModels.remove(position);
    }

    @Override
    public long getItemId(int position){
        return filterlistTaskOpenModels.get(position).getIdint();
    }
    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<ListTaskOpenModel> list = OrilistTaskOpenModels;
            final List<ListTaskOpenModel> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title1 = list.get(i).getId();
                String str_title2 = list.get(i).getNamaRemote();
                String str_title3 = list.get(i).getAlamat();
                String str_title4 = list.get(i).getNoTask();
                String str_title5 = list.get(i).getVid();
                String str_title6 = list.get(i).getTanggalTask();
                String str_title7 = list.get(i).getProvinsi();
                String str_title8 = list.get(i).getIdJenisTask();
                String str_title9 = list.get(i).getNamaKoordinator();
                String str_title10 = list.get(i).getNamaTeknisi();
                String str_title11 = list.get(i).getStatusTask();
                if (str_title1.toLowerCase().contains(query)||
                        str_title2.toLowerCase().contains(query)||
                        str_title3.toLowerCase().contains(query)||
                        str_title4.toLowerCase().contains(query)||
                        str_title5.toLowerCase().contains(query)||
                        str_title6.toLowerCase().contains(query)||
                        str_title7.toLowerCase().contains(query)||
                        str_title8.toLowerCase().contains(query)||
                        str_title9.toLowerCase().contains(query)||
                        str_title10.toLowerCase().contains(query)||
                        str_title11.toLowerCase().contains(query)){
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
            filterlistTaskOpenModels = (List<ListTaskOpenModel>) results.values;
            notifyDataSetChanged();
        }

    }


}
