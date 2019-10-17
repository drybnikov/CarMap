package com.test.denis.carmap.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.test.denis.carmap.R
import com.test.denis.carmap.util.BottomSheetBehaviorGoogleMapsLike

class CarListLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var behaviour: BottomSheetBehaviorGoogleMapsLike<ConstraintLayout>

    init {
        inflate(context, R.layout.car_list_layout, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        behaviour = BottomSheetBehaviorGoogleMapsLike.from(this)
        //behaviour.state = BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED
    }
}