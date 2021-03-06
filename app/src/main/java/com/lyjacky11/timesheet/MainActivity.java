package com.lyjacky11.timesheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Dialog dialog;
    Toast toast;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = this.getSharedPreferences("com.lyjacky11.timesheet", Context.MODE_PRIVATE);
        boolean hasVisited = prefs.getBoolean("HAS_VISITED_BEFORE", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        toast = Toast.makeText(getApplicationContext(),"Welcome to Time Sheet Calculator!\nCopyright © 2019 Jacky Ly.",Toast.LENGTH_LONG);

        if(!hasVisited) {
            toast.show();
            prefs.edit().putBoolean("HAS_VISITED_BEFORE", true).apply();
        }

        final WebView webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/index.html");
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Time Sheets")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                result.confirm();
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                return true;
            }
            @Override
            public boolean onJsConfirm (WebView view, String url, String message, final JsResult result) {
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Time Sheets")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                result.cancel();
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                return true;
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (webView.getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        finish();
                        startActivity(getIntent());
                    }
                }, 500);
            }
        });
    }

    public void clearPrefs(View view) {
        ImageView closeBtn = dialog.findViewById(R.id.closeBtn);
        SharedPreferences prefs = this.getSharedPreferences("com.lyjacky11.timesheet", Context.MODE_PRIVATE);
        prefs.edit().remove("HAS_VISITED_BEFORE").apply();
        closeBtn.performClick();
        Toast.makeText(getApplicationContext(),"Preferences have been cleared!",Toast.LENGTH_SHORT).show();
    }

    public void goToGitHub(View view) {
        goToUrl("https://github.com/lyjacky11");
    }

    public void goToLinkedIn(View view) {
        goToUrl("https://www.linkedin.com/in/lyjacky11/");
    }

    public void goToWeb(View view) {
        goToUrl("https://timesheet.lyjacky11.me/");
    }

    public void goToWebsite (View view) {
        goToUrl("https://lyjacky11.me/");
    }

    public void goToYouTube(View view) {
        goToUrl("https://www.youtube.com/user/LyJacky");
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            ImageView closeBtn;
            dialog.setContentView(R.layout.about);
            closeBtn = dialog.findViewById(R.id.closeBtn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            TextView versionName = dialog.findViewById(R.id.version);
            versionName.setText("App version: " + BuildConfig.VERSION_NAME);
            dialog.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            return true;
        }
        else if (item.getItemId() == R.id.action_refresh) {
            finish();
            startActivity(getIntent());
        }
        else if (item.getItemId() == R.id.action_prefs) {
            ImageView closeBtn;
            dialog.setContentView(R.layout.prefs);
            closeBtn = dialog.findViewById(R.id.closeBtn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            TextView versionName = dialog.findViewById(R.id.version);
            versionName.setText("App version: " + BuildConfig.VERSION_NAME);
            dialog.setCanceledOnTouchOutside(false);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            return true;
        }
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }
}
