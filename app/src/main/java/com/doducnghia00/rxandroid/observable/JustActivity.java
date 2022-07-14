package com.doducnghia00.rxandroid.observable;

import androidx.appcompat.app.AppCompatActivity;
import com.doducnghia00.rxandroid.R;
import com.doducnghia00.rxandroid.model.User;

import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

// Observable - observer
// Observable.just
public class JustActivity extends AppCompatActivity {
    private Disposable disposable;
    //User => Serializable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just);

        Observable<Serializable> observable  = getObservableUser();
        Observer<Serializable> observer = getObserverUser();

        observable.subscribeOn(Schedulers.io()) //Phat ra o luong nao
                .observeOn(AndroidSchedulers.mainThread()) // lang nghe o luong nao
                .subscribe(observer);
    }

    private Observer<Serializable> getObserverUser(){
        return new Observer<Serializable>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("Rx","onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(@NonNull Serializable serializable) {
                Log.e("Rx","onNext");
                if(serializable instanceof User[]){
                    User[] users = (User[]) serializable;
                    for(User user: users){
                        Log.e("Rx","User Information: " + user.toString());
                    }
                }else if(serializable instanceof String){
                    String myString = (String) serializable;
                    Log.e("Rx","String: " + myString);
                }else if (serializable instanceof User){
                    User user = (User) serializable;
                    Log.e("Rx","User Data: " + user.toString());
                }else if(serializable instanceof List){
                    List<User> list = (List<User>) serializable;
                    for (User user: list){
                        Log.e("Rx", "List Data: " + user.toString());
                    }
                }
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


    private Observable getObservableUser(){ //User => Serializable
        List<User> userList = getLisUser();
        User user1 = new User(1,"Nghia");
        User user2 = new User(2,"Linh");

        String strData = "Duy Thien";
        User user3 = new User(3,"Duyen");

        User[] users = new User[]{user1,user2};
        return Observable.just(users, strData, user3, userList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
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

}