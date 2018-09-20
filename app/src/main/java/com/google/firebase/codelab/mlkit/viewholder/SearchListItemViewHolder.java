package com.google.firebase.codelab.mlkit.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.codelab.mlkit.MainActivity;
import com.google.firebase.codelab.mlkit.R;
import com.google.firebase.codelab.mlkit.WebViewActivity;
import com.google.firebase.codelab.mlkit.model.SearchItem;


public class SearchListItemViewHolder extends RecyclerView.ViewHolder
{
    private TextView mTextViewSearchTitle;
    private TextView mTextViewSearchDetail;
    private TextView mTextViewSearchDetail2;
    private TextView mTextViewSearchDetail3;
    private SearchItem mSearchItem;

    public SearchListItemViewHolder(Context context, ViewGroup viewGroup)
    {
        super(LayoutInflater.from(context).inflate(R.layout.vh_search_list, viewGroup, false));
    }

    private void initView()
    {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(itemView.getContext(), WebViewActivity.class);
                myIntent.putExtra("number", mSearchItem.getSearchId()); //Optional parameters
                itemView.getContext().startActivity(myIntent);
            }
        });

        mTextViewSearchTitle = itemView.findViewById(R.id.txtView_search_title);
        mTextViewSearchDetail = itemView.findViewById(R.id.searchId);
        mTextViewSearchDetail2 = itemView.findViewById(R.id.searchType);
        mTextViewSearchDetail3 = itemView.findViewById(R.id.check_status);

        int month = mSearchItem.getSearchDate().getMonth()+1;
        int year = mSearchItem.getSearchDate().getYear() + 1900;

        String title = "ค้นหาวันที่ " + mSearchItem.getSearchDate().getDate() + " เดือน "
                + month + " ปี " + year;
        mTextViewSearchTitle.setText(title);
        mTextViewSearchDetail.setText(mSearchItem.getSearchId());
        mTextViewSearchDetail2.setText(mSearchItem.getCatType());
        if(mSearchItem.getCheck() != null && mSearchItem.getCheck().equals("true"))
        {
            mTextViewSearchDetail3.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            mTextViewSearchDetail3.setText("พบ");
        }
        else {
            mTextViewSearchDetail3.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorRed));
            mTextViewSearchDetail3.setText("ไม่พบ");
        }
    }

    public void setInfo(SearchItem searchItem)
    {
        mSearchItem = searchItem;
        initView();
    }
}
