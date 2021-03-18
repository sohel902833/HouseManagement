package com.sohel.drivermanagement;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

    ProgressDialog progressDialog;
    Context context;
    public LoadingDialog(Context context){
      this.context=context;
    }
    public void showLoadingDiolouge(){
        progressDialog=new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }
    public void dismiss(){
        progressDialog.dismiss();
    }


}
