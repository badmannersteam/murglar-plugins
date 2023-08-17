package com.badmanners.murglar.plugin;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.content.Intent.ACTION_VIEW;


public class LinkRedirectActivity extends Activity {

    public static final String MURGLAR_ACTION_VIEW = "com.badmanners.murglar.action.VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.getAction().equals(ACTION_VIEW) || intent.getData() == null)
            return;

        try {
            Intent redirectIntent = new Intent(MURGLAR_ACTION_VIEW);
            redirectIntent.putExtra("uri", intent.getData().toString());
            startActivity(redirectIntent);
        } catch (ActivityNotFoundException e) {
            Log.w("LinkRedirectActivity", e);
            Toast.makeText(this, R.string.murglar_not_found, Toast.LENGTH_LONG).show();
        }
        finish();
        System.exit(0);
    }
}