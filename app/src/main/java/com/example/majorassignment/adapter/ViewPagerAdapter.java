package com.example.majorassignment.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.majorassignment.fragment.FragmentHome;
import com.example.majorassignment.fragment.FragmentProfile;
import com.example.majorassignment.fragment.FragmentSongs;
import com.example.majorassignment.fragment.MapsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MapsFragment();
            case 1: return new FragmentSongs();
            case 2: return new FragmentProfile();
            default: return new FragmentHome();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
