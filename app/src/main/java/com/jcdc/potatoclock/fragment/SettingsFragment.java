package com.jcdc.potatoclock.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jcdc.potatoclock.R;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

/**
 * Created by jcdc on 1/29/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    private Context mContext;

    ListPreference date_format;
    Preference ninja_clock;
    PreferenceCategory pussy_clock_prefs;
    PreferenceCategory normal_clock;
    PreferenceCategory normal_clock_prefs;
    PreferenceCategory custom_clock_prefs;

    private static final String KEY_PARENT = "parent";

    int clock_styleChoosah;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        mContext = getContext();

        addPreferencesFromResource(R.xml.potato_clock);

        date_format = (ListPreference) findPreference("date_format");
        ninja_clock = (Preference) findPreference("fuzzy");
        pussy_clock_prefs = (PreferenceCategory) findPreference("pussy_clock_prefs");
        normal_clock = (PreferenceCategory) findPreference("normal_clock");
        normal_clock_prefs = (PreferenceCategory) findPreference("normal_clock_prefs");
        custom_clock_prefs = (PreferenceCategory) findPreference("custom_clock_prefs");


        clock_styleChoosah = Settings.System.getInt(mContext
                .getContentResolver(), "clock_styleChoosah", 1);

        PreferenceGroup parent = (PreferenceGroup) findPreference(KEY_PARENT);


        if (clock_styleChoosah == 1) {
            parent.addPreference(normal_clock);
            clockSettings();
            parent.removePreference(pussy_clock_prefs);
            parent.removePreference(normal_clock_prefs);
            parent.removePreference(custom_clock_prefs);
        } else if (clock_styleChoosah == 2) {
            parent.addPreference(pussy_clock_prefs);
            pussyClockSettings();
            parent.removePreference(normal_clock);
            parent.removePreference(normal_clock_prefs);
            parent.removePreference(custom_clock_prefs);
        } else if (clock_styleChoosah == 3) {
            parent.addPreference(normal_clock_prefs);
            normalClockSettings();
            parent.removePreference(normal_clock);
            parent.removePreference(pussy_clock_prefs);
            parent.removePreference(custom_clock_prefs);
        } else {
            parent.addPreference(custom_clock_prefs);
            custom_clock();
            parent.removePreference(normal_clock);
            parent.removePreference(pussy_clock_prefs);
            parent.removePreference(normal_clock_prefs);
        }

        findPreference("clock_style_choosah").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(
                            Preference preference, Object newValue) {
                        preference = (ListPreference) preference;
                        Integer val = Integer
                                .parseInt((String) newValue);

                        Settings.System.putInt(mContext.getContentResolver(),
                                "clock_styleChoosah", val);

                        PreferenceGroup parent = (PreferenceGroup) findPreference(KEY_PARENT);

                        if (val == 1) {
                            parent.addPreference(normal_clock);
                            clockSettings();
                            parent.removePreference(pussy_clock_prefs);
                            parent.removePreference(normal_clock_prefs);
                            parent.removePreference(custom_clock_prefs);
                        } else if (val == 2) {
                            parent.addPreference(pussy_clock_prefs);
                            pussyClockSettings();
                            parent.removePreference(normal_clock);
                            parent.removePreference(normal_clock_prefs);
                            parent.removePreference(custom_clock_prefs);
                        } else if (val == 3) {
                            parent.addPreference(normal_clock_prefs);
                            normalClockSettings();
                            parent.removePreference(normal_clock);
                            parent.removePreference(pussy_clock_prefs);
                            parent.removePreference(custom_clock_prefs);
                        } else {
                            parent.addPreference(custom_clock_prefs);
                            custom_clock();
                            parent.removePreference(normal_clock);
                            parent.removePreference(pussy_clock_prefs);
                            parent.removePreference(normal_clock_prefs);
                        }

                        updateClock();
                        return true;
                    }
                });


        findPreference("clock_location").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(
                            Preference preference, Object newValue) {
                        preference = (ListPreference) preference;
                        Integer val = Integer
                                .parseInt((String) newValue);

                        Settings.System.putInt(mContext.getContentResolver(),
                                "clock_style", val);

                        updateClock();
                        return true;
                    }
                });

        findPreference("clock_font").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(
                            Preference preference, Object newValue) {
                        preference = (ListPreference) preference;
                        Integer val = Integer
                                .parseInt((String) newValue);

                        Settings.System.putInt(mContext.getContentResolver(),
                                "clock_font", val);


                        updateClock();
                        return true;
                    }
                });

        findPreference("animate").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        boolean checked = ((CheckBoxPreference) preference)
                                .isChecked();
                        if (checked) {
                            Settings.System.putInt(mContext.getContentResolver(),
                                    "animate", 1);
                            updateClock();

                        } else {
                            Settings.System.putInt(mContext.getContentResolver(),
                                    "animate", 0);
                            updateClock();
                        }
                        updateClock();
                        return true;
                    }
                });


        ((ColorPickerPreference) findPreference("clock_color")).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setDefaultValue(Color.WHITE);
                preference.setSummary(ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
//	    	Toast.makeText(getBaseContext(), newValue+"", Toast.LENGTH_SHORT).show();
                Settings.System.putString(mContext.getContentResolver(),
                        "clock_color", (String) ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(newValue))));
                updateClock();
                return true;
            }

        });
    }

    private void clockSettings(){
        findPreference("ampm").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(
                            Preference preference, Object newValue) {
                        preference = (ListPreference) preference;
                        Integer val = Integer
                                .parseInt((String) newValue);

                        Settings.System.putInt(mContext.getContentResolver(),
                                "ampm", val);

                        Settings.System.putInt(mContext.getContentResolver(), "status_bar_am_pm",
                                val);

                        if(val==3){
                            customAMPMSize(null);
                        }

                        updateClock();
                        return true;
                    }
                });

        findPreference("day").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(
                            Preference preference, Object newValue) {
                        preference = (ListPreference) preference;
                        Integer val = Integer
                                .parseInt((String) newValue);

                        Settings.System.putInt(mContext.getContentResolver(),
                                "day", val);

                        if(val==3){
                            customDaySize(null);
                        }

                        updateClock();
                        return true;
                    }
                });

        findPreference("date_format").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {

                    public boolean onPreferenceChange(
                            Preference preference, Object newValue) {
                        preference = (ListPreference) preference;
                        Integer val = Integer
                                .parseInt((String) newValue);

                        Settings.System.putInt(mContext.getContentResolver(),
                                "date_format", val);

                        if (val==14){
//                    				Toast.makeText(getBaseContext(), "Huli to", Toast.LENGTH_SHORT).show();
                            customDate(null);
                        }

                        updateClock();
                        return true;
                    }
                });
    }

    private void pussyClockSettings(){

        findPreference("fuzzy_upper").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        boolean checked = ((CheckBoxPreference) preference)
                                .isChecked();
                        if (checked) {
                            Settings.System.putInt(mContext.getContentResolver(),
                                    "ninja_clock_upper", 1);
                            updateClock();

                        } else {
                            Settings.System.putInt(mContext.getContentResolver(),
                                    "ninja_clock_upper", 0);
                            updateClock();
                        }
                        updateClock();
                        return true;
                    }
                });
    }

    private void normalClockSettings(){

        findPreference("normal_upper").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {

                    public boolean onPreferenceClick(Preference preference) {
                        boolean checked = ((CheckBoxPreference) preference)
                                .isChecked();
                        if (checked) {
                            Settings.System.putInt(mContext.getContentResolver(),
                                    "normal_upper", 1);
                            updateClock();

                        } else {
                            Settings.System.putInt(mContext.getContentResolver(),
                                    "normal_upper", 0);
                            updateClock();
                        }
                        updateClock();
                        return true;
                    }
                });
    }

    private void custom_clock(){

        try{
            EditTextPreference clock_format = (EditTextPreference) findPreference("custom_format");

            clock_format
                    .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {
                            Settings.System.putString(mContext.getContentResolver(),
                                    "custom_clock_format", (String) newValue);
                            //preference.setSummary("\"" + newValue + "\"");
                            updateClock();
                            return true;
                        }
                    });
        }catch(Exception e){
//			Toast.makeText(getApplicationContext(), "WTF", 1).show();
        }



    }

    private void setState(Boolean bool){
        findPreference("ampm").setEnabled(bool);
        findPreference("day").setEnabled(bool);
        findPreference("date_format").setEnabled(bool);

    }

    private void updateClock() {

        Intent send = new Intent();
        send.setAction("com.potato.clock");
        mContext.sendBroadcast(send);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO Auto-generated method stub
        return false;
    }

    public void customDate(Context context){
        ContentResolver resolver = mContext.getContentResolver();

        AlertDialog dialog;

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Custom JAVA format");
        alert.setMessage("Enter string");

        final EditText input = new EditText(mContext);

        String oldText = Settings.System.getString(resolver,
                "custom_date_format");

        if (oldText != null) {
            input.setText(oldText);
        }
        alert.setView(input);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int whichButton) {
                String value = input.getText().toString();
                if (value.equals("")) {
                    return;
                }
                Settings.System.putString(mContext.getContentResolver(),
                        "custom_date_format", value);
                updateClock();
                return;
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        return;
                    }
                });
        dialog = alert.create();
        dialog.show();

    }


    public void customDaySize(Context context){
        AlertDialog dialog;



        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Custom Date size");
        alert.setMessage("Size");

        SeekBar seek = new SeekBar(mContext);
        seek.setProgress(Settings.System.getInt(mContext
                .getContentResolver(), "date_size_custom",100));
        seek.setMax(100);



        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressed = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar,final int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progressed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, progressed  + "%", Toast.LENGTH_SHORT).show();
                Settings.System.putInt(mContext.getContentResolver(),
                        "date_size_custom", progressed);
                updateClock();
            }

        });

        alert.setView(seek);
        alert.setNegativeButton("Let's get it on!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(mContext, "Brought to you by" +
                                " your friends at PotatoInc", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
        dialog = alert.create();
        dialog.show();

    }

    public void customAMPMSize(Context context){
        AlertDialog dialog;



        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Custom AM/PM size");
        alert.setMessage("Size");

        SeekBar seek = new SeekBar(mContext);
        seek.setProgress(Settings.System.getInt(mContext
                .getContentResolver(), "AMPM_size_custom",100));
        seek.setMax(100);



        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressed = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar,final int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                progressed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, progressed  + "%",  Toast.LENGTH_SHORT).show();
                Settings.System.putInt(mContext.getContentResolver(),
                        "AMPM_size_custom", progressed);
                updateClock();
            }

        });

        alert.setView(seek);
        alert.setNegativeButton("Let's get it on!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(mContext, "Brought to you by" +
                                " your friends at PotatoInc", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
        dialog = alert.create();
        dialog.show();

    }


    private void resetDialog(){
        AlertDialog dialog;

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Potato settings");
        alert.setMessage("Use Potato clock settings?");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int whichButton) {

                Settings.System.putInt(mContext.getContentResolver(),
                        "clock_style", 3);
                Settings.System.putInt(mContext.getContentResolver(),
                        "clock_font", 5);
                Settings.System.putInt(mContext.getContentResolver(),
                        "ampm", 3);
                Settings.System.putInt(mContext.getContentResolver(),
                        "day", 3);
                Settings.System.putInt(mContext.getContentResolver(),
                        "date_format", 10);
                Settings.System.putString(mContext.getContentResolver(),
                        "clock_color", (String) ColorPickerPreference.convertToARGB(Integer.valueOf(String.valueOf(-1))));
                Settings.System.putInt(mContext.getContentResolver(),
                        "ninja_clock", 0);
                updateClock();
                return;
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        return;
                    }
                });
        dialog = alert.create();
        dialog.show();
    }
}
