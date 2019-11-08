package org.appspot.apprtc.signalling;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

public class Connection extends AsyncTask<Void, Void, Void> {
    private XMPPTCPConnectionConfiguration configuration;
    private AbstractXMPPConnection connection;

    private String host = "ckotha.com";
    X509TrustManager x509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            Log.d(TAG, "checkClientTrusted: " + s);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            Log.d(TAG, "checkServerTrusted: " + s);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    Connection(String username, String password) {
        try {
            configuration = getConfiguration(username, password);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        connection = new XMPPTCPConnection(configuration);
        connection.addConnectionListener(connectionListener);
    }

    private XMPPTCPConnectionConfiguration getConfiguration(String username, String password) throws XmppStringprepException {
        return XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setXmppDomain(host)
                .setCustomX509TrustManager(x509TrustManager)
                .setHostnameVerifier((s, sslSession) -> true)
                .build();
    }
//    get xmppconnection instance
    public AbstractXMPPConnection getConnection() throws IllegalAccessException{
        if (connection.isAuthenticated())
            return connection;
        throw new IllegalAccessException();
    }

    private ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection1) {
            Log.i(TAG, "connected: successful");
            try {
                connection.login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean resumed) {
            Log.i(TAG, "authenticated: successful");
        }

        @Override
        public void connectionClosed() {
            Log.i(TAG, "connectionClosed: connection closed successfully");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log.e(TAG, "connectionClosedOnError: ", e);
        }
    };


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
