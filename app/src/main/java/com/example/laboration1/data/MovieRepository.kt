package com.example.laboration1.data

import com.example.laboration1.core.util.Resource
import com.example.laboration1.core.util.networkBoundResource
import com.example.laboration1.data.local.dao.MovieDao
import com.example.laboration1.data.local.entity.MovieEntity
import com.example.laboration1.model.Movie
import com.example.laboration1.network.TmdbApiService
import com.example.laboration1.network.model.ApiMovie
import com.example.laboration1.url.Secrets
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map   // only the Flow‐map

class MovieRepository(
    private val api: TmdbApiService,
    private val dao: MovieDao
) {

    fun movies(category: String): Flow<Resource<List<Movie>>> =
        networkBoundResource<List<Movie>, List<ApiMovie>>(
            query = {
                // ① grab the flow of entities
                val entityFlow: Flow<List<MovieEntity>> = dao.movies(category)

                // ② map that flow into movies
                entityFlow.map { listOfEntities ->
                    listOfEntities.map { it.toDomain() }  // now this is clearly List<Movie>
                }
            },
            fetch = { api.getPopularMovies(Secrets.API_KEY).results },
            saveFetchResult = { remote ->
                dao.clear()
                dao.insertAll(remote.map { it.toEntity(category) })
            },
            shouldFetch = { it.isEmpty() }
        )


// ──────────────────────
// Mapper-extensions (in same file & package)

    private fun ApiMovie.toEntity(cat: String) = MovieEntity(
        id = id,
        title = title,
        posterPath = posterPath ?: "",
        category = cat
    )

    private fun MovieEntity.toDomain() = Movie(
        id = id,
        title = title,
        genres = emptyList(),      // Kotlin’s emptyList<T>()
        homepage = "",
        imdbId = "",
        posterPath = posterPath
    )
}