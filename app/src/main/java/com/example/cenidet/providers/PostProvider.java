
package com.example.cenidet.providers;

import android.widget.Toast;

import com.example.cenidet.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostProvider {

    CollectionReference mCollection;

    public PostProvider(){

        mCollection = FirebaseFirestore.getInstance().collection("Posts");
    }
    public Task<Void> save(Post post){
        return mCollection.document().set(post);
    }

    public Query getAll(){
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getPostByCategoryAndTimeStamp(String category){
        return mCollection.whereEqualTo("category", category).orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getPostByTitle(String title){
        return mCollection.orderBy("title").startAt(title).endAt(title+'\uf8ff');
    }

    public Query getPostByUser(String id){
        return mCollection.whereEqualTo("idUser", id);
    }
    public Query getPostByUserandTimestamp(String id){
        return mCollection.whereEqualTo("idUser", id).orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getPostById(String id){
        return mCollection.document(id).get();
    }

    public Task<Void> delete(String id ){
        return mCollection.document(id).delete();
    }

    public Query getPostTipoCuenta(String tipocuenta){
        return mCollection.whereEqualTo(tipocuenta, true).orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getPostByCategoryAndTipocuenta(String category, String tipocuenta){
        return mCollection.whereEqualTo("category", category).whereEqualTo(tipocuenta, true).orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getPostByTitleTipocuenta(String tipocuenta, String title){
        return mCollection.whereEqualTo(tipocuenta, true).orderBy("title").startAt(title).endAt(title+'\uf8ff');
    }
}
