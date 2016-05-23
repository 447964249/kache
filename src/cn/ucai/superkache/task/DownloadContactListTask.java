package cn.ucai.superkache.task;

import android.content.Context;
import android.content.Intent;


import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ucai.superkache.I;
import cn.ucai.superkache.SuperWeChatApplication;
import cn.ucai.superkache.activity.BaseActivity;
import cn.ucai.superkache.bean.Contact;
import cn.ucai.superkache.data.ApiParams;
import cn.ucai.superkache.data.GsonRequest;
import cn.ucai.superkache.utils.Utils;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DownloadContactListTask extends BaseActivity {
    private static final String TAG = DownloadContactListTask.class.getName();
    Context mcontext;
    String username;
    String path;

    public DownloadContactListTask(Context mcontext, String username) {
        this.mcontext = mcontext;
        this.username = username;
        initPath();
    }

    private void initPath() {
        try {

            //url= http://10.0.2.2:8080/SuperWeChatServer/Server?request=download_groups&m_user_name=
            path = new ApiParams()
                    .with(I.Contact.USER_NAME, username)
                    .getRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        executeRequest(new GsonRequest<Contact[]>(path, Contact[].class,
                responseDownloadContactListTaskListener(), errorListener()));
    }

    private Response.Listener<Contact[]> responseDownloadContactListTaskListener() {
        return new Response.Listener<Contact[]>() {

            @Override
            public void onResponse(Contact[] response) {
                if (response != null) {
                    ArrayList<Contact> contactList = SuperWeChatApplication.getInstance().getContactList();
                    ArrayList<Contact> list = Utils.array2List(response);
                    contactList.clear();
                    contactList.addAll(list);
                    HashMap<String, Contact> userList = SuperWeChatApplication.getInstance().getUserList();
                    userList.clear();
                    for (Contact c : list) {
                        userList.put(c.getMContactCname(), c);
                    }
                    mcontext.sendStickyBroadcast(new Intent("update_contact_list"));
                }
            }
        };
    }
}
