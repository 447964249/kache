package cn.ucai.superkache.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ucai.superkache.Constant;
import cn.ucai.superkache.I;
import cn.ucai.superkache.R;
import cn.ucai.superkache.SuperWeChatApplication;
import cn.ucai.superkache.applib.controller.HXSDKHelper;
import cn.ucai.superkache.DemoHXSDKHelper;
import cn.ucai.superkache.bean.Contact;
import cn.ucai.superkache.bean.User;
import cn.ucai.superkache.data.RequestManager;
import cn.ucai.superkache.domain.EMUser;

import com.android.volley.toolbox.NetworkImageView;
import com.easemob.util.HanziToPinyin;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

public class UserUtils {
	static String TAG = UserUtils.class.getName();
    /**
     * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
     * @param username
     * @return
     */
    public static EMUser getUserInfo(String username){
		String TAG = EMUser.class.getName();
		EMUser user = ((DemoHXSDKHelper) HXSDKHelper.getInstance()).getContactList().get(username);
        if(user == null){
            user = new EMUser(username);
        }

        if(user != null){
            //demo没有这些数据，临时填充
        	if(TextUtils.isEmpty(user.getNick()))
        		user.setNick(username);
        }
        return user;
    }
    public static Contact getUserBeanInfo(String username){
		Contact contact = SuperWeChatApplication.getInstance().getUserList().get(username);
		return contact;
	}

    /**
     * 设置用户头像
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EMUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            Picasso.with(context).load(user.getAvatar()).placeholder(cn.ucai.superkache.R.drawable.default_avatar).into(imageView);
        }else{
            Picasso.with(context).load(cn.ucai.superkache.R.drawable.default_avatar).into(imageView);
        }
    }
	public static void setUserBeanAvatar(String username, NetworkImageView ImageView){
		Contact contact = getUserBeanInfo(username);
		if (contact!=null&&contact.getMContactCname()!=null){
			setUserAvatar(getAvatarPath(username),ImageView);
		}
	}

private static void setUserAvatar(String url,NetworkImageView ImageView){
	Log.e(TAG, "setUserAvatar: " );
	if (url==null||url.isEmpty())return;
	ImageView.setDefaultImageResId(R.drawable.default_avatar);
	ImageView.setImageUrl(url, RequestManager.getImageLoader());
	ImageView.setErrorImageResId(R.drawable.default_avatar);
}
	private static String getAvatarPath(String username) {
		if (username.isEmpty()||username==null) return null;
		String path = I.DOWNLOAD_AVATAR_USER_URL + username;
		return path;
	}

	/**
     * 设置当前用户头像
     */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		EMUser user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar()).placeholder(cn.ucai.superkache.R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(cn.ucai.superkache.R.drawable.default_avatar).into(imageView);
		}
	}
	public static void setCurrentUserAvatar(NetworkImageView imageView) {
		User user = SuperWeChatApplication.getInstance().getUser();
		if (user != null) {
			setUserAvatar(getAvatarPath(user.getMUserName()), imageView);
		}
	}
    
    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username,TextView textView){
    	EMUser user = getUserInfo(username);
    	if(user != null){
    		textView.setText(user.getNick());
    	}else{
    		textView.setText(username);
    	}
    }
	public  static void setUserBeanNick(String username,TextView textView){
		Contact contact = getUserBeanInfo(username);
		if (contact!=null){
			if (contact.getMUserNick()!=null){
				textView.setText(contact.getMUserNick());
			} else 	if (contact.getMContactCname()!=null){
				textView.setText(contact.getMContactCname());
			}
		}/*else {
			textView.setText(username);
		}*/
		if (contact.getMUserNick() == null) {
			textView.setText(username);}
	}
    
    /**
     * 设置当前用户昵称
     */
    public static void setCurrentUserNick(TextView textView){
    	EMUser user = ((DemoHXSDKHelper)HXSDKHelper.getInstance()).getUserProfileManager().getCurrentUserInfo();
    	if(textView != null){
    		textView.setText(user.getNick());
    	}
    }
	public static void setCurrentUserBeanNick(TextView textView){
		User user = SuperWeChatApplication.getInstance().getUser();
		if(user !=null && user.getMUserNick()!=null&& textView != null){
			textView.setText(user.getMUserNick());
		}
	}
    
    /**
     * 保存或更新某个用户
     * @param
     */
	public static void saveUserInfo(EMUser newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}
	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 *
	 * @param username
	 * @param user
	 */
	public static void setUserHearder(String username, Contact user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getMUserNick())) {
			headerName = user.getMUserNick();
		} else {
			headerName = user.getMContactCname();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)
				||username.equals(Constant.GROUP_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1)
					.toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}
}
