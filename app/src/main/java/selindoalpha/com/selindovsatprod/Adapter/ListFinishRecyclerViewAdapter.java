package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import selindoalpha.com.selindovsatprod.Model.ListTaskFinishModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefManager;

public class ListFinishRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    private List<ListTaskFinishModel> OrilistTaskFinishModels;
    private List<ListTaskFinishModel> filterlistTaskFinishModels;
    Context context;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    SharedPrefManager sharedPrefManager;

    private int lastPosition = -1;

    // data is passed into the constructor

    private SparseBooleanArray selectedItem;
    private ItemFilter mFilter = new ItemFilter();

    private OnItemClickListener monItemClickListener;

    public interface  OnItemClickListener{
        void onItemClick(View view, ListTaskFinishModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.monItemClickListener = mItemClickListener;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView myTitle, myTask, myDate, myAlamat, myStatus, myLokasi, myOrder, myButton,Vid;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            myTitle = itemView.findViewById(R.id.tvTitleFinish);
            myTask = itemView.findViewById(R.id.tvListFinish);
            myStatus = itemView.findViewById(R.id.tvStatusFinish);
            myDate = itemView.findViewById(R.id.tvDateFinish);
            myAlamat = itemView.findViewById(R.id.tvAlamatFinish);
            myLokasi = itemView.findViewById(R.id.tvLocationFinish);
            myOrder = itemView.findViewById(R.id.tvOrderFinish);
            myButton = itemView.findViewById(R.id.btnStartFinish);
            Vid = itemView.findViewById(R.id.vidfinishtask);
        }

    }

    public Filter getFilter(){
        return mFilter;
    }

    public ListFinishRecyclerViewAdapter(Context context, List<ListTaskFinishModel> listTaskFinishModels) {
        this.OrilistTaskFinishModels = listTaskFinishModels;
        this.filterlistTaskFinishModels = listTaskFinishModels;
        this.context = context;
        selectedItem = new SparseBooleanArray();
    }

    // inflates the row layout from xml when needed
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_finish_list, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

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
        return filterlistTaskFinishModels == null ? 0 : filterlistTaskFinishModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return filterlistTaskFinishModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    // stores and recycles views as they are scrolled off screen


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        ListTaskFinishModel  listTaskFinishModel = filterlistTaskFinishModels.get(position);
        sharedPrefManager = new SharedPrefManager(context);
        if (listTaskFinishModel.getStatusTask().equals("Finish")){
            viewHolder.myButton.setText("View");
        }else {
            viewHolder.myButton.setText("Start");
        }
        if (listTaskFinishModel.getIdJenisTask().equals("null")){
            viewHolder.myOrder.setVisibility(View.GONE);
        }else {
            viewHolder.myOrder.setText(listTaskFinishModel.getIdJenisTask());
        }
        if (listTaskFinishModel.getNamaRemot().equals("null")){
            viewHolder.myTitle.setText("N/A");
        }else {
            viewHolder.myTitle.setText(listTaskFinishModel.getNamaRemot());
        }
        if (listTaskFinishModel.getNoTask().equals("null")){
            viewHolder.myTask.setText("N/A");
        }else {
            viewHolder.myTask.setText(listTaskFinishModel.getNoTask());
        }
        if (listTaskFinishModel.getTanggalTask().equals("null")){
            viewHolder.myDate.setText("N/A");
        }else {
            viewHolder.myDate.setText(listTaskFinishModel.getTanggalTask());
        }
        if (listTaskFinishModel.getStatusTask().equals("null")){
            viewHolder.myStatus.setText("N/A");
        }else {
            viewHolder.myStatus.setText(listTaskFinishModel.getStatusTask());
        }
        if (listTaskFinishModel.getProvinsi().equals("null")){
            viewHolder.myLokasi.setText("N/A");
        }else {
            viewHolder.myLokasi.setText(listTaskFinishModel.getProvinsi());
        }
        if (listTaskFinishModel.getAlamat().equals("null")){
            viewHolder.myAlamat.setText("N/A");
        }else {
            viewHolder.myAlamat.setText(listTaskFinishModel.getAlamat());
        }
        if (listTaskFinishModel.getVid().equals("null")){
            viewHolder.Vid.setText("N/A");
        }else {
            viewHolder.Vid.setText(listTaskFinishModel.getVid());
        }
//        viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monItemClickListener !=null){
                    Animation fadein = new AlphaAnimation(0,1);
                    fadein.setDuration(50);
                    viewHolder.myButton.startAnimation(fadein);
                    monItemClickListener.onItemClick(v,listTaskFinishModel,position);
                }
            }
        });
        viewHolder.myButton.setActivated(selectedItem.get(position,false));
        // call Animation function
        setAnimation(viewHolder.itemView, position);

    }

    @Override
    public long getItemId(int position){
        return filterlistTaskFinishModels.get(position).getIdInt();
    }
    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            String query = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();
            final List<ListTaskFinishModel> list = OrilistTaskFinishModels;
            final List<ListTaskFinishModel> result_list = new ArrayList<>(list.size());

            for (int i =0; i < list.size(); i++){
                String str_title1 = list.get(i).getId();
                String str_title2 = list.get(i).getNamaRemot();
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
            filterlistTaskFinishModels = (List<ListTaskFinishModel>) results.values;
            notifyDataSetChanged();
        }

    }

}
