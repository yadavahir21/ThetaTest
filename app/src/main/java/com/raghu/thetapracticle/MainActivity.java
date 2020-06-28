package com.raghu.thetapracticle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.raghu.thetapracticle.extra.Constant;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private NavController navController;
    private boolean isLogin = AppMain.getInstance().getPreferences().getBoolean(Constant.IS_LOGIN, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initViews();
    }

    private void initViews(){
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHost.getNavController();
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.main_graph);
        if (isLogin) {
            graph.setStartDestination(R.id.pagerFragment);
        } else {
            graph.setStartDestination(R.id.loginFragment);
        }
        navController.setGraph(graph);
    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(MainActivity.this, R.style.toolbar_style);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_24sp));
                    tv.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    break;
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination() != null) {
            navController.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
