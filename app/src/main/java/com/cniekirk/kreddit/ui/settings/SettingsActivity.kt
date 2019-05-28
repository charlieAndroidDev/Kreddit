package com.cniekirk.kreddit.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.fragment.app.Fragment
import com.cniekirk.kreddit.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_settings.*
import javax.inject.Inject

class SettingsActivity: AppCompatActivity(), HasSupportFragmentInjector, GestureDetector.OnGestureListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

//        theme_three.setOnTouchListener { v, event ->
//
////            when (event) {
////                MotionEvent.
////            }
//
//        }

        theme_three.setOnClickListener {
            when (root.currentState) {

                R.id.fanOut -> {
                    root.setTransition(R.id.fanOut, R.id.topCardOnTop)
                    root.transitionToEnd()
                    collapsedCardCompletedListener(R.id.bottomCardOnTop)
                }

                R.id.bottomCardOnTop -> {
                    Log.d("MOTION", "Do nothing!")
                }

                R.id.middleCardOnTop -> {
                    root.setTransition(R.id.middleCardOnTop, R.id.bottomCardOnTop)
                    root.transitionToEnd()
                }

                R.id.topCardOnTop -> {
                    root.setTransition(R.id.topCardOnTop, R.id.bottomCardOnTop)
                    root.transitionToEnd()
                }

            }
        }

        theme_two.setOnClickListener {
            when (root.currentState) {

                R.id.fanOut -> {
                    root.setTransition(R.id.fanOut, R.id.topCardOnTop)
                    root.transitionToEnd()
                    collapsedCardCompletedListener(R.id.middleCardOnTop)
                }

                R.id.middleCardOnTop -> {
                    Log.d("MOTION", "Do nothing!")
                }

                R.id.bottomCardOnTop -> {
                    root.setTransition(R.id.bottomCardOnTop, R.id.middleCardOnTop)
                    root.transitionToEnd()
                }

                R.id.topCardOnTop -> {
                    root.setTransition(R.id.topCardOnTop, R.id.middleCardOnTop)
                    root.transitionToEnd()
                }

            }
        }

        theme_one.setOnClickListener {
            when (root.currentState) {

                R.id.fanOut -> {
                    root.setTransition(R.id.fanOut, R.id.topCardOnTop)
                    root.transitionToEnd()
                }

                R.id.topCardOnTop -> {
                    Log.d("MOTION", "Do nothing!")
                }

                R.id.middleCardOnTop -> {
                    root.setTransition(R.id.middleCardOnTop, R.id.topCardOnTop)
                    root.transitionToEnd()
                }

                R.id.bottomCardOnTop -> {
                    root.setTransition(R.id.bottomCardOnTop, R.id.topCardOnTop)
                    root.transitionToEnd()
                }

            }
        }
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return true
    }

    private fun collapsedCardCompletedListener(@IdRes endStateId: Int) {
        root.setTransitionListener(object : TransitionAdapter() {

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                if (currentId == R.id.topCardOnTop) {
                    root.setTransition(R.id.topCardOnTop, endStateId)
                    root.transitionToEnd()
                }
                root.setTransitionListener(null)
            }
        })
    }

    override fun onShowPress(e: MotionEvent?) {}
    override fun onLongPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}