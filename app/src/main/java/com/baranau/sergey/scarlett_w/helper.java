package com.baranau.sergey.scarlett_w;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.baranau.sergey.scarlett_w.Entity.UserEntity;
import com.baranau.sergey.scarlett_w.dao.DataBaseHelper;

/**
 * Created by sergey on 2/10/16.
 */
public class Helper {

    public static boolean showUserDialog(UserEntity userEntity, final Activity activity) {

        final DataBaseHelper dataBaseHelper;
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(activity);
        final LayoutInflater inflater = (activity).getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.enter_params_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);

        final EditText userName = (EditText) promptsView.findViewById(R.id.user_name);
        final EditText userHeight = (EditText) promptsView.findViewById(R.id.user_height);
        final RadioButton userMan = (RadioButton) promptsView.findViewById(R.id.user_man);
        final RadioButton userWoman = (RadioButton) promptsView.findViewById(R.id.user_female);
        final Spinner userAge = (Spinner) promptsView.findViewById(R.id.user_age);
        Integer[] ages = new Integer[90];
        for (int i = 1; i < 90; i++) {
            ages[ i-1 ] = i;
        }
        ArrayAdapter adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_dropdown_item, ages);
        userAge.setAdapter(adapter);
        if (userEntity == null) {
            userEntity = new UserEntity();
            activity.findViewById(R.id.welcom_values_title).setVisibility(View.INVISIBLE);
        }
        userName.setText(userEntity.getName());
        userHeight.setText(String.format("%d", userEntity.getHeight()));
        userMan.setChecked(userEntity.getGender() == 1);
        userWoman.setChecked(userEntity.getGender() == 0);
        userAge.setSelection(userEntity.getAge() - 1);
        final UserEntity finalUserEntity = userEntity;
        userAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                finalUserEntity.setAge(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean isOk = true;
                                try {
                                    finalUserEntity.setHeight(Integer.parseInt(userHeight.getText().toString()));
                                } catch (Exception ex) {
                                    userHeight.setError("Enter right height");
                                    isOk = false;
                                }
                                if (userName.getText().length() > 0) {
                                    finalUserEntity.setName(userName.getText().toString());
                                } else {
                                    isOk = false;
                                }
                                if (userMan.isChecked()) {
                                    finalUserEntity.setGender(1);
                                } else if (userWoman.isChecked()) {
                                    finalUserEntity.setGender(0);
                                }
                                if (isOk) {
                                    if (finalUserEntity.getId() < 0) {
                                        long idb = dataBaseHelper.addUser(finalUserEntity);
                                        if (idb > 0) {
                                          //TODO  GlobalVars.getInstance().setId(idb);
                                            dialog.cancel();
                                        }
                                    } else {
                                        dataBaseHelper.updateUser(finalUserEntity);
                                    }
                                  //TODO  GlobalVars.getInstance().setName(userName.getText().toString().trim());
                                }
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }


}
