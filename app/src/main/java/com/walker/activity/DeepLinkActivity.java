package com.walker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.walker.R;
import com.walker.base.BasePureActivity;

/**
 * summary :android deep link
 * time    :2016/8/23 13:46
 * e-mail  :singlewolf968@gmail.com
 *
 * @author :Walker
 */
public class DeepLinkActivity extends BasePureActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.act_deeplink;
    }

    @Override
    protected void initialization(Bundle savedInstanceState) {
        initToolBar();
        handleIntent();
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected void unbindListener() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent();
    }


    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_goback);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri == null) {
                Snackbar.make(getWindow().getDecorView(), "uri is null !", Snackbar.LENGTH_LONG).show();
                return;
            }
            String host = uri.getHost();
            String query = uri.getQuery();
            AlertDialog.Builder builder = new AlertDialog.Builder(DeepLinkActivity.this);
            builder.setTitle("Uri信息");
            String message = String.format("Uri:%s\nHost:%s\nQuery:%s", uri.toString(), host, query);
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();
        }
    }
}
