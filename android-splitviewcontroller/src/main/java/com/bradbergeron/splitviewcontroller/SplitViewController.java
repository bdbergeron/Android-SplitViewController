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

package com.bradbergeron.splitviewcontroller;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/*
 * Created by Bradley David Bergeron on 10/14/14.
 */
public abstract class SplitViewController extends Fragment implements SplitViewNavigationDelegate {
    private final FragmentManager.OnBackStackChangedListener mBackStackListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged () {
                    final int detailItemCount = getFragmentManager().getBackStackEntryCount();

                    if (detailItemCount == 0 && !isSplitViewLayout()) {
                        attachMasterFragment();

                        return;
                    }

                    onDetailItemCountChanged(detailItemCount);
                }
            };

    private SplitViewMasterFragment mMasterFragment;

    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onStart () {
        super.onStart();

        configureMasterDetailFragments();

        getFragmentManager().addOnBackStackChangedListener(mBackStackListener);
    }

    @Override
    public void onStop () {
        getFragmentManager().removeOnBackStackChangedListener(mBackStackListener);

        super.onStop();
    }

    private void configureMasterDetailFragments () {
        mMasterFragment = (SplitViewMasterFragment) getFragmentManager()
                .findFragmentById(getMasterFragmentContainerId());

        if (mMasterFragment == null) {
            throw new IllegalStateException("Master view Fragment could not be found.");
        }

        mMasterFragment.setController(this);

        final SplitViewDetailFragment detailFragment =
                (SplitViewDetailFragment) getFragmentManager()
                        .findFragmentById(getDetailFragmentContainerId());

        if (detailFragment != null) {
            detailFragment.setController(this);
        }

        if (!isSplitViewLayout() && detailFragment != null) {
            detachMasterFragment();
        } else if (mMasterFragment.isDetached()) {
            attachMasterFragment();
        }
    }

    private void attachMasterFragment () {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.attach(mMasterFragment);
        transaction.commit();

        onDetailItemCountChanged(getFragmentManager().getBackStackEntryCount());
    }

    private void detachMasterFragment () {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(mMasterFragment);
        transaction.commit();

        onDetailItemCountChanged(getFragmentManager().getBackStackEntryCount());
    }

    public abstract int getMasterFragmentContainerId ();

    public abstract int getDetailFragmentContainerId ();

    public void setDetailFragment (final SplitViewDetailFragment detailFragment) {
        final FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        detailFragment.setController(this);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(getDetailFragmentContainerId(), detailFragment);
        transaction.addToBackStack(null);

        if (!isSplitViewLayout() && !mMasterFragment.isDetached()) {
            transaction.detach(mMasterFragment);
        }

        transaction.commit();
    }


    // ================================================================================
    // Properties
    // ================================================================================

    public abstract boolean isSplitViewLayout ();

    public void setTitle (final CharSequence title) { }

    public void setSubtitle (final CharSequence subtitle) { }


    // ================================================================================
    // SplitViewNavigationDelegate
    // ================================================================================

    @Override
    public boolean usesNavigationDrawer () {
        return false;
    }

    @Override
    public boolean shouldShowActionBarNavigationDrawerIndicator (final int detailItemCount) {
        return false;
    }

    @Override
    public boolean shouldShowActionBarUpIndicator (final int detailItemCount) {
        return false;
    }

    @Override
    public void onDetailItemCountChanged (final int detailItemCount) {
        final ActionBar actionBar = getActivity().getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(shouldShowActionBarUpIndicator(detailItemCount));
        }
    }
}
