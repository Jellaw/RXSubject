package com.example.rxsubject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    Button publish, behavior, replay, async;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, " ==========PublishSubject================ ");
                excutePublish();
            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, " ==========ReplaySubject================ ");
                excuteReplay();
            }
        });
        behavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, " ==========BehaviorSubject================ ");
                excuteBehavior();
            }
        });
        async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, " ==========AsyncSubject================ ");
                excuteAsync();
            }
        });

    }
    private void init(){
        publish=findViewById(R.id.publish);
        replay=findViewById(R.id.replay);
        behavior=findViewById(R.id.behavior);
        async=findViewById(R.id.asyncBTN);
    }
    private void excutePublish(){
        PublishSubject<Integer> source = PublishSubject.create();

        source.subscribe(getFirstObserver());
        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        source.subscribe(getSecondObserver());
        source.onNext(4);
        source.onComplete();

    }
    private void excuteReplay() {

        ReplaySubject<Integer> source = ReplaySubject.create();

        source.subscribe(getFirstObserver()); // it will get 1, 2, 3, 4

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);
        source.onNext(4);
        source.onComplete();

        /*
         * it will emit 1, 2, 3, 4 for second observer also as we have used replay
         */
        source.subscribe(getSecondObserver());

    }
    private void excuteBehavior(){
        BehaviorSubject<Integer> source = BehaviorSubject.create();

        source.subscribe(getFirstObserver()); // it will get 1, 2, 3, 4 and onComplete

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        /*
         * it will emit 3(last emitted), 4 and onComplete for second observer also.
         */
        source.subscribe(getSecondObserver());

        source.onNext(4);
        source.onComplete();
    }

    private void excuteAsync(){
        AsyncSubject<Integer> source = AsyncSubject.create();

        source.subscribe(getFirstObserver()); // it will emit only 4 and onComplete

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        /*
         * it will emit 4 and onComplete for second observer also.
         */
        source.subscribe(getSecondObserver());

        source.onNext(4);
        source.onComplete();
    }

    private Observer<Integer> getFirstObserver(){
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, " First onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, " First onNext value : " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, " First onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, " First onComplete");
            }
        };
    }
    private Observer<Integer> getSecondObserver(){
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, " Second onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, " Second onNext value : " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, " Second onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, " Second onComplete");
            }
        };
    }
}