package selindoalpha.com.selindovsatprod.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import selindoalpha.com.selindovsatprod.Activity.DetailPhotoActivity;
import selindoalpha.com.selindovsatprod.Api.BaseUrl;
import selindoalpha.com.selindovsatprod.Model.ListPhotoModel;
import selindoalpha.com.selindovsatprod.R;

public class ListPhotoAdapter extends RecyclerView.Adapter<ListPhotoAdapter.ViewHolder> {

    public List<ListPhotoModel> listPhotoModels;
    Context context;

    public ListPhotoAdapter(Context context, List<ListPhotoModel> listPhotoModels) {
        this.listPhotoModels = listPhotoModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_grid_foto, parent, false);


        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position) {

        ListPhotoModel list = listPhotoModels.get(position);

        Log.i("TestObj", "list: " +list.getImageurl());
        Log.i("TestObj", "list: " +list.getName());
//        String localFoto = "vsatphase2/";
        String localFoto = "vsatmonitoring/";
        String getUrl = list.getImageurl();
        String replaceAll = getUrl.replaceAll(" ", "");

        Picasso.with(context)
                .load(BaseUrl.getPublicIp + localFoto + replaceAll)
                .into(viewHolder.imageView);

        Log.i("Foto", BaseUrl.getPublicIp + localFoto + replaceAll);

        viewHolder.descrip.setText(list.getName());
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, DetailPhotoActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                String localFoto = "vsatphase2/";
                String localFoto = "vsatmonitoring/";
                String getUrl = list.getImageurl();
                String replaceAll = getUrl.replaceAll(" ", "");
                mIntent.putExtra("Image", BaseUrl.getPublicIp +localFoto+ replaceAll);
                mIntent.putExtra("Desc", list.getName());
                Log.i("TAG", "Image: " + BaseUrl.getPublicIp +localFoto+ replaceAll);
                context.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhotoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView descrip;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivImages);
            descrip = itemView.findViewById(R.id.titlePhoto);
        }
    }

}
