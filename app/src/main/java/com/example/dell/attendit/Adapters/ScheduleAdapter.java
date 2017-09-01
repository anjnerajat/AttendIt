package com.example.dell.attendit.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendit.Classes.CircleDisplay;
import com.example.dell.attendit.Classes.ExtraDB;
import com.example.dell.attendit.Classes.Subject;
import com.example.dell.attendit.Classes.SubjectsDB;
import com.example.dell.attendit.Classes.Timetable;
import com.example.dell.attendit.Classes.TimetableDB;
import com.example.dell.attendit.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleAdapter  extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>{
    private List<Subject> eventslist = new ArrayList<>();
    public List<Timetable> todayslist = new ArrayList<>();
    Context context;
    ScheduleAdapter mAdapter;
    RecyclerView recyclerView;
    SubjectsDB subjectsDB;
    TimetableDB timetableDB;
    ExtraDB  extraDB;
    private int expandedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, present, total, absent, cancelled;
        public ImageView absentButton, presentButton, cancelButton, statusImage;
        public CircleDisplay mCircleDisplay;
        public RelativeLayout expandablePanel;
        public MyViewHolder(View view){
            super(view);
            title=(TextView)view.findViewById(R.id.subject_title);
            present=(TextView)view.findViewById(R.id.present);
            total=(TextView)view.findViewById(R.id.total);
            absent=(TextView)view.findViewById(R.id.absent);
            cancelled=(TextView)view.findViewById(R.id.cancelled);
            //percentage=(TextView)view.findViewById(R.id.percentage);
            presentButton = (ImageView)view.findViewById(R.id.presentButton);
            absentButton = (ImageView)view.findViewById(R.id.absentButton);
            cancelButton = (ImageView)view.findViewById(R.id.cancelButton);
            statusImage = (ImageView)view.findViewById(R.id.statusImage);
            mCircleDisplay = (CircleDisplay)view.findViewById(R.id.circleDisplay);
            expandablePanel = (RelativeLayout)view.findViewById(R.id.expandablePanel);
        }
    }

    public ScheduleAdapter(List<Subject> eventslist,Context context,RecyclerView recyclerView){
        this.eventslist=eventslist;
        this.context=context;
        this.recyclerView=recyclerView;
        subjectsDB=new SubjectsDB(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Subject movie = eventslist.get(position);
        holder.title.setText(movie.getName());
        holder.present.setText("Present=" + movie.getPresent() + "");
        holder.total.setText("Total=" + movie.getTotal() + "");
        holder.absent.setText("Absent=" + movie.getAbsent() + "");
        holder.cancelled.setText("Cancel=" + movie.getCancelled() + "");

        if(movie.getStatus() == 0){
            holder.statusImage.setImageResource(R.drawable.roundedinfobutton);
        }
        else if(movie.getStatus() == 1){
            holder.statusImage.setImageResource(R.drawable.checkmarkbutton);
        }
        else if(movie.getStatus() == 2){
            holder.statusImage.setImageResource(R.drawable.deletebutton);
        }
        else if(movie.getStatus() == 3){
            holder.statusImage.setImageResource(R.drawable.disabled);
        }
        //holder.percentage.setText(String.format("Percentage = %02.1f", movie.getPercentage() ));

        if (position == expandedPosition) {
            holder.expandablePanel.setVisibility(View.VISIBLE);
            holder.statusImage.setVisibility(View.GONE);
        } else {
            holder.expandablePanel.setVisibility(View.GONE);
            holder.statusImage.setVisibility(View.VISIBLE);
        }

        holder.statusImage.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View view) {
                        // Check for an expanded view, collapse if you find one
                        if (expandedPosition >= 0) {
                            int prev = expandedPosition;
                            notifyItemChanged(prev);
                        }
                        // Set the current position to "expanded"
                        expandedPosition = position;
                        notifyItemChanged(expandedPosition);
                        //Toast.makeText(context, "Clicked: ", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        holder.presentButton.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View view) {
                        //subjectsDB.isPresent(movie.getName());
                        subjectsDB.setStatus(movie.getName(), 1);
                        setUpSubjectList();
                    }
                }
        );

        holder.absentButton.setOnClickListener(
                new ImageView.OnClickListener(){
                    public void onClick(View view){
                        //subjectsDB.isAbsent(movie.getName());
                        subjectsDB.setStatus(movie.getName(), 2);
                        setUpSubjectList();
                    }
                }
        );

        holder.cancelButton.setOnClickListener(
                new ImageView.OnClickListener() {
                    public void onClick(View view) {
                        //subjectsDB.isCancel(movie.getName());
                        subjectsDB.setStatus(movie.getName(), 3);
                        setUpSubjectList();
                    }
                }
        );

        holder.mCircleDisplay.setAnimDuration(0);
        holder.mCircleDisplay.setValueWidthPercent(25f);
        holder.mCircleDisplay.setFormatDigits(1);
        holder.mCircleDisplay.setDimAlpha(100);
        if(movie.getPercentage()<movie.getMinimum()){
            holder.mCircleDisplay.setColor(Color.rgb(227,0,34));
        }
        else{
            holder.mCircleDisplay.setColor(Color.rgb(70,210,70));
        }
        holder.mCircleDisplay.setTouchEnabled(false);
        holder.mCircleDisplay.setUnit("%");
        holder.mCircleDisplay.setStepSize(20.0f);
        holder.mCircleDisplay.setTextSize(15.0f);
        holder.mCircleDisplay.showValue((float)movie.getPercentage(), 100f, true);
    }
/*
    public void deleteSubject(final String name){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

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
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.edit_subject_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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
                                String addedSubjectName;
                                if (!userInput.getText().toString().equals("") || seekBar.getProgress()==0) {
                                    addedSubjectName = userInput.getText().toString();
                                    if(!subjectsDB.isSubjectPresent(addedSubjectName) || addedSubjectName.equals(name)){
                                        subjectsDB.editSubject(name,addedSubjectName,seekBar.getProgress());
                                        setUpSubjectList();
                                        //workshopList = subjectsDB.getAllSubjects();
                                        //mAdapter.notifyDataSetChanged();
                                    }
                                    else{
                                        Toast.makeText(context, addedSubjectName + " already in Subjects list", Toast.LENGTH_SHORT).show();
                                    }
                                    //workshopList.add(new Subject(addedSubjectName, seekBar.getProgress()));
                                    //mAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(context,"Required feilds missing", Toast.LENGTH_SHORT).show();
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
*/

    public void setUpSubjectList(){
        Calendar calendar = Calendar.getInstance();
        subjectsDB = new SubjectsDB(context);
        timetableDB = new TimetableDB(context);
        extraDB = new ExtraDB(context);
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

        eventslist = new ArrayList<>();

        for(int i=0; i<todayslist.size(); i++){
            Timetable timetable = todayslist.get(i);
            Subject subject = subjectsDB.getSubjectDetails(timetable.getName());
            eventslist.add(subject);
        }

        if(extraDB.numberOfRows() != 0){
            List<Timetable> extrasList = extraDB.getAllExtras();
            for(int i=0; i<extrasList.size(); i++){
                Timetable extra = extrasList.get(i);
                Subject subject = subjectsDB.getSubjectDetails(extra.getName());
                eventslist.add(subject);
            }
        }

        mAdapter= new ScheduleAdapter(eventslist,context,recyclerView);
        recyclerView.setAdapter(mAdapter);
    }



    public int getItemCount(){
        return eventslist.size();
    }
}

