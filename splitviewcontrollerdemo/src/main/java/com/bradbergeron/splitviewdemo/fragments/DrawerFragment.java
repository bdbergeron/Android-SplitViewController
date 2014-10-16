/*
 * Copyright (c) 2014 Bradley David Bergeron
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bradbergeron.splitviewdemo.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bradbergeron.splitviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Bradley David Bergeron on 10/16/14.
 */
public class DrawerFragment extends Fragment {
    private static final String TAG = DrawerFragment.class.getSimpleName();

    private static final String STATE_SELECTED_ITEM_POSITION = "selectedItemPosition";

    private final ArrayList<FragmentDrawerItem> mDrawerItems =
            new ArrayList<FragmentDrawerItem>() {{
                add(new FragmentDrawerItem("Home", SplitViewFragment.class, true));
                add(new FragmentDrawerItem("GitHub", WebViewFragment.class, false));
            }};

    private ListView mListView;
    private DrawerListAdapter mListAdapter;
    private DrawerItemSelectionListener mDrawerItemSelectionListener;

    private int mSelectedItemPosition = AdapterView.INVALID_POSITION;


    // ================================================================================
    // Lifecycle
    // ================================================================================

    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new DrawerListAdapter(getActivity(), mDrawerItems);

        if (savedInstanceState != null) {
            mSelectedItemPosition = savedInstanceState
                    .getInt(STATE_SELECTED_ITEM_POSITION, AdapterView.INVALID_POSITION);
        }
    }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container,
                              final Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_drawer, container, false);
        mListView.setAdapter(mListAdapter);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mListView.setItemChecked(mSelectedItemPosition, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (final AdapterView<?> parent, final View view,
                                     final int position, final long id) {
                mListView.setItemChecked(position, true);

                if (mDrawerItemSelectionListener != null) {
                    mDrawerItemSelectionListener.onDrawerItemSelected(mDrawerItems.get(position));
                }
            }
        });

        return mListView;
    }

    @Override
    public void onStart () {
        super.onStart();

        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume () {
        super.onResume();

        Log.d(TAG, "onResume");

        if (mSelectedItemPosition == AdapterView.INVALID_POSITION) {
            mListView.performItemClick(mListView, 0, 0);
        }
    }

    @Override
    public void onSaveInstanceState (final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_SELECTED_ITEM_POSITION, mListView.getCheckedItemPosition());
    }

    @Override
    public void onPause () {
        Log.d(TAG, "onPause");

        super.onPause();
    }

    @Override
    public void onStop () {
        Log.d(TAG, "onStop");

        super.onStop();
    }

    // ================================================================================
    // Drawer Item Listener Interface
    // ================================================================================

    public void setDrawerItemSelectionListener (final DrawerItemSelectionListener listener) {
        mDrawerItemSelectionListener = listener;
    }

    public interface DrawerItemSelectionListener {
        public void onDrawerItemSelected (final FragmentDrawerItem fragmentDrawerItem);
    }


    // ================================================================================
    // List Adapter
    // ================================================================================

    private class DrawerListAdapter extends ArrayAdapter<FragmentDrawerItem> {
        public DrawerListAdapter (final Context context, final List<FragmentDrawerItem> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }
    }


    // ================================================================================
    // Drawer Items
    // ================================================================================

    public class FragmentDrawerItem {
        private final String mTitle;
        private final Class mFragmentClass;
        private final boolean mDefaultItem;

        public FragmentDrawerItem (final String title, final Class fragmentClass,
                                   final boolean isDefaultItem) {
            mTitle = title;
            mFragmentClass = fragmentClass;
            mDefaultItem = isDefaultItem;
        }

        public String getTitle () {
            return mTitle;
        }

        public Class getFragmentClass () {
            return mFragmentClass;
        }

        public boolean isDefaultItem () {
            return mDefaultItem;
        }
    }
}
