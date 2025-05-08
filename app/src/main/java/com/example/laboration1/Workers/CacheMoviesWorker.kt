package com.example.laboration1.Workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.laboration1.data.local.MovieDatabase
import com.example.laboration1.data.toEntity
import com.example.laboration1.network.TmdbClient
import com.example.laboration1.url.Secrets

class CacheMoviesWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {


     //workmanagers entry point. Called when the worker is triggered.

    //all suspend functions can be paused and resumed at a later time. important for workers!!!
    override suspend fun doWork(): Result {
        // we retrieve the sort type (e.g., "popular" or "top_rated") passed to this worker.
        val sortType = inputData.getString("sortType") ?: "popular"

        // Get an instance of the database and DAO
        val db = MovieDatabase.getDatabase(applicationContext)
        val dao = db.movieDao()

        Log.d("CacheWorker", "Running background cache for $sortType")

        return try {
            // fetch genres first so we can map genre IDs to their string names
            val genreResponse = TmdbClient.api.getGenres(Secrets.API_KEY)
            val genreMap = genreResponse.genres.associateBy({ it.id }, { it.name })

            // fetch movies depending on sortType (default to "popular")
            val response = when (sortType) {
                "top_rated" -> TmdbClient.api.getTopRatedMovies(Secrets.API_KEY)
                else -> TmdbClient.api.getPopularMovies(Secrets.API_KEY)
            }

            // convert fetched movies to Room entities
            val entities = response.results.map { it.toEntity(sortType, genreMap) }

            // clear previous entries for this viewType and insert the new ones
            dao.clearMoviesExcept(sortType)
            dao.insertMovies(entities)

            // show that the work completed successfully
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()

            // workmanager can retry the task later (e.g. if no internet connection)
            Result.retry()
        }
    }
}
