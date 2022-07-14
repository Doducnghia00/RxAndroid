package com.doducnghia00.rxandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
// Observable.create
public class MainActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        List<User> userList = getLisUser();
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> emitter) throws Throwable {
                Log.e("Rx", "Observable thread: " + Thread.currentThread().getName());
                if(userList == null || userList.isEmpty()){
                    if(!emitter.isDisposed()){
                        emitter.onError(new Exception());
                    }
                }

                for (User user: userList) {
                    if(!emitter.isDisposed()){
                         emitter.onNext(user);
                    }
                }
                if(!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        });
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