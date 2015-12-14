package ir.mobin.yooz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchPage extends ActionBarActivity {

	EditText textBox;
	ListView listView;
	ImageButton searchBtn;
	int s = 0;
	BinderData bindingData;
	int x = 0;
	int z =0;
	
	boolean itemClicked = false;
	
	EditText text;
	
	private int visibleThreshold = 10;
	private int currentPage = 0;
	private int previousTotal = 0;
	private boolean loading = true;

	boolean loadingMore = false;
	List<Map<String, String>> dataList;
	Activity mainActivity;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		getSupportActionBar().hide();
		mainActivity = this;
		textBox = (EditText) findViewById(R.id.s_txt);
		listView = (ListView) findViewById(R.id.search_list);
		searchBtn = (ImageButton) findViewById(R.id.searchBtn);
		text = (EditText) findViewById(R.id.item);
		Bundle bundle = getIntent().getExtras();
		String query = bundle.getString("searchItem");
		textBox.setText(query);

		View footerView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.loading_view, null, false);
		this.listView.addFooterView(footerView);

		getData(textBox.getText().toString());

		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!(textBox.getText().equals("")) ) {					
					s = 0;
					getData(textBox.getText().toString());
				}
			}
		});

	}

	@SuppressLint("NewApi")
	private void getData(String query) {
		String result = getStringDataFromJSON(query);
		try {
			JSONObject jObj = new JSONObject(result);
			JSONObject jsonArray = jObj.getJSONObject("data");
			JSONArray js = jsonArray.getJSONArray("results");
			dataList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < js.length(); i++) {
				JSONObject json = js.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("title", json.getString("title"));
				map.put("snippet", json.getString("snippet"));
				map.put("url", json.getString("url"));
				map.put("domain", json.getString("domain"));
				dataList.add(map);
			}
			bindingData = new BinderData(this, dataList);
			listView.setAdapter(bindingData);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
		    	
		        if (loading) {
		            if (totalItemCount > previousTotal) {
		                loading = false;
		                previousTotal = totalItemCount;
		                currentPage++;
		            }
		        }
		        
		        if ((lastInScreen == totalItemCount) && !(loading)) {
					Thread thread = new Thread(runn);
					thread.start();
					loading = true;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {	
				// TODO Auto-generated method stub
			}

		});
		
//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				
//				if (itemClicked) {
//					itemClicked = !itemClicked;
//					arg1.setBackgroundColor(Color.parseColor("#FAE4D7"));
//				}else{
//					arg1.setBackgroundColor(Color.parseColor("#FFFFFF"));
//					itemClicked = !itemClicked;
//					
//				}
//				
//			}
//		});

		
		textBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH && !(textBox.getText().equals(""))) {
		        	s = 0;
					getData(textBox.getText().toString());
		            return true;
		        }
		        return false;
		    }
		});
		
	}

	private Runnable runn = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			s += 10;
			String qq = getStringDataFromJSON(textBox.getText().toString());
			try {
				JSONObject jObj = new JSONObject(qq);
				JSONObject jsonArray = jObj.getJSONObject("data");
				JSONArray js;
				js = jsonArray.getJSONArray("results");
				 dataList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < js.length(); i++) {
					JSONObject json = js.getJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("title", json.getString("title"));
					map.put("snippet", json.getString("snippet"));
					map.put("url", json.getString("url"));
					map.put("domain", json.getString("domain"));
					dataList.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.toString(),
						Toast.LENGTH_LONG).show();
			}			
			runOnUiThread(returnRes);
		}
	};

	private Runnable returnRes = new Runnable() {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			if (dataList != null && dataList.size() > 0) {
					for (int i = 0; i < dataList.size(); i++) {
						bindingData.add(dataList.get(i));
					}		
			}
			bindingData.notifyDataSetChanged();
		}

	};

	@SuppressLint("NewApi")
	private String getStringDataFromJSON(String query) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String url = "http://yooz.ir/rest?s=" + s + "&q=" + query;

		String result = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(new HttpGet(url));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "utf-8"));
			result = reader.readLine();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void onBackPressed() {
		finish();
		startActivity(new Intent(SearchPage.this, Main.class));
		super.onBackPressed();
	}

}
