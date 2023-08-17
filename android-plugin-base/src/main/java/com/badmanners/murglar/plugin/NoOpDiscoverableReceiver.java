package com.badmanners.murglar.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoOpDiscoverableReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { }
}
