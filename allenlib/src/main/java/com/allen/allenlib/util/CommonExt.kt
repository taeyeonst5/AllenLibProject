package com.allen.allenlib.util

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.allen.allenlib.BuildConfig
import com.allen.allenlib.R

const val ALLEN_LOG_TAG = "allenlib"

fun logd(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(ALLEN_LOG_TAG, message)
    }
}

fun loge(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(ALLEN_LOG_TAG, message)
    }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun toast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun View.setSafeClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun getAlertDialogArgsBundle(
    context: Context,
    type: Int,
    title: String,
    message: String,
    positiveText: String,
    negativeText: String? = null,
    destinationId: Int = 0,
    cancelable: Boolean = true
): Bundle {
    val appContext = context.applicationContext
    val bundle = bundleOf(
        appContext.getString(R.string.args_alert_dialog_type) to type,
        appContext.getString(R.string.args_alert_dialog_title) to title,
        appContext.getString(R.string.args_alert_dialog_message) to message,
        appContext.getString(R.string.args_alert_dialog_cancelable) to cancelable,
        appContext.getString(R.string.args_alert_dialog_btn_positive_text) to positiveText,
        appContext.getString(R.string.args_alert_dialog_btn_negative_text) to negativeText,
        appContext.getString(R.string.args_alert_dialog_back_destinationId) to destinationId
    )
    return bundle
}

fun showAlertDialog(
    bundle: Bundle,
    navController: NavController, @IdRes actionToAlertDialogFragment: Int
) {
    navController.navigate(actionToAlertDialogFragment, bundle)
}

fun popBackToHomeFragment(nav: NavController, @IdRes navigationHomeId: Int) {
    nav.popBackStack(navigationHomeId, false)
}

/**
 * for RecyclerView inside NestedScrollView LoadMore
 * @param listener return if needLoadMore currentItemPosition & nowPage
 * */
fun NestedScrollView.setupLoadMoreListener(
    layoutManager: LinearLayoutManager,
    nowPageParam: Int,
    maxPageParam: Int,
    currentItemPositionParam: Int,
    listener: LoadMoreListener
) {

    var currentItemPosition = currentItemPositionParam
    var nowPage = nowPageParam

    this.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (v.getChildAt(v.childCount - 1) != null) {
            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight)) &&
                scrollY > oldScrollY
            ) {
                val totalItemCount = layoutManager.itemCount
                val lastVisiblesItems =
                    layoutManager.findLastVisibleItemPosition() + 1 //when scroll to end

                logd("currentItemPosition:$currentItemPosition,totalItemCount:$totalItemCount,lastVisiblesItems:$lastVisiblesItems")

                if (nowPage < maxPageParam) {
                    if (needFetchMoreList(
                            currentItemPosition,
                            lastVisiblesItems,
                            totalItemCount
                        )
                    ) {
                        currentItemPosition += DEFAULT_LIMIT_API_AMOUNT
                        nowPage += 1

                        //use callback
                        listener.onLoadMore(currentItemPosition, nowPage)
                    }
                }
            }
        }
    })
}

private fun needFetchMoreList(
    currentItemPosition: Int,
    lastVisibleItemPosition: Int,
    itemCount: Int
) = (lastVisibleItemPosition == itemCount
        && currentItemPosition != lastVisibleItemPosition)

interface LoadMoreListener {
    fun onLoadMore(currentItemPosition: Int, nowPage: Int)
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun setupAnimation(position: Int, totalItem: Int, recyclerView: RecyclerView) {
    //animate last item
    if (position - 1 >= 0) {
        recyclerView.setViewHolderScale(position - 1, 1.0f, 1.0f)
    }
    //animate next item
    if (position + 1 <= totalItem) {
        recyclerView.setViewHolderScale(position + 1, 1.0f, 1.0f)
    }
    //animate SnapPosition item
    recyclerView.setViewHolderScale(position, 1.2f, 1.2f)
}

private fun RecyclerView.setViewHolderScale(position: Int, scaleX: Float, scaleY: Float) {
    val findViewHolderForLayoutPosition =
        findViewHolderForLayoutPosition(position)
    findViewHolderForLayoutPosition?.let {
        it.itemView.animate().scaleX(scaleX)
        it.itemView.animate().scaleY(scaleY)
    }
}

/**
 * for observer two liveData combine to One LiveData
 */
fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block.invoke(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block.invoke(this.value, liveData.value)
    }
    return result
}