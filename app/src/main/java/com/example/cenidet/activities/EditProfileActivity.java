package com.example.cenidet.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.models.User;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.ImageProvider;
import com.example.cenidet.providers.UsersProvider;
import com.example.cenidet.utils.FileUtil;
import com.example.cenidet.utils.ViewedMessageHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewCover;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputMatricula;
    Button mButtonEditProfile;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE_PROFILE = 1;
    private final int GALLERY_REQUEST_CODE_COVER = 2;
    private final int PHOTO_REQUEST_CODE_PROFILE = 3;
    private final int PHOTO_REQUEST_CODE_COVER = 4;

    //PHOTO 1
    String mAbsolutePhotoPath;
    String mPhotopath;
    File mPhotoFile;
    //PHOTO 2
    String mAbsolutePhotoPath2;
    String mPhotopath2;
    File mPhotoFile2;

    File mImageFile;
    File mImageFile2;

    String mUsername ="";
    String mMatricula ="";
    String mImageProfile ="";
    String mImageCover ="";
    String mTipoCuenta = "";

    AlertDialog mDialogo;

    ImageProvider mImageProvider;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    LinearLayout mLLMatricula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mCircleImageViewBack = findViewById(R.id.cricleimageback);
        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mTextInputUsername = findViewById(R.id.textInputUsername2);
        mTextInputMatricula = findViewById(R.id.textInputMatricula);
        mButtonEditProfile = findViewById(R.id.btnEditProfile);
        mLLMatricula = findViewById(R.id.linearLayoutMatricula);

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Seleciona una opcion");
        options = new CharSequence[]{"Imagen de galeria", "Tomar foto"};

        mImageProvider = new ImageProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        mDialogo = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEditProfile();
            }
        });

        mCircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(1);
            }
        });

        mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUser();
    }

    /*CLAVE PARA LLAMAR INFORMACION EN LA BASE DE DATOS!!!!!!!!!!!

    lO QUE HACE ES CON EL ID QUE YA TIENE ALMACENADO O MANDA A LLAMAR CON EL GET ID, COMPRUEBE SI EXISTE
    Y POSTERIORMENTE MANDAS A TRAER DATOS/CAMPOS CON EL MISMO NOMBRE DE LA BASE DE DATOS
    TODO ESTO LO REALIZA EN EL DOCUMENTO
     */
    private void getUser(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        mUsername = documentSnapshot.getString("username");
                        mTextInputUsername.setText(mUsername);
                    }
                    if(documentSnapshot.contains("matricula")){
                        mMatricula = documentSnapshot.getString("matricula");
                        //Aqui puedo saber si recupero una matricula en caso de ser null podria ser un usuario externo que es lo unico probable pero ahorita checamos de se este el caso aqui intentare ocultar el poder cambiar la matricula para este tipo de usuarios
                        mTextInputMatricula.setText(mMatricula);
                        //Toast.makeText(getApplicationContext(), "Matricula: " + mMatricula, Toast.LENGTH_LONG).show();
                        if(mMatricula == null || mMatricula == ""){
                            mTextInputMatricula.setEnabled(false);
                            mLLMatricula.setVisibility(View.GONE);
                        }
                    }
                    if(documentSnapshot.contains("image_profile")){
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if(mImageProfile != null){
                            if(!mImageProfile.isEmpty()){
                                Picasso.with(EditProfileActivity.this).load(mImageProfile).into(mCircleImageViewProfile);
                            }
                        }
                    }
                    if(documentSnapshot.contains("image_cover")){
                        mImageCover = documentSnapshot.getString("image_cover");
                        if(mImageCover != null){
                            if(!mImageCover.isEmpty()){
                                Picasso.with(EditProfileActivity.this).load(mImageCover).into(mImageViewCover);
                            }
                        }
                    }
                    if(documentSnapshot.contains("tipocuenta")){
                        mTipoCuenta = documentSnapshot.getString("tipocuenta");
                    }
                }
            }
        });
    }

    private void clickEditProfile() {
        mUsername = mTextInputUsername.getText().toString();
        mMatricula= mTextInputMatricula.getText().toString();

        if((!mUsername.isEmpty() && !mMatricula.isEmpty()) || (!mUsername.isEmpty() && !mTextInputMatricula.isEnabled())){
            if(mImageFile != null && mImageFile2 !=null){
                saveImageCoverAndProfile(mImageFile, mImageFile2);
            }
            //Seleciono las dos photos de camara
            else if (mPhotoFile != null && mPhotoFile2 != null){
                saveImageCoverAndProfile(mPhotoFile, mPhotoFile2);
            }
            //Seleciono uno en photos y otro en camara
            else if (mImageFile != null && mPhotoFile2 != null){
                saveImageCoverAndProfile(mImageFile, mPhotoFile2);
            }
            else if (mPhotoFile != null && mImageFile2 != null){
                saveImageCoverAndProfile(mPhotoFile, mImageFile2);
            }
            else if(mPhotoFile != null){
                saveImage(mPhotoFile, true);
            }
            else if(mPhotoFile2 != null){
                saveImage(mPhotoFile2, false);
            }
            else if(mImageFile != null){
                saveImage(mImageFile, true);
            }
            else if(mImageFile2 != null){
                saveImage(mImageFile2, false);
            }
            else {
                User user = new User();
                user.setUsername(mUsername);
                user.setMatricula(mMatricula);
                user.setId(mAuthProvider.getUid());
                user.setImageCover(mImageCover);
                user.setImageProfile(mImageProfile);
                updateInfo(user);
            }

        }else{
            Toast.makeText(EditProfileActivity.this, "Ingrese el nombre de usuario y la matricula ", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImageCoverAndProfile(File imageFile1, final File imageFile2 ) {
        mDialogo.show();
        mImageProvider.save(EditProfileActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String urlProfile = uri.toString();

                            mImageProvider.save(EditProfileActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String urlCover = uri2.toString();
                                                User user = new User();
                                                user.setUsername(mUsername);
                                                user.setMatricula(mMatricula);
                                                user.setImageProfile(urlProfile);
                                                user.setImageCover(urlCover);
                                                user.setId(mAuthProvider.getUid());
                                                updateInfo(user);
                                            }
                                        });
                                    }else{
                                        mDialogo.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "La imagen numero 2 no de pudo guardar", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });

                }
                else {
                    mDialogo.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveImage(File imagen, final boolean isProfileImage){
        mDialogo.show();
        mImageProvider.save(EditProfileActivity.this, imagen).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            User user = new User();
                            user.setUsername(mUsername);
                            user.setMatricula(mMatricula);
                            if(isProfileImage){
                                user.setImageProfile(url);
                                user.setImageCover(mImageCover);
                            }
                            else{
                                user.setImageCover(url);
                                user.setImageProfile(mImageProfile);
                            }

                            user.setId(mAuthProvider.getUid());
                            updateInfo(user);
                        }

                    });

                }
                else {
                    mDialogo.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateInfo(User user){
        if (mDialogo.isShowing()){
            mDialogo.show();
        }

        if(!mTextInputMatricula.isEnabled()){
            user.setTipocuenta("Externo");
        }else{
            user.setTipocuenta(mTipoCuenta);
        }
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialogo.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Se actualizado correctamente", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "La informacion no se pudo actualizar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void selectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    if(numberImage == 1){
                        openGallery(GALLERY_REQUEST_CODE_PROFILE);
                    }else if(numberImage == 2){
                        openGallery(GALLERY_REQUEST_CODE_COVER);
                    }
                }else if(i == 1){
                    if(numberImage == 1){
                        takePhoto(PHOTO_REQUEST_CODE_PROFILE);
                    }else if(numberImage == 2){
                        takePhoto(PHOTO_REQUEST_CODE_COVER);
                    }
                }
            }
        });
        mBuilderSelector.show();
    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) !=null){
            File photoFile = null;
            try{
                photoFile = createPhotoFile(requestCode);
            }catch (Exception e){
                Toast.makeText(EditProfileActivity.this, "Hubo un error con el archivo " +e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(EditProfileActivity.this, "com.example.cenidet", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );

        if(requestCode==PHOTO_REQUEST_CODE_PROFILE){
            mPhotopath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }else if(requestCode==PHOTO_REQUEST_CODE_COVER){
            mPhotopath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SELECION DE IMAGEN DESDE LA GALERIA

        if (requestCode == GALLERY_REQUEST_CODE_PROFILE && resultCode == RESULT_OK) {
            try {
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE_COVER && resultCode == RESULT_OK) {
            try {
                mPhotoFile2 = null;
                mImageFile2 = FileUtil.from(this, data.getData());
                mImageViewCover.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        //SELECION DE PHOTOGRAFIA
        if(requestCode == PHOTO_REQUEST_CODE_PROFILE && resultCode == RESULT_OK){
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(EditProfileActivity.this).load(mPhotopath).into(mCircleImageViewProfile);
        }

        //SELECION DE PHOTOGRAFIA
        if(requestCode == PHOTO_REQUEST_CODE_COVER && resultCode == RESULT_OK){
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsolutePhotoPath2);
            Picasso.with(EditProfileActivity.this).load(mPhotopath2).into(mImageViewCover);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.upDateOnline(true, EditProfileActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.upDateOnline(true, EditProfileActivity.this);

    }
}