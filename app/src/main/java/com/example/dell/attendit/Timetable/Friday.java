package com.example.dell.attendit.Timetable;

/**
 * Created by dell on 24/06/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.attendit.Adapters.TimetableAdapter;
import com.example.dell.attendit.Classes.Timetable;
import com.example.dell.attendit.Classes.TimetableDB;
import com.example.dell.attendit.R;

import java.util.ArrayList;
import java.util.List;

public class Friday extends Fragment{

    public List<Timetable> workshopList = new ArrayList<>();
    public RecyclerView recyclerView;
    public TimetableAdapter mAdapter;
    public TimetableDB timetableDB;
    TextView noTimetable;

    public Friday() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.timetable_day_fragment, container, false);


        timetableDB = new TimetableDB(getContext());
        recyclerView=(RecyclerView)v.findViewById(R.id.timetable_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noTimetable = (TextView)v.findViewById(R.id.no_timetable_text);
        noTimetable.setText(getString(R.string.no_timetable_string)+"Friday");
        //timetableDB.insertMonday("Data Structures");
        //timetableDB.insertMonday("Designing Algorithms");

        getSubjects();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Timetable timetable = workshopList.get(position);
                deleteSubjectTimetable(timetable.getName(), "friday");
            }
        }));

        return v;
    }

    public void deleteSubjectTimetable(final String name, final String day){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set dialog message
        alertDialogBuilder
                .setCancelable(true).setMessage("Are you sure you want to delete " + name + "?")
                .setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timetableDB.deleteTimetable(name, day);
                                getSubjects();
                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void getSubjects(){
        workshopList = timetableDB.getAllTimeTable("friday");
        if(workshopList.size()==0)
            noTimetable.setVisibility(View.VISIBLE);
        else
            noTimetable.setVisibility(View.GONE);
        mAdapter= new TimetableAdapter(workshopList,this.getContext(),recyclerView,"friday");
        recyclerView.setAdapter(mAdapter);
    }

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private Friday.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final Friday.ClickListener clickListener) {
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
