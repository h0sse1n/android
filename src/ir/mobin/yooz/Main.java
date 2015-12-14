package ir.mobin.yooz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Main extends ActionBarActivity {

	RelativeLayout r;
	ImageButton ib;
	ImageView arm;
	EditText searchText;
	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	Button btn5;
	Button btn6;
	Button btn7;
	Button btn8;
	Button btn9;
	boolean isConnected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().hide();

		r = (RelativeLayout) findViewById(R.id.relativeLayout1);
		ib = (ImageButton) findViewById(R.id.imageButton1);
		arm = (ImageView) findViewById(R.id.imageView2);
		searchText = (EditText) findViewById(R.id.edit_txt);
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.Button02);
		btn3 = (Button) findViewById(R.id.Button03);
		btn4 = (Button) findViewById(R.id.Button04);
		btn5 = (Button) findViewById(R.id.Button05);
		btn6 = (Button) findViewById(R.id.Button06);
		btn7 = (Button) findViewById(R.id.Button07);
		btn8 = (Button) findViewById(R.id.Button08);
		btn9 = (Button) findViewById(R.id.Button09);

		btnClickListner(btn1);
		btnClickListner(btn2);
		btnClickListner(btn3);
		btnClickListner(btn4);
		btnClickListner(btn5);
		btnClickListner(btn6);
		btnClickListner(btn7);
		btnClickListner(btn8);
		btnClickListner(btn9);

		isConnected = isNetworkAvailable();

		if (!isConnected) {
			Toast.makeText(getApplicationContext(), "عدم دسترسی به اینترنت",
					Toast.LENGTH_LONG).show();
		}

		ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (isConnected) {
					Intent intent = new Intent(getApplicationContext(),
							SearchPage.class);
					intent.putExtra("searchItem", searchText.getText()
							.toString());
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"عدم دسترسی به اینترنت", Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	private void btnClickListner(final Button button) {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isConnected) {
					Intent intent = new Intent(getApplicationContext(),
							SearchPage.class);
					intent.putExtra("searchItem", button.getText());
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"عدم دسترسی به اینترنت", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	/*
	 * private class HttpAsyncTask extends AsyncTask<String, Void, String> {
	 * 
	 * @Override protected String doInBackground(String... urls) {
	 * 
	 * return GET(urls[0]); } // onPostExecute displays the results of the
	 * AsyncTask.
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
	 * etResponse.setText(result); } }
	 */

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
