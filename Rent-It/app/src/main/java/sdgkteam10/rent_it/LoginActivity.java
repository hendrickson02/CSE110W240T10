package sdgkteam10.rent_it;

import android.animation.Animator;                  //  http://developer.android.com/reference/android/animation/Animator.html
import android.animation.AnimatorListenerAdapter;   //  http://developer.android.com/reference/android/animation/AnimatorListenerAdapter.html
import android.annotation.TargetApi;                //  http://developer.android.com/reference/android/annotation/TargetApi.html
import android.app.AlertDialog;                     //  http://developer.android.com/reference/android/app/AlertDialog.html
import android.content.Intent;                      //  http://developer.android.com/reference/android/content/Intent.html
import android.content.pm.PackageManager;           //  http://developer.android.com/reference/android/content/pm/PackageManager.html
import android.support.annotation.NonNull;          //  http://tools.android.com/tech-docs/support-annotations
import android.support.design.widget.Snackbar;      //  http://developer.android.com/reference/android/support/design/widget/Snackbar.html
import android.support.v7.app.AppCompatActivity;    //  http://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html
import android.app.LoaderManager.LoaderCallbacks;   //  http://developer.android.com/reference/android/app/LoaderManager.LoaderCallbacks.html

import android.content.CursorLoader;                //  http://developer.android.com/reference/android/content/CursorLoader.html
import android.content.Loader;                      //  http://developer.android.com/reference/android/content/Loader.html
import android.database.Cursor;                     //  http://developer.android.com/reference/android/database/Cursor.html
import android.net.Uri;                             //  http://developer.android.com/reference/android/net/Uri.html
import android.os.AsyncTask;                        //  http://developer.android.com/reference/android/os/AsyncTask.html

import android.os.Build;                            //  http://developer.android.com/reference/android/os/Build.html
import android.os.Bundle;                           //  http://developer.android.com/reference/android/os/Bundle.html
import android.provider.ContactsContract;           //  http://developer.android.com/reference/android/provider/ContactsContract.html
import android.text.TextUtils;                      //  http://developer.android.com/reference/android/text/TextUtils.html
import android.view.KeyEvent;                       //  http://developer.android.com/reference/android/view/KeyEvent.html
import android.view.View;                           //  http://developer.android.com/reference/android/view/View.html
import android.view.View.OnClickListener;           //  http://developer.android.com/reference/android/view/View.OnClickListener.html
import android.view.inputmethod.EditorInfo;         //  http://developer.android.com/reference/android/view/inputmethod/EditorInfo.html
import android.widget.ArrayAdapter;                 //  http://developer.android.com/reference/android/widget/ArrayAdapter.html
import android.widget.AutoCompleteTextView;         //  http://developer.android.com/reference/android/widget/AutoCompleteTextView.html
import android.widget.Button;                       //  http://developer.android.com/reference/android/widget/Button.html
import android.widget.EditText;                     //  http://developer.android.com/reference/android/widget/EditText.html
import android.widget.TextView;                     //  http://developer.android.com/reference/android/widget/TextView.html

import com.firebase.client.AuthData;                //  https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/AuthData.html

//for info on package com.firebase.client:   https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/package-summary.html

import com.firebase.client.Firebase;                        //  https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/Firebase.html
import com.firebase.client.FirebaseError;                   //  https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/FirebaseError.html
import com.google.android.gms.appindexing.Action;           //  https://developers.google.com/android/reference/com/google/android/gms/appindexing/Action
import com.google.android.gms.appindexing.AppIndex;         //  https://developers.google.com/android/reference/com/google/android/gms/appindexing/AppIndex
import com.google.android.gms.common.api.GoogleApiClient;   //  https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient

import java.util.ArrayList;                                 //  https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
import java.util.HashMap;                                   //  https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html
import java.util.List;                                      //  https://docs.oracle.com/javase/8/docs/api/java/util/List.html
import java.util.Map;                                       //  https://docs.oracle.com/javase/7/docs/api/java/util/Map.html

import static android.Manifest.permission.READ_CONTACTS;
import static sdgkteam10.rent_it.R.id.button_signin;

/**
 * A login screen that offers login via email/password.
 *
 * AppCombatActivity - base class for activities that use the support library action bar features
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Firebase database reference
     */
    private Firebase myFirebaseRef;

    private User user;

    //TODO: go directly to home page if still logged in
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.button_signin);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button buttonRegister = (Button)findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    public void goRegister(View v) {
        Intent intent;
        intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        //log out anyone previously logged in
        //myFirebaseRef.unauth();

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://sdgkteam10.rent_it/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://sdgkteam10.rent_it/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://sdgkteam10.rent_it/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://sdgkteam10.rent_it/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private Firebase.AuthResultHandler mHandler;
        private FirebaseError mLoginError;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            mLoginError = null;

            //log out anyone previously logged in
            myFirebaseRef.unauth();

            mHandler = new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    //store user state in Firebase -- used for login persistence
                     //user = new User(authData, getApplicationContext());

                    //Map<String, String> map = new HashMap<String, String>();
                    //map.put(getResources().getString(R.string.firebase_provider), authData.getProvider());
                    //map.put(getResources().getString(R.string.firebase_email),
                    //        authData.getProviderData().get(getResources().getString(R.string.firebase_email)).toString());

                    //myFirebaseRef.child("users").child(authData.getUid()).setValue(map);


                   // mLoginError = null;
                }

                @Override
                public void onAuthenticationError(FirebaseError error) {
                    mLoginError = error;
                }
            };
        }

        /**
         * Performs user authentication in the background.
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            //authenticate user login info with the database
            //myFirebaseRef.authWithPassword(mEmail, mPassword, mHandler);
            user = new User(mEmail, mPassword, getApplicationContext());

            try {
                // Simulate network access.
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }

            //TODO: change to user.isLoggedIn();
            AuthData userData = myFirebaseRef.getAuth();

            //user successfully logged in
            //userData.getProviderData().get(getResources().getString(R.string.firebase_email)).toString() == mEmail)
            if (userData != null) //&& mLoginError == null)
                return true;
            else
                return false;
        }

        /**
         * Called once doInBackground is complete. Either indicates incorrect user
         * information, or starts the main page's activity.
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            //super.onPostExecute(success);

            showProgress(false);

            if (success) {
                //load the main page
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                mAuthTask = null;
                finish();

            } else {
                //check the error message thrown by Firebase
                if (mLoginError != null)
                {
                    switch (mLoginError.getCode()) {
                        //handle invalid email
                        case FirebaseError.USER_DOES_NOT_EXIST:
                        case FirebaseError.INVALID_EMAIL:
                            mEmailView.setError(getString(R.string.error_invalid_email));
                            mEmailView.requestFocus();
                            break;
                        //handle invalid password
                        case FirebaseError.INVALID_PASSWORD:
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            break;
                        default:
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Error")
                                    .setMessage("An error occurred when attempting to login.\n\n" +
                                            "Please try again.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setCancelable(true)
                                    .show();
                            break;
                    }
                }
                else
                {
                    //TODO: only for debugging
                    mEmailView.setText("Something went wrong");
                }
                mAuthTask = null;
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

