package ir.mobin.yooz;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class CustomListAdaptor extends ArrayAdapter<String> {
 
	private final Activity context;
	private final List<Map<String, String>> itemname;
	
	@SuppressWarnings("unchecked")
	public CustomListAdaptor(Activity context, List itemname) {
		super(context, R.layout.mylist, itemname);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.itemname=itemname;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.mylist, null,true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
//		TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
		
		txtTitle.setText(itemname.get(position).get("title"));
//		extratxt.setText(itemname.get(position).get("snippet"));
		return rowView;
		
	};
}