package com.cocorporation.androidman.menus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.cocorporation.androidman.R;

import java.util.UUID;

/**
 * Created by Corentin on 5/2/2015.
 */
public class LevelFragment extends Fragment {
    public static final String EXTRA_LEVEL_ID = "androidman.LEVEL_ID";

    Level mLevel;
    EditText mTitleField;
    Button mDateButton;
    CheckBox mPassedCheckBox;

    public static LevelFragment newInstance(UUID levelId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LEVEL_ID, levelId);

        LevelFragment fragment = new LevelFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID levelId = (UUID)getArguments().getSerializable(EXTRA_LEVEL_ID);
        mLevel = LevelLab.get(getActivity()).getLevel(levelId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_level, parent, false);

        mTitleField = (EditText)v.findViewById(R.id.level_title);
        mTitleField.setText(mLevel.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mLevel.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

        mDateButton = (Button)v.findViewById(R.id.level_date);
        mDateButton.setText(mLevel.getDate().toString());
        mDateButton.setEnabled(false);

        mPassedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mPassedCheckBox.setChecked(mLevel.isPassed());
        mPassedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the crime's solved property
                mLevel.setPassed(isChecked);
            }
        });

        return v;
    }
}
