package org.ctdworld.propath.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ctdworld.propath.R;
import org.ctdworld.propath.activity.ActivityMatchDayFuture2;

import java.util.ArrayList;


public class AdapterMatchDayFuture1 extends RecyclerView.Adapter<AdapterMatchDayFuture1.MyViewHolder> {

    Context mContext;
    ArrayList<String> mArrayListText = new ArrayList<>();

    public AdapterMatchDayFuture1(Context context, ArrayList<String> arrayListText)
    {
        this.mContext = context;
        this.mArrayListText = arrayListText;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_match_set_up, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.matchSetupTextItem.setText(mArrayListText.get(i));
        myViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ActivityMatchDayFuture2.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayListText.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView matchSetupTextItem;
        private RelativeLayout matchSetupLayout;
        private ImageView matchSetupImages;
        private View layout;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            layout = itemView;
            matchSetupTextItem = itemView.findViewById(R.id.match_setup_text_item);
            matchSetupImages = itemView.findViewById(R.id.match_setup_image_item);
            matchSetupLayout = itemView.findViewById(R.id.match_setup_layout_item);

        }



    }


}
