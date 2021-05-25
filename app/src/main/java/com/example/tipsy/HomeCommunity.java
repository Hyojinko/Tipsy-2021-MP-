package com.example.tipsy;


import android.content.Intent;
import android.nfc.Tag;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeCommunity extends AppCompatActivity {

//    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_user_id;

    private FloatingActionButton addPostBtn;
//    private BottomNavigationView mainbottomNav;
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;

    private BlogRecyclerAdapter blogRecyclerAdapter;

    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;

//    private HomeFragment homeFragment;
//    private NotificationFragment notificationFragment;
//    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        blog_list = new ArrayList<>();
        blog_list_view = findViewById(R.id.recycler_view);

        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(this));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        blog_list_view.setHasFixedSize(true);

//        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(mainToolbar);
//
//        getSupportActionBar().setTitle("Photo Blog");

        if(mAuth.getCurrentUser() != null) {
            blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if(reachedBottom){

                        loadMorePost();

                    }

                }
            });
            Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
            firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            blog_list.clear();

                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                                if (isFirstPageFirstLoad) {

                                    blog_list.add(blogPost);

                                } else {

                                    blog_list.add(0, blogPost);

                                }


                                blogRecyclerAdapter.notifyDataSetChanged();

                            }
                        }

                        isFirstPageFirstLoad = false;

                    }

                }

            });
//            mainbottomNav = findViewById(R.id.mainBottomNav);

            // FRAGMENTS
//            homeFragment = new HomeFragment();
//            notificationFragment = new NotificationFragment();
//            accountFragment = new AccountFragment();

//            initializeFragment();

//            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
//
//                    switch (item.getItemId()) {
//
//                        case R.id.bottom_action_home:
//
//                            replaceFragment(homeFragment, currentFragment);
//                            return true;
//
//                        case R.id.bottom_action_account:
//
//                            replaceFragment(accountFragment, currentFragment);
//                            return true;
//
//                        case R.id.bottom_action_notif:
//
//                            replaceFragment(notificationFragment, currentFragment);
//                            return true;
//
//                        default:
//                            return false;
//
//
//                    }
//
//                }
//            });
//

            addPostBtn = findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent newPostIntent = new Intent(HomeCommunity.this, NewPostActivity.class);
                    startActivity(newPostIntent);

                }
            });

        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){

            sendToLogin();

        } else {

            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(HomeCommunity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();

                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(HomeCommunity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }

    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.action_logout_btn:
//                logOut();
//                return true;
//
//            case R.id.action_settings_btn:
//
//                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
//                startActivity(settingsIntent);
//
//                return true;
//
//
//            default:
//                return false;
//
//
//        }
//
//    }
//
//    private void logOut() {
//
//
//        mAuth.signOut();
//        sendToLogin();
//    }
//
    private void sendToLogin() {

        Intent loginIntent = new Intent(HomeCommunity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }
    public void loadMorePost(){

        if(mAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                blog_list.add(blogPost);

                                blogRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                }
            });

        }

    }
//
//    private void initializeFragment(){
//
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//        fragmentTransaction.add(R.id.main_container, homeFragment);
//        fragmentTransaction.add(R.id.main_container, notificationFragment);
//        fragmentTransaction.add(R.id.main_container, accountFragment);
//
//        fragmentTransaction.hide(notificationFragment);
//        fragmentTransaction.hide(accountFragment);
//
//        fragmentTransaction.commit();
//
//    }
//
//    private void replaceFragment(Fragment fragment, Fragment currentFragment){
//
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if(fragment == homeFragment){
//
//            fragmentTransaction.hide(accountFragment);
//            fragmentTransaction.hide(notificationFragment);
//
//        }
//
//        if(fragment == accountFragment){
//
//            fragmentTransaction.hide(homeFragment);
//            fragmentTransaction.hide(notificationFragment);
//
//        }
//
//        if(fragment == notificationFragment){
//
//            fragmentTransaction.hide(homeFragment);
//            fragmentTransaction.hide(accountFragment);
//
//        }
//        fragmentTransaction.show(fragment);
//
//        //fragmentTransaction.replace(R.id.main_container, fragment);
//        fragmentTransaction.commit();
//
//    }

}

