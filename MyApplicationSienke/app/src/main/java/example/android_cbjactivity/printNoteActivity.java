package example.android_cbjactivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import cc.thksoft.myfirstpro.myactivitymanager.MyActivityManager;
import cc.thksoft.myfirstpro.util.Gadget;
import cc.thksoft.myfirstpro.util.Mytoast;

public class printNoteActivity extends Activity implements OnClickListener {
	private EditText print_note_edit;// �༭��
	private TextView print_note_save;// ����
	private LinearLayout set_back_Btn_print;
	private SharedPreferences preferences;
	private Editor editor;
	private String printNote;// ��ӡ��ע

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyActivityManager mam = MyActivityManager.getInstance();
		mam.pushOneActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.print_note);
		innit();
	}

	private void innit() {
		preferences = getApplication()
				.getSharedPreferences("IP_PORT_DBNAME", 0);
		editor = preferences.edit();
		print_note_edit = (EditText) findViewById(R.id.print_note_edit);
		print_note_save = (TextView) findViewById(R.id.print_note_save);
		print_note_save.setOnClickListener(this);
		set_back_Btn_print = (LinearLayout) findViewById(R.id.set_back_Btn_print);
		set_back_Btn_print.setOnClickListener(this);

		if (preferences.contains("printNote")) {
			printNote = preferences.getString("printNote", "");
			print_note_edit.setText(printNote);
			print_note_edit.setSelection(printNote.length());
	
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.print_note_save:
			printNote = print_note_edit.getText().toString().trim();
			if (printNote == null || "".equals(printNote)) {
				Mytoast.showToast(this, "���ݲ���Ϊ�գ���༭", 1000);
			} else {
				editor.putString("printNote", printNote);
				editor.commit();
				Mytoast.showToast(this, "����ɹ�", 1000);
			}
			break;
		case R.id.set_back_Btn_print:
			Gadget.closeKeybord(print_note_edit, this);
			finish();

			break;

		default:
			break;
		}
	}
}
