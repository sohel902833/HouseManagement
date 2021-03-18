package com.sohel.drivermanagement.User.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sohel.drivermanagement.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCustomAdapter extends BaseAdapter {



    private Context context;
    private  String[] values;

    public SpinnerCustomAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null) {


            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);



            TextView textView=convertView.findViewById(R.id.floor_Spinner_Textviewid);
            textView.setText(values[position]);




        }





        return convertView;
    }
}
