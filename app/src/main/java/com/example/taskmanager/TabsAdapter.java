package com.example.taskmanager;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private List<Fragment> fragments = new ArrayList<>();
    public TabsAdapter(FragmentManager fm, int noOfTabs){
        super(fm);
        this.mNumOfTabs = noOfTabs;
    }
    public int getCount() {
        return mNumOfTabs;
    }

//    public Fragment getItem(int position){
//        switch (position){
//            case 0:
//                TodayTasksFragment todayTasks= new TodayTasksFragment();
//                return todayTasks;
//            case 1:
//                AllTasksFragment allTasks= new AllTasksFragment();
//                return allTasks;
//            default:
//                return null;
//        }
//    }
public Fragment getItem(int position) {
    switch (position) {
            case 0:
                TodayTasksFragment todayTasks= new TodayTasksFragment();
                return todayTasks;
            case 1:
                AllTasksFragment allTasks= new AllTasksFragment();
                return allTasks;
            default:
                return null;
    }
}
    public void replaceFragment(int position, Fragment fragment) {
        fragments.set(position, fragment);
        notifyDataSetChanged();
    }

}
