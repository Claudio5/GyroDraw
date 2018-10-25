package ch.epfl.sweng.SDP.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.SDP.Account;
import ch.epfl.sweng.SDP.ConstantsWrapper;
import ch.epfl.sweng.SDP.R;
import ch.epfl.sweng.SDP.firebase.Database;
import ch.epfl.sweng.SDP.home.HomeActivity;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AccountCreationActivity extends AppCompatActivity {
    private EditText usernameInput;
    private Button createAcc;
    private TextView usernameTaken;
    private String username;
    private Account account;
    private View.OnClickListener createAccListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
        usernameInput = this.findViewById(R.id.usernameInput);
        createAcc = this.findViewById(R.id.createAcc);
        createAcc.setOnClickListener(createAccListener);
        usernameTaken = this.findViewById(R.id.usernameTaken);
        createAccListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccClicked();
            }
        };
        createAcc.setOnClickListener(createAccListener);
    }

    /**
     * Gets called when user entered username and clicked on create account.
     */
    public void createAccClicked() {
        username = usernameInput.getText().toString();
        if (username.isEmpty()) {
            usernameTaken.setText("Username must not be empty.");
        } else {
            account = new Account(new ConstantsWrapper(), username);
            try {
                Database.INSTANCE.getReference("users").orderByChild("username").equalTo(username)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    usernameTaken.setText("Username already taken, try again");
                                } else {
                                    account.registerAccount();
                                    getDefaultSharedPreferences(getThis()).edit()
                                            .putBoolean("hasAccount", true).apply();
                                    gotoHome();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        });
            } catch (Exception exception) {
                usernameTaken.setText(exception.getMessage());
            }
        }
    }

    /**
     * Important for function above.
     *
     * @return this
     */
    private AccountCreationActivity getThis() {
        return this;
    }

    /**
     * Calls HomeActivity.
     */
    public void gotoHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("account", this.account);
        startActivity(intent);
        finish();
    }
}