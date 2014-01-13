package com.telerik.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class CreateNewEverliveUserActivity extends Activity implements View.OnClickListener {

    private ToggleButton maleButton;
    private ToggleButton femaleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_user);

        getActionBar().hide();

        this.maleButton = (ToggleButton) findViewById(R.id.cnu_maleButton);
        this.femaleButton = (ToggleButton) findViewById(R.id.cnu_femaleButton);
        this.maleButton.setOnClickListener(this);
        this.femaleButton.setOnClickListener(this);

        if (savedInstanceState == null) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cnu_femaleButton : {
                if (this.maleButton.isChecked()) {
                    this.maleButton.setChecked(false);
                }
                break;
            }
            case R.id.cnu_maleButton : {
                if (this.femaleButton.isChecked()) {
                    this.femaleButton.setChecked(false);
                }
                break;
            }
        }
    }
}
