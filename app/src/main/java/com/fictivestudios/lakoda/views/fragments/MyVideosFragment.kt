package com.fictivestudios.lakoda.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2
import com.fictivestudios.docsvisor.apiManager.client.ApiClient
import com.fictivestudios.imdfitness.activities.fragments.BaseFragment
import com.fictivestudios.lakoda.Interface.OnItemClickListener
import com.fictivestudios.lakoda.R
import com.fictivestudios.lakoda.adapters.VideosAdapter
import com.fictivestudios.lakoda.apiManager.response.CommentResponse
import com.fictivestudios.lakoda.apiManager.response.CommonResponse
import com.fictivestudios.lakoda.apiManager.response.HomePostData
import com.fictivestudios.lakoda.apiManager.response.HomePostResponse
import com.fictivestudios.lakoda.utils.PreferenceUtils
import com.fictivestudios.lakoda.utils.Titlebar
import com.fictivestudios.lakoda.views.activities.MainActivity
import com.fictivestudios.lakoda.views.activities.RegisterationActivity
import com.fictivestudios.ravebae.utils.Constants
import kotlinx.android.synthetic.main.my_profile_fragment.view.*
import kotlinx.android.synthetic.main.video_view_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MyVideosFragment: BaseFragment()  , OnItemClickListener {
    private lateinit var mView: View

    companion object {
        fun newInstance() = MyVideosFragment()
    }
    private var videosAdapter: VideosAdapter? = null
    var videoList = ArrayList<HomePostData>()

    override fun setTitlebar(titlebar: Titlebar) {
        titlebar.setTitlewithBackButton("VIDEOS",
            View.OnClickListener {
                MainActivity.getMainActivity?.onBackPressed()
            }, R.drawable.icon_btn_back
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!this::mView.isInitialized) {
            mView = inflater.inflate(R.layout.my_videos_fragment, container, false)

        }
       var myvideolist = arguments?.getParcelableArrayList<HomePostData>("video")
        setData(myvideolist)

     /*   Constants.PROMOTE_RESPONSE.observe(viewLifecycleOwner){
            videosAdapter?.invisibleLoader()
            getAllAfterPromotePost()
        }

        Constants.REPORT_RESPONSE.observe(viewLifecycleOwner){
            videosAdapter?.invisibleLoader()
            getAllAfterPromotePost()
        }*/


        return mView
    }

    override fun onItemClick(position: Int, view: View, value: String) {
        if (value == Constants.COMMENTS)
        {
            val bundle = bundleOf(Constants.POST_ID to   videoList?.get(position)?.id.toString(),
                Constants.POST_TYPE to Constants.TYPE_POST)
            Log.d(Constants.POST_ID,  videoList?.get(position)?.id.toString())

            MainActivity.getMainActivity
                ?.navControllerMain?.navigate(R.id.commentsFragment,bundle)
        }
        else if (value == Constants.REPORT)
        {
            // Hit report API
            reportPost(videoList?.get(position)?.id)
            // change icon to show disabled // and disable click
            // view.isEnabled = false
            // view.setBackgroundResource(R.drawable.ic_unpromote)
        }

        else if(value == Constants.PROMOTE) {
            // Hit promote API
            promotePost(videoList?.get(position)!!.id)
            // change icon to show disabled // and disable click
            view.isEnabled = false
            view.setBackgroundResource(R.drawable.ic_unpromote)
        }
        else if(value == Constants.SHARESPROMOTE) {
            // Hit promote API
            promotePost(position)
            // change icon to show disabled // and disable click
            view.isEnabled = false
            view.setBackgroundResource(R.drawable.ic_unpromote)
        }


        else if(value == Constants.SHARER_COMMENTS)
        {

            val bundle = bundleOf(
                Constants.POST_ID to   videoList?.get(position)?.id.toString(),
                Constants.POST_TYPE to Constants.TYPE_SHARE
            )
            Log.d(Constants.POST_ID,  videoList?.get(position)?.id.toString())

            MainActivity.getMainActivity
                ?.navControllerMain?.navigate(R.id.commentsFragment,bundle)
        }
        else if(value == Constants.LIKES)
        {

            likeUnlike(Constants.TYPE_POST, videoList?.get(position)?.id)
        }

        else if(value == Constants.SHARER_LIKES)
        {

            likeUnlike(Constants.TYPE_SHARE, videoList?.get(position)?.id)
        }
        else if (value == Constants.PROFILE)
        {
            val bundle = bundleOf(
                Constants.USER_ID to   videoList?.get(position)?.user!!.id.toString(),
                Constants.USER_NAME to   videoList?.get(position)?.user!!.name.toString(),
                Constants.PROFILE to   videoList?.get(position)?.user?.image?.toString()
            )


            if (!Constants.getUser().id.equals(videoList?.get(position)?.user?.id))
            {
                MainActivity.getMainActivity
                    ?.navControllerMain?.navigate(R.id.friendProfileFragment,bundle)

            }
            else
            {

                MainActivity.getMainActivity
                    ?.navControllerMain?.navigate(R.id.myProfileFragment)

            }
        }

        else if (value == Constants.FOLLOW)
        {
            followRequest(videoList?.get(position)?.id)
        }

        else if (value == Constants.TYPE_SHARE)
        {
            sharePost(videoList?.get(position)?.id)
        }
    }
    private fun likeUnlike(postType:String,postId:Int)
    {

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.likePost(postId,postType).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        //  mView.pb_createPos.visibility=View.GONE
                        // Toast.makeText(requireContext(),"Liking post...", Toast.LENGTH_SHORT).show()
                    })

                    try {

                        Log.d("response", ""+response.body()?.message ?: "null")

                        val response: CommonResponse? =response.body()
                        val statuscode= response!!.status
                        if (statuscode==1) {

                            Log.d("response",response.message)


                            //    MainActivity.getMainActivity?.onBackPressed()
                            activity?.runOnUiThread(java.lang.Runnable {
                       //         Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                            })

                        }
                        else {
                            activity?.runOnUiThread(java.lang.Runnable {
                       //         Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                            })
                        }
                    }
                    catch (e:Exception)
                    {

                        activity?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            Log.d("response", e.localizedMessage)
                            Log.d("response", e.message.toString())
                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        // mView.pb_createPos.visibility=View.GONE

                        Toast.makeText(requireContext(), ""+t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", ""+t.localizedMessage)
                    })

                }
            })

        }


    }

    private fun followRequest(userID: Int)
    {

        // mView.btn_unfollow.isEnabled = false

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())
        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.followRequest(userID).enqueue(object: retrofit2.Callback<CommonResponse> {
                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                )
                {


                    Log.d("Response", ""+response?.body()?.message)

                    try {

                        if (response?.message() == "Unauthorized"||
                            response?.body()?.message == "Unauthorized"
                            || response?.message() == "Unauthenticated.")
                        {
                            PreferenceUtils.remove(Constants.USER_OBJECT)
                            PreferenceUtils.remove(Constants.ACCESS_TOKEN)
                            MainActivity.getMainActivity?.finish()
                            MainActivity.getMainActivity=null
                            startActivity(Intent(requireContext(), RegisterationActivity::class.java))
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(), "Login expired please login again", Toast.LENGTH_SHORT).show()
                            })
                        }

                        if (response.body()?.status==1)
                        {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(), "" + response?.body()?.message, Toast.LENGTH_SHORT).show()
                                // mView.btn_unfollow.setText("unfollow")
                                // mView.btn_unfollow.isEnabled = true

                            })



                        }
                        else
                        {
                            activity?.runOnUiThread(java.lang.Runnable {
                                Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()
                                // mView.btn_unfollow.setText("follow")
                                // mView.btn_unfollow.isEnabled = true
                            })
                        }


                    }
                    catch (e:Exception)
                    {
                        //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                        activity?.runOnUiThread(java.lang.Runnable {
                            //mView.pb_pofile.visibility=View.GONE
/*                            mView.shimmer_following.stopShimmer()
                            mView.shimmer_following.visibility = View.GONE*/
                            Toast.makeText(requireContext(), "msg: "+e.message, Toast.LENGTH_SHORT).show()
                            Log.d("execption","msg: "+e.localizedMessage)
                            // mView.btn_unfollow.setText("follow")
                            // mView.btn_unfollow.isEnabled = true
                        })
                    }
                }

                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        //mView.pb_pofile.visibility=View.GONE
