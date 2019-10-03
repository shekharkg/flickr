/*
 * Copyright 2019 ShekharKG. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shekharkg.flickr.repo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.shekharkg.flickr.bean.FlickrResponse
import com.shekharkg.flickr.repo.data.FlickrDatabase
import com.shekharkg.flickr.repo.data.FlickrEntity
import com.shekharkg.flickr.rest.ApiClient
import com.shekharkg.flickr.utils.NetworkState
import com.shekharkg.flickr.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
    private val application: Application
) {
    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(application: Application) = instance ?: synchronized(this) {
            instance ?: Repository(application).also { instance = it }
        }
    }

    private val db: FlickrDatabase? =
        Room.databaseBuilder(application, FlickrDatabase::class.java, "flickr_database")
            .fallbackToDestructiveMigration().build()


    private val photos: LiveData<List<FlickrEntity>>? = db?.flickeDao()?.getAll()
    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    fun getPhotos(): LiveData<List<FlickrEntity>>? = photos
    fun getNetworkState(): MutableLiveData<NetworkState> = networkState


    fun updateFromService() {
        networkState.value = NetworkState.LOADING

        if (Utils.isNetworkConnected(application)) {
            ApiClient.getApiInterface().getPhotos().enqueue(object : Callback<FlickrResponse> {
                override fun onFailure(call: Call<FlickrResponse>?, t: Throwable?) {
                    networkState.value = NetworkState(NetworkState.Status.FAILED, t?.message)
                }

                override fun onResponse(
                    call: Call<FlickrResponse>?,
                    response: Response<FlickrResponse>?
                ) {
                    networkState.value = NetworkState.SUCCESS
                    response?.isSuccessful?.let {
                        response.body()?.photos?.photo?.let {
                            for (photo in it)
                                db?.flickeDao()?.insertAll(photo)
                        }
                    }
                }

            })
        } else networkState.value = NetworkState.FAILED
    }

}