package com.jamal2367.minicarlauncher.presentation.shortcuts_view.recycler_view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jamal2367.minicarlauncher.R


class ShortcutsRecyclerView(private val ctx: Context, attrs: AttributeSet?) :
    RecyclerView(ctx, attrs) {

    companion object {
        const val ITEMS_PER_SCREEN_DEFAULT = 5
        const val ITEMS_MARGIN_DEFAULT_DP = 10f
    }

    private var itemsPerScreen: Int = ITEMS_PER_SCREEN_DEFAULT
    private var itemsMarginPx: Int = ITEMS_MARGIN_DEFAULT_DP.getPxFromDp()

    init {
        ctx.obtainStyledAttributes(attrs, R.styleable.ShortcutsRecyclerView).apply {
            itemsPerScreen =
                getInt(R.styleable.ShortcutsRecyclerView_shortcutsPerScreen, itemsPerScreen)
            itemsMarginPx = getDimensionPixelSize(
                R.styleable.ShortcutsRecyclerView_marginBetweenShortcuts,
                itemsMarginPx
            )

            recycle()
        }

        layoutManager = ShortcutsLayoutManager(ctx, 2)
        addItemDecoration(ShortcutsDecorator(itemsPerScreen, itemsMarginPx))
        setHasFixedSize(true)
    }

    private fun Float.getPxFromDp(): Int {
        val displayMetrics = ctx.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics).toInt()
    }

    fun getItemsCountDesired(): Int = itemsPerScreen

    private class ShortcutsLayoutManager(ctx: Context, spanCount: Int) : GridLayoutManager(ctx, spanCount) {
        init {
            orientation = HORIZONTAL
        }

        override fun canScrollHorizontally() = true
    }

    private class ShortcutsDecorator(private val itemsPerScreen: Int, private val itemMargin: Int) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            super.getItemOffsets(outRect, view, parent, state)

            outRect.apply {
                if (parent.getChildAdapterPosition(view) < 0) {
                    left = itemMargin
                    top = itemMargin
                }

                right = itemMargin
                bottom = itemMargin
            }

            val itemWidth = (parent.measuredWidth - parent.paddingLeft - parent.paddingRight) / 4
            view.layoutParams.width = itemWidth - itemMargin - (itemMargin / itemsPerScreen)
        }
    }
}

class ShortcutsTouchCallback(private val consumer: (Int, Int) -> Unit) :
    ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        srcHolder: RecyclerView.ViewHolder,
        targetHolder: RecyclerView.ViewHolder
    ): Boolean {
        val sourcePosition = srcHolder.adapterPosition
        val targetPosition = targetHolder.adapterPosition
        consumer.invoke(sourcePosition, targetPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = false
}