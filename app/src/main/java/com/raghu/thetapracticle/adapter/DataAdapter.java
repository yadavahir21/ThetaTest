package com.raghu.thetapracticle.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.model.Data;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = DataAdapter.class.getSimpleName();
    private List<Data> dataList = new ArrayList<>();
    private Context context;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    public DataAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_data, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.view_progress, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_NORMAL:
                DataViewHolder dataViewHolder = (DataViewHolder) holder;
                if (dataList == null || dataList.size() <= 0 || position < 0)
                    return;
                dataViewHolder.txtFirstName.setText(dataList.get(position).getFirstName());
                dataViewHolder.txtLastName.setText(dataList.get(position).getLastName());
                dataViewHolder.txtEmailID.setText(dataList.get(position).getEmail());

                if (dataList.get(position).getAvatar() != null){
                    Glide.with(context)
                            .load(dataList.get(position).getAvatar())
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_user))
                            .apply(RequestOptions.noAnimation())
                            .into(dataViewHolder.imgUser);
                }
                break;
            case VIEW_TYPE_LOADING:
                Log.d(TAG, "onBindViewHolder: loading");
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == dataList.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public void addItems(List<Data> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        dataList.add(new Data());
        notifyItemInserted(dataList.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = dataList.size() - 1;
        Data data = getItem(position);
        if (data != null) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Data getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        } else {
            return dataList.size();
        }
    }

    class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgUser;
        TextView txtFirstName;
        TextView txtLastName;
        TextView txtEmailID;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtFirstName = itemView.findViewById(R.id.txtFirstName);
            txtLastName = itemView.findViewById(R.id.txtLastName);
            txtEmailID = itemView.findViewById(R.id.txtEmailID);

            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {

            }
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder {

        ProgressHolder(View itemView) {
            super(itemView);
        }
    }
}

