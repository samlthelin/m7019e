package com.example.laboration1.data.local

import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE viewType = :viewType")
    suspend fun getMoviesByViewType(viewType: String): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies WHERE viewType = :viewType")
    suspend fun deleteMoviesByViewType(viewType: String)

    //for movie details:

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movies: MovieDetailEntity)

    @Query("SELECT * FROM movie_details WHERE id = :id")
    suspend fun getMovieDetailById(id: Int): MovieDetailEntity?


}