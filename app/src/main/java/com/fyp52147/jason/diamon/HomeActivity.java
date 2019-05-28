package com.fyp52147.jason.diamon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mUsers, mAdmin;
    Button btnAddLog, btnAddMeal;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAdmin = FirebaseDatabase.getInstance().getReference().child("Admin");

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                else
                {
                    checkUser();
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MailActivity.class));
            }
        });

        btnAddLog = findViewById(R.id.buttonAddLog);
        btnAddMeal = findViewById(R.id.buttonAddMeal);

        btnAddLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddLogActivity.class));
            }
        });

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddMealActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkUser() {
         final String current_UID = mAuth.getCurrentUser().getUid();

         mUsers.addListenerForSingleValueEvent (new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (!dataSnapshot.hasChild(current_UID)) {
                     mAdmin.addListenerForSingleValueEvent (new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot ds) {
                             if (!ds.hasChild(current_UID)) {
                                 startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                             }
                             else if (ds.hasChild(current_UID)){
                                 startActivity(new Intent(HomeActivity.this, MainActivity.class));
                             }
                         }
                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {
                         }
                     });
                 }
                 else {

                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
         });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent startIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(startIntent);
        }
        else if (id == R.id.action_signout) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loginActivity() {
        Intent login = new Intent (HomeActivity.this, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent startIntent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(startIntent);
        }else if (id == R.id.nav_log) {
            Intent startIntent = new Intent(getApplicationContext(), LogbookActivity.class);
            startActivity(startIntent);
        } else if (id == R.id.nav_medication) {
            Intent startIntent = new Intent(getApplicationContext(), MedicationActivity.class);
            startActivity(startIntent);
        } else if (id == R.id.nav_diet) {
            Intent startIntent = new Intent(getApplicationContext(), MealActivity.class);
            startActivity(startIntent);
        } else if (id == R.id.nav_calculator) {
            Intent startIntent = new Intent(getApplicationContext(), CalculatorActivity.class);
            startActivity(startIntent);
        } else if (id == R.id.nav_education) {
            Intent startIntent = new Intent(getApplicationContext(), EducationActivity.class);
            startActivity(startIntent);
        } else if (id == R.id.nav_send) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi Doctor, I need help");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
