package com.example.htk.designtemplate.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.htk.designtemplate.Adapter.AccountSearchAdapter;
import com.example.htk.designtemplate.Model.Account;
import com.example.htk.designtemplate.R;
import com.example.htk.designtemplate.Service.AccountService;
import com.example.htk.designtemplate.Service.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountSearch_Fragment extends Fragment {
    private ListView listViewAccount;
    private TextView recentlySearch;
    private AccountSearchAdapter accountSearchAdapter;
    private AccountService mService;
    private EditText searchEditText;
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
        // set recentlySearch text view
        recentlySearch = (TextView) getView().findViewById(R.id.recentlySearchTextView_AccountSearchFragment);
        // set adaper for list view
        listViewAccount = (ListView) getView().findViewById(R.id.listView_accountSearchActivity);
        accountSearchAdapter = new AccountSearchAdapter(getActivity(),R.layout.item_account_search_listview,accountArrayList);
        listViewAccount.setAdapter(accountSearchAdapter);
        // set retrofit accont service
        mService = ApiUtils.getAccountService();
        recentlySearch();
    }

    public void searchAccounts(String key){
        mService.searchAccount(key).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {

                if(response.isSuccessful()) {
                    //itemArrayList = new ArrayList<Item>(response.body().getItems());
                    accountArrayList.clear();
                    for (Account account:response.body()){
                        accountArrayList.add(account);
                    }
                    accountSearchAdapter.notifyDataSetChanged();
                    recentlySearch.setVisibility(View.GONE);
                    Log.d("AccountSearchFragment", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                    Log.d("AccountSearchFragment", "fail loaded from API");
                    Log.d("AccountSearchFragment", ((Integer)statusCode).toString());
                    // handle request errors depending on status code
                }
            }
            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Log.d("AccountSearchFragment", t.getMessage());
            }
        });
    }
    public void recentlySearch(){
        // set recently search
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("searchActivity", Context.MODE_PRIVATE);
        String recentlyKeySearch = sharedPreferences.getString("keySearch","");
        searchAccounts(recentlyKeySearch);
        recentlySearch.setVisibility(View.VISIBLE);
    }
}
