package com.baranau.sergey.scarlett_w;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.baranau.sergey.scarlett_w.Entity.Emotions;
import com.baranau.sergey.scarlett_w.Entity.ParamsEntity;
import com.baranau.sergey.scarlett_w.Entity.UserEntity;
import com.baranau.sergey.scarlett_w.Global.GlobalFunc;
import com.baranau.sergey.scarlett_w.Global.GlobalVars;
import com.baranau.sergey.scarlett_w.dao.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class Welcome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DataBaseHelper dataBaseHelper;
    private ParamsEntity previousParamsEntity = new ParamsEntity();
    private ParamsEntity todayParamsEntity = new ParamsEntity();
    private ParamsEntity deltaParamsEntity = new ParamsEntity();
    private SimpleDateFormat formatterForInt = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    private int todaySql = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            formatterForInt.setTimeZone(TimeZone.getTimeZone("UTC"));
            todaySql = Integer.parseInt(formatterForInt.format(new Date()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setContentView(R.layout.activity_welcom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataBaseHelper = DataBaseHelper.getDataBaseHelper(Welcome.this);

        dataBaseHelper.addTestParams(2);

        final HashMap<Integer, String> user = dataBaseHelper.getUsersNames();
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
                            updateTitleInfo(true);
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
                    dataBaseHelper.deleteAllParams();
                    dataBaseHelper.addTestParams(GlobalVars.getInstance().getId());
                    dialog.cancel();
                    updateTitleInfo(true);
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
                        .setAction("Action", null).show();*/
                if (todayParamsEntity == null || todayParamsEntity.getDate() == 0) {
                    todayParamsEntity = dataBaseHelper.getLastParams(GlobalVars.getInstance().getId());
                }
                if (todayParamsEntity.getDate() == todaySql) {
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
                            showParametersDialog(todayParamsEntity, new ArrayList<Integer>());
                        }
                    });
                    builder.create().show();
                } else {
                    showParametersDialog(null, new ArrayList<Integer>());
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

    private boolean showParametersDialog(ParamsEntity paramsEntity, final ArrayList<Integer> dates) {
        final int[] dateIndex = {0}; // use for show date position in date list
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
        final CalendarView datePicker = (CalendarView) promptsView.findViewById(R.id.welcom_calendar);
        final CheckBox setDateCheckBox = (CheckBox) promptsView.findViewById(R.id.welcom_change_date);
        final TextView showCurrentDate = ((TextView)promptsView.findViewById(R.id.welcom_show_date));

        final Button prevButton = ((Button)promptsView.findViewById(R.id.prev_but));
        final Button nextButton = ((Button)promptsView.findViewById(R.id.next_button));

        showCurrentDate.setText("Params for today");
        setDateCheckBox.setChecked(false);

        datePicker.setVisibility(View.INVISIBLE);
        if (paramsEntity == null || paramsEntity.getWeight() == 0) {
            paramsEntity = new ParamsEntity();
            paramsEntity.setDate(todaySql);
        } else {
            kg.setText(String.format("%s", paramsEntity.getWeight()));
            fat.setText(String.format("%s", paramsEntity.getFat()));
            tdw.setText(String.format("%s", paramsEntity.getTdw()));
            muscle.setText(String.format("%s", paramsEntity.getMuscle()));
            bones.setText(String.format("%s", paramsEntity.getBones()));
            bmi.setText(String.format("%s", paramsEntity.getBmi()));
            kcal.setText(String.format("%s", paramsEntity.getKcal()));
        }
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
        final ParamsEntity finalParamsEntity = paramsEntity;
        setDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    datePicker.setVisibility(View.VISIBLE);
                    showCurrentDate.setVisibility(View.GONE);
                } else {
                    datePicker.setVisibility(View.GONE);
                    showCurrentDate.setVisibility(View.VISIBLE);
                    showCurrentDate.setText("Params for today");
                    finalParamsEntity.setDate(todaySql);
                }
            }
        });
        if (dates.size() == 1) {
            nextButton.setEnabled(false);
            prevButton.setEnabled(false);
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kg.getText().length() > 0
                        || bmi.getText().length() > 0
                        || tdw.getText().length() > 0
                        || bones.getText().length() > 0
                        || muscle.getText().length() > 0
                        || fat.getText().length() > 0
                        || kcal.getText().length() > 0) {
                    try {
                        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(Welcome.this);
                        confirmDialog.setCancelable(false);
                        confirmDialog.setTitle("Warning!!!");
                        confirmDialog.setMessage("If You change date You will lost all unsaved parameters" +
                                " for date: "
                                + GlobalFunc.dateIntToString(dates.get(dateIndex[0])));
                        confirmDialog.setPositiveButton("Ok", null);
                        confirmDialog.setNegativeButton("Cancel", null);
                        final AlertDialog alertDialog1 = confirmDialog.create();
                        alertDialog1.show();
                        alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kg.setText("");
                                fat.setText("");
                                tdw.setText("");
                                muscle.setText("");
                                bones.setText("");
                                kcal.setText("");
                                bmi.setText("");
                                if (dateIndex[0] != dates.size() - 1) {
                                    dateIndex[0]++;
                                    prevButton.setEnabled(true);
                                    showCurrentDate.setText(GlobalFunc.dateIntToString(dates.get(dateIndex[0])));
                                    finalParamsEntity.setDate(dates.get(dateIndex[0]));
                                } else {
                                    nextButton.setEnabled(false);
                                }
                                alertDialog1.cancel();
                                alertDialog1.dismiss();
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (dateIndex[0] != dates.size() - 1) {
                        dateIndex[0]++;
                        prevButton.setEnabled(true);
                        showCurrentDate.setText(GlobalFunc.dateIntToString(dates.get(dateIndex[0])));
                        finalParamsEntity.setDate(dates.get(dateIndex[0]));
                    } else {
                        nextButton.setEnabled(false);
                    }
                }
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kg.getText().length() > 0
                        || bmi.getText().length() > 0
                        || tdw.getText().length() > 0
                        || bones.getText().length() > 0
                        || muscle.getText().length() > 0
                        || fat.getText().length() > 0
                        || kcal.getText().length() > 0) {
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(Welcome.this);
                    confirmDialog.setTitle("Warning!!!");
                    confirmDialog.setMessage("If You change date You will lost all unsaved parameters" +
                            " for date: "
                            + GlobalFunc.dateIntToString(dates.get(dateIndex[0])));
                    confirmDialog.setPositiveButton("Ok", null);
                    confirmDialog.setNegativeButton("Cancel", null);
                    final AlertDialog alertDialog1 = confirmDialog.create();
                    alertDialog1.show();
                    alertDialog1.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            kg.setText("");
                            fat.setText("");
                            tdw.setText("");
                            muscle.setText("");
                            bones.setText("");
                            kcal.setText("");
                            bmi.setText("");
                            if (dateIndex[0] != 0) {
                                nextButton.setEnabled(true);
                                dateIndex[0]--;
                                showCurrentDate.setText(GlobalFunc.dateIntToString(dates.get(dateIndex[0])));
                                finalParamsEntity.setDate(dates.get(dateIndex[0]));
                            } else {
                                prevButton.setEnabled(false);
                            }
                            alertDialog1.cancel();
                            alertDialog1.dismiss();
                        }
                    });
                    alertDialog1.show();
                } else {
                    if (dateIndex[0] != 0) {
                        nextButton.setEnabled(true);
                        dateIndex[0]--;
                        showCurrentDate.setText(GlobalFunc.dateIntToString(dates.get(dateIndex[0])));
                        finalParamsEntity.setDate(dates.get(dateIndex[0]));
                    } else {
                        prevButton.setEnabled(false);
                    }
                }


            }
        });
        prevButton.setEnabled(false);
        datePicker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int date = year * 10000 + (month + 1) * 100 + dayOfMonth;
                ParamsEntity oldParamsEntity = dataBaseHelper.getParamsByDate(GlobalVars.getInstance().getId(), date);
                if (oldParamsEntity.getDate() > 0 || oldParamsEntity.getWeight() == 0) {
                    kg.setText(String.format("%s", oldParamsEntity.getWeight()));
                    fat.setText(String.format("%s", oldParamsEntity.getFat()));
                    tdw.setText(String.format("%s", oldParamsEntity.getTdw()));
                    muscle.setText(String.format("%s", oldParamsEntity.getMuscle()));
                    bones.setText(String.format("%s", oldParamsEntity.getBones()));
                    bmi.setText(String.format("%s", oldParamsEntity.getBmi()));
                    kcal.setText(String.format("%s", oldParamsEntity.getKcal()));
                }
                finalParamsEntity.setDate(date);
            }


        });

        datePicker.setMaxDate(new Date().getTime());
        if (dates.size() == 0) {
            prevButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            // if user want to add missing values
            setDateCheckBox.setVisibility(View.INVISIBLE);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("Save");
            finalParamsEntity.setDate(dates.get(0));
            showCurrentDate.setText(GlobalFunc.dateIntToString(dates.get(0)));
        }
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOk = true;

                finalParamsEntity.setUserId(GlobalVars.getInstance().getId());
                String param;
                if ((param = kg.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setWeight(Float.parseFloat(param));
                        if (finalParamsEntity.getWeight() <= 0) {
                            kg.setError(String.format("I You really %s kg?", finalParamsEntity.getWeight()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        kg.setError(String.format("I You really %s kg?", finalParamsEntity.getWeight()));
                        isOk = false;
                    }
                } else {
                    kg.setError(String.format("I You really %s kg?", finalParamsEntity.getWeight()));
                }
                if ((param = fat.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setFat(Float.parseFloat(param));

                        if (finalParamsEntity.getFat() <= 0 || finalParamsEntity.getFat() > 100) {
                            fat.setError(String.format("Your FAT is really %s%%?", finalParamsEntity.getFat()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        fat.setError(String.format("Your FAT is really %s%%?", finalParamsEntity.getFat()));
                        isOk = false;
                    }
                } else {
                    fat.setError("Please, set value");
                    isOk = false;
                }
                if ((param = tdw.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setTdw(Float.parseFloat(param));
                        if (finalParamsEntity.getTdw() <= 0 || finalParamsEntity.getTdw() > 100) {
                            tdw.setError(String.format("Your TDW is really %s%%?", finalParamsEntity.getTdw()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        tdw.setError(String.format("Your TDW is really %s%%?", finalParamsEntity.getTdw()));
                        isOk = false;
                    }
                } else {
                    tdw.setError("Please, set value");
                    isOk = false;
                }
                if ((param = muscle.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setMuscle(Float.parseFloat(param));
                        if (finalParamsEntity.getMuscle() <= 0 || finalParamsEntity.getMuscle() > 100) {
                            muscle.setError(String.format("Your Muscles is really %s%%?", finalParamsEntity.getMuscle()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        muscle.setError(String.format("Your Muscles is really %s%%?", finalParamsEntity.getMuscle()));
                        isOk = false;
                    }
                } else {
                    muscle.setError("Please, set value");
                    isOk = false;
                }
                if ((param = bones.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setBones(Float.parseFloat(param));
                        if (finalParamsEntity.getBones() <= 0 || finalParamsEntity.getBones() >= finalParamsEntity.getWeight()) {
                            bones.setError(String.format("Your Bones is really %skg?", finalParamsEntity.getBones()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        bones.setError(String.format("Your Bones is really %skg?", finalParamsEntity.getBones()));
                        isOk = false;
                    }
                } else {
                    bones.setError("Please, set value");
                    isOk = false;
                }
                if ((param = kcal.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setKcal(Float.parseFloat(param));
                        if (finalParamsEntity.getKcal() <= 0) {
                            kcal.setError(String.format("Your really needed %skcal?", finalParamsEntity.getKcal()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        kcal.setError(String.format("Your really needed %skcal?", finalParamsEntity.getKcal()));
                        isOk = false;
                    }
                } else {
                    kcal.setError("Please, set value");
                    isOk = false;
                }
                if ((param = bmi.getText().toString()).length() > 0) {
                    try {
                        param = param.replaceAll(",", ".");
                        finalParamsEntity.setBmi(Float.parseFloat(param));
                        if (finalParamsEntity.getBmi() <= 0) {
                            bmi.setError(String.format("Your BMI is really %s?", finalParamsEntity.getBmi()));
                            isOk = false;
                        }
                    } catch (NumberFormatException ex) {
                        bmi.setError(String.format("Your BMI is really %s?", finalParamsEntity.getBmi()));
                        isOk = false;
                    }
                } else {
                    bmi.setError("Please, set value");
                    isOk = false;
                }
                if (isOk) {

                    if (dataBaseHelper.addParams(finalParamsEntity)) {
                        kg.setText("");
                        fat.setText("");
                        tdw.setText("");
                        muscle.setText("");
                        bones.setText("");
                        kcal.setText("");
                        bmi.setText("");
                        if (dates.size() <= 1) {
                            alertDialog.dismiss();
                        } else {
                            if (dates.size() > 1 && dateIndex[0] == dates.size() - 1) {
                                prevButton.callOnClick();
                            } else {
                                showCurrentDate.setText(GlobalFunc.dateIntToString(dates.get(dateIndex[0] + 1)));
                            }
                            dates.remove(dateIndex[0]);
                            if (dateIndex[0] == dates.size() - 1) {
                                nextButton.setEnabled(false);
                            } else if (dateIndex[0] == 0) {
                                prevButton.setEnabled(false);
                            } else {
                                nextButton.setEnabled(true);
                                prevButton.setEnabled(true);
                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                        builder.setTitle(R.string.congratulations);
                        updateTitleInfo(false);
                        updateParameters();
                        SpannableStringBuilder messageBuilder = new SpannableStringBuilder();
                        if (previousParamsEntity == null || previousParamsEntity.getWeight() == 0
                                || previousParamsEntity.getDate() == todaySql) {
                            builder.setTitle("Great!!!");
                            builder.setMessage("Nice! But it's only fist step. Hope to see you tomorrow!");
                        } else {
                            if (deltaParamsEntity.getWeight() < 0 && deltaParamsEntity.getFat() < 0) {
                                builder.setTitle("WAU-WAU-WAU!!!!");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    messageBuilder.append("You are looking so good!!!")
                                            .append(" ", new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_LAUGHING)), 0)
                                            .append(" You did it!!!");
                                } else {
                                    messageBuilder.append("You are looking so good!!!").append(" ");
                                    messageBuilder.setSpan(new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_LAUGHING)),
                                            messageBuilder.length() - 1, messageBuilder.length(), 0);
                                    messageBuilder.append(" You did it!!!");
                                }

                            } else if (deltaParamsEntity.getWeight() < 0 || deltaParamsEntity.getFat() < 0) {
                                builder.setTitle("Very Good!");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    messageBuilder.append(" Your results is quiet good")
                                            .append(" ", new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_LAUGHING)), 0)
                                            .append(" ");
                                } else {
                                    messageBuilder.append(" Your results is quiet good").append(" ");
                                    messageBuilder.setSpan(new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_LAUGHING)),
                                            messageBuilder.length() - 1, messageBuilder.length(), 0);
                                    messageBuilder.append(" ");
                                }

                            } else {
                                builder.setTitle("Ops");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    messageBuilder.append("Somebody eats cakes at night?")
                                            .append(" ", new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_CONFUSED)), 0)
                                            .append(" ");
                                } else {
                                    messageBuilder.append("Somebody eats cakes at night?").append(" ");
                                    messageBuilder.setSpan(new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_CONFUSED)),
                                            messageBuilder.length() - 1, messageBuilder.length(), 0);
                                    messageBuilder.append(" ");
                                }
                            }
                            builder.setMessage(messageBuilder);
                        }

                        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
        ArrayAdapter adapter = new ArrayAdapter<>(Welcome.this,
                android.R.layout.simple_spinner_dropdown_item, ages);
        userAge.setAdapter(adapter);
        if (userEntity == null) {
            userEntity = new UserEntity();
            findViewById(R.id.welcom_values_title).setVisibility(View.INVISIBLE);
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
                                        GlobalVars.getInstance().setId(idb);
                                        dialog.cancel();
                                    }
                                } else {
                                    dataBaseHelper.updateUser(finalUserEntity);
                                }
                                GlobalVars.getInstance().setName(userName.getText().toString().trim());
                                updateTitleInfo(false);
                            }
                        }
                    });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    private void showMissingDatesDialog(final UserEntity userEntity) {

        if (userEntity.getMissingDateReminder() == 0) {
            return;
        }
        final ArrayList<Integer> dates = (ArrayList<Integer>) dataBaseHelper.getEmptyDates(userEntity.getId());
        if (dates.size() == 0) {
            return;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Welcome.this);
        alertDialogBuilder
                .setCancelable(false)
                .setTitle(userEntity.getName())
                .setMessage("I've found that You forgot to enter weighting " +
                        "parameters for some dates. May be You enter them now?")
                .setNegativeButton("Later", null)
                .setNeutralButton("Don't show", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userEntity.setMissingDateReminder(0);
                        dataBaseHelper.updateUser(userEntity);
                    }
                })
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showParametersDialog(null, dates);
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private boolean updateTitleInfo(boolean showReminder) {
        ((TextView)findViewById(R.id.welcom_user_hello)).setText(String.format("Welcome %s", GlobalVars.getInstance().getName()));
        UserEntity userEntity = dataBaseHelper.getUser(GlobalVars.getInstance().getId());
        if (showReminder) {
            showMissingDatesDialog(userEntity);
        }
        GlobalVars.getInstance().setAge(userEntity.getAge());
        GlobalVars.getInstance().setGender(userEntity.getGender());
                ((TextView) findViewById(R.id.welcom_age)).setText(String.format("Your age: %d", userEntity.getAge()));
        ((TextView)findViewById(R.id.welcom_height)).setText(String.format("Height: %d", userEntity.getHeight()));
        ((TextView)findViewById(R.id.welcom_gender)).setText(String.format("Gender:  %s", userEntity.getGenderValue()));
        return true;
    }

    private void updateParameters() {
        previousParamsEntity = dataBaseHelper.getLastParams(GlobalVars.getInstance().getId());
        todayParamsEntity = dataBaseHelper.getParamsByDate(GlobalVars.getInstance().getId(), todaySql);
        if (previousParamsEntity.getDate() > 0 && todayParamsEntity.getDate() > 0) {
            findViewById(R.id.welcom_delta).setVisibility(View.VISIBLE);
            findViewById(R.id.welcom_new_date).setVisibility(View.VISIBLE);
            findViewById(R.id.welcom_values_title).setVisibility(View.VISIBLE);
            findViewById(R.id.welcom_use_last_enter).setVisibility(View.INVISIBLE);
            setNewParameters(todayParamsEntity);
            setPreviousParameters(previousParamsEntity);
            deltaParamsEntity = getParametersDelta(todayParamsEntity,previousParamsEntity);
            setDeltaParams(deltaParamsEntity);
        } else if (previousParamsEntity.getDate() > 0 && todayParamsEntity.getDate() == 0) {
            findViewById(R.id.welcom_delta).setVisibility(View.INVISIBLE);
            findViewById(R.id.welcom_new_date).setVisibility(View.INVISIBLE);
            findViewById(R.id.welcom_values_title).setVisibility(View.VISIBLE);
            findViewById(R.id.welcom_use_last_enter).setVisibility(View.INVISIBLE);
            setPreviousParameters(previousParamsEntity);
        } else {

            SpannableStringBuilder builder = new SpannableStringBuilder();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.append("Last weighing: Never ")
                        .append(" ", new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_CONFUSED)), 0)
                        .append(" Do it now!!!!");
            } else {
                builder.append("Last weighing: Never ").append(" ");
                builder.setSpan(new ImageSpan(Welcome.this, Emotions.getSmileyResource(Emotions.CAT_CONFUSED)),
                        builder.length() - 1, builder.length(), 0);
                builder.append(" Do it now!!!!");
            }

            findViewById(R.id.welcom_values_title).setVisibility(View.INVISIBLE);
            findViewById(R.id.welcom_prev_date).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView21).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView22).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView23).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView24).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView25).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView26).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView27).setVisibility(View.INVISIBLE);

            findViewById(R.id.welcom_delta).setVisibility(View.INVISIBLE);
            findViewById(R.id.welcom_new_date).setVisibility(View.INVISIBLE);
            findViewById(R.id.welcom_delta).setVisibility(View.INVISIBLE);
            (findViewById(R.id.welcom_use_last_enter)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.welcom_use_last_enter)).setText(builder);

        }

    }

    private ParamsEntity getParametersDelta(ParamsEntity todayParamsEntity, ParamsEntity previousParamsEntity) {
        ParamsEntity deltaParamsEntity = new ParamsEntity();
        deltaParamsEntity.setKcal(todayParamsEntity.getKcal() - previousParamsEntity.getKcal());
        deltaParamsEntity.setBones(todayParamsEntity.getBones() - previousParamsEntity.getBones());
        deltaParamsEntity.setWeight(todayParamsEntity.getWeight() - previousParamsEntity.getWeight());
        deltaParamsEntity.setMuscle(todayParamsEntity.getMuscle() - previousParamsEntity.getMuscle());
        deltaParamsEntity.setBmi(todayParamsEntity.getBmi() - previousParamsEntity.getBmi());
        deltaParamsEntity.setFat(todayParamsEntity.getFat() - previousParamsEntity.getFat());
        deltaParamsEntity.setTdw(todayParamsEntity.getTdw() - previousParamsEntity.getTdw());
        return deltaParamsEntity;
    }


    private void setPreviousParameters(ParamsEntity paramsEntity) {
        String lastDate = (GlobalFunc.dateIntToString(paramsEntity.getDate()));
        ((TextView)findViewById(R.id.welcom_prev_date)).setText(lastDate);
        ((TextView)findViewById(R.id.welcom_kg_last)).setText(String.format("%s", paramsEntity.getWeight()));
        ((TextView)findViewById(R.id.welcom_fat_last)).setText(String.format("%s", paramsEntity.getFat()));
        setFatColor(((TextView) findViewById(R.id.welcom_fat_last)), paramsEntity.getFat(), paramsEntity.getTdw());
        ((TextView)findViewById(R.id.welcom_tdw_last)).setText(String.format("%s", paramsEntity.getTdw()));
        ((TextView)findViewById(R.id.welcom_musculs_last)).setText(String.format("%s", paramsEntity.getMuscle()));
        ((TextView)findViewById(R.id.welcom_bones_last)).setText(String.format("%s", paramsEntity.getBones()));
        ((TextView)findViewById(R.id.welcom_kcal_last)).setText(String.format("%s", paramsEntity.getKcal()));
        ((TextView)findViewById(R.id.welcom_bmi_last)).setText(String.format("%s", paramsEntity.getBmi()));
        setFatColor(((TextView) findViewById(R.id.welcom_bmi_last)), paramsEntity.getBmi());
    }

    private void setNewParameters(ParamsEntity paramsEntity) {
        String lastDate = GlobalFunc.dateIntToString(paramsEntity.getDate());
        ((TextView) findViewById(R.id.welcom_new_date)).setText(lastDate);
        ((TextView)findViewById(R.id.welcom_kg_new)).setText(String.format("%s", paramsEntity.getWeight()));
        setFatColor(((TextView) findViewById(R.id.welcom_fat_new)), paramsEntity.getFat(), paramsEntity.getTdw());
        ((TextView)findViewById(R.id.welcom_fat_new)).setText(String.format("%s", paramsEntity.getFat()));
        ((TextView)findViewById(R.id.welcom_tdw_new)).setText(String.format("%s", paramsEntity.getTdw()));
        ((TextView)findViewById(R.id.welcom_musculs_new)).setText(String.format("%s", paramsEntity.getMuscle()));
        ((TextView)findViewById(R.id.welcom_bones_new)).setText(String.format("%s", paramsEntity.getBones()));
        ((TextView)findViewById(R.id.welcom_kcal_new)).setText(String.format("%s", paramsEntity.getKcal()));
        ((TextView)findViewById(R.id.welcom_bmi_new)).setText(String.format("%s", paramsEntity.getBmi()));
        setFatColor(((TextView)findViewById(R.id.welcom_bmi_new)),paramsEntity.getBmi());
    }

    private void setFatColor(TextView viewById, float fat, float water) {
        if (GlobalVars.getInstance().getGender() == 0 ) {   //for woman
            if (GlobalVars.getInstance().getAge() <= 30) {
                if (fat > 30.5 && water < 47) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (fat > 25 && water < 51.5) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.orange));
                } else if (fat > 20.5 && water < 54.6) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (fat > 16 && water < 57.7) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.lite_blue));
                } else if (fat > 4 && water < 66) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            } else {
                if (fat > 35 && water < 44.6) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (fat > 30 && water < 48) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.orange));
                } else if (fat > 25 && water < 51.5) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (fat > 20 && water < 54.9) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.lite_blue));
                } else if (fat > 4 && water < 66) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }
        } else {                                            //for man
            if (GlobalVars.getInstance().getAge() <= 30) {
                if (fat > 24.6 && water < 51.8) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (fat > 20.1 && water < 54.9) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.orange));
                } else if (fat > 15.6 && water < 58.0) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (fat > 11.1 && water < 61.1) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.lite_blue));
                } else if (fat > 4 && water < 66) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            } else {
                if (fat > 28.6 && water < 49.1) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.red));
                } else if (fat > 24.1 && water < 52.3) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.orange));
                } else if (fat > 19.6 && water < 58.2) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.green));
                } else if (fat > 15.1 && water < 58.3) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.lite_blue));
                } else if (fat > 4 && water < 66) {
                    viewById.setBackgroundColor(getResources().getColor(R.color.blue));
                }
            }
        }
    }


    private void setFatColor(TextView view, float fat) {
        if (fat >= 40) {
            view.setBackgroundColor(getResources().getColor(R.color.dark_red));
        } else if (fat >= 35) {
            view.setBackgroundColor(getResources().getColor(R.color.red));
        } else if (fat >= 30) {
            view.setBackgroundColor(getResources().getColor(R.color.orange));
        } else if (fat >= 25) {
            view.setBackgroundColor(getResources().getColor(R.color.yellow));
        } else if (fat >= 18.5) {
            view.setBackgroundColor(getResources().getColor(R.color.green));
        } else if (fat >= 16) {
            view.setBackgroundColor(getResources().getColor(R.color.lite_blue));
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.blue));
        }
    }

    private void setDeltaParams(ParamsEntity paramsEntity) {
      //  ((TextView)findViewById(R.id.welcom_new_date)).setText(lastDate);
        ((TextView)findViewById(R.id.welcom_kg_delta)).setText(String.format("%s", paramsEntity.getWeight()));
        if (paramsEntity.getWeight() > 1) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_kg_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.VERY_VERY_SAD), 0);
        } else if (paramsEntity.getWeight() > 0.5) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_kg_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.VERY_SAD), 0);
        } else if (paramsEntity.getWeight() > 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_kg_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.SAD), 0);
        } else if (paramsEntity.getWeight() == 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_kg_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.NORMAL), 0);
        } else if (paramsEntity.getWeight() < 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView)findViewById(R.id.welcom_kg_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.HAPPY), 0);
        }
        ((TextView)findViewById(R.id.welcom_fat_delta)).setText(String.format("%s", paramsEntity.getFat()));
        if (paramsEntity.getFat() > 1) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_fat_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.VERY_VERY_SAD), 0);
        } else if (paramsEntity.getFat() > 0.5) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_fat_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.VERY_SAD), 0);
        } else if (paramsEntity.getFat() > 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_fat_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.SAD), 0);
        } else if (paramsEntity.getFat() == 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_fat_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.NORMAL), 0);
        } else if (paramsEntity.getFat() < 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView)findViewById(R.id.welcom_fat_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.HAPPY), 0);
        }
        if (paramsEntity.getMuscle() > 1) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_musculs_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.VERY_VERY_HAPPY), 0);
        } else if (paramsEntity.getMuscle() > 0.5) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_musculs_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.VERY_HAPPY), 0);
        } else if (paramsEntity.getMuscle() > 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_musculs_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.HAPPY), 0);
        } else if (paramsEntity.getMuscle() == 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView) findViewById(R.id.welcom_musculs_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.NORMAL), 0);
        } else if (paramsEntity.getMuscle() < 0) {
            GlobalFunc.addScaledImage(Welcome.this, ((TextView)findViewById(R.id.welcom_musculs_delta)),
                    0, 0, Emotions.getSmileyResource(Emotions.SAD), 0);
        }
        ((TextView)findViewById(R.id.welcom_tdw_delta)).setText(String.format("%s", paramsEntity.getTdw()));
        ((TextView) findViewById(R.id.welcom_musculs_delta)).setText(String.format("%s", paramsEntity.getMuscle()));
        ((TextView)findViewById(R.id.welcom_bones_delta)).setText(String.format("%s", paramsEntity.getBones()));
        ((TextView)findViewById(R.id.welcom_kcal_delta)).setText(String.format("%s", paramsEntity.getKcal()));
        ((TextView)findViewById(R.id.welcom_bmi_delta)).setText(String.format("%s", paramsEntity.getBmi()));
    }

}
