package org.ctdworld.propath.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.SchoolReview;

import java.util.ArrayList;
import java.util.List;

public class AdapterSchoolReview extends RecyclerView.Adapter<AdapterSchoolReview.MyViewHolder>{
    private final String TAG = AdapterSchoolReview.class.getSimpleName();
    private Context context;
    private List<SchoolReview> mLayouts;


    public static final int VALUE_MODE_CREATE = 1;
    public static final int VALUE_MODE_EDIT = 2;

    int MODE_CRETE_OR_EDIT;

    public AdapterSchoolReview(Context context, int modeCreateOrEdit) {

        this.context=context;
        this.mLayouts = new ArrayList<>();

        this.MODE_CRETE_OR_EDIT = modeCreateOrEdit;

        if (MODE_CRETE_OR_EDIT != VALUE_MODE_EDIT)
            this.mLayouts.add(new SchoolReview());  //# adding 1 layout by default
    }



   public void addLayout()
    {
        mLayouts.add(new SchoolReview());
        notifyDataSetChanged();
    }


    public void updateProgressReportList(List<SchoolReview> schoolReviewList)
    {
        mLayouts.addAll(schoolReviewList);
        notifyDataSetChanged();
    }


    public List<SchoolReview> getReviewList()
    {
        ArrayList<SchoolReview> dataList  = new ArrayList<>();

        if (mLayouts != null)
        {
            for (int i=0 ; i<mLayouts.size() ; i++)
            {
                SchoolReview schoolReview = mLayouts.get(i);
                if (schoolReview !=  null)
                {
                    if (schoolReview.getSubject() != null && !schoolReview.getSubject().contains("null") && !schoolReview.getSubject().isEmpty()  && schoolReview.getGrade() !=  null && !schoolReview.getGrade().contains("null") && !schoolReview.getGrade().isEmpty())
                    {
                    Log.i(TAG,"position = "+i+"  ********************************************************************");
                        Log.i(TAG,"subject = "+schoolReview.getSubject());
                        Log.i(TAG,"grade = "+schoolReview.getGrade());
                        Log.i(TAG,"subject id = "+schoolReview.getSubjectID());
                        dataList.add(schoolReview);
                    }
                    else
                        Log.e(TAG,"schoolReview is null or subject is empty or grade is null or grade is empty in getReviewList() method ,  position = "+i);
                }
                else
                    Log.e(TAG,"schoolReview is null in getReviewList() method");
            }
        }
        else
            Log.e(TAG,"mLayouts is null in geReviewList() method ");


        return dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_school_review_items, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Log.i(TAG,"position......"+ position);
        Log.i(TAG,"position...... holder.getAdapterPosition()"+ holder.getAdapterPosition());

        final SchoolReview schoolReview = mLayouts.get(holder.getAdapterPosition());

        if (MODE_CRETE_OR_EDIT != VALUE_MODE_EDIT)
        {
            schoolReview.setSubject(holder.editSubjectName.getText().toString().trim());
        }


        if (MODE_CRETE_OR_EDIT == VALUE_MODE_EDIT)
        {
            holder.editSubjectName.setText(schoolReview.getSubject());
            setGrade(schoolReview.getGrade(),holder);
        }


        holder.nButtonSelect.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {


                  holder.nButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                  schoolReview.setGrade("n");

                  setButtonUnselected(holder.aButtonSelect, holder.mButtonSelect, holder.eButtonSelect);

              }
          });
        holder.aButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.aButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
                schoolReview.setGrade("a");

                setButtonUnselected(holder.nButtonSelect, holder.mButtonSelect, holder.eButtonSelect);

            }
        });
        holder.mButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.mButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                schoolReview.setGrade("m");

                setButtonUnselected(holder.nButtonSelect, holder.aButtonSelect, holder.eButtonSelect);


            }
        });
        holder.eButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                holder.eButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorBlue));
                schoolReview.setGrade("e");

                setButtonUnselected(holder.nButtonSelect, holder.aButtonSelect, holder.mButtonSelect);

            }
        });

       // mLayouts.set(holder.getAdapterPosition(), schoolReview);
        handleEditBox(holder);

    }

    @Override
    public int getItemCount() {
        if (mLayouts != null)
            return mLayouts.size();
        else
            return 0;
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public EditText editSubjectName;
        public Button eButtonSelect,nButtonSelect,aButtonSelect,mButtonSelect;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            editSubjectName = itemView.findViewById(R.id.subjectName);
            nButtonSelect = itemView.findViewById(R.id.nButtonSelect);
            aButtonSelect = itemView.findViewById(R.id.aButtonSelect);
            mButtonSelect = itemView.findViewById(R.id.mButtonSelect);
            eButtonSelect = itemView.findViewById(R.id.eButtonSelect);
        }
        //  public void bind(String text) {textView.setText(text); }


    }


    public void setButtonUnselected(Button button1, Button button2, Button button3)
    {
        button1.setBackgroundColor(Color.LTGRAY);
        button2.setBackgroundColor(Color.LTGRAY);
        button3.setBackgroundColor(Color.LTGRAY);
    }

   /* public int getTotalLayout()
    {
       return layoutSize;
    }*/

   private void handleEditBox(final MyViewHolder holder)
   {
       int position = holder.getAdapterPosition();
       final EditText editText = holder.editSubjectName;
       editText.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SchoolReview schoolReview = mLayouts.get(holder.getAdapterPosition());
                schoolReview.setSubject(charSequence.toString().trim());
                mLayouts.set(holder.getAdapterPosition(), schoolReview);
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
   }





   // setting grade if review is being edited
   private void setGrade(String grade, AdapterSchoolReview.MyViewHolder holder)
   {
       if (grade == null || grade.isEmpty())
           return;


       switch (grade)
       {
           case "n" :
               holder.nButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
               setButtonUnselected(holder.aButtonSelect, holder.mButtonSelect, holder.eButtonSelect);
               break;

           case "a" :
               holder.aButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
               setButtonUnselected(holder.nButtonSelect, holder.mButtonSelect, holder.eButtonSelect);
               break;

           case "m" :
               holder.mButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
               setButtonUnselected(holder.nButtonSelect, holder.aButtonSelect, holder.eButtonSelect);
               break;

           case "e" :
               holder.eButtonSelect.setBackgroundColor(context.getResources().getColor(R.color.colorBlue));
               setButtonUnselected(holder.nButtonSelect, holder.aButtonSelect, holder.mButtonSelect);
               break;
       }
   }


}
