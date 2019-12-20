// the users starts with this page. if they are not logged in,they need to.
// they can sign in with gmail or register with normal account.at both gmail and register buttons,
//the user goes to the RegisterActivity.In login button,the user goes to the LoginActivity.
//If the user is already exists, then they go to the MainActivity.

package com.example.appapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class StartingActivity extends AppCompatActivity {
    SignInButton signInButton;
    Button loginButton,registerButton;
    FirebaseAuth mAuth ;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //overrided method for google sign-in.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account !=null;
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                Log.w( "Google sign in failed", e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        signInButton=findViewById(R.id.signinButton);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
         mAuth=FirebaseAuth.getInstance();

         if(mAuth.getCurrentUser()!=null){
             Intent intent= new Intent(getApplicationContext(),MainActivity.class);
             startActivity(intent);}


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            signIn();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void signIn() {
        //method for google sign-in
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //method for google sign-in
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent= new Intent(getApplicationContext(), RegisterActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"some problems has occurred",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


}
