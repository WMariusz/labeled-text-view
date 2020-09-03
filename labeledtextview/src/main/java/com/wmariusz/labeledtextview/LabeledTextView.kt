package com.wmariusz.labeledtextview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Space
import androidx.annotation.StringRes
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat

/**
 * Copyright 2020 Mariusz Wojtach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

const val DEFAULT_RES_VALUE = -1
const val DEFAULT_DIMENS_VALUE = 0

class LabeledTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val textView by lazy {
        AppCompatTextView(context)
            .apply {
                id = View.generateViewId()

                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
    }
    private val labelView by lazy {
        AppCompatTextView(context)
            .apply {
                id = View.generateViewId()
                labelFor = textView.id

                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
    }
    private val spaceBetween by lazy {
        Space(context)
            .apply {
                id = View.generateViewId()

                layoutParams = LayoutParams(0, 0, 1f)
            }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LabeledTextView,
            0, 0
        ).apply {

            textView.text = getText(R.styleable.LabeledTextView_android_text)
            trySetTextAppearance(
                this,
                textView,
                R.styleable.LabeledTextView_android_textAppearance
            )

            labelView.text = getText(R.styleable.LabeledTextView_labelText)
            trySetTextAppearance(
                this,
                labelView,
                R.styleable.LabeledTextView_labelTextAppearance
            )

            if (hasValue(R.styleable.LabeledTextView_textPadding)) {
                val pixelValue = getDimensionPixelSize(
                    R.styleable.LabeledTextView_textPadding,
                    DEFAULT_DIMENS_VALUE
                )

                when (orientation) {
                    VERTICAL -> textView.setPadding(
                        textView.paddingLeft,
                        pixelValue,
                        textView.paddingRight,
                        textView.paddingBottom
                    )
                    HORIZONTAL -> textView.setPadding(
                        pixelValue,
                        textView.paddingTop,
                        textView.paddingRight,
                        textView.paddingBottom
                    )
                    else -> throw IllegalArgumentException("Unknown view orientation:  $orientation")
                }
            }

            recycle()
        }

        addView(labelView)
        addView(spaceBetween)
        addView(textView)
    }

    private fun trySetTextAppearance(
        typedArray: TypedArray,
        textView: AppCompatTextView,
        @StyleableRes resourceId: Int
    ) {
        if (typedArray.hasValue(resourceId)) {
            TextViewCompat.setTextAppearance(
                textView,
                typedArray.getResourceId(
                    resourceId,
                    DEFAULT_RES_VALUE
                )
            )
        }
    }

    fun setText(@StringRes resid: Int) {
        textView.setText(resid)
    }

    fun setText(text: CharSequence?) {
        textView.text = text
    }

    fun getText(): CharSequence? = textView.text

    fun setLabel(@StringRes resid: Int) {
        labelView.setText(resid)
    }

    fun setLabel(text: CharSequence?) {
        labelView.text = text
    }

    fun getLabel(): CharSequence? = labelView.text


}