/*                        mView.shimmer_following.stopShimmer()
                        mView.shimmer_following.visibility = View.GONE
                        mView.rv_followers.visibility =View.VISIBLE*/
                        Toast.makeText(requireContext(), ""+t.localizedMessage, Toast.LENGTH_SHORT).show()
                        // mView.btn_unfollow.setText("follow")
                        // mView.btn_unfollow.isEnabled = true
                    })
                }
            })

        }


    }

    private fun sharePost(postId:Int)
    {

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.sharePost(postId).enqueue(object: retrofit2.Callback<CommentResponse> {
                override fun onResponse(
                    call: Call<CommentResponse>,
                    response: Response<CommentResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        //  mView.pb_createPos.visibility=View.GONE
                        Toast.makeText(requireContext(),"Sharing post...", Toast.LENGTH_SHORT).show()


                        try {

                            Log.d("response", ""+response.body()?.message ?: "null")

                            val response: CommentResponse? =response.body()
                            val statuscode= response!!.status
                            if (statuscode==1) {

                                Log.d("response",response.message)


                                //    MainActivity.getMainActivity?.onBackPressed()

                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()

                            }
                            else {

                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()

                            }
                        }
                        catch (e:Exception)
                        {


                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            Log.d("response", e.localizedMessage)
                            Log.d("response", e.message.toString())

                        }
                    })  }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        // mView.pb_createPos.visibility=View.GONE

                        Toast.makeText(requireContext(), ""+t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", ""+t.localizedMessage)
                    })

                }
            })

        }


    }


    /** A Fuunction which is responsible to hit api for promote post
     * @param postId */
    private fun promotePost(postId:Int)
    {
        val hashMap = HashMap<String, Int>()
        hashMap.put("post_id",postId)

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.promotePost(hashMap).enqueue(object: retrofit2.Callback<CommentResponse> {
                override fun onResponse(
                    call: Call<CommentResponse>,
                    response: Response<CommentResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        Constants.PROMOTE_RESPONSE.value = true // var for loader purpose
                        //  mView.pb_createPos.visibility=View.GONE
                        Toast.makeText(requireContext(),"Promoting post...", Toast.LENGTH_SHORT).show()


                        try {

                            Log.d("response", ""+response.body()?.message ?: "null")

                            val response: CommentResponse? =response.body()
                            val statuscode= response!!.status
                            if (statuscode==1) {

                                Log.d("response",response.message)


                                //    MainActivity.getMainActivity?.onBackPressed()

                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()


                            }
                            else {
                                Constants.PROMOTE_RESPONSE.value = true // var for loader purpose
                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()

                            }
                        }
                        catch (e:Exception)
                        {

                            Constants.PROMOTE_RESPONSE.value = true // var for loader purpose
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            Log.d("response", e.localizedMessage)
                            Log.d("response", e.message.toString())

                        }
                    })   }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        // mView.pb_createPos.visibility=View.GONE
                        Constants.PROMOTE_RESPONSE.value = true // var for loader purpose

                        Toast.makeText(requireContext(), ""+t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", ""+t.localizedMessage)
                    })

                }
            })

        }


    }

    /** A Function which is responsible to hit api for reporting post
     * @param postId */
    protected fun reportPost(postId:Int)
    {
        val hashMap = HashMap<String, Int>()
        hashMap.put("post_id",postId)

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())

        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.reportPost(hashMap).enqueue(object: retrofit2.Callback<CommentResponse> {
                override fun onResponse(
                    call: Call<CommentResponse>,
                    response: Response<CommentResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        Constants.REPORT_RESPONSE.value = true // var for loader purpose
                        //  mView.pb_createPos.visibility=View.GONE
                        Toast.makeText(requireContext(),"Reporting post...", Toast.LENGTH_SHORT).show()


                        try {

                            Log.d("response", ""+response.body()?.message ?: "null")

                            val response: CommentResponse? =response.body()
                            val statuscode= response!!.status
                            if (statuscode==1) {

                                Log.d("response",response.message)


                                //    MainActivity.getMainActivity?.onBackPressed()

                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()


                            }
                            else {
                                Constants.REPORT_RESPONSE.value = true // var for loader purpose
                                Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()

                            }
                        }
                        catch (e:Exception)
                        {

                            Constants.REPORT_RESPONSE.value = true // var for loader purpose
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            Log.d("response", e.localizedMessage)
                            Log.d("response", e.message.toString())

                        }
                    })   }

                override fun onFailure(call: Call<CommentResponse>, t: Throwable)
                {

                    activity?.runOnUiThread(java.lang.Runnable {
                        // mView.pb_createPos.visibility=View.GONE
                        Constants.REPORT_RESPONSE.value = true // var for loader purpose

                        Toast.makeText(requireContext(), ""+t.localizedMessage, Toast.LENGTH_SHORT).show()
                        Log.d("response", ""+t.localizedMessage)
                    })

                }
            })

        }


    }

    private fun setData(response: ArrayList<HomePostData>?) {


        if (response != null) {
            videoList = response

            var myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                    Log.d("pagePosition","Selected position: ${position}")
                    videosAdapter?.updatePosition(position)


                }
            }
            videosAdapter = VideosAdapter(videoList,requireContext(),this@MyVideosFragment)

            mView.viewPagerVideos.setAdapter( videosAdapter);

            mView.viewPagerVideos.registerOnPageChangeCallback(myPageChangeCallback)
        }




    }
    private fun getAllAfterPromotePost()
    {

        // mView.pb_home.visibility=View.VISIBLE
        /* mView.shimmer_view_container.startShimmer()
         mView.shimmer_view_container.visibility = View.VISIBLE
 */

        val apiClient = ApiClient.RetrofitInstance.getApiService(requireContext())


        GlobalScope.launch(Dispatchers.IO)
        {

            apiClient.getAllPost("home").enqueue(object: retrofit2.Callback<HomePostResponse> {
                override fun onResponse(
                    call: Call<HomePostResponse>,
                    response: Response<HomePostResponse>
                )
                {
                    activity?.runOnUiThread(java.lang.Runnable {
                        mView.pb_home.visibility=View.GONE
                        /* mView.shimmer_view_container.stopShimmer()
                         mView.shimmer_view_container.visibility = View.GONE*/



                        Log.d("Response",""+ response.message())
                        Log.d("ResponseMessage",""+ response?.body()?.message.toString())
                        Log.d("ResponseData",""+ response?.body()?.data.toString())

                        try {






                            if (response?.message() == "Unauthorized" || response?.message() == "Unauthenticated."||
                                response?.body()?.message == "Unauthorized" || response?.body()?.message == "Unauthenticated."
                            )
                            {
                                PreferenceUtils.remove(Constants.USER_OBJECT)
                                PreferenceUtils.remove(Constants.ACCESS_TOKEN)
                                MainActivity.getMainActivity?.finish()
                                MainActivity.getMainActivity=null
                                startActivity(Intent(requireContext(), RegisterationActivity::class.java))
                                activity?.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(requireContext(), "Login expired please login again", Toast.LENGTH_SHORT).show()
                                })
                            }

                            if (response.body()?.status==1)
                            {

                                if (response.body()?.data != null)
                                {

                                    var response = response.body()?.data
                                    Log.d("Response", response.toString())
                                    setData(response as ArrayList<HomePostData>)
                                }


                            }
                            else
                            {
                                Toast.makeText(requireContext(), ""+response.body()?.message, Toast.LENGTH_SHORT).show()

                            }


                        }
                        catch (e:Exception)
                        {
                            //Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()

                            //mView.pb_pofile.visibility=View.GONE
                            /*   mView.shimmer_view_container.stopShimmer()
                               mView.shimmer_view_container.visibility = View.GONE*/
                            Toast.makeText(requireContext(),""+ e.message, Toast.LENGTH_SHORT).show()
                            Log.d("execption",""+e.localizedMessage)

                        }
                    })   }

                override fun onFailure(call: Call<HomePostResponse>, t: Throwable)
                {
                    Toast.makeText(requireContext(), ""+t.localizedMessage, Toast.LENGTH_SHORT).show()
                    activity?.runOnUiThread(java.lang.Runnable {
                        mView.pb_home.visibility=View.GONE
                        /*   mView.shimmer_view_container.stopShimmer()
                           mView.shimmer_view_container.visibility = View.GONE
                           mView.feed_post_layout.visibility =View.VISIBLE*/
                        Toast.makeText(requireContext(),""+ t.message, Toast.LENGTH_SHORT).show()
                    })
                }
            })

        }

    }
}