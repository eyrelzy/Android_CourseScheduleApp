package neusoft.hzy.mytimetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DataBaseHelper extends SQLiteOpenHelper {
	
	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+
					DataBaseInfo.M_TABLE+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					DataBaseInfo.M_DAY+" VARCHAR,"+
					DataBaseInfo.M_NAME+" VARCHAR,"+
					DataBaseInfo.M_ADDRESS+" VARCHAR,"+
					DataBaseInfo.M_TYPE+" VARCHAR)"
				);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+DataBaseInfo.M_TABLE);
        onCreate(db);	
	}

}
