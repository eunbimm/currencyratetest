package com.example.rvlcurrencytestapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends RxAppCompatActivity {

    public static final String TAG = "EunbiTest";

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private Disposable mDisposable;

    private Observable<Observable<Map<String, String>>> mObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        initLayout();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAdapter == null) {
            return;
        }

        callCurrencyRateApi(null);
    }

    /**
     * call API
     * @param baseCurrency base parameter
     */
    private void callCurrencyRateApi(String baseCurrency) {

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        CurrencyService currencyService = new CurrencyService();

        mObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                 .map(data -> currencyService.getCurrencyRatesApi(baseCurrency))
                .compose(bindToLifecycle())
                .retry();


        mDisposable = mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Observable<Map<String, String>>>() {
                    @Override
                    public void onNext(Observable<Map<String, String>> mapObservable) {
                        mapObservable.observeOn(AndroidSchedulers.mainThread())
                                .subscribe(result -> {

                                    if (mAdapter != null) {
                                        if (mAdapter.getItemCount() < 1) {
                                            mAdapter.updateItem(0, new RecyclerItem("EUR", "100"));

                                        }

                                        ArrayList<RecyclerItem> itemList = new ArrayList<>();
                                        Set<Map.Entry<String, String>> entries = result.entrySet();
                                        for (Map.Entry<String, String> entry : entries) {
                                            itemList.add(new RecyclerItem(entry.getKey(), entry.getValue()));
                                        }

                                        mAdapter.updateItems(itemList);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * Initialize view items
     */
    private void initLayout() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.getItemPublishSubject()
                .subscribe(clickedItem -> onCurrencyItemClicked(clickedItem));
    }

    /**
     * Item click event
     * @param item selected item
     */
    private void onCurrencyItemClicked(RecyclerItem item) {
        mAdapter.moveItemToTop(item);
        mRecyclerView.getLayoutManager().startSmoothScroll(getSmoothScroller());

//        setBaseCurrencyItemTextChangeEvents();

        callCurrencyRateApi(item.getTitle());
    }

    private void setBaseCurrencyItemTextChangeEvents() {
        EditText baseCurrencyView = (EditText) mRecyclerView.getLayoutManager().findViewByPosition(0).findViewById(R.id.curren_rate);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w(TAG, "onTextChanged(text = " + s + ")");


            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v(TAG, "afterTextChanged(text = " + s + ")");

                RecyclerItem item = mAdapter.getBaseCurrencyItem();
                item.setRate(s.toString());

                baseCurrencyView.removeTextChangedListener(this);
                mAdapter.updateItem(0, item);
                mAdapter.notifyDataSetChanged();
                baseCurrencyView.addTextChangedListener(this);
            }
        };

        baseCurrencyView.addTextChangedListener(textWatcher);
    }

    private RecyclerView.SmoothScroller getSmoothScroller() {
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        smoothScroller.setTargetPosition(0);

        return smoothScroller;
    }

}
