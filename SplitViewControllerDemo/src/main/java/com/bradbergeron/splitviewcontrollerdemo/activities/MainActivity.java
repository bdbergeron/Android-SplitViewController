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

package com.bradbergeron.splitviewcontrollerdemo.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bradbergeron.splitviewcontrollerdemo.R;
import com.bradbergeron.splitviewcontrollerdemo.fragments.DrawerFragment;

public class MainActivity extends ActionBarActivity
        implements DrawerFragment.DrawerItemSelectionListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerFragment mDrawerFragment;


    // ================================================================================
    // Lifecycle
    // ================================================================================

    @Override
    protected void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activty_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                                                  R.string.drawer_close) {
            @Override
            public void onDrawerOpened (final View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed (final View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerFragment = (DrawerFragment) getFragmentManager().findFragmentById(R.id.drawer);
    }

    @Override
    protected void onPostCreate (final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
        setNavigationDrawerEnabled(mDrawerToggle.isDrawerIndicatorEnabled());
    }

    @Override
    protected void onStart () {
        super.onStart();

        Log.d(TAG, "onStart");

        mDrawerFragment.setDrawerItemSelectionListener(this);
    }

    @Override
    protected void onStop () {
        Log.d(TAG, "onStop");

        mDrawerFragment.setDrawerItemSelectionListener(null);

        super.onStop();
    }

    @Override
    public void onConfigurationChanged (final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed () {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);

            return;
        }

        final FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();

            return;
        }

        super.onBackPressed();
    }

    // ================================================================================
    // Options Menu
    // ================================================================================

    @Override
    public boolean onCreateOptionsMenu (final Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (final Menu menu) {
        final boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.START);
        menu.findItem(R.id.mainMenu_aboutMenuItem).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (final MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home: {
                getFragmentManager().popBackStack();

                return true;
            }
            case R.id.mainMenu_aboutMenuItem: {
                startActivity(new Intent(this, AboutActivity.class));

                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }


    // ================================================================================
    // Navigation Drawer
    // ================================================================================

    public void setNavigationDrawerEnabled (final boolean enabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(enabled);

        if (enabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.START);
        } else {
            if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
                mDrawerLayout.closeDrawer(Gravity.START);
            }

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.START);
        }
    }

    @Override
    public void onDrawerItemSelected (final DrawerFragment.FragmentDrawerItem fragmentDrawerItem) {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }

        final FragmentManager fragmentManager = getFragmentManager();
        final String fragmentClassName = fragmentDrawerItem.getFragmentClass().getName();

        Fragment newFragment = fragmentManager.findFragmentByTag(fragmentClassName);

        if (newFragment == null) {
            newFragment = Fragment.instantiate(this, fragmentClassName);
        } else if (newFragment.isAdded()) {
            return;
        } else if (fragmentDrawerItem.isDefaultItem() &&
                   fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack("Drawer", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, newFragment, fragmentClassName);

        if (!fragmentDrawerItem.isDefaultItem()) {
            transaction.addToBackStack("Drawer");
        }

        transaction.commit();
    }
}
