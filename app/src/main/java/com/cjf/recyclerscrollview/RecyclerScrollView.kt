package com.cjf.recyclerscrollview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ScrollView

class RecyclerScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var mOnScrollChanged: OnScrollChanged? = null

    var pageHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        pageHeight = MeasureSpec.getSize(heightMeasureSpec)
        for (i in 0 until (getChildAt(0) as ViewGroup).childCount) {
            val v = (getChildAt(0) as ViewGroup).getChildAt(i)
            v.layoutParams.height = pageHeight
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        post { scrollTo(0, pageHeight) }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (t == 0) {
            scrollTo(0, pageHeight)
            mOnScrollChanged?.onLast()
        } else if (t == 2 * pageHeight) {
            scrollTo(0, pageHeight)
            mOnScrollChanged?.onNext()
        }
    }

    fun setOnScrollChanged(onScrollChanged: OnScrollChanged?) {
        mOnScrollChanged = onScrollChanged
    }

    interface OnScrollChanged {
        fun onNext()
        fun onLast()
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> if (scrollY > 1.1 * pageHeight) { //上划
                post {
                    smoothScrollTo(0, 2 * pageHeight) //返回第二页
                }
            } else if (scrollY < 0.9 * pageHeight) { //下滑
                post {
                    smoothScrollTo(0, 0) //返回第二页
                }
            } else {
                post {
                    smoothScrollTo(0, pageHeight) //返回第二页
                }
            }
        }
        return super.onTouchEvent(ev)
    }

}