package com.example.rxdeneme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.rxdeneme.Models.Album;
import com.example.rxdeneme.Models.Photo;
import com.example.rxdeneme.Request.ServiceGenerator;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //ui
    private RecyclerView recyclerView;

    // vars
    private CompositeDisposable disposables = new CompositeDisposable();
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        initRecyclerView();

        getAlbumsObservable()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Album, ObservableSource<Album>>() {
                    @Override
                    public ObservableSource<Album> apply(Album album) throws Exception { //combine album and photos
                        return getPhotosObservable(album); //returns Observable<Album> with photos
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Album>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Album album) {
                    updateAlbum(album);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private Observable<Album> getAlbumsObservable(){
        return ServiceGenerator.getRequestApi()
                .getAlbums()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Album>, ObservableSource<Album>>() { //converting list of albums to Observable<Album> object
                    @Override
                    public ObservableSource<Album> apply(final List<Album> albums) throws Exception {
                        adapter.setAlbums(albums);
                        return Observable.fromIterable(albums)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }


    private Observable<Album> getPhotosObservable(final Album album){
        return ServiceGenerator.getRequestApi()
                .getPhotos(album.getId())
                .map(new Function<List<Photo>, Album>() {
                    @Override
                    public Album apply(List<Photo> photos) throws Exception {

                      int delay = ((new Random()).nextInt(5) + 1) * 1000; // sleep thread for x ms
                      Thread.sleep(delay);

                        album.setPhotos(photos);
                        return album;
                    }
                })
                .subscribeOn(Schedulers.io());

    }

    private void updateAlbum(Album album){
        adapter.updatePost(album);
    }

    private void initRecyclerView(){
        adapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}