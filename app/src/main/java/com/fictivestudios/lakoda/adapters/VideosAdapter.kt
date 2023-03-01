package com.fictivestudios.lakoda.adapters

import android.animation.Animator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.lakoda.Interface.OnItemClickListener
import com.fictivestudios.lakoda.R
import com.fictivestudios.lakoda.apiManager.response.HomePostData
import com.fictivestudios.lakoda.utils.PreferenceUtils
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.Constants.Companion.LIKED
import com.fictivestudios.ravebae.utils.Constants.Companion.STATUS_UNFOLLOWED
import com.fictivestudios.ravebae.utils.Constants.Companion.UNLIKED
import com.fictivestudios.ravebae.utils.Constants.Companion.getUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_home_post.view.*
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.coroutines.*


class VideosAdapter(
    var videolist: ArrayList<HomePostData>,
    val requireContext: Context,
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VideosAdapter.VideoViewHolder>() {


    var mVideoItems = videolist
    private var context = requireContext
    private var onItemClickListener = onItemClickListener
    var pagePostion: Int? = -1

    private var toggleMenu = false
    private var animSlide: Animation? = null


    private var videoView: VideoView? = null

    var isClick: Boolean = false
    var row_index = -1

    var loader = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {


        var view = VideoViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false)
        );


        return view


    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {

        try {
            holder.itemView.right_lay.visibility = View.INVISIBLE
            toggleMenu = false

            if (true) { // mVideoItems.size - 1 != position

                if (!mVideoItems[position].video_image.isNullOrEmpty()) {
                    holder.itemView.tv_like_count.setText(mVideoItems[position].like_count.toString())
                    holder.itemView.tv_comment_count.setText(mVideoItems[position].comment_count.toString())
                    holder.itemView.tv_share_count.setText(mVideoItems[position].share_count.toString())
                 /*   if(mVideoItems[position].shared_by!=null){
                        holder.itemView.txtTitle.setText("@"+mVideoItems[position].shared_by!!.name)
                        if (!mVideoItems[position].user!!.image.isNullOrEmpty()) {
                            Picasso.get()
                                .load(Constants.IMAGE_BASE_URL + mVideoItems[position].shared_by!!.image)
                                .into(holder.itemView.iv_user)
                        }

                    }
                    else{
                        holder.itemView.txtTitle.setText("@"+mVideoItems[position].user!!.name)

                        if (!mVideoItems[position].user!!.image.isNullOrEmpty()) {
                            Picasso.get()
                                .load(Constants.IMAGE_BASE_URL + mVideoItems[position].user!!.image)
                                .into(holder.itemView.iv_user)
                        }


                    }*/

                    holder.itemView.txtTitle.setText("@"+mVideoItems[position].user!!.name)

                    if (!mVideoItems[position].user!!.image.isNullOrEmpty()) {
                        Picasso.get()
                            .load(Constants.IMAGE_BASE_URL + mVideoItems[position].user!!.image)
                            .into(holder.itemView.iv_user)
                    }



                    holder.itemView.txtDesc.setText(mVideoItems[position].description)

                    if (mVideoItems[position].follow_status == STATUS_UNFOLLOWED) {
                        holder.itemView.btn_follow.visibility = View.VISIBLE

                    } else {
                        holder.itemView.btn_follow.visibility = View.GONE
                    }

                  /*  if(mVideoItems[position].shared_by!=null){

                        if (getUser().id.equals(mVideoItems[position].shared_by!!.id)) {
                            holder.itemView.btn_report_video.visibility = View.GONE
                            holder.itemView.tv_report_video.visibility = View.GONE
                            holder.itemView.tv_share_count.visibility = View.GONE
                            holder.itemView.btn_share.visibility = View.GONE
                        }
                        else{
                            holder.itemView.btn_report_video.visibility = View.VISIBLE
                            holder.itemView.tv_report_video.visibility = View.VISIBLE
                            holder.itemView.tv_share_count.visibility = View.VISIBLE
                            holder.itemView.btn_share.visibility = View.VISIBLE
                        }

                    }
                    else{
                        if (getUser().id.equals(mVideoItems[position].user!!.id)) {
                            holder.itemView.btn_share.visibility = View.GONE
                            holder.itemView.btn_follow.visibility = View.INVISIBLE
                            holder.itemView.tv_share_count.visibility = View.GONE
                            holder.itemView.btn_report_video.visibility = View.GONE
                            holder.itemView.tv_report_video.visibility = View.GONE
                        } else {
                            holder.itemView.btn_share.visibility = View.VISIBLE
                            holder.itemView.btn_report_video.visibility = View.VISIBLE
                            holder.itemView.tv_report_video.visibility = View.VISIBLE
                            holder.itemView.tv_share_count.visibility = View.VISIBLE

                        }

                    }*/

                    if (getUser().id.equals(mVideoItems[position].user!!.id)) {
                        holder.itemView.btn_share.visibility = View.GONE
                        holder.itemView.btn_follow.visibility = View.INVISIBLE
                        holder.itemView.tv_share_count.visibility = View.GONE
                        holder.itemView.btn_report_video.visibility = View.GONE
                        holder.itemView.tv_report_video.visibility = View.GONE
                    } else {
                        holder.itemView.btn_share.visibility = View.VISIBLE
                        holder.itemView.btn_report_video.visibility = View.VISIBLE
                        holder.itemView.tv_report_video.visibility = View.VISIBLE
                        holder.itemView.tv_share_count.visibility = View.VISIBLE

                    }




                    if (mVideoItems[position]?.is_liked == 1) {
                        holder.itemView.btn_heart_like.setTag(LIKED)
                        // holder.itemView.btn_heart_like.setBackgroundResource(R.drawable.heart_icon)
                        //    itemView.iv_heart.tint
                        holder.itemView.btn_heart_like.setBackgroundResource(R.drawable.ic_star)
                    } else {
                        holder.itemView.btn_heart_like.setTag(UNLIKED)
                        holder.itemView.btn_heart_like.setBackgroundResource(R.drawable.ic_star_white)
                    }



                    if (!mVideoItems[position].video_image.isNullOrEmpty()) {


                        holder.itemView.videoView.setVideoURI((Constants.IMAGE_BASE_URL + mVideoItems[position].video_image).toUri())
                        holder.itemView.videoView.setOnPreparedListener { mp ->
                            holder.itemView.progressBar.visibility = View.GONE
                            holder.itemView.videoView.setOnCompletionListener {

                                holder.itemView.btn_play.visibility = View.VISIBLE
                            }

                            holder.itemView.videoView.seekTo(1);
                            val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
                            val screenRatio =
                                holder.itemView.videoView.width / holder.itemView.videoView.height.toFloat()
                            val scale = videoRatio / screenRatio
                            if (scale >= 1f) {
                                holder.itemView.videoView.scaleX = scale
                            } else {
                                holder.itemView.videoView.scaleY = 1f / scale
                            }
                        }

                        /*          if (pagePostion == position)
                                  {

                                      holder.itemView.videoView.start()
                                      //holder.itemView.videoView.seekTo( 1 );
                                  }
                                  else
                                  {
                                      holder.itemView.videoView.stopPlayback()
                                  }*/
                    }


                }

                if (!loader) {
                    holder.itemView.progress_promote_video.visibility = View.GONE
                }

                holder.itemView.btn_lakoda_menu.setOnClickListener {

                    if (!toggleMenu) {
                        animSlide = AnimationUtils.loadAnimation(
                            requireContext, R.anim.slide_up
                        )

                        holder.itemView.right_lay.startAnimation(animSlide)
                        holder.itemView.right_lay.visibility = View.VISIBLE
                        toggleMenu = true
                        // MENU_TOGGLE_CHECKER = true
                    } else {
                        animSlide = AnimationUtils.loadAnimation(requireContext, R.anim.slide_down)

                        animateMenuView(holder.itemView.right_lay)

                        // holder.itemView.right_lay.visibility = View.GONE
                        toggleMenu = false
                        // MENU_TOGGLE_CHECKER = false
                    }
                }



                holder.itemView.lay_video.setOnClickListener {

                    if (holder.itemView.btn_play.visibility == View.VISIBLE) {
                        holder.itemView.videoView.start()
                        holder.itemView.btn_play.visibility = View.GONE
                    } else {
                        holder.itemView.videoView.pause()
                        holder.itemView.btn_play.visibility = View.VISIBLE

                    }

                }


            }

            val postUserId = videolist?.get(position)?.user?.id


          /*  if( mVideoItems[position].shared_by!=null){

                if(videolist?.get(position)?.is_promoted == 0 &&  videolist?.get(position)?.shared_by?.id == PreferenceUtils.getInt(Constants.USER_ID)){
                    holder.itemView.btn_promote_video.visibility = View.VISIBLE
                    holder.itemView.btn_promote_video.setBackgroundResource(R.drawable.ic_promote)
                    holder.itemView.btn_promote_video.isEnabled = true
                }
                else if(videolist?.get(position)?.is_promoted == 1 &&  videolist?.get(position)?.shared_by?.id == PreferenceUtils.getInt(Constants.USER_ID)) {
                    holder.itemView.btn_promote_video.visibility = View.VISIBLE
                    holder.itemView.btn_promote_video.setBackgroundResource(R.drawable.ic_unpromote)
                    holder.itemView.btn_promote_video.isEnabled = false

                }
                else {
                    holder.itemView.btn_promote_video.visibility = View.GONE
                }

            }
            else{
                if (postUserId == PreferenceUtils.getInt(Constants.USER_ID) && videolist?.get(position)?.is_promoted == 0) {
                    holder.itemView.btn_promote_video.visibility = View.VISIBLE
                    holder.itemView.btn_promote_video.setBackgroundResource(R.drawable.ic_promote)
                    holder.itemView.btn_promote_video.isEnabled = true
                } else if (postUserId == PreferenceUtils.getInt(Constants.USER_ID) && videolist?.get(position)?.is_promoted == 1) {

                    holder.itemView.btn_promote_video.visibility = View.VISIBLE
                    holder.itemView.btn_promote_video.setBackgroundResource(R.drawable.ic_unpromote)
                    holder.itemView.btn_promote_video.isEnabled = false
                }
                else {
                    holder.itemView.btn_promote_video.visibility = View.GONE
                }
            }*/

            if (postUserId == PreferenceUtils.getInt(Constants.USER_ID) && videolist?.get(position)?.is_promoted == 0) {
                holder.itemView.btn_promote_video.visibility = View.VISIBLE
                holder.itemView.btn_promote_video.setBackgroundResource(R.drawable.ic_promote)
                holder.itemView.btn_promote_video.isEnabled = true
            } else if (postUserId == PreferenceUtils.getInt(Constants.USER_ID) && videolist?.get(position)?.is_promoted == 1) {

                holder.itemView.btn_promote_video.visibility = View.VISIBLE
                holder.itemView.btn_promote_video.setBackgroundResource(R.drawable.ic_unpromote)
                holder.itemView.btn_promote_video.isEnabled = false
            }
            else {
                holder.itemView.btn_promote_video.visibility = View.GONE
            }



            holder.itemView.btn_promote_video.setOnClickListener {
           /*     if(mVideoItems[position].shared_by!=null){
                    onItemClickListener.onItemClick(mVideoItems[position].id, it, Constants.SHARESPROMOTE)
                }
                else{
                    onItemClickListener.onItemClick(position, it, Constants.PROMOTE)
                    holder.itemView.progress_promote_video.visibility = View.VISIBLE
                    loader = true
                }*/

                onItemClickListener.onItemClick(position, it, Constants.PROMOTE)
                holder.itemView.progress_promote_video.visibility = View.VISIBLE
                loader = true

                // notifyDataSetChanged()
            }

            holder.itemView.btn_share.setOnClickListener {

                onItemClickListener.onItemClick(position, it, Constants.TYPE_SHARE)

            }

            holder.itemView.btn_follow.setOnClickListener {
                //   onItemClickListener.onItemClick(position,it, Constants.FOLLOW)
                onItemClickListener.onItemClick(position, it, Constants.PROFILE)

            }


            holder.itemView.txtTitle.setOnClickListener {
                onItemClickListener.onItemClick(position, it, Constants.PROFILE)
            }

            holder.itemView.iv_user.setOnClickListener {
                onItemClickListener.onItemClick(position, it, Constants.PROFILE)
            }
            holder.itemView.btn_comment.setOnClickListener {


                if (mVideoItems[position].shared_by != null) {
                    onItemClickListener.onItemClick(position, it, Constants.SHARER_COMMENTS)
                } else {
                    onItemClickListener.onItemClick(position, it, Constants.COMMENTS)
                }
            }

            holder.itemView.btn_heart_like.setOnClickListener {

                if (holder.itemView.btn_heart_like.tag == Constants.UNLIKED)

                //(mVideoItems?.get(position)?.is_liked == 0)
                {
                    var count = mVideoItems?.get(position)?.like_count
                    if (count != null) {
                        count += 1
                        holder.itemView.tv_like_count.text = count.toString()
                        mVideoItems?.get(position)?.like_count = count
                    }

                    holder.itemView.btn_heart_like.setTag(LIKED)
                    holder.itemView.btn_heart_like.setBackground(context.getDrawable(R.drawable.ic_star))
                } else {
                    var count = mVideoItems?.get(position)?.like_count
                    if (count != null) {
                        count -= 1
                        holder.itemView.tv_like_count.text = count.toString()
                        mVideoItems?.get(position)?.like_count = count
                    }
                    holder.itemView.btn_heart_like.setTag(UNLIKED)
                    holder.itemView.btn_heart_like.setBackground(context.getDrawable(R.drawable.ic_star_white))
                }


                if (mVideoItems[position].shared_by != null) {
                    onItemClickListener.onItemClick(position, it, Constants.SHARER_LIKES)
                } else {
                    onItemClickListener.onItemClick(position, it, Constants.LIKES)
                }
            }


            holder.itemView.tv_report_video.setOnClickListener {

                onItemClickListener.onItemClick(position,it, Constants.REPORT)
                loader = true
                holder.itemView.progress_promote_video.visibility = View.VISIBLE
            }

            holder.itemView.btn_report_video.setOnClickListener {

                onItemClickListener.onItemClick(position,it, Constants.REPORT)
                loader = true
                holder.itemView.progress_promote_video.visibility = View.VISIBLE
            }



        } catch (e: Exception) {
            Log.e("Exception", e.message.toString())
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return videolist.size
    }

    private fun animateMenuView(rightLay: ConstraintLayout) {
        rightLay.startAnimation(animSlide)

        animSlide?.setAnimationListener(object : Animator.AnimatorListener,
            Animation.AnimationListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                rightLay.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                rightLay.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                rightLay.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}

        })
    }

    fun updatePosition(position: Int) {
        pagePostion = position
    }

    fun invisibleLoader() {
        loader = false
        notifyDataSetChanged()
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }


}


