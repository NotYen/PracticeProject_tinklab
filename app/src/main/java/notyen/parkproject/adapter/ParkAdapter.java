package notyen.parkproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import notyen.parkproject.ParkApplication;
import notyen.parkproject.apidata.OpenData;
import notyen.practiceproject.R;

public class ParkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OpenData> lists;
    private Context context;
    private OnItemClickListener mListener;

    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_GROUP = 2;

    public ParkAdapter(Context context, List<OpenData> lists) {
        this.context = context;
        this.lists = lists;
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            View view = LayoutInflater.from(context).inflate(R.layout.park_item_view, parent, false);
            parkViewHolder viewHolder = new parkViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_IMAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.park_img_item_view, parent, false);
            parkImgViewHolder viewImgHolder = new parkImgViewHolder(view);
            return viewImgHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_GROUP:
                parkViewHolder holder = (parkViewHolder) viewHolder;
                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();//得到item的LayoutParams
                holder.itemView.setLayoutParams(params);//把params给item布局

                holder.parkName.setText(lists.get(position).getParkName());
                holder.name.setText(lists.get(position).getName());
                if (!TextUtils.isEmpty(lists.get(position).getImage())) {
                    holder.parkPic.setDefaultImageResId(R.mipmap.ic_launcher);
                    holder.parkPic.setImageUrl(lists.get(position).getImage(),
                            ParkApplication.getInstance().getImageLoader());
                }

                break;

            case TYPE_IMAGE:
                parkImgViewHolder holder2 = (parkImgViewHolder) viewHolder;
                ViewGroup.LayoutParams params2 = holder2.itemView.getLayoutParams();//得到item的LayoutParams
                holder2.itemView.setLayoutParams(params2);//把params给item布局
                if (!TextUtils.isEmpty(lists.get(position).getImage())) {
                    holder2.parkPic.setDefaultImageResId(R.mipmap.ic_launcher);
                    holder2.parkPic.setImageUrl(lists.get(position).getImage(),
                            ParkApplication.getInstance().getImageLoader());
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return TYPE_IMAGE;
        } else {
            return TYPE_GROUP;
        }
    }
}

class parkViewHolder extends RecyclerView.ViewHolder {
    NetworkImageView parkPic;
    TextView parkName;
    TextView name;

    public parkViewHolder(View itemView) {
        super(itemView);
        parkPic = (NetworkImageView) itemView.findViewById(R.id.park_pic);
        parkName = (TextView) itemView.findViewById(R.id.park_name);
        name = (TextView) itemView.findViewById(R.id.name);
    }
}

class parkImgViewHolder extends RecyclerView.ViewHolder {
    NetworkImageView parkPic;

    public parkImgViewHolder(View itemView) {
        super(itemView);
        parkPic = (NetworkImageView) itemView.findViewById(R.id.park_pic);
    }
}

