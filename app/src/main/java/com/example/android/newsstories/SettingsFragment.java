package com.example.android.newsstories;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);
        final CheckBoxPreference checkBoxPreference = (CheckBoxPreference) getPreferenceManager().findPreference(getString(R.string.show_all));
        checkBoxPreference.setOnPreferenceChangeListener(this);
        android.support.v7.preference.EditTextPreference editTextPreference = (android.support.v7.preference.EditTextPreference) getPreferenceManager().findPreference(getString(R.string.pref_number_key));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String number = sharedPreferences.getString(getString(R.string.pref_number_key),getString(R.string.pref_number_default_value));
        setPreferenceSummary(editTextPreference,number);
        editTextPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Toast error = Toast.makeText(getContext(),"Please, select a number between 1 and 50",Toast.LENGTH_SHORT);
        String numberKey = getString(R.string.pref_number_key);
        if (preference.getKey().equals(numberKey)){String numberSize = (String) newValue;
        try { int number = Integer.parseInt(numberSize);
                if (number < 1 || number > 50){
                    error.show();
                    return false;
                }
            } catch (NumberFormatException e){
                error.show();
                return false;
            }setPreferenceSummary(preference,numberSize);
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            boolean isEnabled = sharedPreferences.getBoolean(getString(R.string.show_all), true);
            CheckBoxPreference checkBoxPreference1 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.technology_key));
            checkBoxPreference1.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.law_key));
            checkBoxPreference2.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.science_key));
            checkBoxPreference3.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference4 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.education_key));
            checkBoxPreference4.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference5 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.travel_key));
            checkBoxPreference5.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference6 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.music_key));
            checkBoxPreference6.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference7 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.art_and_design_key));
            checkBoxPreference7.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference8 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.film_key));
            checkBoxPreference8.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference9 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.fashion_key));
            checkBoxPreference9.setChecked(!isEnabled);
            CheckBoxPreference checkBoxPreference10 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.crosswords_key));
            checkBoxPreference10.setChecked(!isEnabled);}
        return true;
    }

    private void setPreferenceSummary(Preference preference, String value) {
        if (preference instanceof android.support.v7.preference.EditTextPreference) {
            preference.setSummary(value);
        }
    }
}
