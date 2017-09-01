package com.example.dell.attendit.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.attendit.Classes.Timetable;
import com.example.dell.attendit.Classes.TimetableDB;
import com.example.dell.attendit.R;

import java.util.List;

/**
 * Created by dell on 25/06/2016.
 */

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.MyViewHolder>{

    private List<Timetable> eventslist;
    public Context context;
    public  RecyclerView recyclerView;
    public TimetableDB timetableDB;
    public TimetableAdapter mAdapter;
    String day;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title,venue,start,end,date,remembered;
        public MyViewHolder(View view){
            super(view);
            title=(TextView)view.findViewById(R.id.subject_title);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/andikanewbasic.ttf");
            title.setTypeface(tf);
        }
    }

    public TimetableAdapter(List<Timetable> eventslist, Context context, RecyclerView recyclerView,String day){
        this.eventslist=eventslist;
        this.context = context;
        this.recyclerView = recyclerView;
        this.day=day;
        timetableDB = new TimetableDB(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_list_row,parent,false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(final MyViewHolder holder,int position) {
        final Timetable movie = eventslist.get(position);
        holder.title.setText(movie.getName());

    }

    public void deleteTimetable(final String name,final String day){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set dialog message
        alertDialogBuilder
                .setCancelable(true).setMessage("Are you sure you want to delete " + name + "?")
                .setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                timetableDB.deleteTimetable(name, day);
                                setUpSubjectList(day);
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

    public void setUpSubjectList(String day){
        eventslist = timetableDB.getAllTimeTable(day);
        mAdapter= new TimetableAdapter(eventslist,context,recyclerView,day);
        recyclerView.setAdapter(mAdapter);
    }

    public int getItemCount(){
        return eventslist.size();
    }

}
