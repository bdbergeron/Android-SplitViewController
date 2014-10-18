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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bradbergeron.splitviewcontroller.SplitViewDetailFragment;
import com.bradbergeron.splitviewcontrollerdemo.R;

public class MoreDetailsFragment extends SplitViewDetailFragment {
    private static final String TAG = MoreDetailsFragment.class.getSimpleName();

    private String mItemName;


    // ================================================================================
    // Lifecycle
    // ================================================================================

    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();

        if (args != null) {
            mItemName = args.getString(DetailFragment.ARGS_ITEM_NAME);
        }
    }

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container,
                              final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_moredetails, container, false);

        final TextView itemNameTextView =
                (TextView) view.findViewById(R.id.moreDetails_itemNameTextView);
        itemNameTextView.setText(mItemName);

        return view;
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

        setTitle(mItemName + ": More Information");
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
}
