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

package com.bradbergeron.splitviewcontrollerdemo.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bradbergeron.splitviewcontroller.SplitViewMasterFragment;
import com.bradbergeron.splitviewcontrollerdemo.R;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends SplitViewMasterFragment {
    private static final String TAG = ListFragment.class.getSimpleName();

    private final ArrayList<String> mItems = new ArrayList<String>() {{
        add("Item 1");
        add("Item 2");
        add("Item 3");
        add("Item 4");
        add("Item 5");
    }};

    private ListView mListView;
    private StringListAdapter mListAdapter;


    // ================================================================================
    // Lifecycle
    // ================================================================================

    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new StringListAdapter(getActivity(), mItems);
    }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container,
                              final Bundle savedInstanceState) {
        mListView = (ListView) inflater.inflate(R.layout.fragment_listview, container, false);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (final AdapterView<?> parent, final View view,
                                     final int position, final long id) {
                final String itemName = mItems.get(position);

                final Bundle args = new Bundle();
                args.putString(DetailFragment.ARGS_ITEM_NAME, itemName);

                final DetailFragment detailFragment = (DetailFragment) Fragment
                        .instantiate(getActivity(), DetailFragment.class.getName(), args);

                setDetailFragment(detailFragment);
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
    // List Adapter
    // ================================================================================

    private class StringListAdapter extends ArrayAdapter<String> {
        public StringListAdapter (final Context context, final List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }
    }
}
