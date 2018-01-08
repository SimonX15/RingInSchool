package com.app.simon.ringinschool.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * desc: RecyclerView 分割线
 * date: 2017/12/11
 *
 * @author xw
 */
class CustomerItemDecoration(mContext: Context, orientation: Int, @DrawableRes drawableId: Int = 0) : RecyclerView.ItemDecoration() {

    /** 分割线 */
    private val mDivider: Drawable?

    /** 方向 */
    private var mOrientation: Int = 0

    init {
        if (drawableId != 0) {
            this.mDivider = ContextCompat.getDrawable(mContext, drawableId)
        } else {
            val typedArray = mContext.obtainStyledAttributes(ATRRS)
            this.mDivider = typedArray.getDrawable(0)
            typedArray.recycle()
        }

        setOrientation(orientation)
    }

    /**
     * onDraw是在itemView绘制之前，onDrawOver是在 itemView绘制之后
     */
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (mOrientation == HORIZONTAL_LIST) {
            drawVerticalLine(canvas, parent, state)
        } else {
            drawHorizontalLine(canvas, parent, state)
        }
    }

    /*override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (mOrientation == HORIZONTAL_LIST) {
            drawVerticalLine(c, parent, state)
        } else {
            drawHorizontalLine(c, parent, state)
        }
    }*/

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        if (mOrientation == HORIZONTAL_LIST) {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
        } else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
        }
    }

    //设置屏幕的方向
    private fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException("invalid orientation")
        }
        mOrientation = orientation
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    private fun drawHorizontalLine(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            //获得child的布局信息
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
            //Log.d("wnw", left + " " + top + " "+right+"   "+bottom+" "+i);
        }
    }

    //画竖线
    private fun drawVerticalLine(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            //获得child的布局信息
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider!!.intrinsicWidth
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
    }

    companion object {
        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL

        //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
        val ATRRS = intArrayOf(android.R.attr.listDivider)
    }
}