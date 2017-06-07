package example.android_cbjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import baoyz.swipemenulistview.SwipeMenu;
import baoyz.swipemenulistview.SwipeMenuCreator;
import baoyz.swipemenulistview.SwipeMenuItem;
import baoyz.swipemenulistview.SwipeMenuListView;
import cc.thksoft.myfirstpro.lvadapter.MeterDBAdapter;
import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

@SuppressLint("NewApi")
public class DelDataBasesActivity extends Activity {
	private SwipeMenuListView lv_database;
	private List<String> dbNameList;
	private LinearLayout back;
	private TextView deletdata_count;
	private MeterDBAdapter meterDBAdapter;
	private NiftyDialogBuilder dialogBuilder;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deletedatabase);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		lv_database = (SwipeMenuListView) findViewById(R.id.lv_set_del);
		back = (LinearLayout) findViewById(R.id.set_del_database_back_Btn);
		deletdata_count = (TextView) findViewById(R.id.deletdata_count);
		sharedPreferences = getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = sharedPreferences.edit();
		
		dbNameList = new ArrayList<String>();
		final String filepath = Environment.getDataDirectory().getPath() + "/data/"+"com.example.android_cbjactivity"+"/databases";
		File file = new File(filepath);
		String[] filename = file.list();
		if(filename!=null)
		for(String name:filename){
			if(name.contains("journal")){	
				System.out.println("����");
				File file1 = new File(filepath+name);
				file1.delete();
			}else{
				dbNameList.add(name);
			}
		}
		if(dbNameList.isEmpty()){
			dbNameList.add("û���ļ�");
			deletdata_count.setText("��"+0+"��");
		}else{
			deletdata_count.setText("��"+dbNameList.size()+"��");
		}
		
		
		meterDBAdapter = new MeterDBAdapter(this,dbNameList);
		lv_database.setAdapter(meterDBAdapter);
		SwipeMenuCreator creator = new SwipeMenuCreator() {
		    @Override
		    public void create(SwipeMenu menu) {
/*		        // create "open" item
		        SwipeMenuItem openItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
		                0xCE)));
		        // set item width
		        openItem.setWidth(90);
		        // set item title
		        openItem.setTitle("Open");
		        // set item title fontsize
		        openItem.setTitleSize(18);
		        // set item title font color
		        openItem.setTitleColor(Color.WHITE);
		        // add to menu
		        menu.addMenuItem(openItem);*/
		        // create "delete" item
		        SwipeMenuItem deleteItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
		                0x3F, 0x25)));
		        // set item width
		        deleteItem.setWidth(90);
		        // set a icon
		        deleteItem.setIcon(R.mipmap.delete_file_item);
		        // add to menu
		        menu.addMenuItem(deleteItem);
		    }
		};
		lv_database.setMenuCreator(creator);
		
