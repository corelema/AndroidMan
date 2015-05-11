package com.cocorporation.androidman.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.cocorporation.androidman.R;

import java.util.ArrayList;

/**
 * Created by Corentin on 5/2/2015.
 */
public class LevelListFragment extends ListFragment {
    private ArrayList<Level> mLevels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.menu_level_title);
        mLevels = LevelLab.get(getActivity()).getLevels();
        LevelAdapter adapter = new LevelAdapter(mLevels);
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        // get the Crime from the adapter
        Level c = ((LevelAdapter)getListAdapter()).getItem(position);
        // start an instance of CrimePagerActivity
        Intent i = new Intent(getActivity(), LevelPagerActivity.class);
        i.putExtra(LevelFragment.EXTRA_LEVEL_ID, c.getId());
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((LevelAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class LevelAdapter extends ArrayAdapter<Level> {
        public LevelAdapter(ArrayList<Level> levels) {
            super(getActivity(), android.R.layout.simple_list_item_1, levels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_level, null);
            }

            // configure the view for this Level
            Level l = getItem(position);

            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.level_list_item_titleTextView);
            titleTextView.setText(l.getTitle());
            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.level_list_item_dateTextView);
            dateTextView.setText(l.getDate().toString());
            CheckBox PassedCheckBox =
                    (CheckBox)convertView.findViewById(R.id.level_list_item_passedCheckBox);
            PassedCheckBox.setChecked(l.isPassed());

            return convertView;
        }
    }
}
