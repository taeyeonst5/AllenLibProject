package com.allen.allenlib.view.viewpager

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.parcel.Parcelize

/**
 * for ViewPager2 use DiffUtil notifyDataSet. need with BaseViewPager2ViewModel & ViewPager2Item
 */
abstract class BaseFragmentStateAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    abstract val viewPager2ViewModel: BaseViewPager2ViewModel
    abstract val createFragment: Fragment

    companion object {
        const val KEY_VIEW_PAGER2_ITEM = "key_view_pager2_item"
    }

    fun changeDataSet(performChanges: () -> Unit) {
        val idsOld = viewPager2ViewModel.createIdSnapshot()
        performChanges()
        val idsNew = viewPager2ViewModel.createIdSnapshot()
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = idsOld.size
            override fun getNewListSize(): Int = idsNew.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                idsOld[oldItemPosition].id == idsNew[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                idsOld[oldItemPosition] == idsNew[newItemPosition]
        }, true).dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return viewPager2ViewModel.items.size
    }

    override fun createFragment(position: Int): Fragment {
        return createFragment.also {
            it.arguments = Bundle().apply {
                putParcelable(KEY_VIEW_PAGER2_ITEM, viewPager2ViewModel.items[position])
            }
        }
    }
}

open class BaseViewPager2ViewModel : ViewModel() {

    open var items: MutableList<ViewPager2Item> = mutableListOf()
    fun addNewAt(position: Int, item: ViewPager2Item) = items.add(position, item)
    fun removeAt(position: Int) = items.removeAt(position)
    fun createIdSnapshot(): List<ViewPager2Item> = items.toMutableList()
    val size: Int get() = items.size
}

@Parcelize
data class ViewPager2Item(val id: Long, val title: String) : Parcelable
