package com.cniekirk.kreddit.ui.submission

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.*
import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.di.Injectable
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.viewmodel.AppViewModelFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_submission.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.saket.inboxrecyclerview.globalVisibleRect
import me.saket.inboxrecyclerview.page.ExpandablePageLayout
import me.saket.inboxrecyclerview.page.InterceptResult
import ru.noties.markwon.Markwon
import javax.inject.Inject

/**
 * Fragment to display a Reddit Submission
 */
@ExperimentalCoroutinesApi
class FragmentSubmission: Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var submissionViewModel: SubmissionViewModel

    private val submissionPage by lazy { view!!.parent as ExpandablePageLayout }

    /**
     * Inflate our layout to display our UI
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_submission, container, false)
    }

    /**
     * Called when the Fragment has been instantiated
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        submissionViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SubmissionViewModel::class.java)
        submissionViewModel.imageInformation.observe(this, Observer { renderImgurImage(it) })
    }

    /**
     * Called once the view has been created/rendered
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        submissionPage.pullToCollapseInterceptor = { downX, downY, upwardPull ->
            if (submission_scrollable_container.globalVisibleRect().contains(downX, downY).not()) {
                InterceptResult.IGNORED
            }

            val directionInt = if (upwardPull) +1 else -1
            val canScrollFurther = submission_scrollable_container.canScrollVertically(directionInt)
            when {
                canScrollFurther -> InterceptResult.INTERCEPTED
                else -> InterceptResult.IGNORED
            }
        }

    }

    /**
     * Special function the render Imgur images specifically
     * @param imageInformation: The [ImageInformation] returned by the [SubmissionViewModel]
     */
    private fun renderImgurImage(imageInformation: ImageInformation) {

        val animation = AutoTransition()
        animation.interpolator = FastOutSlowInInterpolator()
        animation.duration = 150

        val constraintSet = ConstraintSet()

        val marginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        )

        if (imageInformation.data.type has "video"
            || imageInformation.data.type has "gif") {

            player_view.visibility = View.VISIBLE
            val player = ExoPlayerFactory.newSimpleInstance(requireContext())
            player_view.player = player

            val dataSourceFactory = DefaultDataSourceFactory(requireContext(),
                    Util.getUserAgent(context, "yourApplicationName"))
            val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(imageInformation.data.mp4))
            player.prepare(mediaSource)

            constraintSet.clone(submission_layout)
            constraintSet.connect(R.id.submission_header, ConstraintSet.TOP, R.id.player_view, ConstraintSet.BOTTOM, marginPx.toInt())
            TransitionManager.beginDelayedTransition(submission_layout, animation)
            constraintSet.applyTo(submission_layout)


        } else {

            submission_image.visibility = View.VISIBLE
            Glide.with(requireContext()).load(imageInformation.data.link)
                .transition(DrawableTransitionOptions.withCrossFade()).into(submission_image)

            constraintSet.clone(submission_layout)
            constraintSet.connect(R.id.submission_header, ConstraintSet.TOP, R.id.submission_image, ConstraintSet.BOTTOM, marginPx.toInt())
            TransitionManager.beginDelayedTransition(submission_layout, animation)
            constraintSet.applyTo(submission_layout)

        }

        submission_content_container.visibility = View.GONE


    }

    /**
     * TODO: Make use of the scrollable RecyclerView sheet
     *
     * Complex function to render the Submission UI
     * @param submissionUiModel: The model class representing what to display in the UI
     */
    fun populateUi(submissionUiModel: SubmissionUiModel) {

        val animation = AutoTransition()
        animation.interpolator = FastOutSlowInInterpolator()
        animation.duration = 150

        val constraintSet = ConstraintSet()
        constraintSet.clone(submission_layout)

        val marginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        )

        constraintSet.connect(R.id.submission_header, ConstraintSet.TOP, R.id.close_btn, ConstraintSet.BOTTOM, marginPx.toInt())
        //TransitionManager.beginDelayedTransition(submission_layout, animation)
        constraintSet.applyTo(submission_layout)

        // Reset so no previous image is displayed
        player_view.visibility = View.GONE
        submission_image.visibility = View.GONE
        web_link_container.visibility = View.GONE
        submission_content_container.visibility = View.GONE

        submissionUiModel.url?.let {

            if (it.isImage()) {

                if (it.isImgur()) {
                    val imageHash = it.substring(it.lastIndexOf("/"), it.lastIndexOf("."))
                    submissionViewModel.getImageInformation(imageHash)
                    submission_title.text = submissionUiModel.title
                    submission_score.text = submissionUiModel.voteString
                    return@let
                }

                submission_image.visibility = View.VISIBLE

                if (it.isGif()) {
                    Glide.with(requireContext()).asGif().load(it)
                        .transition(DrawableTransitionOptions.withCrossFade()).listener(
                            object: RequestListener<GifDrawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<GifDrawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {

                                    return false

                                }

                                override fun onResourceReady(
                                    resource: GifDrawable?,
                                    model: Any?,
                                    target: Target<GifDrawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    submission_title.text = submissionUiModel.title
                                    submission_score.text = submissionUiModel.voteString

                                    submission_content_container.visibility = View.GONE

                                    constraintSet.clone(submission_layout)
                                    constraintSet.connect(R.id.submission_header, ConstraintSet.TOP, R.id.submission_image, ConstraintSet.BOTTOM, marginPx.toInt())
                                    TransitionManager.beginDelayedTransition(submission_layout, animation)
                                    constraintSet.applyTo(submission_layout)

                                    val bmp = resource?.toBitmap()
                                    bmp?.let {

                                        val palette = Palette.from(bmp).generate()
                                        val dominantColour = palette.getDominantColor(Color.WHITE)

                                        val lightness = Color.red(dominantColour) + Color.blue(dominantColour) + Color.green(dominantColour)
                                        when {
                                            lightness < 300 -> close_btn.setImageResource(R.drawable.ic_close_white)
                                            else -> close_btn.setImageResource(R.drawable.ic_close_black_24dp)
                                        }

                                    }

                                    return false

                                }


                            }
                        ).into(submission_image)
                } else {
                    Glide.with(requireContext()).asBitmap().load(it)
                        .transition(BitmapTransitionOptions.withCrossFade()).listener(
                            object: RequestListener<Bitmap> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    isFirstResource: Boolean
                                ): Boolean {

                                    return false

                                }

                                override fun onResourceReady(
                                    resource: Bitmap?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    submission_title.text = submissionUiModel.title
                                    submission_score.text = submissionUiModel.voteString
                                    submission_content_container.visibility = View.GONE

                                    constraintSet.clone(submission_layout)
                                    constraintSet.connect(R.id.submission_header, ConstraintSet.TOP, R.id.submission_image, ConstraintSet.BOTTOM, marginPx.toInt())
                                    TransitionManager.beginDelayedTransition(submission_layout, animation)
                                    constraintSet.applyTo(submission_layout)

                                    resource?.let {

                                        val palette = Palette.from(resource).generate()
                                        val dominantColour = palette.getDominantColor(Color.WHITE)

                                        val lightness = Color.red(dominantColour) + Color.blue(dominantColour) + Color.green(dominantColour)
                                        when {
                                            lightness < 300 -> close_btn.setImageResource(R.drawable.ic_close_white)
                                            else -> close_btn.setImageResource(R.drawable.ic_close_black_24dp)
                                        }

                                    }

                                    return false

                                }

                            }
                        ).into(submission_image)
                }

            } else if (submissionUiModel.content != null) {

                web_link_container.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .asBitmap()
                    .load(submissionUiModel.thumbnailUrl)
                    .listener(object: RequestListener<Bitmap> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false

                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            resource?.let { bitmap ->

                                val palette = Palette.from(bitmap).generate()
                                val dominantColour = palette.getDominantColor(Color.BLACK)
                                web_link_container.setCardBackgroundColor(dominantColour)

                                val lightness = Color.red(dominantColour) + Color.blue(dominantColour) + Color.green(dominantColour)
                                when {
                                    lightness < 300 -> web_link_title.setTextColor(Color.WHITE)
                                    else -> web_link_title.setTextColor(Color.BLACK)
                                }

                            }

                            return false

                        }

                    }).into(web_link_thumbnail)

                submission_content_container.visibility = View.GONE
                submission_title.text = submissionUiModel.title
                submission_score.text = submissionUiModel.voteString

                web_link_title.text = submissionUiModel.url

                constraintSet.clone(submission_layout)
                constraintSet.connect(R.id.web_link_container, ConstraintSet.TOP, R.id.close_btn, ConstraintSet.BOTTOM, marginPx.toInt())
                constraintSet.connect(R.id.submission_header, ConstraintSet.TOP, R.id.web_link_container, ConstraintSet.BOTTOM, marginPx.toInt())
                TransitionManager.beginDelayedTransition(submission_layout, animation)
                constraintSet.applyTo(submission_layout)

            } else {

                submission_content_container.visibility = View.VISIBLE
                // Need to do this async
                submissionUiModel.content?.let {
                    Markwon.setMarkdown(submission_content, it)
                }
                submission_title.text = submissionUiModel.title
                submission_score.text = submissionUiModel.voteString

            }

        }

    }

}