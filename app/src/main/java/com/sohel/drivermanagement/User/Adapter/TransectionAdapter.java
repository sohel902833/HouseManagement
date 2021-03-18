package com.sohel.drivermanagement.User.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.HomeList;
import com.sohel.drivermanagement.User.DataModuler.Transection;

import java.util.List;

public class TransectionAdapter extends RecyclerView.Adapter<TransectionAdapter.MyViewHolder>{

    private Context context;
    private List<Transection> transectionList;
    private  OnItemClickListner listner;

    public TransectionAdapter(Context context, List<Transection> transectionList) {
        this.context = context;
        this.transectionList = transectionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.transection_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Transection currentItem=transectionList.get(position);

        holder.transectorNameText.setText(currentItem.getPersonName());
        holder.transectionAmount.setText("Transection Amount : "+currentItem.getPayment());
        holder.transectionDate.setText(currentItem.getDate()+" at "+currentItem.getTime());

    }

    @Override
    public int getItemCount() {
        return transectionList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView transectorNameText,transectionAmount,transectionDate;

      public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            transectorNameText=itemView.findViewById(R.id.transector_NameTextview);
            transectionAmount=itemView.findViewById(R.id.transection_Balance_TextViewid);
            transectionDate=itemView.findViewById(R.id.transecotr_DateTextViewid);



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
