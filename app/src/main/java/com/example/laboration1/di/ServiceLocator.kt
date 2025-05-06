package com.example.laboration1.di


import androidx.room.Room
import com.example.laboration1.data.local.MovieDatabase
import android.app.Application
import com.example.laboration1.core.connectivity.ConnectivityObserver
import com.example.laboration1.network.TmdbClient
import com.example.laboration1.data.MovieRepository

/**
 *  Extremt enkel Dependency‑locator för små projekt
 *  (ersätts gärna av Hilt längre fram).
 */
object ServiceLocator {

    // -------------  Lazy init‑fält  -------------
    @Volatile private var db: MovieDatabase? = null
    @Volatile private var repo: MovieRepository? = null
    @Volatile private var connectivity: ConnectivityObserver? = null

    // -------------  Publika access‑metoder  -------------

    fun provideRepository(app: Application): MovieRepository =
        repo ?: synchronized(this) {
            repo ?: buildRepository(app).also { repo = it }
        }

    fun provideConnectivity(app: Application): ConnectivityObserver =
        connectivity ?: synchronized(this) {
            connectivity ?: ConnectivityObserver(app).also { connectivity = it }
        }

    // -------------  Privata helpers  -------------

    /** Skapar Room‑databas + Repository med alla beroenden. */
    private fun buildRepository(app: Application): MovieRepository {
        val database = db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                app,
                MovieDatabase::class.java,
                "movies.db"
            ).build().also { db = it }
        }

        return MovieRepository(
            api          = TmdbClient.api,          // Retrofit‑service
            dao          = database.movieDao(),     // Room‑DAO
        )
    }
}
