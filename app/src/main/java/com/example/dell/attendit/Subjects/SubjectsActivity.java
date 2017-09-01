package com.example.dell.attendit.Subjects;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendit.Adapters.SubjectAdapter;
import com.example.dell.attendit.Classes.Subject;
import com.example.dell.attendit.Classes.SubjectsDB;
import com.example.dell.attendit.R;
import com.example.dell.attendit.Timetable.TimetableActivity;

import java.util.ArrayList;
import java.util.List;

public class SubjectsActivity extends AppCompatActivity {

    private SubjectsDB subjectsDB ;
    public List<Subject> workshopList = new ArrayList<>();
    public RecyclerView recyclerView;
    public SubjectAdapter mAdapter;
    String addedSubjectName;
    public ImageView done;
    public TextView noSubjects;
    public CoordinatorLayout coordinatorLayout;
    public Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=(RecyclerView)findViewById(R.id.subject_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        snackbar = Snackbar.make(coordinatorLayout,"Long press to delete subject",Snackbar.LENGTH_LONG);
        snackbar.show();

        noSubjects = (TextView)findViewById(R.id.no_timetable_text);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Subject movie = workshopList.get(position);
                //deleteSubject(movie.getName());
            }

            @Override
            public void onLongClick(View view, int position) {
                final Subject movie = workshopList.get(position);
                deleteSubject(movie.getName());
            }
        }));

        subjectsDB = new SubjectsDB(getApplicationContext());

        setUpSubjectList();

        done = (ImageView)findViewById(R.id.done);
        done.setOnClickListener(
                new ImageView.OnClickListener(){
                    public void onClick(View view){
                        startActivity(new Intent(SubjectsActivity.this, TimetableActivity.class));
                    }
                }
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                addSubject();
            }
        });

    }

    public void setUpSubjectList(){
        workshopList = subjectsDB.getAllSubjects();
        if(workshopList.size()==0)
            noSubjects.setVisibility(View.VISIBLE);
        else{
            noSubjects.setVisibility(View.GONE);
        }
        mAdapter= new SubjectAdapter(workshopList,this,recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    public void deleteSubject(final String name){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setCancelable(true).setMessage("Are you sure you want to delete " + name + "?")
                .setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                subjectsDB.deleteSubject(name);
                                setUpSubjectList();
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

    public void editSubject(final String name, int minimum){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.edit_subject_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        final SeekBar seekBar = (SeekBar) promptsView.findViewById(R.id.minimum_seekbar);
        final TextView seekvalue = (TextView) promptsView.findViewById(R.id.minimum_seek_value);

        userInput.setText(name);
        seekvalue.setText(minimum+"%");
        seekBar.setProgress(minimum);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    int value = 0;

                    @Override
                    protected Object clone() throws CloneNotSupportedException {
                        return super.clone();
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value = progress;
                        seekvalue.setText(value+"%");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekvalue.setText(value+"%");
                    }
                }
        );

        // set dialog message
        alertDialogBuilder
                .setCancelable(true).setTitle("Edit Subject")
                .setPositiveButton("EDIT",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!userInput.getText().toString().equals("") || seekBar.getProgress()==0) {
                                    addedSubjectName = userInput.getText().toString();
                                    if(!subjectsDB.isSubjectPresent(addedSubjectName) || addedSubjectName.equals(name)){
                                        subjectsDB.editSubject(name,addedSubjectName,seekBar.getProgress());
                                        setUpSubjectList();
                                        //workshopList = subjectsDB.getAllSubjects();
                                        //mAdapter.notifyDataSetChanged();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),addedSubjectName + " already in Subjects list", Toast.LENGTH_SHORT).show();
                                    }
                                    //workshopList.add(new Subject(addedSubjectName, seekBar.getProgress()));
                                    //mAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Required feilds missing", Toast.LENGTH_SHORT).show();
                                }
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

    public void addSubject(){

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_subject_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        final SeekBar seekBar = (SeekBar) promptsView.findViewById(R.id.minimum_seekbar);
        final TextView seekvalue = (TextView) promptsView.findViewById(R.id.minimum_seek_value);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    int value = 0;

                    @Override
                    protected Object clone() throws CloneNotSupportedException {
                        return super.clone();
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value = progress;
                        seekvalue.setText(value+"%");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekvalue.setText(value+"%");
                    }
                }
        );

        // set dialog message
        alertDialogBuilder
                .setCancelable(true).setTitle("Add Subject")
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!userInput.getText().toString().equals("") || seekBar.getProgress()==0) {
                                    addedSubjectName = userInput.getText().toString();
                                    if(!subjectsDB.isSubjectPresent(addedSubjectName)){
                                        subjectsDB.insertSubject(addedSubjectName, 3, 1, 0, 0, 0, 0, seekBar.getProgress(), 0.00f);
                                        setUpSubjectList();
                                        //workshopList = subjectsDB.getAllSubjects();
                                        //mAdapter.notifyDataSetChanged();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),addedSubjectName + " already in Subjects list", Toast.LENGTH_SHORT).show();
                                    }
                                    //workshopList.add(new Subject(addedSubjectName, seekBar.getProgress()));
                                    //mAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Required feilds missing", Toast.LENGTH_SHORT).show();
                                }
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

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private SubjectsActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SubjectsActivity.ClickListener clickListener) {
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
