package com.example.firebaseakki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2,et3;
    ImageView imv;
    TextView tv1,tv2;
    private StorageReference mStorageRef;

    Uri uri;

    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private EditText etemsin;
    private EditText etempass;



    DatabaseReference reference;
    Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(this);
        etemsin = (EditText) findViewById(R.id.editTextEmailSignin);
        etempass = (EditText) findViewById(R.id.editTextPasswordSignin);

        imv = findViewById(R.id.imv);
        et1 = findViewById(R.id.ET1);
        et2 = findViewById(R.id.ET2);
        et3 = findViewById(R.id.ET3);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);


        reference = FirebaseDatabase.getInstance().getReference().child("Vamsi");
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public void Add(View view) {

        member = new Member();
        member.setName(et1.getText().toString());
        member.setRegno(Integer.parseInt(et2.getText().toString()));
        member.setHeight(Integer.parseInt(et3.getText().toString()));

        reference.push().setValue(member);
        //reference.child("member1").setValue(member);
        Toast.makeText(getApplicationContext(),"Insertion Success",Toast.LENGTH_SHORT).show();
    }

    public void Get(View view) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     String str = dataSnapshot.child("member1").getValue().toString();
                     tv1.setText(str);
                     String str1 = dataSnapshot.child("member1").child("height").getValue().toString();
                     tv2.setText(str1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void upload(View view) {
        if(uri!=null) {
            mStorageRef.child("image/pic.jpg").putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
            Toast.makeText(this, "Uri is NUll", Toast.LENGTH_SHORT).show();
    }

    public void choose(View view) {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null)
        {
            uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else {
            Toast.makeText(getApplicationContext(),"Hello"+data.getData(),Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View view) {

        registerUser();
    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //checking if success
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(MainActivity.this,"registeration failed",Toast.LENGTH_LONG).show();

                        }
                        progressDialog.dismiss();

                    }
                });

    }

    public void SignIn(View view) {
        setContentView(R.layout.activity_signin);
    }

    public void signInEval(View view) {
        signInUser();

    }

    private void signInUser() {

//        String email = etemsin.getText().toString().trim();
//        String password  = etempass.getText().toString().trim();

//        if(TextUtils.isEmpty(email)){
//            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
//            return;
//        }

      //  Toast.makeText(getApplicationContext(),etemsin.getText().toString(),Toast.LENGTH_SHORT).show();

        firebaseAuth.signInWithEmailAndPassword("vamsik586@gmail.com","100plus43")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"SignIn Successful",Toast.LENGTH_SHORT).show();
                            setContentView(R.layout.activity_main);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"SignIn unSuccessful",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
