package cn.ucai.superkache.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

import cn.ucai.superkache.DemoHXSDKHelper;
import cn.ucai.superkache.I;
import cn.ucai.superkache.SuperWeChatApplication;
import cn.ucai.superkache.bean.User;
import cn.ucai.superkache.db.UserDao;
import cn.ucai.superkache.task.DownloadAllGroupTask;
import cn.ucai.superkache.task.DownloadContactListTask;
import cn.ucai.superkache.task.DownloadPublicGroupTask;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    private RelativeLayout rootLayout;
    private TextView versionText;
    Context mcontext;

    private static final int sleepTime = 2000;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(cn.ucai.superkache.R.layout.activity_splash);
        super.onCreate(arg0);
        mcontext = this;

        rootLayout = (RelativeLayout) findViewById(cn.ucai.superkache.R.id.splash_root);
        versionText = (TextView) findViewById(cn.ucai.superkache.R.id.tv_version);
        ///////////////自动退出
        //EMChatManager.getInstance().logout();
        versionText.setText(getVersion());
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DemoHXSDKHelper.getInstance().isLogined()) {
    //
            User user = SuperWeChatApplication.getInstance().getUser();
            SuperWeChatApplication instance = SuperWeChatApplication.getInstance();
            instance.setUser(user);
            String username = user.getMUserName();
// 登陆成功，保存用户名密码
			instance.setUserName(user.getMUserName());
			instance.setPassword(user.getMUserPassword());
			SuperWeChatApplication.currentUserNick = user.getMUserNick();
    //

//
           /* String username = SuperWeChatApplication.getInstance().getUserName();
            UserDao dao = new UserDao(mcontext);
            User user = dao.findUserByUserName(username);
            SuperWeChatApplication.getInstance().setUser(user);
*/


            new DownloadContactListTask(mcontext, username).execute();
            new DownloadAllGroupTask(mcontext, username).execute();
            new DownloadPublicGroupTask(mcontext, username, I.PAGE_ID_DEFAULT, I.PAGE_SIZE_DEFAULT).execute();

        }
        new Thread(new Runnable() {
            public void run() {
                if (DemoHXSDKHelper.getInstance().isLogined()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主页面
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();

    }

    /**
     * 获取当前应用程序的版本号
     */
    private String getVersion() {
        String st = getResources().getString(cn.ucai.superkache.R.string.Version_number_is_wrong);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
            String version = packinfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return st;
        }
    }
}
