package com.myst3ry.moneytrackerxe;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.myst3ry.moneytrackerxe.api.AuthResult;

public class AuthActivity extends AppCompatActivity {

    private static final int LOADER_AUTH = 0;
    public static final int RCODE_SIGN_IN = 23;

    private ImageButton authButton;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        authButton = (ImageButton) findViewById(R.id.auth_button);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), RCODE_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RCODE_SIGN_IN) {
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess() && result.getSignInAccount() != null) {
                final GoogleSignInAccount account = result.getSignInAccount();
                getSupportLoaderManager().restartLoader(LOADER_AUTH, null, new LoaderManager.LoaderCallbacks<AuthResult>() {

                    @Override
                    public Loader<AuthResult> onCreateLoader(int id, Bundle args) {
                        return new AsyncTaskLoader<AuthResult>(AuthActivity.this) {

                            @Override
                            public AuthResult loadInBackground() {
                                try {
                                    return ((LSApp) getApplication()).getApi().auth(account.getId()).execute().body();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }
                        };
                    }

                    @Override
                    public void onLoadFinished(Loader<AuthResult> loader, AuthResult data) {
                        if (data != null && data.isSuccessful()) {
                            ((LSApp) getApplication()).editAuthToken(data.getAuthToken());
                            finish();
                        } else {
                            Toast.makeText(AuthActivity.this, getString(R.string.auth_error, data.getStatus()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<AuthResult> loader) {
                    }
                }).forceLoad();

            } else {
                Toast.makeText(AuthActivity.this, getString(R.string.auth_error, result.getStatus()), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
