package org.ctdworld.propath.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ctdworld.propath.R;


public class AdapterSurveyGraphics extends BaseAdapter
{
    Context context;
    int flags[];
    LayoutInflater inflter;


    public AdapterSurveyGraphics(Context applicationContext, int[] flags) {
        this.context = applicationContext;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.spinner_layout_graphics,null);

        TextView textView = convertView.findViewById(R.id.graphic);
//        if (position== 0)
//        {
//            textView.setVisibility(View.VISIBLE);
//        }
        ImageView star = convertView.findViewById(R.id.star);
        ImageView star2 = convertView.findViewById(R.id.star1);
        ImageView star3 = convertView.findViewById(R.id.star2);

        star.setImageResource(flags[position]);
        star2.setImageResource(flags[position]);
        star3.setImageResource(flags[position]);



        return convertView;
    }
}
