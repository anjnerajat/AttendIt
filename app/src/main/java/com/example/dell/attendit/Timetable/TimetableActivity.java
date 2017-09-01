package com.example.dell.attendit.Timetable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendit.Adapters.TimetablePagerAdapter;
import com.example.dell.attendit.Adapters.addSubjectTimetableAdapter;
import com.example.dell.attendit.Classes.DividerItemDecorator;
import com.example.dell.attendit.Classes.Subject;
import com.example.dell.attendit.Classes.SubjectsDB;
import com.example.dell.attendit.Classes.TimetableDB;
import com.example.dell.attendit.Home.HomeActivity;
import com.example.dell.attendit.R;

import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {

    List<Subject> workshopList = new ArrayList<>();
    TabLayout tabLayout;
    public ImageView done;
    public ViewPager viewPager;
    public TimetablePagerAdapter adapter;
    public CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        tabLayout= (TabLayout)findViewById(R.id.timetable_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("MON"));
        tabLayout.addTab(tabLayout.newTab().setText("TUE"));
        tabLayout.addTab(tabLayout.newTab().setText("WED"));
        tabLayout.addTab(tabLayout.newTab().setText("THU"));
        tabLayout.addTab(tabLayout.newTab().setText("FRI"));
        tabLayout.addTab(tabLayout.newTab().setText("SAT"));
        tabLayout.addTab(tabLayout.newTab().setText("SUN"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.events_pager);
        adapter = new TimetablePagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        done = (ImageView)findViewById(R.id.done);
        done.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(TimetableActivity.this, HomeActivity.class));
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addSubjectTimetable();
            }
        });

        Snackbar snackbar = Snackbar.make(coordinatorLayout,"Long press to delete subject",Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void addSubjectTimetable(){

        RecyclerView recyclerView;
        addSubjectTimetableAdapter mAdapter;
        SubjectsDB subjectsDB ;

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_subject_timetable_prompt, null);

        recyclerView=(RecyclerView)promptsView.findViewById(R.id.add_subject_timetable_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecorator(getApplicationContext(), LinearLayoutManager.VERTICAL));

        subjectsDB = new SubjectsDB(getApplicationContext());
        workshopList = subjectsDB.getAllSubjects();

        mAdapter= new addSubjectTimetableAdapter(workshopList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Toast.makeText(getApplicationContext(), "Selected!!!", Toast.LENGTH_SHORT).show();
                Subject subject = workshopList.get(position);
                int selectedTabPosition=tabLayout.getSelectedTabPosition();
                inserSubject(subject.getName(),selectedTabPosition);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder.setCancelable(true);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        alertDialog.setOnCancelListener(
                new AlertDialog.OnCancelListener(){
                    public void onCancel(DialogInterface dialogInterface){

                    }
                }
        );

    }

    public void inserSubject(String name,int selectedTab){
        TimetableDB timetableDB = new TimetableDB(getApplicationContext());
        switch (selectedTab){
            case 0: timetableDB.insertMonday(name);
                break;
            case 1: timetableDB.insertTuesday(name);
                break;
            case 2:
                timetableDB.insertWednesday(name);
                break;
            case 3: timetableDB.insertThursday(name);
                break;
            case 4: timetableDB.insertFriday(name);
                break;
            case 5: timetableDB.insertSaturday(name);
                break;
            case 6: timetableDB.insertSunday(name);
                break;
        }
    }

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private TimetableActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TimetableActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
