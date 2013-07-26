package com.kqhelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kqhelper.db.WorkListManager;

public class QQFarmEditActivity extends Activity {
	
	private WorkListManager wlm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wlm = new WorkListManager(this);
		setContentView(R.layout.activity_qqfarm_edit);
		initBtn();
		Intent intent = getIntent();
		if ("edit".equalsIgnoreCase(intent.getStringExtra("action"))){
			loadWorkLine(intent.getStringExtra("cWorkid"));
		}
	}

	private void initBtn() {
		Button save = (Button)findViewById(R.id.qqfarm_edit_btn_save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et_name = (EditText)findViewById(R.id.qqfarm_edit_name);
				if ("".equals(et_name.getText().toString())){
					Toast.makeText(QQFarmEditActivity.this, R.string.str_error_noname, Toast.LENGTH_LONG).show();
					return;
				}
				EditText et_sid = (EditText)findViewById(R.id.qqfarm_edit_sid);
				if ("".equals(et_sid.getText().toString())){
					Toast.makeText(QQFarmEditActivity.this, R.string.str_error_nosid, Toast.LENGTH_LONG).show();
					return;
				}
				Map qqcard = new HashMap();
				qqcard.put("cWorkType", "2");
				qqcard.put("cName", getWidgetValue(R.id.qqfarm_edit_name));
				qqcard.put("csid", getWidgetValue(R.id.qqfarm_edit_sid));
				qqcard.put("cWorkid", getWidgetValue(R.id.qqfarm_edit_v_workid));
				Switch swit = (Switch)findViewById(R.id.qqfarm_edit_status);
				qqcard.put("iStatus", swit.isChecked()?"1":"0");
				wlm.saveQQCard(qqcard);
				Toast.makeText(QQFarmEditActivity.this, R.string.str_succ_save, Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.putExtra("msg", "succ");
				setResult(1, intent);
				finish();
			}
		});
		Button cancel = (Button)findViewById(R.id.qqfarm_edit_btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void loadWorkLine(String workid) {
		List<Map> workList = wlm.getWorkListByIds(workid);
		if (workList==null || workList.size()==0){
			return ;
		}
		Map workLine = workList.get(0);
		Switch swit = (Switch)findViewById(R.id.qqfarm_edit_status);
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
		this.setWidgetValue(R.id.qqfarm_edit_name, workLine.get("cName").toString());
		this.setWidgetValue(R.id.qqfarm_edit_sid, workLine.get("csid").toString());
		this.setWidgetValue(R.id.qqfarm_edit_v_workid, workLine.get("cWorkid").toString());
	}
	
	private String getPrefer(String cName, String sid){
		return wlm.getWorkPrefer("1", sid, cName);
	}
	
	private void setPrefer(String sid, String cName, String value){
		wlm.setWorkPrefer("1", sid, cName, value);
	}
	
	public String getWidgetValue(int resid){
		TextView tv = (TextView)findViewById(resid);
		return tv.getText().toString();
	}
	
	public void setWidgetValue(int resid, String value){
		TextView tv = (TextView)findViewById(resid);
		tv.setText(value);
	}

}
