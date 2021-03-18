package com.sohel.drivermanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sohel.drivermanagement.User.FloorListActivity;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, FloorListActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("homeName","sohelrana alarm");
        intent1.putExtra("floorId","skdjfklekrjddkfodkjf");
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
