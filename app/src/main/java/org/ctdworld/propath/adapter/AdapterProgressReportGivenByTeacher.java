package org.ctdworld.propath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.ProgressListData;
import org.ctdworld.propath.model.Teacher;

import java.util.List;


public class AdapterProgressReportGivenByTeacher extends ExpandableRecyclerViewAdapter<AdapterProgressReportGivenByTeacher.TeachersListHolder, AdapterProgressReportGivenByTeacher.ProgressListHolder> {
Context context;
    public AdapterProgressReportGivenByTeacher(Context context, List<? extends ExpandableGroup> groups) {

        super(groups);
       this.context = context;
    }

    @Override
    public TeachersListHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_education_self_reivew_list, parent, false);

        return new TeachersListHolder(view);
    }

    @Override
    public ProgressListHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recycler_view_progress_list, parent, false);
        return new ProgressListHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ProgressListHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final ProgressListData progressListData = (ProgressListData)group.getItems().get(childIndex);
        holder.bind(progressListData);
        holder.progress_report_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   context.startActivity(new Intent(context,ActivityEducationProgressReport.class));
            }
        });

    }

    @Override
    public void onBindGroupViewHolder(TeachersListHolder holder, int flatPosition, ExpandableGroup group) {

        final Teacher teacherListData = (Teacher)group;
        holder.bind(teacherListData);
    }

    class TeachersListHolder extends GroupViewHolder {

        TextView teacherTextView,progress_list_date;
        ImageView arrow;
        public TeachersListHolder(View itemView) {
            super(itemView);
            arrow =  itemView.findViewById(R.id.arrow);
            arrow.setVisibility(View.VISIBLE);
            progress_list_date = itemView.findViewById(R.id.progress_list_date);
            teacherTextView = itemView.findViewById(R.id.progress_list_name);
            progress_list_date.setVisibility(View.INVISIBLE);
            teacherTextView.setTextColor(context.getResources().getColor(R.color.colorTheme));
        }

        public void bind(Teacher teacher)
        {
            teacherTextView.setText(teacher.getTitle());
        }

      /*    @Override
        public void expand() {
            animateExpand();
        }

      @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }*/
//        @Override
//        public void expand() {
//            animateExpand();
//        }
//
//        @Override
//        public void collapse() {
//            animateCollapse();
//        }
//
//        private void animateExpand() {
//            RotateAnimation rotate =
//                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
//            rotate.setDuration(300);
//            rotate.setFillAfter(true);
//            arrow.setAnimation(rotate);
//        }
//
//        private void animateCollapse() {
//            RotateAnimation rotate =
//                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
//            rotate.setDuration(300);
//            rotate.setFillAfter(true);
//            arrow.setAnimation(rotate);
//        }


    }

    class ProgressListHolder extends ChildViewHolder
    {
        TextView progressReportTextView;
        RelativeLayout progress_report_click;
        public ProgressListHolder(View itemView)
        {
            super(itemView);
            progressReportTextView = itemView.findViewById(R.id.progress_list_name);
            progress_report_click = itemView.findViewById(R.id.progress_report_click);

        }

        public void bind(ProgressListData progressListData)
        {
            progressReportTextView.setText(progressListData.name);

        }

    }
}
