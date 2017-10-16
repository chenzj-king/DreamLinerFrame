package com.dreamliner.lib.frame.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.dreamliner.easypermissions.AfterPermissionGranted;
import com.dreamliner.easypermissions.EasyPermissions;
import com.dreamliner.lib.customdialog.CustomButtonCallback;
import com.dreamliner.lib.customdialog.CustomDialog;
import com.dreamliner.lib.frame.R;
import com.dreamliner.lib.frame.entity.DefaultEventData;
import com.dreamliner.lib.frame.util.ConfigurationUtil;
import com.dreamliner.lib.frame.util.LogUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.dreamliner.lib.customdialog.CustomDialog.ALL_BUTTON;
import static com.dreamliner.lib.customdialog.CustomDialog.ONLY_CONFIRM_BUTTON;
import static com.dreamliner.lib.frame.util.ConfigurationUtil.CANCEL_COLOR_RES;
import static com.dreamliner.lib.frame.util.ConfigurationUtil.CANCEL_CONTENT;
import static com.dreamliner.lib.frame.util.ConfigurationUtil.OK_CONTENT;
import static com.dreamliner.lib.frame.util.ConfigurationUtil.THEME_COLOR_RES;
import static com.dreamliner.lib.frame.util.ConfigurationUtil.WARNING_COLOR_RES;

/**
 * @author chenzj
 * @Title: BaseCompatActivity
 * @Description: 类的描述 -
 * @date 2017/6/27 18:40
 * @email admin@chenzhongjin.cn
 */
public abstract class BaseCompatActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        LifecycleProvider<ActivityEvent>, ISupportActivity {

    public UUID mUUID = UUID.randomUUID();

    protected int getLayoutId() {
        return 0;
    }

    protected void initSpecialView(@Nullable Bundle savedInstanceState) {
    }

    protected abstract void initViews(@Nullable Bundle savedInstanceState);

    protected void handleMes(Message msg) {
    }

    protected void getBundleExtras(Bundle extras) {
    }

    protected boolean isRegisterEvent() {
        return true;
    }

    protected boolean isRequestPermissionOnResume() {
        return true;
    }

    public MyHandler mHandler;

    protected MaterialDialog mMaterialDialog;
    protected CustomDialog mCustomDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        overridePendingTransitionIn();
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
        //兼容DataBinding的方式的时候就不需要设置了
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        // base setup
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        //Act 堆栈管理
        AppManager.INSTACE.addActivity(this);

        if (isRegisterEvent())
            EventBus.getDefault().register(this);

        mHandler = new MyHandler(this);

