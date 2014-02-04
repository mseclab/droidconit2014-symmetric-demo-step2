package com.mseclab.droidconit2014.symmetricdemostep2;

import java.security.Security;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import android.app.Application;

public class MainApplication extends Application {

	static {
		Security.insertProviderAt(new BouncyCastleProvider(), 1);
	}

}
