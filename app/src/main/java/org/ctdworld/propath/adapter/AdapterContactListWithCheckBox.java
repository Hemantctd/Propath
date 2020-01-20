package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.ctdworld.propath.R;
public class AdapterContactListWithCheckBox extends RecyclerView.Adapter<AdapterContactListWithCheckBox.MyViewHolder>
{
    private static final String TAG = AdapterContactListWithCheckBox.class.getSimpleName();
    Context context;

    public AdapterContactListWithCheckBox(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_contact_list_with_checkbox,null);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
    }

    @Override
    public int getItemCount()
    {
        return 20;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View view)
        {
            super(view);
        }
    }


}
