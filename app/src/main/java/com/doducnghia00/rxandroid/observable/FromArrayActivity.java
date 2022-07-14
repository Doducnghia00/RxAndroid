package com.doducnghia00.rxandroid.observable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.doducnghia00.rxandroid.R;
import com.doducnghia00.rxandroid.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

// Observable - observer
// Observable.fromArray
public class FromArrayActivity extends AppCompatActivity {
    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_array);

        Observable<User> observable  = getObservableUser();
        Observer<User> observer = getObserverUser();

        observable.subscribeOn(Schedulers.io()) //Phat ra o luong nao
                .observeOn(AndroidSchedulers.mainThread()) // lang nghe o luong nao
                .subscribe(observer);
    }

    private Observer<User> getObserverUser(){
        return new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("Rx","onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e("Rx","onNext" + user.toString());
                Log.e("Rx", "Observer thread: " + Thread.currentThread().getName());
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


    private Observable<User> getObservableUser(){

        User user1 = new User(1,"Nghia");
        User user2 = new User(2,"Linh");

        User[] users = new User[]{user1,user2};
        return Observable.fromArray(users);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }

}