package com.everydaychef.main.helpers

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.everydaychef.EverydayChefApplication

class ImageUtility {
    companion object{
        fun parseImage(pictureUrl: String, resource: ImageView,
                       context: Context){
            var realUrl = pictureUrl
            if(realUrl.length > 3) {
                if(realUrl.contains("local:")) {
                    realUrl = pictureUrl
                        .replace("local:", EverydayChefApplication.API_BASE_URL)
                }
                Glide.with(context)
                    .load(realUrl)
                    .into(resource)
            }
        }
    }
}