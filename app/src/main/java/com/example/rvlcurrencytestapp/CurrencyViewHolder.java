package com.example.rvlcurrencytestapp;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;

public class CurrencyViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private EditText mRate;

    public CurrencyViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView) itemView.findViewById(R.id.curren_title);
        mRate = (EditText) itemView.findViewById(R.id.curren_rate);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setRate(String rate) {
        mRate.setText(rate);
    }

    Observable<RecyclerItem> getClickObserver(final RecyclerItem item) {
        return Observable.create(e -> itemView.setOnClickListener(view -> e.onNext(item)));
    }
}
