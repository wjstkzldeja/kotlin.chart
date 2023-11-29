package com.osl.base.project.main.views.ruler

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentRulerBinding
import com.osl.base.project.main.utils.ruler.CenteredItemDecoration
import com.osl.base.project.main.utils.ruler.defaultWeightRange
import com.osl.base.project.main.utils.ruler.setupRuler
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.ruler.adapter.RulerAdapter
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber.Forest.d

class RulerFragment : OslFragment<FragmentRulerBinding, RulerViewModel>() {
    override val layoutRes = R.layout.fragment_ruler
    override val destinationId = R.id.rulerFragment
    override val viewModelClass = RulerViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        d("rulerTest defaultWeightRange : ${defaultWeightRange.toList()}")
//        d("rulerTest defaultKeyRange : ${defaultKeyRange.toList()}")
        initRecyclerView()
        createWeightList()
    }

    override fun addObservers() {
    }

    private fun initRecyclerView() {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
            ui.viewDataBinding.rulerLayoutManager = this
            ui.viewDataBinding.rulerAdapter = RulerAdapter()
            ui.viewDataBinding.rvRulerWeight.addItemDecoration(CenteredItemDecoration())
            //val weight = (args.cat.cat.catWeight ?: args.cat.orgCat?.catWeight)?.times(10)?.toInt() ?: 0
            val weight = 1000//체중 400~1400
            setupRuler(
                ui.viewDataBinding.rvRulerWeight, this,
                defaultWeightRange.indexOf(weight)
            ) {
                viewModel.updateWeightValue(it)
            }
        }
    }

    private fun createWeightList() {
        val firstValue = 0
        val lastValue = 10
        val weightList: ArrayList<Float> = arrayListOf()
        /** 인트 값 0,1,2,3,4,5,6,7,8,9,10*/
        for (intIndex in firstValue..lastValue) {
//            d("createFloat i : i")
            weightList.add(intIndex.toFloat())
            /** 마지막은 내부 포문 돌지않음*/
            if (intIndex != lastValue) {
                /** 소수 값 0.1..1.1..2.1*/
                for (a in 1..9) {
//                d("createFloat a : ${a.toFloat()/10f}")
                    weightList.add(a.toFloat() / 10f + intIndex)
                }
            }
        }
        d("rulerTest weightList : ${weightList}")
    }
}