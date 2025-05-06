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

    override suspend fun doWork(): Result {
        val sortType = inputData.getString("sortType") ?: "popular"
        val db = MovieDatabase.getDatabase(applicationContext)
        val dao = db.movieDao()

        Log.d("CacheWorker","Running background cache for $sortType")

        return try {
            val genreResponse = TmdbClient.api.getGenres(Secrets.API_KEY)
            val genreMap = genreResponse.genres.associateBy({ it.id }, { it.name })

            val response = when (sortType) {
                "top_rated" -> TmdbClient.api.getPopularMovies(Secrets.API_KEY)
                //"top_rated" -> TmdbClient.api.getTopRatedMovies(Secrets.API_KEY)
                else -> TmdbClient.api.getPopularMovies(Secrets.API_KEY)
            }

            val entities = response.results.map { it.toEntity(sortType, genreMap) }
            dao.clearMoviesExcept(sortType)
            dao.insertMovies(entities)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
