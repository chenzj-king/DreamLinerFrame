package com.dreamliner.lib.frame.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dreamliner.lib.frame.R;
import com.dreamliner.rvhelper.OptimumRecyclerView;

/**
 * @author chenzj
 * @Title: BaseRvHelperCompatFragment
 * @Description: 类的描述 -
 * @date 2017/6/27 18:50
 * @email admin@chenzhongjin.cn
 */
public abstract class BaseRvHelperCompatFragment extends BaseCompatFragment {

    protected OptimumRecyclerView mOptimumRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    @Override
    protected void initSpecialView(View view) {
        mOptimumRecyclerView = (OptimumRecyclerView) view.findViewById(R.id.optimum_rv);
        mOptimumRecyclerView.setNumberBeforeMoreIsCalled(1);
        mLayoutManager = getLayoutManager();
        mOptimumRecyclerView.setLayoutManager(mLayoutManager);
    }
}
