package com.example.tipsy;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

class BlogPostId {
    @Exclude
    String BlogPostId;
    @SuppressWarnings (value="unchecked")

    <T extends BlogPostId> T withId(@NonNull final String id) {
        this.BlogPostId = id;

        return (T) this;
    }

}
