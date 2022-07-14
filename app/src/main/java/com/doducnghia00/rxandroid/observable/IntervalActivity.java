package com.doducnghia00.rxandroid.observable;

import androidx.appcompat.app.AppCompatActivity;
import com.doducnghia00.rxandroid.R;
import com.doducnghia00.rxandroid.model.User;
// Observable - observer
// Observable.interval
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IntervalActivity extends AppCompatActivity {
    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);
        Observable<Long> observable  = getObservable();
        Observer<Long> observer = getObserver();

        observable.subscribeOn(Schedulers.io()) //Phat ra o luong nao
                .observeOn(AndroidSchedulers.mainThread()) // lang nghe o luong nao
                .subscribe(observer);
    }

    private Observer<Long> getObserver(){
        return new Observer<Long>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("Rx","onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Long i) {
                Log.e("Rx","onNext " + i);
                Log.e("Rx", "Observer thread: " + Thread.currentThread().getName());
                if( i == 5){
                    disposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("Rx","onError");
            }

            @Override
            public void onComplete() {
                Log.e("Rx","onComplete");

            }
        };
    }

    private Observable<Long> getObservable(){
        return Observable.interval(3,5, TimeUnit.SECONDS);
    }

    private List<User> getLisUser(){
        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"Nghia"));
        userList.add(new User(2,"Hang"));
        userList.add(new User(3,"Huy"));
        userList.add(new User(4,"Linh"));
        userList.add(new User(5,"Tuyen"));

        return userList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}