package com.example.dell.attendit.Adapters;

/**
 * Created by dell on 24/06/2016.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dell.attendit.Timetable.Friday;
import com.example.dell.attendit.Timetable.Monday;
import com.example.dell.attendit.Timetable.Saturday;
import com.example.dell.attendit.Timetable.Sunday;
import com.example.dell.attendit.Timetable.Thursday;
import com.example.dell.attendit.Timetable.Tuesday;
import com.example.dell.attendit.Timetable.Wednesday;

public class TimetablePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TimetablePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Monday();
            case 1:
                return new Tuesday();
            case 2:
                return new Wednesday();
            case 3:
                return new Thursday();
            case 4:
                return new Friday();
            case 5:
                return new Saturday();
            case 6:
                return new Sunday();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
