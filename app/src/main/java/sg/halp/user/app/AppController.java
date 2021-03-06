package sg.halp.user.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.exceptions.RealmMigrationNeededException;
import sg.halp.user.R;
import sg.halp.user.Test.NullHostNameVerifier;
import sg.halp.user.Test.NullX509TrustManager;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ForegroundListner;
import sg.halp.user.Utils.LruBitmapCache;


public class AppController extends Application implements IAppController{


    public static final String TAG = AppController.class.getSimpleName();
    private static double total = 0;
    public static ArrayList<Menu> menuListGlobal = new ArrayList<Menu>();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private LruBitmapCache mLruBitmapCache;
    private SSLSocketFactory sf;
    private SSLContext sslContext;

    private static AppController mInstance;

    public static double getTotal() {
        return total;
    }

    public static void setTotal(double total) {
        AppController.total = total;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        configSSL();
        mInstance = this;
        overrideDefaultFont(AppController.this, "SERIF");
        configRealm();

        ForegroundListner.init(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void overrideDefaultFont(Context context, String staticTypefaceFieldName){

        final Typeface newFont = ResourcesCompat.getFont(context, R.font.proximanova_regular);

        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);

            staticField.setAccessible(true);
            staticField.set(null, newFont);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configSSL() {
        CertificateFactory cf = null;
        try {

            cf = CertificateFactory.getInstance("X.509");

        } catch (CertificateException e) {

            e.printStackTrace();

        }

        // Generate the certificate using the certificate file under res/raw/cert.cer
        InputStream caInput = new BufferedInputStream(getResources().openRawResource(R.raw.halp));
        Certificate ca = null;

        try {
            assert cf != null;
            ca = cf.generateCertificate(caInput);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            caInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;

        try {

            keyStore = KeyStore.getInstance(keyStoreType);

        } catch (KeyStoreException e) {

            e.printStackTrace();

        }

        try {

            assert keyStore != null;
            keyStore.load(null, null);

        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {

            e.printStackTrace();

        }

        try {

            keyStore.setCertificateEntry("ca", ca);

        } catch (KeyStoreException e) {

            e.printStackTrace();

        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;

        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert tmf != null;
            tmf.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // Create an SSLContext that uses our TrustManager
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert sslContext != null;
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                if (!hostname.equalsIgnoreCase(Const.ServiceType.HOST_URL)){

                    return true;

                }else {

                    return false;

                }
            }
        });

        try {
            sslContext.init(null, new X509TrustManager[]{new NullX509TrustManager()}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        sf = sslContext.getSocketFactory();
    }

    @Override
    public void configRealm() {

        try{
            RealmConfiguration realmConfiguration = new RealmConfiguration
                    .Builder(this)
                    .name(Realm.DEFAULT_REALM_NAME)
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .migration(new RealmMigration() {
                        @Override
                        public long execute(Realm realm, long version) {
                            return 0;
                        }
                    })
                    .build();

            Realm.setDefaultConfiguration(realmConfiguration);

        }catch (RealmMigrationNeededException e){

            e.printStackTrace();
            EbizworldUtils.appLogError("HaoLS", "RealmMigrationNeededException: " + e.toString());

        }

    }

}

