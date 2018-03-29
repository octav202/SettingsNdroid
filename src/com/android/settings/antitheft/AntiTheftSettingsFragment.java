/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.antitheft;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class AntiTheftSettingsFragment extends SettingsPreferenceFragment {
    private static final String TAG = "AT_Settings";

    public static final String KEY_ENABLED = "enabled";
    public static final String KEY_SERVER = "server";
    public static final String KEY_CHECK_FREQUENCY = "check_frequency";
    public static final String KEY_LOCATION_FREQUENCY = "location_frequency";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DEVICE_PASS = "device_pass";

    SwitchPreference mEnabledPreference;
    EditTextPreference mServerPreference;
    EditTextPreference mCheckFreqPreference;
    EditTextPreference mLocationFreqPreference;
    EditTextPreference mDeviceIdPreference;
    EditTextPreference mDevicePassPreference;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.SOUND;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.antitheft_settings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEnabledPreference = (SwitchPreference) findPreference(KEY_ENABLED);
        mServerPreference = (EditTextPreference) findPreference(KEY_SERVER);
        mCheckFreqPreference = (EditTextPreference) findPreference(KEY_CHECK_FREQUENCY);
        mLocationFreqPreference = (EditTextPreference) findPreference(KEY_LOCATION_FREQUENCY);
        mDeviceIdPreference = (EditTextPreference) findPreference(KEY_DEVICE_ID);
        mDevicePassPreference = (EditTextPreference) findPreference(KEY_DEVICE_PASS);

        mEnabledPreference.setOnPreferenceChangeListener(mPreferenceListener);
        mServerPreference.setOnPreferenceChangeListener(mPreferenceListener);
        mCheckFreqPreference.setOnPreferenceChangeListener(mPreferenceListener);
        mLocationFreqPreference.setOnPreferenceChangeListener(mPreferenceListener);
        mDeviceIdPreference.setOnPreferenceChangeListener(mPreferenceListener);
        mDevicePassPreference.setOnPreferenceChangeListener(mPreferenceListener);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private Preference.OnPreferenceChangeListener mPreferenceListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference instanceof EditTextPreference) {
                String text = (String) newValue;
                preference.setSummary(text);
            } else if (preference instanceof SwitchPreference) {
                boolean checked = (Boolean) newValue;
                ((SwitchPreference) preference).setChecked(checked);
                setAllPreferencesEnabled(checked);
            }
            return true;
        }
    };

    private void setAllPreferencesEnabled(boolean status) {
        mServerPreference.setEnabled(status);
        mCheckFreqPreference.setEnabled(status);
        mLocationFreqPreference.setEnabled(status);
        mDeviceIdPreference.setEnabled(status);
        mDevicePassPreference.setEnabled(status);
    }
}
