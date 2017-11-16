package com.example.htk.designtemplate.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.htk.designtemplate.Adapter.AccountSearchAdapter;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.R;

import java.util.ArrayList;


public class AccountSearch_Fragment extends Fragment {
    private ListView listViewAccount;
    private AccountSearchAdapter accountSearchAdapter;
    private ArrayList<Account> accountArrayList= new ArrayList<Account>();
    public AccountSearch_Fragment() {
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
        return inflater.inflate(R.layout.fragment_account_search_, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        // add sample data to list view
        adđSampleItems();
        // Get view
        View view = getView();
        // set adaper for list view
        listViewAccount = (ListView) view.findViewById(R.id.listView_accountSearchActivity);
        accountSearchAdapter = new AccountSearchAdapter(getActivity(),R.layout.item_account_search_listview,accountArrayList);
        listViewAccount.setAdapter(accountSearchAdapter);

    }
    public void adđSampleItems(){
        String url="http://genknews.genkcdn.vn/2017/smile-emojis-icon-facebook-funny-emotion-women-s-premium-long-sleeve-t-shirt-1500882676711.jpg";
        Account a = new Account();
        a.setUserName("hoanghohtk");
        a.setBiography("Hello world. I'm a Vietnamese girl. I love Korean drama. I'm studying IT. Nice to meet you all");
        a.setUrlAvatar(url);
        accountArrayList.add(a);

        Account ab = new Account();
        ab.setUserName("yudaidang");
        ab.setBiography("I'm a stupid boy. I'm stupid boy. I'm stupid boy.I'm stupid boy I'm stupid boy. I'm stupid boy");
        ab.setUrlAvatar(url);
        accountArrayList.add(ab);

        Account abc = new Account();
        abc.setUserName("nguyentuanit");
        abc.setBiography("i'm a cute lovely gay.");
        abc.setUrlAvatar(url);
        accountArrayList.add(abc);
        accountArrayList.add(a);
        accountArrayList.add(ab);
        accountArrayList.add(abc);
        accountArrayList.add(abc);
        accountArrayList.add(a);
        accountArrayList.add(ab);
        accountArrayList.add(abc);

    }

}
