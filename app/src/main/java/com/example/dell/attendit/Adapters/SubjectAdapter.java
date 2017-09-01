package com.example.dell.attendit.Adapters;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.graphics.Typeface;
        import android.media.Image;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.dell.attendit.Classes.Subject;
        import com.example.dell.attendit.Classes.SubjectsDB;
        import com.example.dell.attendit.Classes.TimetableDB;
        import com.example.dell.attendit.R;

        import java.util.List;

public class SubjectAdapter  extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder>{
    private List<Subject> eventslist;
    Context context;
    SubjectAdapter mAdapter;
    RecyclerView recyclerView;
    SubjectsDB subjectsDB;
    TimetableDB timetableDB;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        ImageView edit;
        public MyViewHolder(View view){
            super(view);
            title=(TextView)view.findViewById(R.id.subject_title);
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/andikanewbasic.ttf");
            title.setTypeface(tf);

            edit=(ImageView)view.findViewById(R.id.editImage);
        }
    }

    public SubjectAdapter(List<Subject> eventslist,Context context,RecyclerView recyclerView){
        this.eventslist=eventslist;
        this.context=context;
        this.recyclerView=recyclerView;
        subjectsDB=new SubjectsDB(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(final MyViewHolder holder,int position) {
        final Subject movie = eventslist.get(position);
        holder.title.setText(movie.getName());
        holder.edit.setOnClickListener(
                new ImageView.OnClickListener(){
                    public void onClick(View view){
                        editSubject(movie.getName(),movie.getMinimum());
                    }
                }
        );
    }

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

    public void setUpSubjectList(){
        eventslist = subjectsDB.getAllSubjects();
        mAdapter= new SubjectAdapter(eventslist,context,recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    public int getItemCount(){
        return eventslist.size();
    }
}


