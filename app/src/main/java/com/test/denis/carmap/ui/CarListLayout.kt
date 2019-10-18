package com.test.denis.carmap.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.denis.carmap.R
import com.test.denis.carmap.model.CarModel
import com.test.denis.carmap.util.BottomSheetBehaviorGoogleMapsLike
import kotlinx.android.synthetic.main.car_list_layout.view.*

class CarListLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var behaviour: BottomSheetBehaviorGoogleMapsLike<ConstraintLayout>
    private var carListAdapter = CarListAdapter {}

    init {
        inflate(context, R.layout.car_list_layout, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        behaviour = BottomSheetBehaviorGoogleMapsLike.from(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        contentList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = carListAdapter
        }
    }

    fun setData(listItems: List<CarModel>) {
        carListAdapter.initData(listItems)
    }

    fun setItemSelectedAction(action: (CarModel) -> Unit) {
        carListAdapter.callback = action
    }

    fun setBottomSheetState(newState: Int) {
        behaviour.state = newState
    }
}