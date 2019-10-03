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
import androidx.room.Room
import com.shekharkg.flickr.repo.data.FlickrDatabase

class Repository(application: Application) {
    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(application: Application) = instance ?: synchronized(this) {
            instance ?: Repository(application).also { instance = it }
        }
    }

    var db: FlickrDatabase? =
        Room.databaseBuilder(application, FlickrDatabase::class.java, "flickr_database")
            .fallbackToDestructiveMigration().build()

}