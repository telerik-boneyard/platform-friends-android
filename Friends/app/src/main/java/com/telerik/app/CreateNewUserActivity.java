package com.telerik.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.telerik.everlive.sdk.core.query.definition.UserSecretInfo;
import com.telerik.everlive.sdk.core.result.RequestResult;
import com.telerik.everlive.sdk.core.result.RequestResultCallbackAction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.BaseViewModel;
import model.MyUser;

public class CreateNewUserActivity extends Activity implements View.OnClickListener {

    private ToggleButton maleButton;
    private ToggleButton femaleButton;
    private EditText name;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText birthDay;
    private EditText about;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_user);

        getActionBar().hide();

        this.maleButton = (ToggleButton) findViewById(R.id.cnu_maleButton);
        this.femaleButton = (ToggleButton) findViewById(R.id.cnu_femaleButton);
        this.maleButton.setOnClickListener(this);
        this.femaleButton.setOnClickListener(this);
        this.birthDay = (EditText) findViewById(R.id.cnu_birthday);
        this.birthDay.setOnClickListener(this);
        this.birthDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    displayDatePicker();
                }
            }
        });

        this.name = (EditText) findViewById(R.id.cnu_name);
        MyTextWatcher textWatcher = new MyTextWatcher();
        this.name.addTextChangedListener(textWatcher);

        this.email = (EditText) findViewById(R.id.cnu_email);
        this.username = (EditText) findViewById(R.id.cnu_username);
        this.username.addTextChangedListener(textWatcher);

        this.password = (EditText) findViewById(R.id.cnu_password);
        this.password.addTextChangedListener(textWatcher);

        this.about = (EditText) findViewById(R.id.cnu_about);
        this.registerButton = (Button) findViewById(R.id.cnu_register);
        this.registerButton.setOnClickListener(this);
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
            case R.id.cnu_birthday : {
                this.onBirthdayClick();
                break;
            }
            case R.id.cnu_register : {
                this.onRegisterClick();
                break;
            }
        }
    }

    private void onRegisterClick() {
        UserSecretInfo userSecretInfo = new UserSecretInfo();
        userSecretInfo.setPassword(this.password.getText().toString());

        final MyUser user = new MyUser();
        user.setDisplayName(this.name.getText().toString());
        user.setUserName(this.username.getText().toString());
        user.setEmail(this.email.getText().toString());

        try {
            user.setBirthDate(this.getDateFormat().parse(this.birthDay.getText().toString()));
        } catch (ParseException e) {

        }

        Integer gender = 0;
        if (this.maleButton.isChecked()) {
            gender = 1;
        } else if (this.femaleButton.isChecked()) {
            gender = 2;
        }
        user.setGender(gender);
        user.setAbout(this.about.getText().toString());

        BaseViewModel.EverliveAPP.workWith().
                users(MyUser.class).
                create(user, userSecretInfo).
                executeAsync(new RequestResultCallbackAction() {
                    @Override
                    public void invoke(RequestResult requestResult) {
                        final String message;
                        final boolean hasErrors;
                        if (requestResult.getSuccess()) {
                            message = "User " + user.getDisplayName() + " created successfully.";
                            hasErrors = false;
                        } else {
                            message = requestResult.getError().getMessage();
                            hasErrors = true;
                        }
                        CreateNewUserActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.showAlertMessage(CreateNewUserActivity.this, message, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!hasErrors) {
                                            Intent i = new Intent(CreateNewUserActivity.this, LoginActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    }

    private void onBirthdayClick() {
        displayDatePicker();
    }

    private void displayDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {

                DateFormat dateFormat = getDateFormat();

                birthDay.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

        new DatePickerDialog(CreateNewUserActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private DateFormat getDateFormat() {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");;
        }
        return dateFormat;
    }

    private void updateRegisterButton() {
        if (this.name.getText().length() > 0 &&
            this.username.getText().length() > 0 &&
            this.password.getText().length() > 0) {
            this.registerButton.setEnabled(true);
        } else {
            this.registerButton.setEnabled(false);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            updateRegisterButton();
        }
    }
}
