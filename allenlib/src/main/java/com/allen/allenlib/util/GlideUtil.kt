package com.allen.allenlib.util

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import org.jetbrains.annotations.NotNull

object GlideUtil {

    fun setProfileImage(
        context: Context, url: String?, imageVIew: ImageView, @DrawableRes defaultImage: Int
    ) {
        Glide.with(context).load(url).error(defaultImage).into(imageVIew)
    }

    //todo PhotoView can refactor to here

    fun setDefaultProfileImage(
        context: Context, @NotNull @DrawableRes resId: Int,
        imageVIew: ImageView
    ) {
        Glide.with(context).load(resId).into(imageVIew)
    }

    fun setLocalImageUri(context: Context, uri: Uri, view: ImageView) {
        Glide.with(context).load(uri).into(view)
    }
}