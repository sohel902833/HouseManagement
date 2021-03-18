package com.sohel.drivermanagement.User.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.HomeList;
import com.sohel.drivermanagement.User.DataModuler.RenterList;
import com.sohel.drivermanagement.User.IndividualPersonTransactionActivity;

import java.util.List;

public class RenterListAdapter extends RecyclerView.Adapter<RenterListAdapter.MyViewHolder>{

    private Context context;
    private List<RenterList> renterLists;
    private  OnItemClickListner listner;

    private FirebaseAuth mAuth;
    private DatabaseReference floorRef,homeRef;



    public RenterListAdapter(Context context, List<RenterList> renterLists) {
        this.context = context;
        this.renterLists = renterLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.renter_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RenterList currentItem=renterLists.get(position);
        holder.renterNameTextView.setText(currentItem.getName());
        holder.renterDetailsTextView.setText("Home: "+currentItem.getHomeName()+",Floor: "+currentItem.getFloorName());
        holder.renterDueAmountTextView.setText("Due:"+currentItem.getDueAmount());

        holder.callImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+88"+currentItem.getPhone()));
                context.startActivity(intent);
            }
        });

        holder.transectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, IndividualPersonTransactionActivity.class);

                intent.putExtra("userId",currentItem.getRenterId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return renterLists.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView renterNameTextView,renterDetailsTextView,renterDueAmountTextView;
        ImageView callImageButton;
        Button transectionButton;
      public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

           renterNameTextView=itemView.findViewById(R.id.renterList_RenterNameTextviewid);
           renterDetailsTextView=itemView.findViewById(R.id.renterList_RenterLiveDetailsTextviewid);
           renterDueAmountTextView=itemView.findViewById(R.id.renterList_RenterDueBalanceTextViewid);
           callImageButton=itemView.findViewById(R.id.renterList_CallButtonid);
            transectionButton=itemView.findViewById(R.id.renterList_TransectionButtonid);
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
