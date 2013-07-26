package com.kqhelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.kqhelper.model.SpinnerItem;

public class QQCardEditActivity extends Activity {
	
	private WorkListManager wlm;
	
	private ServiceMessageReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wlm = new WorkListManager(this);
		setContentView(R.layout.activity_qqcard_edit);
		initBtn();
		initCardSuitList();
		initReceiver();
		Intent intent = getIntent();
		if ("edit".equalsIgnoreCase(intent.getStringExtra("action"))){
			loadWorkLine(intent.getStringExtra("cWorkid"));
		}
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(1000);
		intentFilter.addAction("com.kqhelper.message");
		receiver = new ServiceMessageReceiver();
		registerReceiver(receiver, intentFilter);
	}

	private void initCardSuitList() {
		Spinner cardsuit = (Spinner)findViewById(R.id.edit_sp_cardsuits);
		ArrayAdapter<SpinnerItem> sa = new ArrayAdapter<SpinnerItem>(this,  android.R.layout.simple_spinner_item);
		List<Map> cardsuitList = wlm.getAllCardSuit();
		if (cardsuitList==null || cardsuitList.size()==0){
			Toast.makeText(this, R.string.str_alert_no_cardInfo, Toast.LENGTH_LONG).show();
			return;
		}
		sa.add(new SpinnerItem("0","------"));
		for (Map cardsuitLine: cardsuitList){
			sa.add(new SpinnerItem(cardsuitLine.get("cThemeid").toString(),cardsuitLine.get("cName").toString()));
		}
		cardsuit.setAdapter(sa);
		Button refresh = (Button)findViewById(R.id.edit_btn_refresh);
		refresh.setClickable(true);
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
				qqcard.put("cWorkType", "1");
				qqcard.put("cName", getWidgetValue(R.id.edit_name));
				qqcard.put("csid", getWidgetValue(R.id.edit_sid));
				qqcard.put("cWorkid", getWidgetValue(R.id.edit_v_workid));
				Switch swit = (Switch)findViewById(R.id.edit_status);
				qqcard.put("iStatus", swit.isChecked()?"1":"0");
				wlm.saveQQCard(qqcard);
				Switch swit_steal = (Switch)findViewById(R.id.edit_steal);
				setPrefer(getWidgetValue(R.id.edit_sid), "isSteal", swit_steal.isChecked()?"1":"0");
				Spinner cardsuit = (Spinner)findViewById(R.id.edit_sp_cardsuits);
				if (cardsuit.getSelectedItem()!=null){
					setPrefer(getWidgetValue(R.id.edit_sid), "smeltCard", ((SpinnerItem)cardsuit.getSelectedItem()).getId());
				}
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
		Button refresh = (Button)findViewById(R.id.edit_btn_refresh);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText et_sid = (EditText)findViewById(R.id.edit_sid);
				if (et_sid.getText().toString().equals("")){
					Toast.makeText(QQCardEditActivity.this, R.string.str_error_nosid, Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent();
				intent.setClass(QQCardEditActivity.this, QQHelperWorkerService.class);
				intent.putExtra("action", "qqcard.refreshCard");
				intent.putExtra("sid", et_sid.getText().toString());
				startService(intent);
				Toast.makeText(QQCardEditActivity.this, R.string.str_alert_refresh_card, Toast.LENGTH_LONG).show();
				v.setClickable(false);
			}
		});
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
		Switch sw_steal = (Switch)findViewById(R.id.edit_steal);
		if ("1".equals(getPrefer("isSteal",workLine.get("csid").toString()))){
			sw_steal.setChecked(true);
		}else{
			sw_steal.setChecked(false);
		}
		
		this.setWidgetValue(R.id.edit_name, workLine.get("cName").toString());
		this.setWidgetValue(R.id.edit_sid, workLine.get("csid").toString());
		this.setWidgetValue(R.id.edit_v_workid, workLine.get("cWorkid").toString());
		Spinner cardsuit = (Spinner)findViewById(R.id.edit_sp_cardsuits);
		String putCardSuit = getPrefer("smeltCard",workLine.get("csid").toString());
		Map cardSuit = wlm.getCardSuit(putCardSuit);
		if ("0".equals(putCardSuit)){
			setSpinner(cardsuit, "------");
		}else{
			setSpinner(cardsuit, cardSuit.get("cName")==null?"":cardSuit.get("cName").toString());
		}
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

	private void setSpinner(Spinner worktype, String item) {
		ArrayAdapter<SpinnerItem> aa = (ArrayAdapter<SpinnerItem>)worktype.getAdapter();
		if (aa==null){
			return;
		}
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
	
	class ServiceMessageReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("qqcard.refresh".equalsIgnoreCase(intent.getStringExtra("messageType"))){
				qqcardRefresh(context, intent);
			}
		}

		private void qqcardRefresh(Context context, Intent intent) {
			initCardSuitList();
		}
	}

}
