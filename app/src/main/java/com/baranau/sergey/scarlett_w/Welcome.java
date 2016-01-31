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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(Welcome.this);
       // dataBaseHelper.deleteAllUsers();
        user = dataBaseHelper.getUser();
        final AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
        if (user == null || user.isEmpty() || user.size() == 0) {
            builder.setTitle(R.string.welcom);
            builder.setMessage("Need to register");
            builder.setPositiveButton("Registration", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    addUser();
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
                            break;
                        }
                    }
                }
            });
            builder.setPositiveButton("New User", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    addUser();
                    dialog.cancel();
                }
            });
        } else {
            builder.setTitle(R.string.welcom);
            Object[] keys = user.keySet().toArray();
            int key = (int) keys[0];
            builder.setMessage("Welcome " + user.get(key));
            GlobalVars.getInstance().setId(key);
            GlobalVars.getInstance().setName(user.get(key));
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    updateTitleInfo();
                }
            });
            builder.setNegativeButton("New User", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    addUser();
                }
            });
        }

        builder.create().show();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
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
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        boolean isOk = true;
                                        ParamsEntity paramsEntity = new ParamsEntity();
                                        paramsEntity.setUserId(GlobalVars.getInstance().getId());
                                        if (kg.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(kg.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                kg.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (fat.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(fat.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                fat.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (tdw.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(tdw.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                tdw.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (muscle.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(muscle.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                muscle.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (bones.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(bones.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                bones.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (kcal.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(kcal.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                kcal.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (bmi.getText().length() > 0) {
                                            try {
                                                paramsEntity.setWeight(Float.parseFloat(bmi.getText().toString()));
                                            } catch (NumberFormatException ex) {
                                                bmi.setError("Check value");
                                                isOk = false;
                                            }
                                        }
                                        if (isOk) {
                                            dataBaseHelper.addParams(paramsEntity);

                                            dialog.cancel();
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean addUser() {
        final LayoutInflater inflater = (Welcome.this).getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.enter_params_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Welcome.this);
        alertDialogBuilder.setView(promptsView);
        final UserEntity userEntity = new UserEntity();
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
        userAge.setSelection(10);
        userEntity.setAge(11);
        userAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                userEntity.setAge(position + 1);
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
                                userEntity.setHeight(Integer.parseInt(userHeight.getText().toString()));
                            } catch (Exception ex) {
                                userHeight.setError("Enter right height");
                                isOk = false;
                            }
                            if (userName.getText().length() > 0) {
                                userEntity.setName(userName.getText().toString());
                            } else {
                                isOk = false;
                            }
                            if (userMan.isChecked()) {
                                userEntity.setGender(1);
                            } else if (userWoman.isChecked()) {
                                userEntity.setGender(0);
                            }
                            if (isOk) {
                                long idb = dataBaseHelper.addUser(userEntity);
                                if (idb > 0) {
                                    GlobalVars.getInstance().setId(idb);
                                    dialog.cancel();
                                    updateTitleInfo();
                                }

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

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDateTimeString = df.format(new Date());
        ((TextView)findViewById(R.id.welcom_date)).setText(String.format("Today: %s", currentDateTimeString));
        lastParamsEntity = dataBaseHelper.getLastParams(GlobalVars.getInstance().getId());
        String lastDate;
        if (lastParamsEntity.getDate() > 0) {
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
        }
        ((TextView)findViewById(R.id.welcom_gender)).setText(lastDate);
        return true;
    }

    private boolean updateDelta(ParamsEntity newParams) {
        return true;
    }

}
