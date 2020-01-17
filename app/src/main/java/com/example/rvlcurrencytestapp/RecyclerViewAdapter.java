package com.example.rvlcurrencytestapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

    private List<RecyclerItem> mItems = new ArrayList<>();

    private PublishSubject<RecyclerItem> mPublishSubject;

    RecyclerViewAdapter() {
        this.mPublishSubject = PublishSubject.create();
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_currency_item, parent, false);

        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        final RecyclerItem item = mItems.get(position);
        holder.setTitle(item.getTitle().toUpperCase());

        if (position == 0) {
            holder.setRate(item.getRate());
        } else {
            holder.setRate(getCalculatedRate(position));
        }
        holder.getClickObserver(item).subscribe(mPublishSubject);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateItems(List<RecyclerItem> items) {
        RecyclerItem firstItem = getBaseCurrencyItem();
        mItems.clear();

        mItems.add(firstItem);
        mItems.addAll(items);
    }

    public void updateItem(int position, RecyclerItem item) {
        if (getItemCount() > position) {
            mItems.set(position, item);
        } else {
            mItems.add(item);
        }
    }

    public void moveItemToTop(RecyclerItem item) {
        mItems.remove(item);
        mItems.add(0, item);
    }

    public RecyclerItem getBaseCurrencyItem() {
        return mItems.get(0);
    }

    public PublishSubject<RecyclerItem> getItemPublishSubject() {
        return mPublishSubject;
    }

    private String getCalculatedRate(int position) {
        BigDecimal var1 = new BigDecimal(String.valueOf(getBaseCurrencyItem().getRate()));
        BigDecimal var2 = new BigDecimal(String.valueOf(mItems.get(position).getRate()));

        BigDecimal result = var1.multiply(var2);

        return String.valueOf(result.setScale(4, BigDecimal.ROUND_HALF_UP));
    }
}