/*		lv_database.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, final int position,
					long arg3) {
				final String dbName = dbNameList.get(position);
				if(!"û���ļ�".equals(dbName)){
					dialogBuilder= new NiftyDialogBuilder(DelDataBasesActivity.this,R.style.dialog_untran);
			        dialogBuilder
	                .withTitle("�Ƿ�ɾ����")                                  
	                .withTitleColor("#000000")                                  
	                .withDividerColor("#999999")                             
	                .withMessage(dbName)                     
	                .withMessageColor("#000000")                               
	                .isCancelableOnTouchOutside(true)                           
	                .withDuration(700)                                          
	                .withEffect(Effectstype.Slidetop)                                         
	                .withButton1Text("ȷ��")                                      
	                .withButton2Text("ȡ��")                                  
	                .setButton1Click(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
							File file = new File(filepath+"/"+dbName);
							if(file.exists()){
								file.delete();
								if(dbNameList.get(position).equals(dbName)){
									editor.remove("dbName");
									editor.commit();
									editor.remove("con_signal");
									editor.commit();
									editor.remove("con_position");
									editor.commit();
								}
								dbNameList.remove(position);
								meterDBAdapter.notifyDataSetChanged();
								String dbName = sharedPreferences.getString("dbName", "");
								Toast.makeText(DelDataBasesActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(DelDataBasesActivity.this, "δ�ܳɹ�", Toast.LENGTH_SHORT).show();
							}
	                    	dialogBuilder.dismiss();
	                    }
	                })
	                .setButton2Click(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                    	dialogBuilder.dismiss();
	                    }
	                })
	                .show();
				}
			}
		});*/

		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lv_database.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					final int position, long arg3) {
				final String dbName = dbNameList.get(position);
				if(!"û���ļ�".equals(dbName)){
					dialogBuilder= new NiftyDialogBuilder(DelDataBasesActivity.this,R.style.dialog_untran);
			        dialogBuilder
	                .withTitle("�Ƿ�ɾ����")                                  
	                .withTitleColor("#000000")                                  
	                .withDividerColor("#999999")                             
	                .withMessage(dbName)                     
	                .withMessageColor("#000000")                               
	                .isCancelableOnTouchOutside(true)                           
	                .withDuration(700)                                          
	                .withEffect(Effectstype.Slidetop)
	                .withButton1Text("ȷ��")                                      
	                .withButton2Text("ȡ��")                                  
	                .setButton1Click(new OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
							File file = new File(filepath+"/"+dbName);
							if(file.exists()){
								file.delete();
								if(dbNameList.get(position).equals(dbName)){
									editor.remove("dbName");
									editor.commit();
									editor.remove("con_signal");
									editor.commit();
									editor.remove("con_position");
									editor.commit();
								}
								dbNameList.remove(position);
								meterDBAdapter.notifyDataSetChanged();
								if(dbNameList!=null)
									deletdata_count.setText("��"+dbNameList.size()+"��");
								String dbName = sharedPreferences.getString("dbName", "");
								Toast.makeText(DelDataBasesActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
							}else{
								Toast.makeText(DelDataBasesActivity.this, "δ�ܳɹ�", Toast.LENGTH_SHORT).show();
							}
	                    	dialogBuilder.dismiss();
	                    }
	                })
	                .setButton2Click(new OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                    	dialogBuilder.dismiss();
	                    }
	                })
	                .show();
				}
				return false;
			}
		});
		lv_database.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
				switch (index) {
				case 1:
					// open

					break;
				case 0:
					// delete
//					delete(item);
					final String dbName = dbNameList.get(position);
					if(!"û���ļ�".equals(dbName)){
						dialogBuilder= new NiftyDialogBuilder(DelDataBasesActivity.this,R.style.dialog_untran);
				        dialogBuilder
		                .withTitle("�Ƿ�ɾ����")
		                .withTitleColor("#000000")
		                .withDividerColor("#999999")
		                .withMessage(dbName)
		                .withMessageColor("#000000")
		                .isCancelableOnTouchOutside(true)
		                .withDuration(700)
		                .withEffect(Effectstype.Slidetop)
		                .withButton1Text("ȷ��")
		                .withButton2Text("ȡ��")
		                .setButton1Click(new OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
								File file = new File(filepath+"/"+dbName);
								if(file.exists()){
									file.delete();
									if(dbNameList.get(position).equals(dbName)){
										editor.remove("dbName");
										editor.commit();
										editor.remove("con_signal");
										editor.commit();
										editor.remove("con_position");
										editor.commit();
									}
									dbNameList.remove(position);
									meterDBAdapter.notifyDataSetChanged();
									if(dbNameList!=null)
										deletdata_count.setText("��"+dbNameList.size()+"��");
									String dbName = sharedPreferences.getString("dbName", "");
									Toast.makeText(DelDataBasesActivity.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(DelDataBasesActivity.this, "δ�ܳɹ�", Toast.LENGTH_SHORT).show();
								}
		                    	dialogBuilder.dismiss();
		                    }
		                })
		                .setButton2Click(new OnClickListener() {
		                    @Override
		                    public void onClick(View v) {
		                    	dialogBuilder.dismiss();
		                    }
		                })
		                .show();
					}
					break;
				}
				return false;
			}
		});
	}
}
