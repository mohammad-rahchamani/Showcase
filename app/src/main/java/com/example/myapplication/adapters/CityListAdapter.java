package com.example.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.models.CityDataModel;

import java.util.List;

/**
 * Created by neo on 3/8/16.
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {

    private final List<CityDataModel> mItems;
    private final AdapterClickListener mListener;


    public CityListAdapter(List<CityDataModel> items, AdapterClickListener listener) {
        mItems = items;
        mListener = listener;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_item,
                viewGroup, false);
        return new CityViewHolder(item);
    }

    @Override
    public void onBindViewHolder(CityViewHolder cityViewHolder, final int i) {
        cityViewHolder.title.setText(mItems.get(i).getName());
        cityViewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(view, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {

        final Button title;

        public CityViewHolder(View itemView) {
            super(itemView);
            title = (Button) itemView.findViewById(R.id.city_item_text_view);
        }
    }

}
