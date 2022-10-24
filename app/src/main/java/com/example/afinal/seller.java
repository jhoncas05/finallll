package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class seller extends AppCompatActivity {
    String idseller;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        EditText email = findViewById(R.id.etemailSeller);
        EditText name= findViewById(R.id.etnameSeller);
        EditText phone= findViewById(R.id.etphoneSeller);
        Button btnsave= findViewById(R.id.btnsave);
        Button btnsearch = findViewById(R.id.btnsearch);
        Button btnedit = findViewById(R.id.btnedit);
        Button btndeleter = findViewById(R.id.btndelete);

        btndeleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(seller.this);
                alertDialogBuilder.setMessage("estas seguro de eliminar a el vendedor "+ name.getText().toString()+"?");
                alertDialogBuilder.setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(basedatos.collection("sales")==null){

                            basedatos.collection("venta").document(idseller)
                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(seller.this, "se ha eliminado vendedor", Toast.LENGTH_SHORT).show();

                                            email.setText("");
                                            name.setText("");
                                            phone.setText("");
                                            email.requestFocus();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(seller.this, "error al eliminar" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }else{
                            Toast.makeText(getApplicationContext(),"no se puede eliminar alguien con ventas",Toast.LENGTH_SHORT).show();

                        }


                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog =alertDialogBuilder.create();
                alertDialog.show();


            }
        });

       btnedit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Map<String, Object> venta = new HashMap<>();
               venta.put("email",email.getText().toString());
               venta.put("name",name.getText().toString());
               venta.put("phone",phone.getText().toString());

               basedatos.collection("venta").document(idseller).set(venta).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                     Toast.makeText(getApplicationContext(),"datos actualizados",Toast.LENGTH_SHORT).show();
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();

                   }
               });
           }
       });


    btnsearch.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

         basedatos.collection("seller").whereEqualTo("email", email.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    if(!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot buscar: task.getResult()){
                            idseller=buscar.getId();
                            name.setText(buscar.getString("name"));
                            email.setText(buscar.getString("email"));
                            phone.setText(buscar.getString("phone"));
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"no existente",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
            });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarVentas(email.getText().toString(),name.getText().toString(),phone.getText().toString());

            }
        });

    }

    private void guardarVentas(String semail, String sname, String sphone) {
        Task<QuerySnapshot> querySnapshotTask = basedatos.collection("seller")
                .whereEqualTo("email", semail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){


                    if (task.getResult().isEmpty()){

                        Map<String, Object> venta = new HashMap<>();
                        venta.put("email",semail);
                        venta.put("name",sname);
                        venta.put("phone",sphone);
                        basedatos.collection("seller").add(venta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"guardado exitosamente",Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"digite bien su opcion por favor",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Correo ya existente, porfavor elija uno nuevo",Toast.LENGTH_SHORT).show();
                    }
                }

                    }
                });




    }
}