package com.example.rxdeneme.Request;

import com.example.rxdeneme.Models.Photo;
import com.example.rxdeneme.Models.Album;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestApi {
    @GET("albums")
    Observable<List<Album>> getAlbums();

    @GET("albums/{id}/photos")
    Observable<List<Photo>> getPhotos(
            @Path("id") int id
    );
}
