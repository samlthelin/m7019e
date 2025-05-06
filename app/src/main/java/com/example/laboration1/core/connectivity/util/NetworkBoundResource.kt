package com.example.laboration1.core.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.emitAll

/**
 * A generic helper to combine a Room cache (query) with a network fetch:
 *
 * @param query            emits the cached data as Flow<ResultType>
 * @param fetch            suspending call to get RequestType from network
 * @param saveFetchResult  saves the fetched RequestType into Room
 * @param shouldFetch      when to fetch (default = always)
 *
 * Returns Flow<Resource<ResultType>> which will emit Loading, then Success or Error.
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
): Flow<Resource<ResultType>> = flow {
    // 1) tell UI we’re loading
    emit(Resource.Loading)

    // 2) pull the first value from the cache
    val cacheValue = query().first()

    if (shouldFetch(cacheValue)) {
        try {
            // 3) fetch from network
            val fetched = fetch()
            // 4) save into database
            saveFetchResult(fetched)
            // 5) emit the updated cache
            emitAll(query().map { Resource.Success(it) })
        } catch (t: Throwable) {
            // 6) on error emit cached data + the error
            emitAll(query().map { Resource.Error(t, it) })
        }
    } else {
        // 7) just emit cached data
        emitAll(query().map { Resource.Success(it) })
    }
}.flowOn(Dispatchers.IO)
