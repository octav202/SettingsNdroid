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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
//import com.ndroid.atmanager.AntiTheftManager;

public class AntiTheftSettingsFragment extends SettingsPreferenceFragment {
    private static final String TAG = "AT_SettingsFragment";

    public static final String KEY_STATUS = "status";
    public static final String KEY_IP = "ip";
    public static final String KEY_CHECK_FREQUENCY = "frequency";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DEVICE_NAME = "device_name";
    public static final String KEY_DEVICE_PASS = "device_pass";
    public static final String KEY_RESET = "reset";

    SwitchPreference mStatusPreference;
    EditTextPreference mIpAddressPreference;
    EditTextPreference mFrequencyPreference;
    EditTextPreference mDeviceIdPreference;
    EditTextPreference mDeviceNamePreference;
    EditTextPreference mDevicePassPreference;
    Preference mResetPreference;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.SOUND;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //addPreferencesFromResource(R.xml.antitheft_settings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.antitheft_settings);

        mStatusPreference = (SwitchPreference) findPreference(KEY_STATUS);
        mIpAddressPreference = (EditTextPreference) findPreference(KEY_IP);
        mFrequencyPreference = (EditTextPreference) findPreference(KEY_CHECK_FREQUENCY);
        mDeviceIdPreference = (EditTextPreference) findPreference(KEY_DEVICE_ID);
        mDeviceNamePreference = (EditTextPreference) findPreference(KEY_DEVICE_NAME);
        mDevicePassPreference = (EditTextPreference) findPreference(KEY_DEVICE_PASS);
        mResetPreference = (Preference) findPreference(KEY_RESET);

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
        if (status) {
            mStatusPreference.setSummary(R.string.enabled);
        } else {
            mStatusPreference.setSummary(R.string.disabled);
        }

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

                    AntiTheftManager manager = AntiTheftManager.getInstance(getActivity().getApplicationContext());

                    if (checked) {

                        if (manager.getIpAddress() == null || manager.getIpAddress().isEmpty()) {
                            Toast.makeText(getActivity(), "Please set an IP Address.", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        if (manager.getAtFrequency() == 0) {
                            Toast.makeText(getActivity(), "Please set a frequency.", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        int id = manager.getDeviceId();
                        if (id == 0) {
                            // Register Device
                            showRegisterDialog();
                        } else {
                            manager.setAntiTheftStatus(true);
                            mStatusPreference.setSummary(R.string.enabled);
                        }
                    } else {
                        showAuthenticateDialog();
                    }
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

        mResetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getActivity().getApplicationContext().getResources().
                        getString(R.string.at_key_reset_title));
                builder.setMessage(getActivity().getApplicationContext().getResources().
                        getString(R.string.at_key_reset_warning));

                // Ok Button
                builder.setPositiveButton(getActivity().getApplicationContext().getResources().
                        getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AntiTheftManager.getInstance(getActivity().getApplicationContext()).resetSettings();
                        setPreferenceValues();
                    }
                });

                // Cancel Button
                builder.setNegativeButton(getActivity().getApplicationContext().getResources().
                        getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

                return true;
            }
        });


        mDeviceIdPreference.setEnabled(false);
        mDeviceNamePreference.setEnabled(false);
        mDevicePassPreference.setEnabled(false);
    }


    /**
     * Request user to register device.
     */
    private void showRegisterDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout layout= new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText nameInput = new EditText(getActivity());
        nameInput.setHint(R.string.device_name);
        final EditText passInput = new EditText(getActivity());
        passInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passInput.setHint(R.string.pass);
        layout.addView(nameInput);
        layout.addView(passInput);
        alert.setView(layout);

        alert.setTitle(R.string.register);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = nameInput.getText().toString().trim();
                String pass = passInput.getText().toString().trim();
                int deviceId = AntiTheftManager.getInstance(getActivity()).registerDevice(name, pass);
                mDeviceIdPreference.setSummary(String.valueOf(deviceId));
                mDeviceNamePreference.setSummary(nameInput.toString());
                mDevicePassPreference.setSummary(passInput.toString());
                mStatusPreference.setSummary(R.string.enabled);
            }
        });
        alert.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mStatusPreference.setChecked(false);
                        dialog.cancel();
                    }
                });
        alert.create().show();
    }

    /**
     * Request user to authenticate in order to turn off.
     */
    private void showAuthenticateDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LinearLayout layout= new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText passInput = new EditText(getActivity());
        passInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passInput.setHint(R.string.pass);
        layout.addView(passInput);
        alert.setView(layout);

        alert.setTitle(R.string.register);

        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pass = passInput.getText().toString().trim();
                if (pass.equals(AntiTheftManager.getInstance(getActivity()).getDevicePass())) {
                    AntiTheftManager.getInstance(getActivity()).setAntiTheftStatus(false);
                    mStatusPreference.setSummary(R.string.disabled);
                } else {
                    Toast.makeText(getActivity(),"Authentication Failed", Toast.LENGTH_SHORT).show();
                    mStatusPreference.setChecked(true);
                    mStatusPreference.setSummary(R.string.enabled);
                }
            }
        });
        alert.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mStatusPreference.setChecked(true);
                        mStatusPreference.setSummary(R.string.enabled);
                        dialog.cancel();
                    }
                });
        alert.create().show();
    }

}
