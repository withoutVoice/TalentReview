package com.will.talentreview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.will.talentreview.R;
import com.will.talentreview.fragment.TabHomeFragment;
import com.will.talentreview.fragment.TabMineFragment;
import com.will.talentreview.fragment.TabProductFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llTabHome;
    private LinearLayout llTabProduct;
    private LinearLayout llTabMine;

    private TabHomeFragment tabHomeFragment;
    private TabProductFragment tabProductFragment;
    private TabMineFragment tabMineFragment;
    private Fragment currentFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        llTabHome = findViewById(R.id.ll_tab_home);
        llTabProduct = findViewById(R.id.ll_tab_product);
        llTabMine = findViewById(R.id.ll_tab_mine);
        llTabHome.setOnClickListener(this);
        llTabProduct.setOnClickListener(this);
        llTabMine.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        tabHomeFragment = TabHomeFragment.getInstance();
        tabProductFragment = TabProductFragment.getInstance();
        tabMineFragment = TabMineFragment.getInstance();

        llTabHome.performClick();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_tab_home) {
            if (tabHomeFragment == currentFragment) {
                return;
            }
            llTabHome.setSelected(true);
            llTabProduct.setSelected(false);
            llTabMine.setSelected(false);
            switchFragment(tabHomeFragment);
        } else if (id == R.id.ll_tab_product) {
            if (tabProductFragment == currentFragment) {
                return;
            }
            llTabHome.setSelected(false);
            llTabProduct.setSelected(true);
            llTabMine.setSelected(false);
            switchFragment(tabProductFragment);
        } else if (id == R.id.ll_tab_mine) {
            if (tabMineFragment == currentFragment) {
                return;
            }
            llTabHome.setSelected(false);
            llTabProduct.setSelected(false);
            llTabMine.setSelected(true);
            switchFragment(tabMineFragment);
        }
    }

    private void switchFragment(Fragment targetFragment) {
        if (targetFragment == currentFragment) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }

    public void switchToProduct() {
        llTabProduct.performClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (currentFragment != null) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (currentFragment == tabHomeFragment) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    //弹出提示，可以有多种方式
                    showShortToast("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            } else {
                llTabHome.performClick();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
