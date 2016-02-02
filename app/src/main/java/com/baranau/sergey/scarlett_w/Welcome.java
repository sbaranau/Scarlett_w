package com.baranau.sergey.scarlett_w;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.baranau.sergey.scarlett_w.Entity.ParamsEntity;
import com.baranau.sergey.scarlett_w.Entity.UserEntity;
import com.baranau.sergey.scarlett_w.Global.GlobalVars;
import com.baranau.sergey.scarlett_w.dao.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Welcome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HashMap<Integer, String> user;
    DataBaseHelper dataBaseHelper;
    ParamsEntity lastParamsEntity = new ParamsEntity();
    SimpleDateFormat formatterForInt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    int todaySql = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            todaySql = Integer.parseInt(formatterForInt.format(new Date()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setContentView(R.layout.activity_welcom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(Welcome.this);

      //  dataBaseHelper.deleteAllUsers();


        user = dataBaseHelper.getUsersNames();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDateTimeString = df.format(new Date());
        ((TextView)findViewById(R.id.welcom_date)).setText(String.format("Today: %s", currentDateTimeString));
        final AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
        if (user == null || user.isEmpty() || user.size() == 0) {
            builder.setTitle(R.string.welcom);
            builder.setMessage("Need to register");
            builder.setPositiveButton("Registration", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    showUserDialog(null);
                }
            });

        } else if (user.size() >= 2) {
            builder.setTitle(R.string.select_user);
            final String[] names = new String[user.size()];
            int i = 0;
            for (int key : user.keySet()) {
                names[i] = user.get(key);
                i++;
            }
            builder.setItems(names, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    GlobalVars.getInstance().setName(names[which]);
                    for (int key : user.keySet()) {
                        if (user.get(key).equals(names[which])) {
                            GlobalVars.getInstance().setId(key);
                            dialog.cancel();
                            updateTitleInfo();
                            updateParameters();
                            break;
                        }
                    }
                }
            });
            builder.setPositiveButton("New User", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    showUserDialog(null);
                    dialog.cancel();
                }
            });
        } else {
            builder.setTitle(R.string.welcom);
            Object[] keys = user.keySet().toArray();
            int key = (int) keys[0];
            builder.setMessage("Hello " + user.get(key));
            GlobalVars.getInstance().setId(key);
            GlobalVars.getInstance().setName(user.get(key));
            builder.setPositiveButton("Yes, It's me", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    updateTitleInfo();
                    updateParameters();
                }
            });
            builder.setNegativeButton("New User", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    showUserDialog(null);
                }
            });
        }

        builder.create().show();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/http://baraholka.onliner.by/viewtopic.php?t=16950848
                if (lastParamsEntity == null || lastParamsEntity.getDate() == 0) {
                    lastParamsEntity = dataBaseHelper.getLastParams(GlobalVars.getInstance().getId());
                }
                if (lastParamsEntity.getDate() == todaySql) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                    builder.setTitle("Attention!!!");
                    builder.setMessage("You have record for today. Do You want override it?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Yes, I want", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showParametersDialog();
                        }
                    });
                } else {
                    showParametersDialog();
                }


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            UserEntity user = dataBaseHelper.getUser(GlobalVars.getInstance().getId());
            showUserDialog(user);
        } else if (id == R.id.users) {

        } else if (id == R.id.statistic) {

        } else if (id == R.id.tools) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean showParametersDialog() {

        final LayoutInflater inflater = (Welcome.this).getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.enter_params_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Welcome.this);
        alertDialogBuilder.setView(promptsView);
        final EditText kg = (EditText) promptsView.findViewById(R.id.kg);
        final EditText fat = (EditText) promptsView.findViewById(R.id.fat);
        final EditText tdw = (EditText) promptsView.findViewById(R.id.tdw);
        final EditText muscle = (EditText) promptsView.findViewById(R.id.mishci);
        final EditText bones = (EditText) promptsView.findViewById(R.id.bones);
        final EditText kcal = (EditText) promptsView.findViewById(R.id.kcal);
        final EditText bmi = (EditText) promptsView.findViewById(R.id.bmi);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.ok,null)
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = true;
                ParamsEntity paramsEntity = new ParamsEntity();
                paramsEntity.setUserId(GlobalVars.getInstance().getId());
                String param;
                if ((param = kg.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setWeight(Float.parseFloat(param));
                        if (paramsEntity.getWeight() <= 0) {
                            kg.setError(String.format("I You really %s kg?", paramsEntity.getWeight()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        kg.setError(String.format("I You really %s kg?", paramsEntity.getWeight()));
                        isOk = false;
                    }
                }
                if ((param = fat.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setFat(Float.parseFloat(param));

                        if (paramsEntity.getFat() <= 0 || paramsEntity.getFat() > 100) {
                            fat.setError(String.format("Your FAT is really %s%%?", paramsEntity.getFat()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        fat.setError(String.format("Your FAT is really %s%%?", paramsEntity.getFat()));
                        isOk = false;
                    }
                }
                if ((param = tdw.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setTdw(Float.parseFloat(param));
                        if (paramsEntity.getTdw() <= 0 || paramsEntity.getTdw() > 100) {
                            tdw.setError(String.format("Your TDW is really %s%%?", paramsEntity.getTdw()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        tdw.setError(String.format("Your TDW is really %s%%?", paramsEntity.getTdw()));
                        isOk = false;
                    }
                }
                if ((param = muscle.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setMuscle(Float.parseFloat(param));
                        if (paramsEntity.getMuscle() <= 0 || paramsEntity.getMuscle() > 100) {
                            muscle.setError(String.format("Your Muscles is really %s%%?", paramsEntity.getMuscle()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        muscle.setError(String.format("Your Muscles is really %s%%?", paramsEntity.getMuscle()));
                        isOk = false;
                    }
                }
                if ((param = bones.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setBones(Float.parseFloat(param));
                        if (paramsEntity.getBones() <= 0 || paramsEntity.getBones() >=  paramsEntity.getWeight()) {
                            bones.setError(String.format("Your Bones is really %skg?", paramsEntity.getBones()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        bones.setError(String.format("Your Bones is really %skg?", paramsEntity.getBones()));
                        isOk = false;
                    }
                }
                if ((param = kcal.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setKcal(Float.parseFloat(param));
                        if (paramsEntity.getKcal() <= 0) {
                            kcal.setError(String.format("Your really needed %skcal?", paramsEntity.getKcal()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        kcal.setError(String.format("Your really needed %skcal?", paramsEntity.getKcal()));
                        isOk = false;
                    }
                }
                if ((param = bmi.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",",".");
                        paramsEntity.setBmi(Float.parseFloat(param));
                        if (paramsEntity.getBmi() <= 0) {
                            bmi.setError(String.format("Your BMI is really %s?", paramsEntity.getBmi()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        bmi.setError(String.format("Your BMI is really %s?", paramsEntity.getBmi()));
                        isOk = false;
                    }
                }
                if (isOk) {
                    if (dataBaseHelper.addParams(paramsEntity)) {
                        alertDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                        builder.setTitle(R.string.congratulations);
                        if (lastParamsEntity == null || lastParamsEntity.getWeight() == 0) {
                            builder.setMessage("Nice! But it's only fist step. Hope to see you tomorrow!");
                        } else {

                        }

                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                updateTitleInfo();
                                updateParameters();
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
            }
        });
        return true;
    }

    private boolean showUserDialog(UserEntity userEntity) {
        final LayoutInflater inflater = (Welcome.this).getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.enter_params_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Welcome.this);
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
        ArrayAdapter adapter = new ArrayAdapter<Integer>(Welcome.this,
                android.R.layout.simple_spinner_dropdown_item, ages);
        userAge.setAdapter(adapter);
        if (userEntity == null) {
            userEntity = new UserEntity();
            String lastDate = "Last weighing: Never.Lets do it now!!!!";
            findViewById(R.id.welcom_values_title).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.welcom_use_last_enter)).setText(lastDate);
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
                                if (finalUserEntity.getId() <0 ) {
                                    long idb = dataBaseHelper.addUser(finalUserEntity);
                                    if (idb > 0) {
                                        GlobalVars.getInstance().setId(idb);
                                        dialog.cancel();
                                    }
                                } else {
                                    dataBaseHelper.updateUser(finalUserEntity);
                                }
                                GlobalVars.getInstance().setName(userName.getText().toString().trim());
                                updateTitleInfo();
                            }
                        }
                    });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    private boolean updateTitleInfo() {
        ((TextView)findViewById(R.id.welcom_user_hello)).setText("Welcome " + GlobalVars.getInstance().getName());
        UserEntity userEntity = dataBaseHelper.getUser(GlobalVars.getInstance().getId());
        ((TextView)findViewById(R.id.welcom_age)).setText("You age: " + userEntity.getAge());
        ((TextView)findViewById(R.id.welcom_height)).setText("Height: " + userEntity.getHeight());
        ((TextView)findViewById(R.id.welcom_gender)).setText("Gender:  " + userEntity.getGenderValue());
        return true;
    }

    private void updateParameters() {
        lastParamsEntity = dataBaseHelper.getLastParams(GlobalVars.getInstance().getId());
        String lastDate;
        if (lastParamsEntity.getDate() > 0) {
            findViewById(R.id.welcom_values_title).setVisibility(View.VISIBLE);

            lastDate = "Last weighing: " + (lastParamsEntity.getDate()%100)
                    + "." + (lastParamsEntity.getDate() - lastParamsEntity.getDate()%100)%10000
                    + "." + (lastParamsEntity.getDate()/10000);
            ((TextView)findViewById(R.id.welcom_kg_last)).setText(String.format("%s kg", lastParamsEntity.getWeight()));
            ((TextView)findViewById(R.id.welcom_fat_last)).setText(String.format("%s %%", lastParamsEntity.getFat()));
            ((TextView)findViewById(R.id.welcom_tdw_last)).setText(String.format("%s %%", lastParamsEntity.getTdw()));
            ((TextView)findViewById(R.id.welcom_musculs_last)).setText(String.format("%s %%", lastParamsEntity.getMuscle()));
            ((TextView)findViewById(R.id.welcom_bones_last)).setText(String.format("%s kg", lastParamsEntity.getBones()));
            ((TextView)findViewById(R.id.welcom_kcal_last)).setText(String.format("%s kCal", lastParamsEntity.getKcal()));
            ((TextView)findViewById(R.id.welcom_bmi_last)).setText(String.format("%s", lastParamsEntity.getBmi()));
        } else {
            lastDate = "Last weighing: Never. Do it now!!!!";
            findViewById(R.id.welcom_values_title).setVisibility(View.INVISIBLE);
        }
        ((TextView)findViewById(R.id.welcom_use_last_enter)).setText(lastDate);
    }

    private boolean updateDelta(ParamsEntity newParams) {

      //  ParamsEntity lastParams = dataBaseHelper.getLastParams()
        return true;
    }

}
