package com.example.currencyexchange;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {

    private Context context;
    private List<RateItem> rateList;

    public RateAdapter(Context context, List<RateItem> rateList) {
        this.context = context;
        this.rateList = rateList;
    }

    public void updateList(List<RateItem> newList) {
        this.rateList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rate, parent, false);
        return new RateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateViewHolder holder, int position) {
        RateItem item = rateList.get(position);
        holder.flag.setText(item.getFlag());
        holder.code.setText(item.getCode());
        holder.name.setText(item.getName());
        holder.rate.setText(String.format("%.4f", item.getRate()));

        double change = item.getChange();
        if (change >= 0) {
            holder.change.setText(String.format("+%.2f%%", change));
            holder.change.setTextColor(Color.parseColor("#10B981"));
        } else {
            holder.change.setText(String.format("%.2f%%", change));
            holder.change.setTextColor(Color.parseColor("#EF4444"));
        }
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public static class RateViewHolder extends RecyclerView.ViewHolder {
        TextView flag, code, name, rate, change;

        public RateViewHolder(@NonNull View itemView) {
            super(itemView);
            flag = itemView.findViewById(R.id.currencyFlag);
            code = itemView.findViewById(R.id.currencyCode);
            name = itemView.findViewById(R.id.currencyName);
            rate = itemView.findViewById(R.id.currencyRate);
            change = itemView.findViewById(R.id.currencyChange);
        }
    }
}