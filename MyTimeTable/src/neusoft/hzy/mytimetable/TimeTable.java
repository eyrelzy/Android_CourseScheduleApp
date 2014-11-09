package neusoft.hzy.mytimetable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import neusoft.hzy.mytimetable.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TextView;

public class TimeTable extends Activity implements OnGestureListener{
	private SharedPreferences sp;
	private String[] weekDays = new String[]{
    		"星期日", "星期一", "星期二", "星期三", "星期四", "星期五 ", "星期六"};
	private List<TextView> class_n, class_a;
	private String weekday;
	private String[] cType = new String[]{"00", "12", "34", "56", "78", "90"}; 
	private static final int FLING_MIN_DISTANCE = 120;
	private GestureDetector detector;
	private final int mde = Menu.FIRST;
	private final int mclose = Menu.FIRST+1;
	private final int madmin = Menu.FIRST+2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        sp = getSharedPreferences("tabletime_authentication", Context.MODE_PRIVATE);
        if(sp.getString("keys", null) !=null && sp.getString("keys", null).equals("privateKey")){
        	if(isEmpty())
        		emptyDialog();//未注册过
        }else{
        	aboutDialog();//已经注册过
        }
        Calendar calendar = Calendar.getInstance();
        
        if(getIntent().hasExtra("dayofweek"))
        		weekday=getIntent().getStringExtra("dayofweek");
        else
        		weekday= weekDays[calendar.get(Calendar.DAY_OF_WEEK)-1];
        
        setTitle(getResources().getString(R.string.app_name)+"-"+weekday);//课表第几周
        //课程名称的编辑框
        class_n = new ArrayList<TextView>();
        //课程地点的编辑框
        class_a = new ArrayList<TextView>();
        //从早读开始到9,10节
        int[] addrid = new int[]{R.id.class_a00, R.id.class_a12, R.id.class_a34, 
        		R.id.class_a56, R.id.class_a78, R.id.class_a90};
        for(int i = 0; i < addrid.length; i++)
        	class_a.add((TextView)findViewById(addrid[i]));
        
        int[] nameid = new int[]{R.id.class_n12, R.id.class_n34, 
        		 R.id.class_n56, R.id.class_n78, R.id.class_n90};
        
        for(int i = 0; i < nameid.length; i++)
        	class_n.add((TextView)findViewById(nameid[i]));
        detector = new GestureDetector(this);//监听操作
        readTble();
    }
    
    private void aboutDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("课程盒子");  
        builder.setMessage("开发者：刘智月；日期：2014-04");
        builder.setIcon(R.drawable.icon);
        builder.setPositiveButton("确定", new OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		(sp.edit()).putString("keys", "privateKey").commit();
        		dialog.dismiss();
        		emptyDialog();
        	}
        });
        builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
        builder.show();
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
    
    private String preDay(String today){
    	int i;
    	for(i = 0; i< weekDays.length; i++){
    		if(today.equals(weekDays[i]))
    			break;
    	}
    	i--;
    	if(i>=0)	
    		return weekDays[i];
    	else
    		return weekDays[6];
    }
    
    private String nextDay(String today){
    	int i;
    	for(i = 0; i< weekDays.length; i++){
    		if(today.equals(weekDays[i]))
    			break;
    	}
    	i++;
    	if(i<7)	
    		return weekDays[i];
    	else
    		return weekDays[0];

    }
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
			Intent i = new Intent(this, TimeTable.class);
			i.putExtra("dayofweek", preDay(weekday));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
			return true;
		} else if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {
			Intent i = new Intent(this, TimeTable.class);
			i.putExtra("dayofweek", nextDay(weekday));
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
			return true;
		} 
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, mde, Menu.NONE, "查看表格").setIcon(R.drawable.look);
		menu.add(0, madmin, Menu.NONE, "管理").setIcon(R.drawable.admin);
		menu.add(0, mclose, Menu.NONE, "关闭").setIcon(R.drawable.close);
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case mde: {
	  		Intent i = new Intent(this, ShowTable.class);
			startActivity(i);
			finish();
	  		return true;
	  	}
	  	case madmin: {
	  		Intent i = new Intent(this, WeekDay.class);
			startActivity(i);
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
	
	private Boolean isEmpty(){
		DataBaseHelper helper = new DataBaseHelper(this, DataBaseInfo.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from "+DataBaseInfo.M_TABLE, null);
		return !(c.getCount()>0);
	}
	 private void emptyDialog(){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);   
	        builder.setMessage("你还没有添加课程表，是否要添加课程表？");
	        builder.setIcon(R.drawable.icon);
	        builder.setPositiveButton("是", new OnClickListener() {
	        	@Override
	        	public void onClick(DialogInterface dialog, int which) {
	        		//实现activity的跳转
	        		Intent i = new Intent(TimeTable.this, WeekDay.class);
	    			startActivity(i);
	        		finish();
	        	}
	        });
	        builder.setNegativeButton("否", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
	        builder.show();
	 }
}