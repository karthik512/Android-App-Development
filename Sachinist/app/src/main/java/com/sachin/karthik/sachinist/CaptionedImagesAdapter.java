package com.sachin.karthik.sachinist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Karthik on 16-09-2016.
 */
public class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {

    private String[] captions;
    private int[] imageIds;
    private Listener listener;

    public static interface Listener{
        public void onClick(int position);
    }

    public CaptionedImagesAdapter(String[] caps, int[] ids){
        this.captions = caps;
        this.imageIds = ids;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_image,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView)cardView.findViewById(R.id.info_image);
        TextView textView = (TextView)cardView.findViewById(R.id.info_text);

        Drawable drawable = cardView.getResources().getDrawable(imageIds[position], null);
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(captions[position]);

        textView.setText(captions[position]);

        cardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(captions == null)
            return 0;
        return captions.length;
    }
}
