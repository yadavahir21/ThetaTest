package com.raghu.thetapracticle.fragment;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raghu.thetapracticle.AppMain;
import com.raghu.thetapracticle.R;
import com.raghu.thetapracticle.adapter.DataAdapter;
import com.raghu.thetapracticle.database.DataTable;
import com.raghu.thetapracticle.extra.NetworkStatus;
import com.raghu.thetapracticle.extra.PaginationScrollListener;
import com.raghu.thetapracticle.model.Data;
import com.raghu.thetapracticle.viewmodel.DataViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.raghu.thetapracticle.extra.PaginationScrollListener.PAGE_SIZE;
import static com.raghu.thetapracticle.extra.PaginationScrollListener.PAGE_START;
import static com.raghu.thetapracticle.repository.Repository.TOTAL_PAGES;

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private View view;
    private DataViewModel dataViewModel;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<Data> dataList = new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int apiCurrentPage = 1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        getViewRoot();
        initViews();
        return view;
    }

    private void initViews() {
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkStatus.getInstance().isConnected()) {
                    apiCurrentPage = 1;
                    AppMain.getRepository().loadApi(false, apiCurrentPage);
                } else {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        dataAdapter = new DataAdapter(getActivity(), dataList);
        recyclerView.setAdapter(dataAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems(int startPosition) {
                Log.d(TAG, "loadMoreItems: startPosition: " + startPosition);
                isLoading = true;
                currentPage++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (NetworkStatus.getInstance().isConnected()) {
                            apiCurrentPage = apiCurrentPage + 1;
                            AppMain.getRepository().loadApi(true, apiCurrentPage);
                        }
                    }
                }, 1500);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        if (NetworkStatus.getInstance().isConnected()) {
            AppMain.getRepository().loadApi(false, apiCurrentPage);
        }
        loadData();
    }
    private void loadData(){
        dataViewModel.getDataTableLiveData().observeForever(new Observer<DataTable>() {
            @Override
            public void onChanged(DataTable dataTable) {
                Log.d(TAG, "onChanged: ");
                if (getActivity() == null)
                    return;

                swipeRefresh.setRefreshing(false);
                isLoading = false;
                if (dataTable != null) {
                    List<Data> dataList = dataTable.dataList;
                    PAGE_SIZE = dataList.size();
                    if (currentPage != PAGE_START) {
                        dataAdapter.removeLoading();
                    }
                    dataAdapter.addItems(dataList);
                    Log.d(TAG, "onChanged: totalPage: "+ TOTAL_PAGES);
                    if (currentPage < TOTAL_PAGES) {
                        dataAdapter.addLoading();
                    } else {
                        isLastPage = true;
                    }
                }
                isLoading = false;
            }
        });
    }

    @Override
    public void getViewRoot() {
        setViewRoot(view);
    }
}
