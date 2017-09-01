package com.example.dell.attendit.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendit.Adapters.ScheduleAdapter;
import com.example.dell.attendit.Adapters.addSubjectTimetableAdapter;
import com.example.dell.attendit.Classes.DividerItemDecorator;
import com.example.dell.attendit.Classes.ExtraDB;
import com.example.dell.attendit.Classes.Subject;
import com.example.dell.attendit.Classes.SubjectsDB;
import com.example.dell.attendit.Classes.SwipeableRecyclerviewTouchListener;
import com.example.dell.attendit.Classes.Timetable;
import com.example.dell.attendit.Classes.TimetableDB;
import com.example.dell.attendit.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    public SubjectsDB subjectsDB ;
    public TimetableDB timetableDB;
    public ExtraDB extraDB;
    public List<Subject> subjectslist = new ArrayList<>();
    public List<Timetable> todayslist = new ArrayList<>();
    public RecyclerView recyclerView;
    public ScheduleAdapter mAdapter;
    public Button nextDay;
    public FloatingActionButton fab;
    public ImageView addExtraClass;
    public Menu menu;
    public TextView holidayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView)findViewById(R.id.schedule_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

/*
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Subject movie = workshopList.get(position);
                //deleteSubject(movie.getName());
            }

            @Override
            public void onLongClick(View view, int position) {
                //final Subject movie = workshopList.get(position);
                //editSubject(movie.getName(),movie.getMinimum());
            }
        }));
*/

        nextDay = (Button)findViewById(R.id.nextDay);
        holidayText = (TextView)findViewById(R.id.holiday_text);
        addExtraClass = (ImageView)findViewById(R.id.addExtraClass);
        subjectsDB = new SubjectsDB(getApplicationContext());
        timetableDB = new TimetableDB(getApplicationContext());
        extraDB = new ExtraDB(this);

        setUpSubjectList();

        dateChangeDetection();

        nextDay.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        subjectsDB.resetStatus();
                        extraDB.deleteAllExtra();
                        setUpSubjectList();
                    }
                }
        );

        addExtraClass.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View view) {
                        addExtraSubjectPrompt();
                    }
                }
        );
/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExtraSubjectPrompt();
            }
        });*/
    }

    public void addExtraSubjectPrompt(){
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
        final List<Subject> workshopList = subjectsDB.getAllSubjects();

        mAdapter= new addSubjectTimetableAdapter(workshopList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Toast.makeText(getApplicationContext(), "Selected!!!", Toast.LENGTH_SHORT).show();
                Subject subject = workshopList.get(position);
                extraDB.insertExtra(subject.getName());
                setUpSubjectList();
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
    }

    public void dateChangeDetection(){
        Calendar c = Calendar.getInstance();
        int thisDay = c.get(Calendar.DAY_OF_WEEK); //You can chose something else to compare too, such as DATE..
        long todayMillis = c.getTimeInMillis(); //We might need this a bit later.

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long last = prefs.getLong("date", 0); //If we don't have a saved value, use 0.
        c.setTimeInMillis(last);
        int lastDay = c.get(Calendar.DAY_OF_WEEK);

        if( last==0 || lastDay != thisDay ){
            //New day, update TextView and preference:
            SharedPreferences.Editor edit = prefs.edit();
            edit.putLong("date", todayMillis);
            edit.commit();
            subjectsDB.resetStatus();
            extraDB.deleteAllExtra();
            setUpSubjectList();
        }
    }

    public void setUpSubjectList(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                todayslist = timetableDB.getAllTimeTable("sunday");
                break;
            case Calendar.MONDAY:
                todayslist = timetableDB.getAllTimeTable("monday");
                break;
            case Calendar.TUESDAY:
                todayslist = timetableDB.getAllTimeTable("tuesday");
                break;
            case Calendar.WEDNESDAY:
                todayslist = timetableDB.getAllTimeTable("wednesday");
                break;
            case Calendar.THURSDAY:
                todayslist = timetableDB.getAllTimeTable("thursday");
                break;
            case Calendar.FRIDAY:
                todayslist = timetableDB.getAllTimeTable("friday");
                break;
            case Calendar.SATURDAY:
                todayslist = timetableDB.getAllTimeTable("saturday");
                break;
        }

        subjectslist = new ArrayList<>();

        for(int i=0; i<todayslist.size(); i++){
            Timetable timetable = todayslist.get(i);
            Subject subject = subjectsDB.getSubjectDetails(timetable.getName());
            subjectslist.add(subject);
        }

        if(extraDB.numberOfRows() != 0){
            List<Timetable> extrasList = extraDB.getAllExtras();
            for(int i=0; i<extrasList.size(); i++){
                Timetable extra = extrasList.get(i);
                Subject subject = subjectsDB.getSubjectDetails(extra.getName());
                subjectslist.add(subject);
            }
        }

        mAdapter= new ScheduleAdapter(subjectslist,this,recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private HomeActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeActivity.ClickListener clickListener) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.allPresent) {
            for (int i = 0; i < subjectslist.size(); i++) {
                Subject subject = subjectslist.get(i);
                subjectsDB.setStatus(subject.getName(), 1);
                setUpSubjectList();
            }
            return true;
        }
        else if (id == R.id.allAbsent) {
            for (int i = 0; i < subjectslist.size(); i++) {
                Subject subject = subjectslist.get(i);
                subjectsDB.setStatus(subject.getName(), 2);
                setUpSubjectList();
            }
            return true;
        }
        else if (id == R.id.allCancel) {
            for (int i = 0; i < subjectslist.size(); i++) {
                Subject subject = subjectslist.get(i);
                subjectsDB.setStatus(subject.getName(), 3);
                setUpSubjectList();
            }
            return true;
        }
        else if (id == R.id.holiday) {
            recyclerView.setVisibility(View.GONE);
            holidayText.setVisibility(View.VISIBLE);
            for(int i=0; i<subjectslist.size(); i++){
                Subject subject = subjectslist.get(i);
                subjectsDB.setStatus(subject.getName(), 0);
                setUpSubjectList();
            }
            MenuItem holiday = menu.findItem(R.id.holiday);
            holiday.setVisible(false);
            MenuItem allPresent = menu.findItem(R.id.allPresent);
            allPresent.setVisible(false);
            MenuItem allAbsent = menu.findItem(R.id.allAbsent);
            allAbsent.setVisible(false);
            MenuItem allCancel = menu.findItem(R.id.allCancel);
            allCancel.setVisible(false);
            MenuItem unmarkHoliday = menu.findItem(R.id.unMarkHoliday);
            unmarkHoliday.setVisible(true);
            return true;
        }
        else if (id == R.id.unMarkHoliday) {
            recyclerView.setVisibility(View.VISIBLE);
            holidayText.setVisibility(View.GONE);
            MenuItem holiday = menu.findItem(R.id.holiday);
            holiday.setVisible(true);
            MenuItem allPresent = menu.findItem(R.id.allPresent);
            allPresent.setVisible(true);
            MenuItem allAbsent = menu.findItem(R.id.allAbsent);
            allAbsent.setVisible(true);
            MenuItem allCancel = menu.findItem(R.id.allCancel);
            allCancel.setVisible(true);
            MenuItem unmarkHoliday = menu.findItem(R.id.unMarkHoliday);
            unmarkHoliday.setVisible(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
