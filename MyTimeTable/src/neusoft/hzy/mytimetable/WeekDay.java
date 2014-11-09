package neusoft.hzy.mytimetable;

import neusoft.hzy.mytimetable.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WeekDay extends ListActivity{
	
	private final int madmin = Menu.FIRST;
	private final int mclose = Menu.FIRST+1;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        String[] weekDay = new String[]{
	        		"星期日", "星期一", "星期二", "星期三", "星期四", "星期五 ", "星期六"
	        };
	        ArrayAdapter<String> adapter = 
	        	new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, weekDay);
	        setListAdapter(adapter);
	        setTitle("课程表管理");
	    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(this, Edit.class);//跳转到编辑的界面
		i.putExtra("dayofweek", ((TextView)v).getText().toString());
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, madmin, Menu.NONE, "课程表")
		.setIcon(R.drawable.timetable);
		menu.add(0, mclose, Menu.NONE, "关闭")
		.setIcon(R.drawable.close);
		return true;
	} 
	
	@Override   
	public boolean onOptionsItemSelected(MenuItem item) {     
	  switch (item.getItemId()) {   
	  	case madmin: {
	  		Intent i = new Intent(this, ShowTable.class);//跳转到全是星的界面
	  		startActivity(i);
	  		finish();
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
