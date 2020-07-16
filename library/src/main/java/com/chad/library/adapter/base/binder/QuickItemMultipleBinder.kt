package com.chad.library.adapter.base.binder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.util.getItemView
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 使用布局 ID 快速构建 Binder
 * @param T item 数据类型
 */
abstract class QuickItemMultipleBinder<T> {

    private val clickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }
    private val longClickViewIds by lazy(LazyThreadSafetyMode.NONE) { ArrayList<Int>() }

    internal open var _adapter: BaseQuickAdapter<T, BaseViewHolder>? = null
    internal open var _context: Context? = null

    val adapter: BaseQuickAdapter<T, BaseViewHolder>
        get() {
            checkNotNull(_adapter) {
                """This $this has not been attached to BaseBinderAdapter yet.
                    You should not call the method before addItemBinder()."""
            }
            return _adapter!!
        }

    val context: Context
        get() {
            checkNotNull(_context) {
                """This $this has not been attached to BaseBinderAdapter yet.
                    You should not call the method before onCreateViewHolder()."""
            }
            return _context!!
        }

    val data: MutableList<T> get() = adapter.data

    /**
     * 在此处对设置item数据
     * @param holder BaseViewHolder
     * @param data T
     */
    abstract fun convert(holder: BaseViewHolder, data: T)

    /**
     * 使用局部刷新时候，会调用此方法
     * @param holder BaseViewHolder
     * @param data T
     * @param payloads List<Any>
     */
    open fun convert(holder: BaseViewHolder, data: T, payloads: List<Any>) {}

    open fun onFailedToRecycleView(holder: BaseViewHolder): Boolean {
        return false
    }

    /**
     * Called when a view created by this [BaseItemBinder] has been attached to a window.
     * 当此[BaseItemBinder]出现在屏幕上的时候，会调用此方法
     *
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the [BaseItemBinder] previously freed any resources in
     * [onViewDetachedFromWindow][.onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    open fun onViewAttachedToWindow(holder: BaseViewHolder) {}

    /**
     * Called when a view created by this [BaseItemBinder] has been detached from its
     * window.
     * 当此[BaseItemBinder]从屏幕上移除的时候，会调用此方法
     *
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    open fun onViewDetachedFromWindow(holder: BaseViewHolder) {}

    /**
     * item 若想实现条目点击事件则重写该方法
     * @param holder BaseViewHolder
     * @param data T
     * @param position Int
     */
    open fun onClick(holder: BaseViewHolder, view: View, data: T, position: Int) {}

    /**
     * item 若想实现条目长按事件则重写该方法
     * @param holder BaseViewHolder
     * @param data T
     * @param position Int
     * @return Boolean
     */
    open fun onLongClick(holder: BaseViewHolder, view: View, data: T, position: Int): Boolean {
        return false
    }

    /**
     * item 子控件的点击事件
     * @param holder BaseViewHolder
     * @param view View
     * @param data T
     * @param position Int
     */
    open fun onChildClick(holder: BaseViewHolder, view: View, data: T, position: Int) {}

    /**
     * item 子控件的长按事件
     * @param holder BaseViewHolder
     * @param view View
     * @param data T
     * @param position Int
     * @return Boolean
     */
    open fun onChildLongClick(holder: BaseViewHolder, view: View, data: T, position: Int): Boolean {
        return false
    }

    fun addChildClickViewIds(@IdRes vararg ids: Int) {
        ids.forEach {
            this.clickViewIds.add(it)
        }
    }

    fun getChildClickViewIds() = this.clickViewIds

    fun addChildLongClickViewIds(@IdRes vararg ids: Int) {
        ids.forEach {
            this.longClickViewIds.add(it)
        }
    }

    fun getChildLongClickViewIds() = this.longClickViewIds

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            BaseViewHolder(parent.getItemView(getLayoutId()))
}

