package com.example.afinal;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class sales extends AppCompatActivity {
    String idseller;
    FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
    String venta;
    double comision;
    double contadorComision;
    double totalComision;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        EditText correo =findViewById(R.id.etemailSales);
        EditText fecha =findViewById(R.id.etdateSales);
        EditText comercial =findViewById(R.id.etsaleValue);
        Button agregar =findViewById(R.id.btnsaveSales);
//        comercial=(Double.parseDouble(venta));
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(basedatos.collection("venta") == null)) {
//                    if(venta >= 10000000){
//                        agregar(correo.getText().toString(),fecha.getText().toString(),comercial.getText().toString());
//                    }
//                    else{
//                        Toast.makeText(getApplicationContext(),"'error' tu valor debe superar 10 millones",Toast.LENGTH_SHORT).show();
//                    }


                }

            }
        });


    }

    private void agregar(String scorreo, String sfecha, String scomercial) {
//        venta = scomercial;
//        comision = 0.02 * (Double.parseDouble(venta));
//        contadorComision = contadorComision + comision;
//        totalComision = valueOf(contadorComision);

        Task<QuerySnapshot> querySnapshotTask = basedatos.collection("sales")
                .whereEqualTo("email", scorreo).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    idseller= document.getId();

                                }

                                Map<String, Object> venta = new HashMap<>();
                                venta.put("correo",scorreo);
                                venta.put("fecha",sfecha);
                                venta.put("comercial",scomercial);
                                basedatos.collection("sales").add(venta).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getApplicationContext(),"venta agregada exitosamente",Toast.LENGTH_SHORT).show();
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