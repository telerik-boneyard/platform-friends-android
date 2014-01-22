package com.telerik.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.telerik.app.R;
import com.telerik.app.tasks.GoogleLoginTask;

import com.telerik.app.model.BaseViewModel;

public class AccountChooser extends Dialog implements AdapterView.OnItemClickListener {
    String[] accounts;
    private Activity activity;
    private ProgressDialog progressDialog;

    public AccountChooser(Activity activity, String[] accounts, ProgressDialog progressDialog) {
        super(activity);
        this.activity = activity;
        this.accounts = accounts;
        this.progressDialog = progressDialog;

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("Use account for sign-in");
        setContentView(R.layout.accounts_list);

        ListView listView = (ListView) findViewById(R.id.al_accounts);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, this.accounts);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String accountName = this.accounts[position];
        BaseViewModel.getInstance().setSelectedAccount(accountName);
        new GoogleLoginTask(this.activity, accountName, this.progressDialog).execute((Void) null);
        dismiss();
    }
}
