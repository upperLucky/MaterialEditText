package com.upperlucky.materialedittext

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * created by yunKun.wen on 2020/9/5
 * desc:
 */

private val TEXT_SIZE = 13.dp
private val TEXT_MARGIN = 6.dp
private val HORIZONTAL_OFFSET = 5.dp
private val VERTICAL_OFFSET = 23.dp
private val EXTRA_VERTICAL_OFFSET = 10.dp

class MaterialEditTextView(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
    }

    private var floatingLabelShown = false // 是否在显示
    private var floatingLabelFraction = 0f // 动画的完成度
        set(value) {
            field = value
            invalidate()
        }

    var needFloatingLabel = false
        set(value) {
            if (field != value) {
                field = value
                if (field) {
                    setPadding(paddingLeft,
                        (paddingTop + TEXT_SIZE + TEXT_MARGIN).toInt(),
                        paddingRight,
                        paddingBottom)
                } else {
                    setPadding(paddingLeft,
                        (paddingTop - TEXT_SIZE - TEXT_MARGIN).toInt(),
                        paddingRight,
                        paddingBottom)
                }
            }
        }

    // 动画只要初始化一次
    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "floatingLabelFraction", 0f, 1f)
    }

    init {

        var typeArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditTextView)
        needFloatingLabel = typeArray.getBoolean(R.styleable.MaterialEditTextView_needFloatingLabel, true)
        typeArray.recycle()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (!needFloatingLabel) {
            return
        }
        if (floatingLabelShown && text.isNullOrEmpty()) {
            // 隐藏
            animator.reverse()
            floatingLabelShown = false
        } else if (!floatingLabelShown && !text.isNullOrEmpty()) {
            // 显示
            animator.start()
            floatingLabelShown = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha = (0xff * floatingLabelFraction).toInt()
        var currentVerticalOffset = VERTICAL_OFFSET + (1 - floatingLabelFraction) * EXTRA_VERTICAL_OFFSET
        canvas.drawText(hint.toString(), HORIZONTAL_OFFSET, currentVerticalOffset, paint)
    }
}