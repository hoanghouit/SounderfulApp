package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.TrackSearchAdapter;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.ApiUtils;
import com.example.htk.designtemplate.Service.PostService;
import com.example.htk.designtemplate.Utils.MultipleToast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackSearch_Fragment extends Fragment {
    private final static String tag = "TrackSearchFragment";
    private PostService mService;
    private ListView listViewTrack;
    private String currentUser = MainActivity.userName;
    private TrackSearchAdapter trackSearchAdapter;
    private ArrayList<Post> postArrayList= new ArrayList<Post>();
    public TrackSearch_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_search_, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        // Get view
        View view = getView();
        // set adaper for list view
        listViewTrack = (ListView) view.findViewById(R.id.listView_trackSearchActivity);
        trackSearchAdapter = new TrackSearchAdapter(getActivity(),R.layout.item_account_search_listview,postArrayList);
        listViewTrack.setAdapter(trackSearchAdapter);
        // set retrofit accont service
        mService = ApiUtils.getPostService();
        recentlySearch();
        // set context for toasts
        MultipleToast.context = getActivity();

    }
    public void searchPosts(String key){
        mService.searchPost(key).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(response.isSuccessful()) {
                  getLikedPosts(response.body());

                    Log.d(tag, "search posts from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail search posts from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(tag, "fail"); MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }
    public void getLikedPosts(final List<Post> postList){
        mService.getLikedPosts(currentUser).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    for(Post p: response.body()){
                        arr.add( p.getPostId());
                    }
                    trackSearchAdapter.setLikedPostIds(arr);
                    Log.d(tag, "get liked posts from API");
                }else {
                    int statusCode  = response.code();
                    Log.d(tag, "fail get liked from API");
                    Log.d(tag, ((Integer)statusCode).toString());
                    MultipleToast.showToast(MainActivity.fail_request);
                    // handle request errors depending on status code
                }
                postArrayList.clear();
                for (Post post:postList){
                    postArrayList.add(post);
                }
                trackSearchAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d(tag,"fail");
                MultipleToast.showToast(MainActivity.fail_request);
            }
        });
    }
    public void recentlySearch(){
        // set recently search
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("searchActivity", Context.MODE_PRIVATE);
        String recentlyKeySearch = sharedPreferences.getString("keySearch","");
        searchPosts(recentlyKeySearch);
    }

}
