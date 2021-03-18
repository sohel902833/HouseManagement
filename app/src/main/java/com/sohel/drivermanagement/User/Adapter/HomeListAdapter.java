package com.sohel.drivermanagement.User.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sohel.drivermanagement.Admin.DataModuler.Products;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.HomeList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.MyViewHolder>{

    private Context context;
    private List<HomeList> homeDataList;
    private  OnItemClickListner listner;

    public HomeListAdapter(Context context, List<HomeList> homeDataList) {
        this.context = context;
        this.homeDataList = homeDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.home_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HomeList currentItem=homeDataList.get(position);

        holder.homeNameTextview.setText(currentItem.getHomeName());
        holder.homeFloorTextview.setText("Total Floor: "+currentItem.getTotalFloor());

    }

    @Override
    public int getItemCount() {
        return homeDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView homeNameTextview,homeFloorTextview;

      public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            homeNameTextview=itemView.findViewById(R.id.homeList_HomeNameTextviewId);
            homeFloorTextview=itemView.findViewById(R.id.homeList_TotalFloorTextviewid);


        }

        @Override
        public void onClick(View v) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    listner.onItemClick(position);
                }
            }
        }
    }
    public interface  OnItemClickListner{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }


}
