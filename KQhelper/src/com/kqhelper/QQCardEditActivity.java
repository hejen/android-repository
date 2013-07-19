package com.kqhelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kqhelper.db.WorkListManager;

public class QQCardEditActivity extends Activity {
	
	private WorkListManager wlm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wlm = new WorkListManager(this);
		setContentView(R.layout.activity_edit);
		initBtn();
		initSpinner();
		Intent intent = getIntent();
		if ("edit".equalsIgnoreCase(intent.getStringExtra("action"))){
			loadWorkLine(intent.getStringExtra("cWorkid"));
		}
	}

	private void initBtn() {
		Button save = (Button)findViewById(R.id.edit_btn_save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et_name = (EditText)findViewById(R.id.edit_name);
				if ("".equals(et_name.getText().toString())){
					Toast.makeText(QQCardEditActivity.this, R.string.str_error_noname, Toast.LENGTH_LONG).show();
					return;
				}
				EditText et_sid = (EditText)findViewById(R.id.edit_sid);
				if ("".equals(et_sid.getText().toString())){
					Toast.makeText(QQCardEditActivity.this, R.string.str_error_nosid, Toast.LENGTH_LONG).show();
					return;
				}
				Map qqcard = new HashMap();
				Spinner worktype = (Spinner)findViewById(R.id.edit_sp_worktype);
				qqcard.put("cWorkTypeName", worktype.getSelectedItem().toString());
				qqcard.put("cName", getWidgetValue(R.id.edit_name));
				qqcard.put("csid", getWidgetValue(R.id.edit_sid));
				qqcard.put("cWorkid", getWidgetValue(R.id.edit_v_workid));
				Switch swit = (Switch)findViewById(R.id.edit_status);
				qqcard.put("iStatus", swit.isChecked()?"1":"0");
				wlm.saveQQCard(qqcard);
				Toast.makeText(QQCardEditActivity.this, R.string.str_succ_save, Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("msg", "succ");
				setResult(1, intent);
				finish();
			}
		});
		Button cancel = (Button)findViewById(R.id.edit_btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initSpinner() {
		Spinner worktype = (Spinner)findViewById(R.id.edit_sp_worktype);
		ArrayAdapter<String> sa = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item);
		List<Map> workTypeList = wlm.getAllWorkType();
		for (Map workTypeLine: workTypeList){
			sa.add(workTypeLine.get("cName").toString());
		}
		worktype.setAdapter(sa);
	}

	private void loadWorkLine(String workid) {
		List<Map> workList = wlm.getWorkListByIds(workid);
		if (workList==null || workList.size()==0){
			return ;
		}
		Map workLine = workList.get(0);
		Switch swit = (Switch)findViewById(R.id.edit_status);
		swit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					buttonView.setText(R.string.str_valid);
				}else{
					buttonView.setText(R.string.str_invalid);
				}
			}
		});
		if ("1".equals(workLine.get("iStatus").toString())){
			swit.setText(R.string.str_valid);
			swit.setChecked(true);
		}else{
			swit.setText(R.string.str_invalid);
			swit.setChecked(false);
		}
		this.setWidgetValue(R.id.edit_name, workLine.get("cName").toString());
		this.setWidgetValue(R.id.edit_sid, workLine.get("csid").toString());
		this.setWidgetValue(R.id.edit_v_workid, workLine.get("cWorkid").toString());
		Spinner worktype = (Spinner)findViewById(R.id.edit_sp_worktype);
		setSpinner(worktype, workLine.get("cWorkTypeName").toString());
	}
	
	public String getWidgetValue(int resid){
		TextView tv = (TextView)findViewById(resid);
		return tv.getText().toString();
	}
	
	public void setWidgetValue(int resid, String value){
		TextView tv = (TextView)findViewById(resid);
		tv.setText(value);
	}

	private void setSpinner(Spinner worktype, String item) {
		ArrayAdapter aa = (ArrayAdapter)worktype.getAdapter();
		int pos = -1;
		for (int i=0;i<aa.getCount();i++){
			if (aa.getItem(i).toString().equals(item)){
				pos = i;
			}
		}
		if (pos==-1){
			return;
		}
		worktype.setSelection(pos);
	}

}
