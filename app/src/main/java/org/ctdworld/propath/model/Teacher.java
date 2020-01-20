package org.ctdworld.propath.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Teacher extends ExpandableGroup<ProgressListData>
{

    public Teacher(String teacherName, List<ProgressListData> items) {
        super(teacherName, items);
    }
}
