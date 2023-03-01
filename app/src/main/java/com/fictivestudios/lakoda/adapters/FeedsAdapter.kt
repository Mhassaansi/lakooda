package com.fictivestudios.lakoda.adapters

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fictivestudios.lakoda.Interface.OnItemClickListener
import com.fictivestudios.lakoda.R
import com.fictivestudios.lakoda.apiManager.response.HomePostData
import com.fictivestudios.lakoda.utils.PreferenceUtils
import com.fictivestudios.ravebae.utils.Constants
import com.fictivestudios.ravebae.utils.Constants.Companion.LIKED
import com.fictivestudios.ravebae.utils.Constants.Companion.UNLIKED
import com.fictivestudios.ravebae.utils.Constants.Companion.VIEW_TYPE_POST
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home_post.view.*
import kotlinx.android.synthetic.main.item_home_post.view.iv_heart
import kotlinx.android.synthetic.main.item_home_post.view.iv_post
import kotlinx.android.synthetic.main.item_home_post.view.iv_profile
import kotlinx.android.synthetic.main.item_home_post.view.iv_share
import kotlinx.android.synthetic.main.item_home_post.view.tv_like
import kotlinx.android.synthetic.main.item_home_post.view.tv_post_description
import kotlinx.android.synthetic.main.item_home_post.view.tv_username
import kotlinx.android.synthetic.main.item_shared_home_post.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FeedsAdapter(context: Context, post: List<HomePostData>?, onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<FeedsAdapter.ProfileViewHolder>(), Filterable {

    var filteredList = ArrayList<HomePostData>()


    init {
        filteredList = post as ArrayList<HomePostData>
        notifyDataSetChanged()
    }

    private var postList: List<HomePostData>? = post
    private var context = context
    private var onItemClickListener = onItemClickListener

    private var loader = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ProfileViewHolder{


        if (viewType == VIEW_TYPE_POST)
        {
            return ProfileViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_post, parent, false))
        }
        else
        {
            return ProfileViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shared_home_post, parent, false))
        }

    }




    override fun getItemCount() = postList?.size ?: 0

    override fun getItemViewType(position: Int): Int {


       return postList?.get(position)?.is_post!!

   /*    if (postList?.get(position)?.type  == TYPE_POST)
           return VIEW_TYPE_POST
           else
           {
               return VIEW_TYPE_SHARED_POST
           }*/

    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        Log.e("ffdddf",""+postList?.get(position)?.is_post!!)

       if(postList?.get(position)?.is_post==1){
           holder.itemView.iv_post.setClipToOutline(true)
           holder.itemView.card_post_feeds.setPreventCornerOverlap(false)
           holder.itemView.tv_active_feeds.text = "${getAgoTime(postList?.get(position)?.created_at!!)}"

           if(!loader) {
               holder.itemView.progress_promote.visibility = View.GONE
           }

           val postUserId = postList?.get(position)?.user?.id

           if(postUserId == PreferenceUtils.getInt(Constants.USER_ID)){
               holder.itemView.iv_Report.visibility = View.GONE
               holder.itemView.tv_report.visibility = View.GONE
           }
           else{
               holder.itemView.iv_Report.visibility = View.VISIBLE
               holder.itemView.tv_report.visibility = View.VISIBLE
           }

           if(postUserId == PreferenceUtils.getInt(Constants.USER_ID) && postList?.get(position)?.is_promoted == 0){
               holder.itemView.btn_promote.visibility = View.VISIBLE
               holder.itemView.btn_promote.setBackgroundResource(R.drawable.ic_promote)
               holder.itemView.btn_promote.isEnabled = true
           }
           else if(postUserId == PreferenceUtils.getInt(Constants.USER_ID) && postList?.get(position)?.is_promoted == 1){
               holder.itemView.btn_promote.visibility = View.VISIBLE
               holder.itemView.btn_promote.setBackgroundResource(R.drawable.ic_unpromote)
               holder.itemView.btn_promote.isEnabled = false
           }
           else{
//            holder.itemView.btn_promote.visibility = View.GONE
               holder.itemView.btn_promote.visibility = View.INVISIBLE
           }

           if(postList?.get(position)?.video_image == null || postList?.get(position)?.video_image!!.isEmpty()){
               // holder.itemView.card_post_feeds.visibility = View.GONE
               holder.itemView.iv_post.visibility = View.GONE
               // holder.itemView.spacer_view.visibility = View.INVISIBLE
           }
           else {
               holder.itemView.iv_post.visibility = View.VISIBLE
           }


           holder.itemView.btn_promote.setOnClickListener {
               onItemClickListener.onItemClick(position,it, Constants.PROMOTE)
               holder.itemView.progress_promote.visibility = View.VISIBLE
               loader = true
           }

           holder.itemView.iv_profile.setOnClickListener {
               onItemClickListener.onItemClick(position,it, Constants.PROFILE)
           }

           holder.itemView.tv_like.setOnClickListener {

               likeWork(holder,position,it,Constants.LIKES)


           }

           holder.itemView.iv_heart.setOnClickListener {

               likeWork(holder,position,it,Constants.LIKES)


           }


           holder.itemView.iv_share.setOnClickListener {

               onItemClickListener.onItemClick(position,it, Constants.TYPE_SHARE)

           }




               holder.itemView.tv_comment.setOnClickListener {

                   onItemClickListener.onItemClick(position,it, Constants.COMMENTS)

               }

               holder.itemView.iv_Comment.setOnClickListener {
                   onItemClickListener.onItemClick(position,it, Constants.COMMENTS)
               }

               holder.itemView.tv_report.setOnClickListener {

                   onItemClickListener.onItemClick(position,it, Constants.REPORT)
                   loader = true
                   holder.itemView.progress_promote.visibility = View.VISIBLE
               }

               holder.itemView.iv_Report.setOnClickListener {

                   onItemClickListener.onItemClick(position,it, Constants.REPORT)
                   loader = true
                   holder.itemView.progress_promote.visibility = View.VISIBLE
               }

               holder.bind(position, postList?.get(position),holder.itemView,context)










       }
        else{

         //  holder.itemView.tv_active_feeds.text = "${getAgoTime(postList?.get(position)?.created_at!!)}"
           holder.itemView.tv_sharer_active.text = "${getAgoTime(postList?.get(position)?.created_at!!)}"

           holder.itemView.iv_sharer_profile.setOnClickListener {

               onItemClickListener.onItemClick(position,it, Constants.SHARER_PROFILE)
           }

           if(postList?.get(position)?.video_image == null || postList?.get(position)?.video_image!!.isEmpty()){
               // holder.itemView.card_post_feeds.visibility = View.GONE
               holder.itemView.iv_post.visibility = View.GONE
               // holder.itemView.spacer_view.visibility = View.INVISIBLE
           }
           else {
               holder.itemView.iv_post.visibility = View.VISIBLE
           }



           holder.bindShared(position, postList?.get(position),holder.itemView,context)

           holder.itemView.iv_profile.setOnClickListener {
               onItemClickListener.onItemClick(position,it, Constants.PROFILE)
           }

           holder.itemView.tv_like.setOnClickListener {

               likeWork(holder,position,it,Constants.SHARER_LIKES)

           }

           holder.itemView.iv_heart.setOnClickListener {

               likeWork(holder,position,it,Constants.SHARER_LIKES)

           }

           holder.itemView.iv_comment_sharer.setOnClickListener {
               onItemClickListener.onItemClick(position,it, Constants.SHARER_COMMENTS)
           }

           holder.itemView.tv_comment_sharer.setOnClickListener {
               onItemClickListener.onItemClick(position,it, Constants.SHARER_COMMENTS)
           }
           holder.itemView.btn_share_promote.setOnClickListener{
               Log.e("fdffdd",""+postList?.get(position)?.id!!)
               onItemClickListener.onItemClick(postList?.get(position)?.id!!,it, Constants.SHARESPROMOTE)
            //   holder.itemView.progress_promote.visibility = View.VISIBLE
            //   loader = true
           }

           if (postList?.get(position)?.is_liked == 1)
           {
               holder.itemView.iv_heart.setBackgroundResource(R.drawable.ic_star)
               holder.itemView.iv_heart.setTag(LIKED)
               //    itemView.iv_heart.tint
           }
           else
           {
               holder.itemView.iv_heart.setBackgroundResource(R.drawable.ic_star_white)
               holder.itemView.iv_heart.setTag(UNLIKED)
           }


       }




    }

    fun invisibleLoader(){
        loader = false
        notifyDataSetChanged()
    }

    fun getAgoTime(postTime : String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))

        var ago: String? = null

        try {
            val time: Long = sdf.parse(postTime).getTime()
            val now = System.currentTimeMillis()
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ago
    }

     class ProfileViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        fun bind(position: Int, post: HomePostData?, itemView: View, context: Context) {

            itemView.tv_username.setText(post?.user?.name)
            itemView.tv_post_description .setText(post?.description)
            itemView.tv_like.setText(post?.like_count.toString())
            if(post?.comment_count==0){
                itemView.tv_comment.setText("Comment")

            }
            else{
                itemView.tv_comment.setText(post?.comment_count.toString()+" Comment")

            }

            if (Constants.getUser().id.equals(post?.user?.id))
            {
                itemView.iv_share.visibility = View.INVISIBLE
            }
            else
            {
                itemView.iv_share.visibility = View.VISIBLE
            }


            if (post?.is_liked == 1)
            {
                itemView.iv_heart.setBackgroundResource(R.drawable.ic_star)
                itemView.iv_heart.setTag(LIKED)
            //    itemView.iv_heart.tint
            }
            else
            {
                itemView.iv_heart.setBackgroundResource(R.drawable.ic_star_white)
                itemView.iv_heart.setTag(UNLIKED)
            }

            if (post?.type == "post")
            {
                if (!post?.video_image.isNullOrBlank())
                {
/*                   Picasso
                        .get()
                        .load(Constants.IMAGE_BASE_URL +post?.video_image)
                        //.placeholder(R.drawable.loading_spinner)
                        .into(itemView.iv_post);*/
                    Picasso
                        .get()
                        .load(Constants.IMAGE_BASE_URL +post?.video_image)
                        .into(itemView.iv_post);
                }
            }

            if (!post?.user?.image.isNullOrBlank())
            {
               Picasso
                        .get()
                    .load(Constants.IMAGE_BASE_URL +post?.user?.image)
                    //.placeholder(R.drawable.loading_spinner)
                    .into(itemView.iv_profile);

            }

        }

        fun bindShared(position: Int, post: HomePostData?, itemView: View, context: Context)
        {
            itemView.tv_username.setText(post?.shared_by!!.name)
            itemView.tv_post_description .setText(post?.description)
            itemView.tv_like.setText(post?.like_count.toString())
            if(post?.comment_count==0){
                itemView.tv_comment_sharer.setText("Comment")

            }
            else{
                itemView.tv_comment_sharer.setText(post?.comment_count.toString()+" Comment")

            }
            itemView.tv_sharer_username.setText(post?.shared_by?.name)

            if (Constants.getUser().id.equals(post?.shared_by?.id))
            {
                itemView.iv_share.visibility = View.INVISIBLE

            }
            else
            {
                itemView.iv_share.visibility = View.VISIBLE

            }



            if(post.is_promoted == 0 && itemView.iv_share.visibility==View.INVISIBLE){
                itemView.btn_share_promote.visibility = View.VISIBLE
                itemView.btn_share_promote.setBackgroundResource(R.drawable.ic_promote)
                itemView.btn_share_promote.isEnabled = true
            }
            else if(post?.is_promoted == 1  && itemView.iv_share.visibility==View.INVISIBLE){
                itemView.btn_share_promote.visibility = View.VISIBLE
                itemView.btn_share_promote.setBackgroundResource(R.drawable.ic_unpromote)
                itemView.btn_share_promote.isEnabled = false
            }
            else{
                itemView.btn_share_promote.visibility = View.INVISIBLE
            }




            if (!post?.shared_by?.image.isNullOrBlank())
            {
                Picasso
                    .get()
                    .load(Constants.IMAGE_BASE_URL +post?.shared_by?.image)
                    //.placeholder(R.drawable.loading_spinner)
                    .into(itemView.iv_sharer_profile);

            }

            if (post?.type == "post")
            {
                if (!post?.video_image.isNullOrBlank())
                {
/*                   Picasso
                        .get()
                        .load(Constants.IMAGE_BASE_URL +post?.video_image)
                        //.placeholder(R.drawable.loading_spinner)
                        .into(itemView.iv_post);*/
                    Picasso
                        .get()
                        .load(Constants.IMAGE_BASE_URL +post?.video_image)
                        .into(itemView.iv_post);
                }
            }

            if (!post?.user?.image.isNullOrBlank())
            {
                Picasso
                    .get()
                    .load(Constants.IMAGE_BASE_URL +post?.shared_by?.image)
                    //.placeholder(R.drawable.loading_spinner)
                    .into(itemView.iv_profile);

            }




        }


    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val charSearch = constraint.toString()
                if (charSearch== null || charSearch.isEmpty()) {
                    // filteredList = mList
                    filterResults.count = filteredList.size
                    filterResults.values = filteredList
                } else {
                    val resultList = ArrayList<HomePostData>()
                    for (row in filteredList) {
                        if (row.description!!.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
                            || row.user!!.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                postList = results?.values as ArrayList<HomePostData>
                notifyDataSetChanged()
            }
        }
    }
    fun likeWork(holder: ProfileViewHolder, position: Int, itemView: View,like:String) {
        if (holder.itemView.iv_heart.tag == UNLIKED)
        //(postList?.get(position)?.is_liked == 0)
        {
//                    holder.itemView.iv_heart.setTag(LIKED)
            holder.itemView.iv_heart.setTag(UNLIKED)


            var count = 0
            count = postList?.get(position)?.like_count!!
            if (count != null) {
                count += 1
                holder.itemView.tv_like.text = count.toString()
                holder.itemView.iv_heart.setTag(LIKED)
                postList?.get(position)?.like_count = count

            }
            holder.itemView.iv_heart.setBackground(context.getDrawable(R.drawable.ic_star))

        }
        else
        {
            holder.itemView.iv_heart.setTag(LIKED)
            var count = 0
            count = postList?.get(position)?.like_count!!
            if (count != null) {
                count -= 1
                holder.itemView.tv_like.text = count.toString()
                holder.itemView.iv_heart.setTag(UNLIKED)
                postList?.get(position)?.like_count = count
            }
            holder.itemView.iv_heart.setBackground(context.getDrawable(R.drawable.ic_star_white))
        }
        onItemClickListener.onItemClick(position,itemView, like)

    }
}