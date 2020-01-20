package org.ctdworld.propath.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ctdworld.propath.R;

public class SpinnerSimpleAdapter extends ArrayAdapter<String>
{
    private Context mContext;
    private String[] mDataArr;
    private SimpleAdapterListener mListener;

    public SpinnerSimpleAdapter(Context context, int layoutResource, String[] dataArr, SimpleAdapterListener listener)
    {
        super(context,0, dataArr);
        this.mContext = context;
        this.mDataArr = dataArr;
        this.mListener = listener;
    }

    public interface SimpleAdapterListener
    {
        void onItemSelected(String itemText, int itemPosition);
    }



    public int getCount(){
        return this.mDataArr.length;
    }

    public String getItem(int index) {
        return this.mDataArr[index];
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

         View view = convertView;
         if (view == null)
             view = LayoutInflater.from(mContext).inflate(R.layout.adapter_spinner,parent,false);

            TextView textView = view.findViewById(R.id.adapter_simple_spinner_txt);
            textView.setText(mDataArr[position]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(mDataArr[position],position);
                }
            });


         return view;
    }


    /*  public SpinnerSimpleAdapter(Context context, String[] dataArr, SimpleAdapterListener listener)
    {
        this.mContext = context;
        this.mDataArr = dataArr;
        this.mListener = listener;
    }


    public interface SimpleAdapterListener
    {
        void onItemSelected(String itemText, int itemPosition);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_spinner,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        if (mDataArr != null)
        holder.textView.setText(mDataArr[position]);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = holder.textView.getText().toString().trim();
                mListener.onItemSelected(text,position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (mDataArr !=null)
            return mDataArr.length;
        else
        return 0;
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        View layout;

        public MyViewHolder(View view) {
            super(view);
            layout = view;
            textView = view.findViewById(R.id.adapter_simple_spinner_txt);
        }
    }
*/
}
