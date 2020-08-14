package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import selindoalpha.com.selindovsatprod.Activity.TaskDetailActivity;
import selindoalpha.com.selindovsatprod.Model.DetailTaskModel;
import selindoalpha.com.selindovsatprod.R;
import selindoalpha.com.selindovsatprod.Utils.SharedPrefDataSPD;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{

    private List<DetailTaskModel> detailTaskModelList;
    Context context;

    String statusBG;

    SharedPrefDataSPD sharedPrefDataSPD;

    private int lastPosition = -1;

    // data is passed into the constructor


    public TaskListAdapter(Context context, List<DetailTaskModel> detailTaskModelList) {
        this.context = context;
        this.detailTaskModelList = detailTaskModelList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_detail_task_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        sharedPrefDataSPD = new SharedPrefDataSPD(context);

        DetailTaskModel detailTaskModel = detailTaskModelList.get(position);
        statusBG = detailTaskModel.getIdStatusPerbaikan();
        Log.i("Test", "StatusListAdapter: " + statusBG);
        if (statusBG.equals("1")){
            viewHolder.myTask.setText(detailTaskModel.getVid());
            viewHolder.myStatus.setText(detailTaskModel.getStatusTask());
            viewHolder.myLokasi.setText(detailTaskModel.getProvinsi());
            viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation fadein = new AlphaAnimation(0,1);
                    fadein.setDuration(50);
                    viewHolder.myButton.startAnimation(fadein);
                    Intent in = new Intent(v.getContext(), TaskDetailActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(in);
                }
            });
            // call Animation function
            setAnimation(viewHolder.itemView, position);
            viewHolder.mStatusBg.setBackgroundResource(R.color.super_hot);
        }
        if (statusBG.equals("4")){
            viewHolder.myTask.setText(detailTaskModel.getVid());
            viewHolder.myStatus.setText(detailTaskModel.getStatusTask());
            viewHolder.myLokasi.setText(detailTaskModel.getProvinsi());
            viewHolder.myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation fadein = new AlphaAnimation(0,1);
                    fadein.setDuration(50);
                    viewHolder.myButton.startAnimation(fadein);
                    Intent in = new Intent(v.getContext(), TaskDetailActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(in);
                }
            });

            // call Animation function
            setAnimation(viewHolder.itemView, position);
            viewHolder.mStatusBg.setBackgroundResource(R.color.colorGreen700);
        }

        sharedPrefDataSPD.saveSPString(SharedPrefDataSPD.VID,detailTaskModel.getVid());

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
        return detailTaskModelList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView myTask, myStatus, myLokasi, myButton, mStatusBg;

        ViewHolder(View itemView) {
            super(itemView);
            myTask = itemView.findViewById(R.id.tvListTaskDetail);
            myStatus = itemView.findViewById(R.id.tvStatusDetail);
            myLokasi = itemView.findViewById(R.id.tvLocationDetail);
            myButton = itemView.findViewById(R.id.btnStartDetail);
            mStatusBg = itemView.findViewById(R.id.bgStatus);
        }

    }

}
