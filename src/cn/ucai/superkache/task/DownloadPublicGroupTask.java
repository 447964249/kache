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
import cn.ucai.superkache.bean.Group;
import cn.ucai.superkache.data.ApiParams;
import cn.ucai.superkache.data.GsonRequest;
import cn.ucai.superkache.utils.Utils;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DownloadPublicGroupTask extends BaseActivity {
    private static final String TAG = DownloadPublicGroupTask.class.getName();
    Context mcontext;
    String update_public_group;
    String path;

    public DownloadPublicGroupTask(Context mcontext, String update_public_group) {
        this.mcontext = mcontext;
        this.update_public_group = update_public_group;
        initPath();
    }

    private void initPath() {
        try {

            //url= http://10.0.2.2:8080/SuperWeChatServer/Server?request=download_groups&m_user_name=
            path = new ApiParams()
                    .with(I.REQUEST_FIND_PUBLIC_GROUPS,update_public_group)
                    .getRequestUrl(I.REQUEST_FIND_PUBLIC_GROUPS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        executeRequest(new GsonRequest<Group[]>(path, Group[].class,
                responseDownloadContactListTaskListener(), errorListener()));
    }

    private Response.Listener<Group[]> responseDownloadContactListTaskListener() {
        return new Response.Listener<Group[]>() {

            @Override
            public void onResponse(Group[] Groups) {
                if (Groups != null) {
                    ArrayList<Group> list = Utils.array2List(Groups);
                    ArrayList<Group> publicList =
                            SuperWeChatApplication.getInstance().getPublicGroupList();
                    for (Group g : list) {
                       if (!publicList.contains(g)){
                           publicList.add(g);
                       }
                    }
//                    publicList.clear();
//                    publicList.addAll(list);
                    mcontext.sendStickyBroadcast(new Intent("update_public_group"));
                }
            }
        };
    }
}
