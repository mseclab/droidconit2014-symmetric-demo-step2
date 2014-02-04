package com.mseclab.droidconit2014.symmetricdemostep2;

import java.security.Provider;
import java.security.Security;

import com.mseclab.droidconit2014.symmetricdemostep1.R;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView outView;

	private final static String TAG = "DROIDCONIT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outView = (TextView) findViewById(R.id.out_view);

		// Set Action Bar Title
		getActionBar().setTitle(R.string.action_bar_title);
		getActionBar().setSubtitle(R.string.action_bar_subtitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_discard:
			outView.setText("");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onShowProvidersClick(View view) {
		Provider[] providers = Security.getProviders();
		for (Provider provider : providers) {
			debug("Provider: " + provider.getName());
			debug("Version : " + Double.toString(provider.getVersion()));
			debug("Info    : " + provider.getInfo());
			debug("N. Services : " + Integer.toString(provider.getServices().size()));
			debug("");
		}
	}

	public void onShowSCServicesClick(View view) {
		Provider spongyCastle = Security.getProvider("SC");
		if (spongyCastle == null) {
			debug("Spongy Castle Provider not available!");
			return;
		}

		debug("Spongy Castle Services:");
		for (Provider.Service service : spongyCastle.getServices())
			debug("- " + service.getAlgorithm());

	}

	private void debug(String message) {
		Log.v(TAG, message);
		outView.append(message + "\n");
	}

}
