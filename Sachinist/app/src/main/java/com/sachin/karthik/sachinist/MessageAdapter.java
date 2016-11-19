package com.sachin.karthik.sachinist;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Karthik on 20-09-2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private ArrayList<String> mssgs;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public MessageAdapter(){
        titles = new ArrayList<String>();
        mssgs = new ArrayList<String>();
    }

    public void addItems(String tit,String mssg) {
        titles.add(tit);
        mssgs.add(mssg);
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleText = (TextView)cardView.findViewById(R.id.title);
        TextView descText = (TextView)cardView.findViewById(R.id.desc);

        titleText.setText(titles.get(position));
        descText.setText(mssgs.get(position));
    }

    @Override
    public int getItemCount() {
        if(titles == null)
            return 0;
        return titles.size();
    }
}
