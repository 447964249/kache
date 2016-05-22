/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ucai.superkache.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;

import cn.ucai.superkache.I;
import cn.ucai.superkache.Listener.OnSetAvatarListener;
import cn.ucai.superkache.R;
import cn.ucai.superkache.SuperWeChatApplication;
import cn.ucai.superkache.bean.Message;
import cn.ucai.superkache.data.OkHttpUtils;
import cn.ucai.superkache.utils.ImageUtils;
import cn.ucai.superkache.utils.Utils;

import com.easemob.exceptions.EaseMobException;

import java.io.File;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity {
    private final static String TAG = RegisterActivity.class.getName();
    Activity mContext;
    private EditText userNameEditText;
    private EditText usernickEditText;
    private EditText passwordEditText;
    private EditText confirmPwdEditText;
    ImageView mIVAvatar;
    String Avatar;
    ProgressDialog pd;

    OnSetAvatarListener mOnSetAvatarListener;
    String username;
    String pwd;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.ucai.superkache.R.layout.activity_register);
        mContext = this;
        initView();
        setListener();
    }

    private void initView() {
        userNameEditText = (EditText) findViewById(cn.ucai.superkache.R.id.username);
        passwordEditText = (EditText) findViewById(cn.ucai.superkache.R.id.password);
        confirmPwdEditText = (EditText) findViewById(cn.ucai.superkache.R.id.confirm_password);
        usernickEditText = (EditText) findViewById(R.id.etNick);
        mIVAvatar = (ImageView) findViewById(R.id.iv_avatar);

    }

    private void setListener() {
        onLoginClickListener();
        onSetregisterListener();
        onSetAvatarListener();
    }

    private void onSetAvatarListener() {

        findViewById(R.id.layout_user_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSetAvatarListener = new OnSetAvatarListener(mContext, R.id.layout_register, getAvatarName(), I.AVATAR_TYPE_USER_PATH);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mOnSetAvatarListener.setAvatar(requestCode, data, mIVAvatar);
        }
    }

    private String getAvatarName() {
        Avatar = System.currentTimeMillis() + "";
        return Avatar;
    }

    private void onLoginClickListener() {
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 注册
     *
     * @param
     */
    private void onSetregisterListener() {
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = userNameEditText.getText().toString().trim();
                nick = usernickEditText.getText().toString().trim();
                pwd = passwordEditText.getText().toString().trim();
                String confirm_pwd = confirmPwdEditText.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    userNameEditText.requestFocus();
                    userNameEditText.setError(getResources().getString(R.string.User_name_cannot_be_empty));
                    return;
                } else if (username.matches("[\\w][\\w\\d_]+")) {
                    userNameEditText.requestFocus();
                    userNameEditText.setError(getResources().getString(R.string.User_name_cannot_be_wd));
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    passwordEditText.requestFocus();
                    passwordEditText.setError(getResources().getString(R.string.User_name_cannot_be_empty));
                    return;
                } else if (TextUtils.isEmpty(nick)) {
                    usernickEditText.requestFocus();
                    usernickEditText.setError(getResources().getString(R.string.Nick_name_cannot_be_empty));
                    return;
                } else if (TextUtils.isEmpty(confirm_pwd)) {
                    confirmPwdEditText.requestFocus();
                    confirmPwdEditText.setError(getResources().getString(cn.ucai.superkache.R.string.Confirm_password_cannot_be_empty));
                    return;
                } else if (!pwd.equals(confirm_pwd)) {
                    confirmPwdEditText.requestFocus();
                    confirmPwdEditText.setError(getResources().getString(cn.ucai.superkache.R.string.Two_input_password));
                    return;
                }

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
                    pd = new ProgressDialog(mContext);
                    pd.setMessage(getResources().getString(cn.ucai.superkache.R.string.Is_the_registered));
                    pd.show();


                }
            }
        });

    }

    private void registerAppSever() {
        //首先注册远端服务器帐号并上传头像-----okhtttp
        //注册环信帐号
        //如果环信注册失败，调用取消注册方法，删除远端帐号和图片
        File file = new File(ImageUtils.getAvatarPath(mContext, I.AVATAR_TYPE_USER_PATH),
                Avatar + I.AVATAR_SUFFIX_JPG);
        OkHttpUtils<Message> utils = new OkHttpUtils<Message>();
        utils.url(SuperWeChatApplication.SERVER_ROOT)
                .addParam(I.KEY_REQUEST, I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.PASSWORD, pwd)
                .addParam(I.User.NICK, nick)
                .targetClass(Message.class)
                .addFile(file)
                .execute(new OkHttpUtils.OnCompleteListener<Message>() {

                    @Override
                    public void onSuccess(Message result) {
                        if (result.isResult()) {
                            registerEMServer();
                        } else {
                            pd.dismiss();
                            Utils.showToast(mContext, Utils.getResourceString(mContext, result.getMsg()), Toast.LENGTH_SHORT);
                            Log.e(TAG, "register fail,error:" + result.getMsg());
                        }
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        Utils.showToast(mContext,error,Toast.LENGTH_SHORT);
                        Log.e(TAG, "register fail,error:" + error);
                    }
                });

    }

    /**
     * 注册环信帐号
     */
    private void registerEMServer() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(username, pwd);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            // 保存用户名
                            SuperWeChatApplication.getInstance().setUserName(username);
                            Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superkache.R.string.Registered_successfully), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    unRegister();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!RegisterActivity.this.isFinishing())
                                pd.dismiss();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NONETWORK_ERROR) {
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superkache.R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superkache.R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.UNAUTHORIZED) {
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superkache.R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.ILLEGAL_USER_NAME) {
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superkache.R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(cn.ucai.superkache.R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void unRegister() {
        //取消注册
        //url=http://10.0.2.2:8080/SuperWeChatServer/Server?request=unregister&m_user_name=
        OkHttpUtils<Message> utils = new OkHttpUtils<Message>();
        utils.url(SuperWeChatApplication.SERVER_ROOT)
                .addParam(I.KEY_REQUEST,I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,username)
                .targetClass(Message.class)
                .execute(new OkHttpUtils.OnCompleteListener<Message>() {
                    @Override
                    public void onSuccess(Message result) {
                        Log.e(TAG, "onSuccess:注册成功 "+result.getMsg() );
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    public void back(View view) {
        finish();
    }

}
