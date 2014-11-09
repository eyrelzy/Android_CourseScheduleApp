package neusoft.hzy.mytimetable;

import java.util.ArrayList;
import java.util.List;
import neusoft.hzy.mytimetable.R;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends Activity{
	private List<EditText> class_n, class_a;
	private String weekday;
	private String[] cType = new String[]{"00", "12", "34", "56", "78", "90"}; 
	private final int msave = Menu.FIRST;
	private final int mclose = Menu.FIRST+1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        weekday=getIntent().getStringExtra("dayofweek");
        setTitle("管理课程表-"+weekday);
        class_n = new ArrayList<EditText>();
        class_a = new ArrayList<EditText>();
        int[] addrid = new int[]{R.id.class_a00, R.id.class_a12, R.id.class_a34, 
        		R.id.class_a56, R.id.class_a78, R.id.class_a90};
        for(int i = 0; i < addrid.length; i++)
        	class_a.add((EditText)findViewById(addrid[i]));
        
        int[] nameid = new int[]{R.id.class_n12, R.id.class_n34, 
        		 R.id.class_n56, R.id.class_n78, R.id.class_n90};
        
        for(int i = 0; i < nameid.length; i++)
        	class_n.add((EditText)findViewById(nameid[i]));
        
        readTble();
    }
	
	private void writeTable(){
		DataBaseHelper helper = new DataBaseHelper(this, DataBaseInfo.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		String[] C_name = new String[]{"早读",getSTR(class_n.get(0)),getSTR(class_n.get(1)),getSTR(class_n.get(2)),getSTR(class_n.get(3)),getSTR(class_n.get(4))};
		String[] C_address = new String[]{getSTR(class_a.get(0)), getSTR(class_a.get(1)), getSTR(class_a.get(2)), getSTR(class_a.get(3)), getSTR(class_a.get(4)), getSTR(class_a.get(5))};
		
		Cursor c = db.rawQuery("select * from "+DataBaseInfo.M_TABLE+" where "+
				DataBaseInfo.M_DAY + " = '"+weekday+"'", null);
		if(c.getCount() > 0)
			SQLUp(db, C_name, C_address);
		else
			SQLIn(db, C_name, C_address);
		db.close();
		Toast.makeText(this, "写入成功！", Toast.LENGTH_LONG).show();
		finish();
	}
	
	private void SQLUp(SQLiteDatabase db, String[] C_names, String[] C_addresses){
		for(int i = 0; i < C_names.length ; i++){
			String sql = "UPDATE "+DataBaseInfo.M_TABLE+" SET "+
			DataBaseInfo.M_NAME+" = '"+C_names[i]+"',"+
			DataBaseInfo.M_ADDRESS+" = '"+C_addresses[i]+
			"' WHERE "+DataBaseInfo.M_DAY+" = '"+weekday +"' and "+DataBaseInfo.M_TYPE+" = '"+cType[i]+"'";
			db.execSQL(sql);
		}
	}
	
	private void SQLIn(SQLiteDatabase db, String[] C_names, String[] C_addresses){
		for(int i = 0; i < C_names.length ; i++){
			String sql = "insert into "+ DataBaseInfo.M_TABLE+ "(" +
			DataBaseInfo.M_DAY+", " +DataBaseInfo.M_NAME+", "+DataBaseInfo.M_ADDRESS +", "+DataBaseInfo.M_TYPE+")" +
			"values('"+weekday+"', '"+C_names[i]+"', '" + C_addresses[i]+"', '"+ cType[i] +"');";
			db.execSQL(sql);
		}
	}
	
	private String getSTR(EditText name){
		return name.getText().toString();
	}
	
	private void readTble(){
		DataBaseHelper helper = new DataBaseHelper(this, DataBaseInfo.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DataBaseInfo.M_TABLE+" where "+
				DataBaseInfo.M_DAY + " = '"+weekday+"'", null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			if(c.getString(4).equals(cType[0]))
				class_a.get(0).setText(c.getString(3));
			for(int i = 0; i < class_n.size(); i++){
				if(c.getString(4).equals(cType[i+1])){
					class_n.get(i).setText(c.getString(2));
					class_a.get(i+1).setText(c.getString(3));
				}
			}
			c.moveToNext();
		}
		db.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, msave, Menu.NONE, "保存").setIcon(R.drawable.save);
		menu.add(0, mclose, Menu.NONE, "关闭").setIcon(R.drawable.close);
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case msave: {
	  		writeTable();
	  		return true;
	  	}
	  	case mclose: {
	  		finish();
	  		return true;
	  	}
	  	default:
		  return false;
	  }   
	}
}
