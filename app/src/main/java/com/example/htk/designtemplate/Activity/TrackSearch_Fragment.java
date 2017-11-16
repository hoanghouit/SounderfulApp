package com.example.htk.designtemplate.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.TrackSearchAdapter;
import com.example.htk.designtemplate.Model.Post;
import com.example.htk.designtemplate.R;

import java.util.ArrayList;

public class TrackSearch_Fragment extends Fragment {
    private ListView listViewTrack;
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
        // add sample data to list view
        adđSampleItems();
        // Get view
        View view = getView();
        // set adaper for list view
        listViewTrack = (ListView) view.findViewById(R.id.listView_trackSearchActivity);
        trackSearchAdapter = new TrackSearchAdapter(getActivity(),R.layout.item_account_search_listview,postArrayList);
        listViewTrack.setAdapter(trackSearchAdapter);

    }
    public void adđSampleItems(){
        String url="http://genknews.genkcdn.vn/2017/smile-emojis-icon-facebook-funny-emotion-women-s-premium-long-sleeve-t-shirt-1500882676711.jpg";
        String url_image="https://images.vexels.com/media/users/6821/74972/raw/1054e351afe112bca797a70d67d93f9e-purple-daisies-blue-background.jpg";

        Post p = new Post();
        p.setTitle("Mashup Em gái mưa (Hương Tràm) - Từ hôm nay (Chi Pu)| Ghitar version");
        p.setUrlImage(url_image);
        p.setUserName("hoanghtk3108");
        postArrayList.add(p);

        Post pi = new Post();
        pi.setTitle("em gái mưa");
        p.setUrlImage(url);
        pi.setUserName("yudaidang");
        postArrayList.add(pi);

        Post pio = new Post();
        pio.setTitle("Ngày em đến");
        p.setUrlImage(url_image);
        pio.setUserName("hoanghtk3108");
        postArrayList.add(pio);

    }

}
