//댓글 recycler 클라스
package com.example.tipsy;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    //댓글 리스트 가져오기
    public CommentsRecyclerAdapter(List<Comments> commentsList){

        this.commentsList = commentsList;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        //position key에 해당하는 commenslist가져오기
        String commentMessage = commentsList.get(position).getMessage();
        String user_id = commentsList.get(position).getUser_id();
        //댓글 내용 가져오기
        holder.setComment_message(commentMessage);
        //댓글 작성한 사람의 닉네임 가져오기
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String userName = task.getResult().getString("name");
                    holder.setUserName(userName);


                } else {

                    //Firebase Exception

                }

            }
        });
        //댓글 작성자의 프로필 사진 가져오기
        StorageReference storageRef= FirebaseStorage.getInstance().getReferenceFromUrl("gs://tipsy-4b38d.appspot.com").child("profile_images");
        if(storageRef==null){
            Toast.makeText(context, "사진이 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
        }else {
            StorageReference submitProfile = storageRef.child(user_id + ".jpg");
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri userImage = uri;
                    holder.setUserImage(userImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

//댓글 갯수
    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView commentUserName;
        private CircleImageView commentUserImage;

        private TextView comment_message;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        //댓글 메세지 표시
        public void setComment_message(String message) {

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }

        //댓글 닉네임 표시
        public void setUserName(String name) {

            commentUserName = mView.findViewById(R.id.comment_username);

            commentUserName.setText(name);


        }
        //댓글 작성자 프로필 사진 표시
        public void setUserImage(Uri image) {

            commentUserImage = mView.findViewById(R.id.comment_image);
            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(image).into(commentUserImage);

        }
    }
}

