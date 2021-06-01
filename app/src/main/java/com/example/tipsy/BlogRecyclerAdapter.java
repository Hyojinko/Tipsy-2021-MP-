//커뮤니티 블로그 recycler 클라스

package com.example.tipsy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import androidx.annotation.NonNull;

import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.tipsy.BlogPost;
import com.example.tipsy.CommentsActivity;
import com.example.tipsy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BlogRecyclerAdapter(List<BlogPost> blog_list){

        this.blog_list = blog_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        final String blogPostId = blog_list.get(position).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String user_id = blog_list.get(position).getUser_id();

//블로그 포스트 이미지 불러오기
        StorageReference storageRef2= FirebaseStorage.getInstance().getReferenceFromUrl("gs://tipsy-4b38d.appspot.com").child("post_images");
        if(storageRef2==null){
            Toast.makeText(context, "사진이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference submitProfile=storageRef2.child(blog_list.get(position).getImage_name()+".jpg");
            StorageReference submitProfile2=storageRef2.child("thumbs").child(blog_list.get(position).getImage_name()+".jpg");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri image_uri=uri;
                    submitProfile2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri thumb_uri=uri;
                            holder.setBlogImage(image_uri,thumb_uri);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

//블로그 글을 쓴 사람의 이미지 가져오기
        StorageReference storageRef= FirebaseStorage.getInstance().getReferenceFromUrl("gs://tipsy-4b38d.appspot.com").child("profile_images");
        if(storageRef==null){
            Toast.makeText(context, "사진이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference submitProfile=storageRef.child(user_id+".jpg");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri userImage=uri;
                    holder.setUserimage(userImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
//블로그 쓴 사람의 닉네임 가져오기
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String userName = task.getResult().getString("name");
//                    String userImage = task.getResult().getString("image");
                    holder.setUsername(userName);


                } else {

                    //Firebase Exception

                }

            }
        });

//블로그 글의 작성 시간 가져오기
        try {
            long millisecond = blog_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

//블로그 글의 좋아요 개수 나타내기
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();

                    holder.updateLikesCount(count);

                } else {

                    holder.updateLikesCount(0);

                }

            }
        });

//블로그 글의 좋아요 개수 가져오기
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){

                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));

                } else {

                    holder.blogLikeBtn.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));

                }

            }
        });

//블로그 글의 좋아요 클릭시 이벤트 처리
        holder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).delete();

                        }

                    }
                });
            }
        });

//블로그 글의 댓글 클릭시 댓글 액티비티로 이동
        holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });

//블로그 글의 댓글 개수 나타내기
        firebaseFirestore.collection("Posts/" + blogPostId + "/Comments").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();

                    holder.updateCommentsCount(count);

                } else {

                    holder.updateCommentsCount(0);

                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserImage;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;

        private ImageView blogCommentBtn;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);

        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);

        }

        public void setBlogImage(Uri downloadUri, Uri thumbUri){

            blogImageView = mView.findViewById(R.id.blog_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);


        }

        public void setTime(String date) {

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }

        public void setUsername(String name){

            blogUserName = mView.findViewById(R.id.blog_user_name);

            blogUserName.setText(name);


        }
        public void setUserimage(Uri image){

            blogUserImage = mView.findViewById(R.id.blog_user_image);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);
//            Picasso.get().load(image).into(blogUserImage);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(blogUserImage);

        }

        public void updateLikesCount(int count){

            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count + " 좋아요");

        }
        public void updateCommentsCount(int count){

            blogLikeCount = mView.findViewById(R.id.blog_comment_count);
            blogLikeCount.setText(count + " 댓글");

        }

    }

}

