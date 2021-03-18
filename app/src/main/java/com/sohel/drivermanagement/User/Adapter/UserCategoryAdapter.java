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

import com.sohel.drivermanagement.Admin.DataModuler.Category;
import com.sohel.drivermanagement.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserCategoryAdapter extends RecyclerView.Adapter<UserCategoryAdapter.MyViewHolder>{

    private Context context;
    private List<Category> categoryList;
    private  OnItemClickListner listner;
    private  String checker="null";

    public UserCategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.grid_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category currentItem=categoryList.get(position);

        holder.textView.setText(currentItem.getCategoryName());
        Picasso.get().load(currentItem.getImage()).placeholder(R.drawable.select_image).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             imageView=itemView.findViewById(R.id.admin_main_categoryImageView);
             textView=itemView.findViewById(R.id.admin_main_TextViewid);
            itemView.setOnClickListener(this);
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
