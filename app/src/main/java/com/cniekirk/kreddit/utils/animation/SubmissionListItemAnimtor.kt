package com.cniekirk.kreddit.utils.animation

import android.view.View

class SubmissionListItemAnimtor(itemViewElevation: Int): SlideAlphaAnimator<SubmissionListItemAnimtor>(itemViewElevation) {


    override fun getAnimationTranslationY(itemView: View): Float {

        return -dpToPx(32f, itemView.context).toFloat()

    }

    override fun getAnimationTranslationX(itemView: View?): Float {

        return 0f

    }

}