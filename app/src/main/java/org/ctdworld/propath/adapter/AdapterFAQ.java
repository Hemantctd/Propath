package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.model.FAQ;

import java.util.List;


public class AdapterFAQ extends RecyclerView.Adapter<AdapterFAQ.MyViewHolder>
{
    private static final String TAG = AdapterFAQ.class.getSimpleName();
    Context context;
    private List<FAQ> mFAQList;  // it will contain Chat



    public AdapterFAQ(Context context, List<FAQ> faqList)
    {
        this.context = context;
        this.mFAQList= faqList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_faq,null);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        // from chat object only message, image, video andi date time being shown
        final FAQ faq = mFAQList.get(position);

        holder.txtTitle.setText(faq.getTitle());
        holder.txtDescription.setText(faq.getDescription());

    }


    @Override
    public int getItemCount()
    {
        return mFAQList.size();
    }




    @Override
    public int getItemViewType(int position) {
        return position;//super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;// super.getItemId(position);
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtTitle, txtDescription;

        public MyViewHolder(View view)
        {
            super(view);

            txtTitle = view.findViewById(R.id.recycler_faq_txt_title);
            txtDescription = view.findViewById(R.id.recycler_faq_txt_description);
        }
    }


}
