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
import android.content.Context;
import android.os.StrictMode;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

public class EndlessScrollListner implements OnScrollListener {

    private int visibleThreshold = 10;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    static Context context;
    List<Map<String, String>> dataList;
    static String query;
    int s = 0;
    static ListView listView;
    static BinderData bindingData;
    
    
    public EndlessScrollListner() {
    }
    public EndlessScrollListner(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    @SuppressLint("ShowToast")
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

        	s += 10;
        	String qq = getStringDataFromJSON(query);
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
//				Toast.makeText(getApplicationContext(), e.toString(),
//						Toast.LENGTH_LONG).show();
			}
			
			if (dataList != null && dataList.size() > 0) {
				for (int i = 0; i < dataList.size(); i++) {
					bindingData.add(dataList.get(i));
				}		
		}
		bindingData.notifyDataSetChanged();
        	
        	
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
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
    
    
}