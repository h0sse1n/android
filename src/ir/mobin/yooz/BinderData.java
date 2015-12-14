package ir.mobin.yooz;

import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BinderData extends ArrayAdapter {

	static final String TITLE = "title";
	static final String SNIPPET = "snippet";
	static final String URL = "url";
	private int selectedIndex;

	LayoutInflater inflater;
	ImageView thumb_image;
	List<Map<String, String>> result;
	ViewHolder holder;
	private Boolean firstTimeStartup = true;

	public BinderData(Activity act, List<Map<String, String>> map) {
		super(act, 0 , map);
		this.result = map;

		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		 selectedIndex = -1;
	}
	
	 public void setSelectedIndex(int ind)
	    {
	        selectedIndex = ind;
	        notifyDataSetChanged();
	    }

	public int getCount() {
		return result.size();
	}

	public Map<String, String> getItem(int arg0) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.mylist, null);
			holder = new ViewHolder();

			holder.title = (TextView) vi.findViewById(R.id.item); 
			holder.snippet = (TextView) vi.findViewById(R.id.txtSnippet); 
			holder.url = (TextView) vi.findViewById(R.id.txtUrl);
			vi.setTag(holder);
		} else {

			holder = (ViewHolder) vi.getTag();
		}
		

		holder.title.setText(Html.fromHtml(result.get(position).get(TITLE)));
		holder.snippet.setText(Html.fromHtml(result.get(position).get(SNIPPET)));
		holder.url.setText(Html.fromHtml(result.get(position).get(URL)));

		return vi;
	}

	/*
	 * 
	 * */
	static class ViewHolder {

		TextView title;
		TextView snippet;
		TextView url;
	}

}
