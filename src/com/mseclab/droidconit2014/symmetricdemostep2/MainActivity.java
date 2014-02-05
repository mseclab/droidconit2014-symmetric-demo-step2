package com.mseclab.droidconit2014.symmetricdemostep2;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.mseclab.droidconit2014.symmetricdemostep2.R;

import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView outView;
	private EditText mInData;
	private EditText mOutData;

	private final static String TAG = "DROIDCONIT";
	private final static String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	private final static String KEY = "HelloDroidconItK";

	private static final byte[] IV = "1234567890abcdef".getBytes();

	private static SecretKeySpec skeySpec = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outView = (TextView) findViewById(R.id.out_view);
		mInData = (EditText) findViewById(R.id.inDataText);
		mOutData = (EditText) findViewById(R.id.outDataText);

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

	public boolean isKeyReady() {
		if (skeySpec == null)
			skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
		return true;
	}

	public void onEncryptClick(View view) {
		byte[] input = mInData.getText().toString().getBytes();
		byte[] output = cipherData(Cipher.ENCRYPT_MODE, input);

		if (output != null) {
			String outputBase64 = Base64.encodeToString(output, Base64.DEFAULT);
			mOutData.setText(outputBase64);
		}

	}

	
	public void onDecryptClick(View view) {
		byte[] input = Base64.decode(mOutData.getText().toString().getBytes(), Base64.DEFAULT);
		byte[] output = cipherData(Cipher.DECRYPT_MODE, input);

		if (output != null) {
			mInData.setText(new String(output));
		}

	}

	private byte[] cipherData(int opMode, byte[] input) {
		if (!isKeyReady()) {
			debug("Key not ready...");
			return null;
		}

		// Get Cipher Instance
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(TRANSFORMATION);
		} catch (NoSuchAlgorithmException e) {
			debug("Algorithm not available");
			return null;
		} catch (NoSuchPaddingException e) {
			debug("Padding not available");
			return null;
		}

		// Init cipher
		try {
			cipher.init(opMode, skeySpec, new IvParameterSpec(IV));
		} catch (InvalidKeyException e) {
			debug("Key not valid: " + e.getMessage());
			return null;
		} catch (InvalidAlgorithmParameterException e) {
			debug("Cipher Algorithm parameters not valid: " + e.getMessage());
			return null;
		}

		// Encrypt data

		byte[] encryptedText;
		try {
			encryptedText = cipher.doFinal(input);
		} catch (IllegalBlockSizeException e) {
			debug("Illegal block size: " + e.getMessage());
			return null;
		} catch (BadPaddingException e) {
			debug("Bad paggind exception: " + e.getMessage());
			return null;
		}
		return encryptedText;
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
