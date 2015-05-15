package com.example.varut.workshop1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.varut.workshop1.accounts.GenericAccountService;
import com.example.varut.workshop1.provider.MyContentObserver;
import com.example.varut.workshop1.provider.MyProvider;


public class MainActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentRecyclerView fragmentRecyclerView_2 = new FragmentRecyclerView();
        fragmentRecyclerView_2.setLayoutManagerType(FragmentRecyclerView.MNG_GRID);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentRecyclerView_2);
        transaction.commit();
    }
}
