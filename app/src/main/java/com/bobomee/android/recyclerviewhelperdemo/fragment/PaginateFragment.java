package com.bobomee.android.recyclerviewhelperdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bobomee.android.recyclerviewhelper.paginate.LoadingListItemCreator;
import com.bobomee.android.recyclerviewhelper.paginate.WrapperAdapter;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.recycler.BaseRecyclerAdapter;
import com.bobomee.android.recyclerviewhelperdemo.recycler.DataProvider;
import com.bobomee.android.recyclerviewhelperdemo.recycler.RecyclerViewHolder;

/**
 * Project ID：400YF17051<br/>
 * Resume:
 *
 * @author 汪波
 * @version 1.0
 * @see
 * @since 2017/4/15 汪波 first commit
 */
public class PaginateFragment extends BasePaginationFragment {

  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
  private Unbinder unbinder;
  private Activity mActivity;
  private PaginateAdapter mPaginateAdapter;

  private static final int DEFAULT_PAGE_SIZE = 20;
  private static final int DEFAULT_TOTAL_COUNT = 45;

  public static PaginateFragment newInstance() {
    Bundle args = new Bundle();
    PaginateFragment fragment = new PaginateFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();
    mPaginateAdapter = new PaginateAdapter(mActivity);
  }

  @Override public RecyclerView providerRecyclerview() {
    return mRecyclerView;
  }

  @Override public void requestMoreData() {
    requestData(true);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.paginate_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    WrapperAdapter wrapperAdapter = new WrapperAdapter(mPaginateAdapter,
        new LoadingListItemCreator.DefalutLoadingListItemCreator(this),
        new LoadingListItemCreator.DefaultLoadingNoDataItemCreator(this));
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
    linearLayoutManager.setAutoMeasureEnabled(true);

    mRecyclerView.setLayoutManager(linearLayoutManager);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setAdapter(wrapperAdapter);

    setupPagination();

    final SwipeRefreshLayout.OnRefreshListener listener =
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override public void onRefresh() {
            resetPagination();
            requestData(false);
          }
        };

    if (null != mSwipeRefreshLayout) {
      mSwipeRefreshLayout.setOnRefreshListener(listener);

      mRecyclerView.post(new Runnable() {
        @Override public void run() {
          mSwipeRefreshLayout.setRefreshing(true);
          listener.onRefresh();
        }
      });
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void requestData(final boolean isLoadMore) {

    mRecyclerView.postDelayed(new Runnable() {
      @Override public void run() {
        DataProvider.Data data =
            DataProvider.providePaginateData(mCurrentPage, DEFAULT_PAGE_SIZE, DEFAULT_TOTAL_COUNT);

        evaluateValues(data);

        if (isLoadMore) {
          mPaginateAdapter.addAll(data.mContent);
        } else {
          mPaginateAdapter.setData(data.mContent);
        }

        loadComplete();
      }
    }, 1500);
  }

  @Override protected void loadComplete() {
    super.loadComplete();
    if (null != mSwipeRefreshLayout) mSwipeRefreshLayout.setRefreshing(false);
  }

  private void evaluateValues(DataProvider.Data data) {
    mTotalItemCount = data.mTotalItemCount;
    mTotalPage = data.mTotalPage;
    mCurrentPage = data.mCurrentPage;
  }

  private class PaginateAdapter extends BaseRecyclerAdapter<DataProvider.Data.Content> {

    PaginateAdapter(Context context) {
      super(context);
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, DataProvider.Data.Content item) {
      holder.setText(R.id.tvItemName, item.mContent);
    }

    @Override public int getItemLayoutId(int viewType) {
      return R.layout.item_adapter;
    }
  }
}
