package com.example.android.newsstories;


import android.content.SharedPreferences;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
     addPreferencesFromResource(R.xml.preference);
        final CheckBoxPreference checkBoxPreference = (CheckBoxPreference)getPreferenceManager().findPreference(getString(R.string.show_all));
        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                boolean isEnabled = sharedPreferences.getBoolean(getString(R.string.show_all), true);
                CheckBoxPreference checkBoxPreference1 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.technology));
                checkBoxPreference1.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.law));
                checkBoxPreference2.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.science));
                checkBoxPreference3.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference4 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.education));
                checkBoxPreference4.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference5 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.travel));
                checkBoxPreference5.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference6 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.music));
                checkBoxPreference6.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference7 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.art_and_design));
                checkBoxPreference7.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference8 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.film));
                checkBoxPreference8.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference9 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.fashion));
                checkBoxPreference9.setChecked(!isEnabled);
                CheckBoxPreference checkBoxPreference10 = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.crosswords));
                checkBoxPreference10.setChecked(!isEnabled);

                return true;}
        });
    }

}
