package org.ctdworld.propath.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ctdworld.propath.R;

import java.util.ArrayList;


public class AdapterMatchSetUp extends RecyclerView.Adapter<AdapterMatchSetUp.MyViewHolder> {

    Context mContext;
    ArrayList<String> mArrayListText = new ArrayList<>();
    ArrayList<Integer> mArrayListImages = new ArrayList<>();

    public AdapterMatchSetUp(Context context,ArrayList<String> arrayListText, ArrayList<Integer> arrayListImages)
    {
        this.mContext = context;
        this.mArrayListText = arrayListText;
        this.mArrayListImages = arrayListImages;
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
        myViewHolder.matchSetupImages.setBackgroundResource(mArrayListImages.get(i));
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


        public MyViewHolder(View itemView)
        {
            super(itemView);
            matchSetupTextItem = itemView.findViewById(R.id.match_setup_text_item);
            matchSetupImages = itemView.findViewById(R.id.match_setup_image_item);
            matchSetupLayout = itemView.findViewById(R.id.match_setup_layout_item);

        }



    }


}