        initSpecialView(savedInstanceState);
        initViews(savedInstanceState);
    }

    protected void overridePendingTransitionIn() {
    }

    protected void overridePendingTransitionOut() {
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        if (isRequestPermissionOnResume()) {
            checkPermission();
        } else {
            if (!isFirstResume) {
                checkPermission();
            } else {
                isFirstResume = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        //supportFra
        mDelegate.onDestroy();
        //RxLifecycle
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        //EventBus
        if (isRegisterEvent()) {
            EventBus.getDefault().unregister(this);
        }
        mHandler.removeCallbacksAndMessages(null);
        hideDialog();
        EasyPermissions.hidePermissionsDialog();
        hideSoftInputView();
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        AppManager.INSTACE.removeActivity(this);
        overridePendingTransitionOut();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    public void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    public void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    public void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * Eventbus相关
     *
     * @param event
     */
    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void defaultEvent(DefaultEventData defaultEventData) {
        //do nothing.just for somebody not Subscribe but regiest the event will crash
    }

    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    protected static class MyHandler extends Handler {

        private final WeakReference<BaseCompatActivity> mActivity;

        public MyHandler(BaseCompatActivity activity) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseCompatActivity activity = mActivity.get();
            if (activity != null) {
                // do someThing
                activity.handleMes(msg);
            }
        }
    }

    protected Toast mToast;

    public Toast getToast() {
        return mToast;
    }

    public void showToast(final String msg) {
        showToast(msg, -1, LENGTH_SHORT);
    }

    public void showToast(@StringRes final int resId) {
        showToast(null, resId, LENGTH_SHORT);
    }

    public void showLongToast(final String msg) {
        showToast(msg, -1, LENGTH_LONG);
    }

    public void showLongToast(@StringRes final int resId) {
        showToast(null, resId, LENGTH_LONG);
    }

    protected void showToast(final String msg, final @StringRes int resId, final int length) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(BaseCompatActivity.this, "", length);
                }
                mToast.setDuration(length);
                if (TextUtils.isEmpty(msg)) {
                    if (resId != -1) {
                        try {
                            mToast.setText(resId);
                        } catch (Resources.NotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    mToast.setText(msg);
                }
                mToast.show();
            }
        });
    }

    public Snackbar showSnackBar(View contentView, String string) {
        Snackbar snackbar = Snackbar.make(contentView, string, Snackbar.LENGTH_SHORT);
        snackbar.show();
        return snackbar;
    }

    public Snackbar showSnackBar(View contentView, String string, String action, View.OnClickListener clickListener) {
        Snackbar snackbar = Snackbar.make(contentView, string, Snackbar.LENGTH_INDEFINITE).setAction(action, clickListener);
        snackbar.show();
        return snackbar;
    }

    public Snackbar showSnackBar(View contentView, String string, String action, View.OnClickListener clickListener,
                                 Snackbar.Callback callback) {
        Snackbar snackbar = Snackbar.make(contentView, string, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, clickListener).setCallback(callback);
        snackbar.show();
        return snackbar;
    }

    public Snackbar showSnackBar(View contentView, String string, Snackbar.Callback callback) {
        Snackbar snackbar = Snackbar.make(contentView, string, Snackbar.LENGTH_SHORT).setCallback(callback);
        snackbar.show();
        return snackbar;
    }

    public boolean isShowIngDialog() {
        boolean isShowing = false;
        if (null != mCustomDialog && mCustomDialog.isShowing() || null != mMaterialDialog && mMaterialDialog.isShowing()) {
            isShowing = true;
        }
        return isShowing;
    }

    public void hideDialog() {
        if (null != mCustomDialog && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
        if (null != mMaterialDialog && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
    }

    @ColorInt
    protected int getColorInt(@ColorRes int colorRes) {
        return getResources().getColor(colorRes);
    }

    //MD-Dialog相关
    public void showOnlyContent(@NonNull CharSequence content) {
        mMaterialDialog = new MaterialDialog.Builder(this).content(content).build();
        mMaterialDialog.show();
    }

    public void showNoTitleLoadingDialog(@NonNull CharSequence content) {
        mMaterialDialog = new MaterialDialog.Builder(this).content(content).progress(true, 0).cancelable(false).build();
        mMaterialDialog.show();
    }

    public void showNoTitleLoadingDialog(@NonNull CharSequence content, boolean cancel) {
        mMaterialDialog = new MaterialDialog.Builder(this).content(content).progress(true, 0).cancelable(cancel).build();
        mMaterialDialog.show();
    }

    public void showLoadingDialog(@NonNull CharSequence title, @NonNull CharSequence content) {
        mMaterialDialog = new MaterialDialog.Builder(this).title(title).content(content).progress(true, 0).cancelable(false).build();
        mMaterialDialog.show();
    }

    public void showNoTitleMdDialog(@NonNull CharSequence content, SingleButtonCallback singleButtonCallback) {
        mMaterialDialog = new MaterialDialog.Builder(this).content(content).positiveText(OK_CONTENT).negativeText(CANCEL_CONTENT)
                .onPositive(singleButtonCallback).build();
        mMaterialDialog.show();
    }

    public void showEditTextCallback(@NonNull CharSequence title, @NonNull CharSequence content, @NonNull CharSequence positiveStr,
                                     @NonNull CharSequence hintStr, MaterialDialog.InputCallback inputCallback) {
        mMaterialDialog = new MaterialDialog.Builder(this).title(title).content(content).inputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS).inputRange(2, 16)
                .positiveText(positiveStr).input(hintStr, "", false, inputCallback).build();
        mMaterialDialog.show();
    }

    //自定义Dialog相关
    public void showBaseDialog(@NonNull CharSequence content, CustomButtonCallback customButtonCallback) {
        showBaseDialog("", content, OK_CONTENT, CANCEL_CONTENT, customButtonCallback);
    }

    public void showBaseDialog(@NonNull CharSequence title, @NonNull CharSequence content, CustomButtonCallback customButtonCallback) {
        showBaseDialog(title, content, OK_CONTENT, CANCEL_CONTENT, customButtonCallback);
    }

    public void showBaseDialog(@NonNull CharSequence content, @NonNull CharSequence positiveText,
                               @NonNull CharSequence negativeText, CustomButtonCallback customButtonCallback) {
        showBaseDialog("", content, positiveText, negativeText, THEME_COLOR_RES, CANCEL_COLOR_RES, customButtonCallback);
    }

    public void showBaseDialog(@NonNull CharSequence content, @NonNull CharSequence positiveText,
                               @NonNull CharSequence negativeText, @ColorRes int positiveColor,
                               @ColorRes int negativeColor, CustomButtonCallback customButtonCallback) {
        showBaseDialog("", content, positiveText, negativeText, positiveColor, negativeColor, customButtonCallback);
    }

    public void showBaseDialog(@NonNull CharSequence title, @NonNull CharSequence content, @NonNull CharSequence positiveText,
                               @NonNull CharSequence negativeText, CustomButtonCallback customButtonCallback) {
        showBaseDialog(title, content, positiveText, negativeText, THEME_COLOR_RES, CANCEL_COLOR_RES, customButtonCallback);
    }

    public void showBaseDialog(@NonNull CharSequence title, @NonNull CharSequence content, @NonNull CharSequence positiveText,
                               @NonNull CharSequence negativeText, @ColorRes int positiveColor, @ColorRes int negativeColor,
                               CustomButtonCallback customButtonCallback) {
        mCustomDialog = new CustomDialog.Builder(this).title(title).content(content).style(ALL_BUTTON)
                .positiveText(positiveText).positiveColorRes(positiveColor).onPositive(customButtonCallback)
                .negativeText(negativeText).negativeColorRes(negativeColor).onNegative(customButtonCallback)
                .build();
        mCustomDialog.show();
    }

    public void showWarningDialog(@NonNull CharSequence content, @NonNull CharSequence positiveText,
                                  @NonNull CharSequence negativeText, CustomButtonCallback customButtonCallback) {
        showWarningDialog("", content, positiveText, negativeText, customButtonCallback);
    }

    public void showWarningDialog(@NonNull CharSequence title, @NonNull CharSequence content, @NonNull CharSequence positiveText,
                                  @NonNull CharSequence negativeText, CustomButtonCallback customButtonCallback) {
        showBaseDialog(title, content, positiveText, negativeText, WARNING_COLOR_RES, CANCEL_COLOR_RES, customButtonCallback);
    }

    public void showOnlyConfirmCallback(@NonNull CharSequence content, CustomButtonCallback customButtonCallback) {
        showOnlyConfirmCallback("", content, OK_CONTENT, THEME_COLOR_RES, customButtonCallback);
    }

    public void showOnlyConfirmCallback(@NonNull CharSequence content, @NonNull CharSequence onlyPositiveText,
                                        CustomButtonCallback customButtonCallback) {
        showOnlyConfirmCallback("", content, onlyPositiveText, THEME_COLOR_RES, customButtonCallback);
    }

    public void showOnlyConfirmCallback(@NonNull CharSequence title, @NonNull CharSequence content,
                                        @NonNull CharSequence onlyPositiveText, CustomButtonCallback customButtonCallback) {
        showOnlyConfirmCallback(title, content, onlyPositiveText, THEME_COLOR_RES, customButtonCallback);
    }

    public void showOnlyConfirmCallback(@NonNull CharSequence title, @NonNull CharSequence content,
                                        @NonNull CharSequence onlyPositiveText, @ColorRes int onlyPositiveColor,
                                        CustomButtonCallback customButtonCallback) {
        mCustomDialog = new CustomDialog.Builder(this).title(title).content(content).style(ONLY_CONFIRM_BUTTON)
                .onlyPositiveText(onlyPositiveText).onlyPositiveColorRes(onlyPositiveColor).onPositive(customButtonCallback)
                .build();
        mCustomDialog.show();
    }

    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /****************
     * 运行时权限管理
     ****************/
    private boolean isFirstResume = true;

    public final static int RC_ALL_PERM = 0x100;
    private boolean isNeverAskAgain = false;

    protected void doPermissionsSuc() {
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @AfterPermissionGranted(RC_ALL_PERM)
    protected void checkPermission() {

        if (EasyPermissions.hasPermissions(this, ConfigurationUtil.getPermissionArray())) {
            doPermissionsSuc();
        } else {
            String[] deniedPermissions = EasyPermissions.getDeniedPermissions(this, ConfigurationUtil.getPermissionArray());
            if (!isNeverAskAgain) {
                EasyPermissions.requestPermissions(this, getString(R.string.rationale_all), RC_ALL_PERM, deniedPermissions);
            } else {
                ArrayList<String> deniedList = new ArrayList<>();
                Collections.addAll(deniedList, deniedPermissions);
                isNeverAskAgain = EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.rationale_ask_again),
                        R.string.setting, android.R.string.cancel, deniedList);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms.size() == ConfigurationUtil.getPermissionArray().length) {
            doPermissionsSuc();
        } else {
            LogUtil.e("BaseAct", "onPermissionsGranted: 有部分权限没有允许");
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        isNeverAskAgain = EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.rationale_ask_again),
                R.string.setting, android.R.string.cancel, perms);
    }

    /*****************
     * RxJavaCycle相关
     *****************/
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    /*******************
     * Fragmentation相关
     *******************/

    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);

    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnimation) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnimation);
    }

    /**
     * 加载多个同级根Fragment,类似Wechat, QQ主页的场景
     */
    public void loadMultipleRootFragment(int containerId, int showPosition, ISupportFragment... toFragments) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, toFragments);
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     * <p>
     * 建议使用更明确的{@link #showHideFragment(ISupportFragment, ISupportFragment)}
     *
     * @param showFragment 需要show的Fragment
     */
    public void showHideFragment(ISupportFragment showFragment) {
        mDelegate.showHideFragment(showFragment);
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    public void showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment);
    }

    /**
     * It is recommended to use {@link SupportFragment#start(ISupportFragment)}.
     */
    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * It is recommended to use {@link SupportFragment#start(ISupportFragment, int)}.
     *
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * It is recommended to use {@link SupportFragment#startForResult(ISupportFragment, int)}.
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * It is recommended to use {@link SupportFragment#startWithPop(ISupportFragment)}.
     * Launch a fragment while poping self.
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * It is recommended to use {@link SupportFragment#replaceFragment(ISupportFragment, boolean)}.
     */
    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    /**
     * Pop the fragment.
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变其内所有Fragment的默认背景。
     */
    public void setDefaultFragmentBackground(@DrawableRes int backgroundRes) {
        mDelegate.setDefaultFragmentBackground(backgroundRes);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }
}
