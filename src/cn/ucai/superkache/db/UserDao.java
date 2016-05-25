package cn.ucai.superkache.db;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import cn.ucai.superkache.I;
        import cn.ucai.superkache.bean.User;


public class UserDao extends SQLiteOpenHelper {
    public static final String Id = "_id";
    public static final String TABLE_NAME = "user";

    public UserDao(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String sql = "create table if not exists "+ TABLE_NAME +"( " +
                I.User.USER_ID +" integer primary key autoincrement, " +
                I.User.USER_NAME +" varchar unique not null, " +
                I.User.NICK +" varchar, " +
                I.User.PASSWORD +" varchar, " +
                I.User.UN_READ_MSG_COUNT +" int default(0) " +
                ");";*/
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +"("+
                I.User.USER_ID +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                I.User.USER_NAME +" TEXT NOT NULL," +
                I.User.PASSWORD + " TEXT NOT NULL," +
                I.User.NICK + " TEXT NOT NULL," +
                I.User.UN_READ_MSG_COUNT + " INTEGER DEFAULT 0" +
                ");";
        Log.e("123","sql "+sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean addUser(User user){
        ContentValues values = new ContentValues();
        //??
        values.put(I.User.USER_ID,user.getMUserId());
        values.put(I.User.USER_NAME,user.getMUserName());
        values.put(I.User.NICK,user.getMUserNick());
        values.put(I.User.PASSWORD,user.getMUserPassword());
        values.put(I.User.UN_READ_MSG_COUNT,user.getMUserUnreadMsgCount());
        SQLiteDatabase db = getWritableDatabase();
        long insert = db.insert(TABLE_NAME, null, values);
        return insert>0;
    }

    public User findUserByUserName(String userName){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + I.User.USER_NAME + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(I.User.USER_ID));
            String nick = cursor.getString(cursor.getColumnIndex(I.User.NICK));
            String password = cursor.getString(cursor.getColumnIndex(I.User.PASSWORD));
            int unmessage = cursor.getInt(cursor.getColumnIndex(I.User.UN_READ_MSG_COUNT));
            return new User(id, userName, password, nick, unmessage);
        }
        cursor.close();
        return null;
    }

    public boolean updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(I.User.USER_ID,user.getMUserId());
        values.put(I.User.USER_NAME,user.getMUserName());
        values.put(I.User.NICK,user.getMUserNick());
        values.put(I.User.PASSWORD,user.getMUserPassword());
        values.put(I.User.UN_READ_MSG_COUNT,user.getMUserUnreadMsgCount());
        SQLiteDatabase db = getWritableDatabase();
        long insert = db.update(TABLE_NAME, values,I.User.USER_NAME+"=?",new String[]{user.getMUserName()});
        return insert>0;
    }


}
