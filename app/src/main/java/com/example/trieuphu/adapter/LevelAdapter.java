package com.example.trieuphu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trieuphu.R;
import com.example.trieuphu.model.Level;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelHolder> {
    private List<Level> mList;
    private Context mContext;

    public LevelAdapter(List<Level> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext.getApplicationContext();
    }

    @NonNull
    @Override
    public LevelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.level_line,parent,false);
        return new LevelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelHolder holder, int position) {
        Level level = mList.get(position);
        holder.textView.setText(level.getPriceAsText());
        if(position%5==0){
            holder.textView.setBackgroundResource(R.drawable.atp__activity_player_image_money_milestone);
            switch (position){
                case 0:{
                    holder.textView.setTextColor(mContext.getResources().getColor(R.color.red_orange));
                    holder.textView.setTextSize(30);
                    break;
                }
                case 5:{
                    holder.textView.setTextColor(mContext.getResources().getColor(R.color.orange));
                    holder.textView.setTextSize(28);
                    break;
                }
                case 10:{
                    holder.textView.setTextColor(mContext.getResources().getColor(R.color.green));
                    holder.textView.setTextSize(26);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class LevelHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public LevelHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_level);
        }
    }
}
