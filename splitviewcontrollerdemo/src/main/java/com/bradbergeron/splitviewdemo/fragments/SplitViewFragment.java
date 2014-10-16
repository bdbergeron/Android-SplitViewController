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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bradbergeron.splitviewcontroller.SplitViewController;
import com.bradbergeron.splitviewcontroller.SplitViewMasterFragment;
import com.bradbergeron.splitviewdemo.R;
import com.bradbergeron.splitviewdemo.activities.MainActivity;

public class SplitViewFragment extends SplitViewController {
    private static final String TAG = SplitViewFragment.class.getSimpleName();

    private final FragmentManager.OnBackStackChangedListener mBackStackListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged () {
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        setTitle(getString(R.string.app_name));
                    }
                }
            };


    // ================================================================================
    // Lifecycle
    // ================================================================================

    @Override
    public View onCreateView (final LayoutInflater inflater, final ViewGroup container,
                              final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_splitview, container, false);

        SplitViewMasterFragment masterFragment = (SplitViewMasterFragment) getFragmentManager()
                .findFragmentById(getMasterFragmentContainerId());

        if (masterFragment == null) {
            masterFragment = (SplitViewMasterFragment) Fragment
                    .instantiate(getActivity(), ListFragment.class.getName());

            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(getMasterFragmentContainerId(), masterFragment,
                            masterFragment.getClass().getName());
            transaction.commit();
        }

        setMasterFragment(masterFragment);

        return view;
    }

    @Override
    public void onStart () {
        super.onStart();

        Log.d(TAG, "onStart");

        getFragmentManager().addOnBackStackChangedListener(mBackStackListener);
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

        getFragmentManager().removeOnBackStackChangedListener(mBackStackListener);

        super.onStop();
    }


    // ================================================================================
    // Split View Controller
    // ================================================================================

    @Override
    public int getMasterFragmentContainerId () {
        return R.id.masterView;
    }

    @Override
    public int getDetailFragmentContainerId () {
        return R.id.detailView;
    }

    @Override
    public boolean isSplitViewLayout () {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void setTitle (final CharSequence title) {
        final ActionBar actionBar = getActivity().getActionBar();

        if (actionBar != null) {
            actionBar.setTitle(isSplitViewLayout() ? getString(R.string.app_name) : title);
        }
    }

    @Override
    public void setSubtitle (final CharSequence subtitle) {
        final ActionBar actionBar = getActivity().getActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle(isSplitViewLayout() ? null : subtitle);
        }
    }


    // ================================================================================
    // Split View Navigation Listener
    // ================================================================================

    @Override
    public boolean usesNavigationDrawer () {
        return true;
    }

    @Override
    public void setNavigationDrawerEnabled (final boolean enabled) {
        final Activity activity = getActivity();

        if (activity instanceof MainActivity) {
            ((MainActivity) activity).setNavigationDrawerEnabled(enabled);
        }
    }
}
