package com.example.dell.attendit.Adapters;

/**
 * Created by dell on 25/06/2016.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.attendit.Classes.Subject;
import com.example.dell.attendit.R;

import java.util.List;

public class addSubjectTimetableAdapter extends RecyclerView.Adapter<addSubjectTimetableAdapter.MyViewHolder>{
    private List<Subject> eventslist;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public MyViewHolder(View view){
            super(view);
            title=(TextView)view.findViewById(R.id.subject_title);
        }
    }

    public addSubjectTimetableAdapter(List<Subject> eventslist){
        this.eventslist=eventslist;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_subject_timetable_list_row,parent,false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(final MyViewHolder holder,int position) {
        final Subject movie = eventslist.get(position);
        holder.title.setText(movie.getName());
    }

    public int getItemCount(){
        return eventslist.size();
    }
}
