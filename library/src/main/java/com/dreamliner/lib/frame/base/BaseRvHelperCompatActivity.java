package com.dreamliner.lib.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.dreamliner.lib.frame.R;
import com.dreamliner.rvhelper.OptimumRecyclerView;

/**
 * @author chenzj
 * @Title: BaseRvHelperCompatActivity
 * @Description: 类的描述 -
 * @date 2017/6/27 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class BaseRvHelperCompatActivity extends BaseCompatActivity {

    protected OptimumRecyclerView mOptimumRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    @Override
    protected void initSpecialView(@Nullable Bundle savedInstanceState) {
        mOptimumRecyclerView = (OptimumRecyclerView) findViewById(R.id.optimum_rv);
        mOptimumRecyclerView.setNumberBeforeMoreIsCalled(1);
        mLayoutManager = getLayoutManager();
        mOptimumRecyclerView.setLayoutManager(mLayoutManager);
    }
}
