package com.frodo.app.android.simple;

import com.frodo.app.framework.cache.Cache;
import com.frodo.app.framework.controller.AbstractModel;
import com.frodo.app.framework.controller.MainController;
import com.frodo.app.android.simple.cloud.amdb.services.MoviesService;
import com.frodo.app.android.entities.amdb.Movie;

import java.util.List;

import rx.Subscriber;

/**
 * Created by frodo on 2015/4/2.
 */
public class MovieModel extends AbstractModel {
    private FetchMoviesWithRxjavaTask fetchMoviesWithRxjavaTask;
    private MoviesService moviesService;
    private MovieCache movieCache;
    private boolean enableCached;

    private List<Movie> movies;

    public MovieModel(MainController controller) {
        super(controller);
        moviesService = getMainController().getNetworkTransport().create(MoviesService.class);
        if (enableCached) {
            movieCache = new MovieCache(getMainController().getCacheSystem(), Cache.Type.DISK);
        }
    }

    public void loadMoviesWithRxjava(Subscriber<List<Movie>> subscriber) {
        fetchMoviesWithRxjavaTask = new FetchMoviesWithRxjavaTask(moviesService, subscriber);
        getMainController().getBackgroundExecutor().execute(fetchMoviesWithRxjavaTask);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        if (enableCached) {
            movieCache.put(fetchMoviesWithRxjavaTask.key(), movies);
        }
    }

    public List<Movie> getMoviesFromCache() {
        return enableCached ? movieCache.get(fetchMoviesWithRxjavaTask.key()) : null;
    }

    public boolean isEnableCached() {
        return enableCached;
    }

    public void setEnableCached(boolean enableCached) {
        this.enableCached = enableCached;

        if (movieCache == null) {
            movieCache = new MovieCache(getMainController().getCacheSystem(), Cache.Type.DISK);
        }
    }

    @Override
    public void initBusiness() {

    }
}
