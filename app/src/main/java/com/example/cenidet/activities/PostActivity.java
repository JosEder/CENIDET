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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cenidet.R;
import com.example.cenidet.models.Post;
import com.example.cenidet.providers.AuthProvider;
import com.example.cenidet.providers.ImageProvider;
import com.example.cenidet.providers.PostProvider;
import com.example.cenidet.utils.FileUtil;
import com.example.cenidet.utils.ViewedMessageHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    File mImageFile;
    File mImageFile2;
    Button mButtonPost;
    ImageProvider mImageProvider;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;
    TextInputEditText mTextInputTitle;
    TextInputEditText mTextInputDescription;
    TextView mTextViewCategory;
    CircleImageView mCircleImageBack;
    String mCategory = "";
    String mTitle = "";
    String mDescription = "";
    AlertDialog mDialogo;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2 = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    private final int PHOTO_REQUEST_CODE_2 = 4;

    //PHOTO 1
    String mAbsolutePhotoPath;
    String mPhotopath;
    File mPhotoFile;
    //PHOTO 2
    String mAbsolutePhotoPath2;
    String mPhotopath2;
    File mPhotoFile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        mDialogo = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Seleciona una opcion");
        options = new CharSequence[]{"Imagen de galeria", "Tomar foto"};


        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mButtonPost=findViewById(R.id.btnPost);
        mTextInputTitle = findViewById(R.id.textInputTitulo);
        mTextInputDescription = findViewById(R.id.textInputDescripcion);
        mTextViewCategory = findViewById(R.id.textViewCategory);
        mCircleImageBack = findViewById(R.id.cricleimageback);

        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicPost();
            }
        });
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(1);
            }
        });
        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(2);
            }
        });


        String[] data = {"Dep. de Ingenieria Electronica", "Dep. de Ingenieria Mecanica", "Dep. de Ciencias Computacionales", "Dep. de Dess. Academico e Idiomas",
                        "Dep. de Org. y Seguimiento de Estudios", "Oficina de Centro de Com. y Telec.", "Centro de Informacion", "Dep. de Plan., Prog. y Presupestacion.",
                        "Dep. de Gest. Tecn. y Vinculacion", "Dep. de Comunicacion y Eventos", "Dep. de Servicios Escolares", "Dep. de Recursos Materiales y Servicios",
                        "Dep. de Recursos Humanos", "Dep. de Recursos Financieros"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void selectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (i == 0){
                    if(numberImage == 1){
                        openGallery(GALLERY_REQUEST_CODE);
                    }else if(numberImage == 2){
                        openGallery(GALLERY_REQUEST_CODE_2);
                    }
                }else if(i == 1){
                    if(numberImage == 1){
                        takePhoto(PHOTO_REQUEST_CODE);
                    }else if(numberImage == 2){
                        takePhoto(PHOTO_REQUEST_CODE_2);
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
                Toast.makeText(PostActivity.this, "Hubo un error con el archivo " +e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.example.cenidet", photoFile);
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

        if(requestCode==PHOTO_REQUEST_CODE){
            mPhotopath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }else if(requestCode==PHOTO_REQUEST_CODE_2){
            mPhotopath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void clicPost() {
        mTitle = mTextInputTitle.getText().toString();
        mDescription = mTextInputDescription.getText().toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            //Seleciono ambas imagenes de la galeria
            if(mImageFile != null && mImageFile2 !=null){
                saveImage(mImageFile, mImageFile2);
            }
            //Seleciono las dos photos de camara
            else if (mPhotoFile != null && mPhotoFile2 != null){
                saveImage(mPhotoFile, mPhotoFile2);
            }
            //Seleciono uno en photos y otro en camara
            else if (mImageFile != null && mPhotoFile2 != null){
                saveImage(mImageFile, mPhotoFile2);
            }
            else if (mPhotoFile != null && mImageFile2 != null){
                saveImage(mPhotoFile, mImageFile2);
            }
            else {
                Toast.makeText(PostActivity.this, "Debes selecionar una imagen", Toast.LENGTH_LONG).show();

            }
        }else {
            Toast.makeText(PostActivity.this, "Completa los campos para publicar", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImage(File imageFile1, final File imageFile2 ) {
        mDialogo.show();
        mImageProvider.save(PostActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           final String url = uri.toString();

                            mImageProvider.save(PostActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {

                                                String url2 = uri2.toString();
                                                Post post= new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle.toLowerCase());
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());
                                                post.setTimestamp(new Date().getTime());
                                                post.setTipocuenta("Alumno");
                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> tasksave) {
                                                        mDialogo.dismiss();
                                                        if (tasksave.isSuccessful()){
                                                            clearform();
                                                            Toast.makeText(PostActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_LONG).show();
                                                        }else{
                                                            Toast.makeText(PostActivity.this, "No se pudo almacenar la informacion", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }else{
                                        mDialogo.dismiss();
                                        Toast.makeText(PostActivity.this, "La imagen numero 2 no de pudo guardar", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });

                }
                else {
                    mDialogo.dismiss();
                    Toast.makeText(PostActivity.this, "Hubo un error al amacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearform() {

        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImageViewPost1.setImageResource(R.drawable.anadir);
        mImageViewPost2.setImageResource(R.drawable.camara);
        mTitle="";
        mDescription="";
        mCategory="";
        mImageFile=null;
        mImageFile2=null;
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

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            try {
                mPhotoFile2 = null;
                mImageFile2 = FileUtil.from(this, data.getData());
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        //SELECION DE PHOTOGRAFIA
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(PostActivity.this).load(mPhotopath).into(mImageViewPost1);
        }

        //SELECION DE PHOTOGRAFIA
        if(requestCode == PHOTO_REQUEST_CODE_2 && resultCode == RESULT_OK){
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsolutePhotoPath2);
            Picasso.with(PostActivity.this).load(mPhotopath2).into(mImageViewPost2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewedMessageHelper.upDateOnline(true, PostActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.upDateOnline(false, PostActivity.this);
    }

}