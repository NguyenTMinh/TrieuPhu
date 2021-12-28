package com.example.trieuphu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trieuphu.R;
import com.example.trieuphu.model.Player;

import java.text.NumberFormat;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private List<Player> players;
    private Context context;

    public RankAdapter(List<Player> players, Context context) {
        this.players = players;
        this.context = context;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.high_score_player,parent,false);
        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        Player player = players.get(position);
        String string = String.valueOf(position+1);
        holder.rank.setText(string);
        holder.name.setText(player.getName());
        StringBuilder stringBuilder = new StringBuilder(NumberFormat.getCurrencyInstance().format(player.getScore()));
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        holder.score.setText(stringBuilder);
        switch (position){
            case 0:{
                holder.row.setBackgroundColor(context.getResources().getColor(R.color.hcv));
                holder.rank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.atp__leaderboard_item_rank_1,0,0,0);
                break;
            }
            case 1:{
                holder.row.setBackgroundColor(context.getResources().getColor(R.color.hcb));
                holder.rank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.atp__leaderboard_item_rank_2,0,0,0);
                break;
            }
            case 2:{
                holder.row.setBackgroundColor(context.getResources().getColor(R.color.hcd));
                holder.rank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.atp__leaderboard_item_rank_3,0,0,0);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder{
        TextView rank;
        TextView name;
        TextView score;
        TableRow row;

        private RankViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.tr_player);
            rank = itemView.findViewById(R.id.tv_rank);
            name = itemView.findViewById(R.id.tv_name_rank);
            score = itemView.findViewById(R.id.tv_score_rank);
        }
    }
}
