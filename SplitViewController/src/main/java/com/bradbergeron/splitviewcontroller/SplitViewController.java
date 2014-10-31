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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public abstract class SplitViewController extends Fragment implements SplitViewNavigationListener {
    private final FragmentManager.OnBackStackChangedListener mBackStackListener =
            new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged () {
                    configureChildFragments();
                }
            };

    private SplitViewMasterFragment mMasterFragment;
    private SplitViewDetailFragment mDetailFragment;


    // ================================================================================
    // Fragment Lifecycle
    // ================================================================================

    @Override
    public void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onStart () {
        super.onStart();

        configureChildFragments();

        getFragmentManager().addOnBackStackChangedListener(mBackStackListener);
    }

    @Override
    public void onStop () {
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.removeOnBackStackChangedListener(mBackStackListener);

        if (mMasterFragment != null || mDetailFragment != null) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (mMasterFragment != null && !mMasterFragment.isDetached()) {
                transaction.detach(mMasterFragment);
            }

            if (mDetailFragment != null && !mDetailFragment.isDetached()) {
                transaction.detach(mDetailFragment);
            }

            transaction.commitAllowingStateLoss();
        }

        super.onStop();
    }


    // ================================================================================
    // Child Fragment Management
    // ================================================================================

    private void configureChildFragments () {
        if (mMasterFragment == null) {
            throw new IllegalStateException("Master view Fragment could not be found.");
        }

        mMasterFragment.setController(this);

        mDetailFragment = (SplitViewDetailFragment) getFragmentManager()
                .findFragmentById(getDetailFragmentContainerId());

        if (mDetailFragment != null) {
            mDetailFragment.setController(this);
        }

        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (isSplitViewLayout()) {
            transaction.attach(mMasterFragment);

            if (mDetailFragment != null) {
                transaction.attach(mDetailFragment);
            }
        } else if (mDetailFragment != null) {
            transaction.attach(mDetailFragment);
        } else {
            transaction.attach(mMasterFragment);
        }

        transaction.commit();

        onDetailItemCountChanged(getFragmentManager().getBackStackEntryCount());
    }


    // ================================================================================
    // Master Fragment
    // ================================================================================

    public abstract int getMasterFragmentContainerId ();

    public void setMasterFragment (final SplitViewMasterFragment masterFragment) {
        masterFragment.setController(this);

        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(getMasterFragmentContainerId(), masterFragment,
                            masterFragment.getClass().getSimpleName());
        transaction.commit();

        mMasterFragment = masterFragment;
    }


    // ================================================================================
    // Detail Fragment
    // ================================================================================

    public abstract int getDetailFragmentContainerId ();

    public void setDetailFragment (final SplitViewDetailFragment detailFragment) {
        setDetailFragment(detailFragment, getFragmentManager().beginTransaction());
    }

    public void setDetailFragment (final SplitViewDetailFragment detailFragment,
                                   final FragmentTransaction transaction) {
        if (detailFragment == null) {
            clearDetailFragment(false);

            return;
        }

        detailFragment.setController(this);

        final FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            clearDetailFragment(false);
        }

        transaction.replace(getDetailFragmentContainerId(), detailFragment);
        transaction.addToBackStack(null);

        if (!isSplitViewLayout() && !mMasterFragment.isDetached()) {
            transaction.detach(mMasterFragment);
        }

        transaction.commit();

        mDetailFragment = detailFragment;
    }

    public void clearDetailFragment (final boolean animated) {
        final FragmentManager fragmentManager = getFragmentManager();

        if (animated) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void setDetailViewTitle (final CharSequence title) { }

    public void setDetailViewSubtitle (final CharSequence subtitle) { }


    // ================================================================================
    // Properties
    // ================================================================================

    public abstract boolean isSplitViewLayout ();
}
