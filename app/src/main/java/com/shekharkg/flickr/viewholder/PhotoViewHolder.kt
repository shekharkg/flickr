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

package com.shekharkg.flickr.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shekharkg.flickr.R
import com.shekharkg.flickr.repo.data.FlickrEntity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_photo.view.*
import java.lang.Exception

class PhotoViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {


    fun bind(photo: FlickrEntity) {
        itemView.titleTV.text = photo.title ?: ""

        itemView.photoLoader.visibility = View.VISIBLE

        Picasso.get().load(photo.url_q).into(itemView.photo, object : Callback {
            override fun onSuccess() {
                itemView.photoLoader.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                itemView.photoLoader.visibility = View.GONE
                Picasso.get().load(R.mipmap.ic_launcher_round).into(itemView.photo)
            }

        })
    }

}