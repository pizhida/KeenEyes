package com.google.firebase.codelab.mlkit.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.codelab.mlkit.R;
import com.google.firebase.codelab.mlkit.model.SearchItem;
import com.google.firebase.codelab.mlkit.viewholder.SearchListItemViewHolder;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListItemViewHolder>
{
    private List<SearchItem> searchItems;

    @NonNull
    @Override
    public SearchListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new SearchListItemViewHolder(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListItemViewHolder holder, int position)
    {
        holder.setInfo(searchItems.get(searchItems.size()-1-position));
    }

    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = searchItems;
    }

    @Override
    public int getItemCount() {
        if(searchItems != null)
        return searchItems.size();
        else
            return 0;
    }
}
