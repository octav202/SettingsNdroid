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
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.util.Log;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.ndroid.atmanager.AntiTheftManager;

public class AntiTheftSettingsFragment extends SettingsPreferenceFragment {
    private static final String TAG = "AT_SettingsFragment";

    public static final String KEY_STATUS = "status";
    public static final String KEY_IP = "ip";
    public static final String KEY_CHECK_FREQUENCY = "frequency";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DEVICE_NAME = "device_name";
    public static final String KEY_DEVICE_PASS = "device_pass";

    SwitchPreference mStatusPreference;
    EditTextPreference mIpAddressPreference;
    EditTextPreference mFrequencyPreference;
    EditTextPreference mDeviceIdPreference;
    EditTextPreference mDeviceNamePreference;
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

        mStatusPreference = (SwitchPreference) findPreference(KEY_STATUS);
        mIpAddressPreference = (EditTextPreference) findPreference(KEY_IP);
        mFrequencyPreference = (EditTextPreference) findPreference(KEY_CHECK_FREQUENCY);
        mDeviceIdPreference = (EditTextPreference) findPreference(KEY_DEVICE_ID);
        mDeviceNamePreference = (EditTextPreference) findPreference(KEY_DEVICE_NAME);
        mDevicePassPreference = (EditTextPreference) findPreference(KEY_DEVICE_PASS);

        setPreferenceValues();

        setListeners();
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

    private void setPreferenceValues() {
        boolean status = AntiTheftManager.getInstance(getActivity().getApplicationContext()).getAntiTheftStatus();
        mStatusPreference.setChecked(status);

        String ip = AntiTheftManager.getInstance(getActivity().getApplicationContext()).getIpAddress();
        mIpAddressPreference.setSummary(ip);

        Integer freq = AntiTheftManager.getInstance(getActivity().getApplicationContext()).getAtFrequency();
        mFrequencyPreference.setSummary(freq.toString());

        Integer id = AntiTheftManager.getInstance(getActivity().getApplicationContext()).getDeviceId();
        mDeviceIdPreference.setSummary(id.toString());

        String name = AntiTheftManager.getInstance(getActivity().getApplicationContext()).getDeviceName();
        mDeviceNamePreference.setSummary(name);

        String pass = AntiTheftManager.getInstance(getActivity().getApplicationContext()).getDevicePass();
        mDevicePassPreference.setSummary(pass);
    }

    private void setListeners() {
        mStatusPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference instanceof SwitchPreference) {
                    boolean checked = (Boolean) newValue;
                    //((SwitchPreference) preference).setChecked(checked);
                    AntiTheftManager.getInstance(getActivity().getApplicationContext()).setAntiTheftStatus(checked);
                }
                return true;
            }
        });

        mIpAddressPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference instanceof EditTextPreference) {
                    String text = (String) newValue;
                    preference.setSummary(text);
                    AntiTheftManager.getInstance(getActivity().getApplicationContext()).setIpAddress(text);
                }
                return true;
            }
        });

        mFrequencyPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference instanceof EditTextPreference) {
                    String text = (String) newValue;
                    preference.setSummary(text);
                    Integer frequency = Integer.parseInt(text);
                    AntiTheftManager.getInstance(getActivity().getApplicationContext()).setAtFrequency(frequency);
                }
                return true;
            }
        });

        mDeviceIdPreference.setEnabled(false);
        mDeviceNamePreference.setEnabled(false);
        mDevicePassPreference.setEnabled(false);
    }



}
