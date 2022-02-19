package com.example.sandeep.javaexosia.Admin.Fragments_Admin;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import static com.example.sandeep.javaexosia.R.id.edtschemeType;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.sandeep.javaexosia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.sandeep.javaexosia.SchemedataCollection;


public class FragmentSchemeAdd extends Fragment {

    EditText schName,equitment,amount,below,above,criteria;
    ProgressBar prograss;
    Spinner type,spinner;
    String strschName,str_dep,strtype,strequitment,stramount,strbelow,strabove,strcriteria;
    Button sub;
    View view;
    String authority;
    String authority_Place;
    DatabaseReference RKTDRef= FirebaseDatabase.getInstance().getReference("Scheme_Table");
    public FragmentSchemeAdd(String authority, String authority_Place) {
        this.authority=authority;
        this.authority_Place=authority_Place;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_scheme_add, container, false);
         schName=view.findViewById(R.id.edtschemename);
         spinner=view.findViewById(R.id.spinner);
         type=view.findViewById(edtschemeType);
         amount=view.findViewById(R.id.edtamount);
         below=view.findViewById(R.id.edtfrom);
         criteria=view.findViewById(R.id.edtschemecriteria);
         above=view.findViewById(R.id.edtto);
         sub=view.findViewById(R.id.btnsubmit);
         prograss=view.findViewById(R.id.prograss);

        sub.setOnClickListener(v -> {
            strschName = schName.getText().toString();
            str_dep = spinner.getSelectedItem().toString();
            strtype = type.getSelectedItem().toString();
//                 strequitment=equitment.getText().toString();
            stramount = amount.getText().toString();
            strcriteria = criteria.getText().toString();
            strbelow = below.getText().toString();
            strabove = above.getText().toString();


            if (TextUtils.isEmpty(strschName)) {
                makeText(getActivity(),
                        "please Enter scheme name",
                        LENGTH_SHORT).show();
            } else if (str_dep.equals("select category")){
                Toast.makeText(getContext(),"pls select category", LENGTH_SHORT).show();
            } else if (strtype.equals("select Scheme")) {
                Toast.makeText(getActivity(), "please select scheme type", LENGTH_SHORT).show();
            } else if (strcriteria.isEmpty()) {
                Toast.makeText(getActivity(), "please select scheme Criteria", LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strbelow)) {
                Toast.makeText(getActivity(), "please Enter Below Limit", LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(strabove)) {
                Toast.makeText(getActivity(), "please Enter Above Limit", LENGTH_SHORT).show();
            } else {

                SchemedataCollection schemedata = new SchemedataCollection(strschName, str_dep, strtype, strequitment, stramount, strbelow, strabove, strcriteria,authority,authority_Place,"Null");
                prograss.setVisibility(View.VISIBLE);
                checktoInsert(schemedata);
            }
        });

         return view;
    }

    public void InsertScheme(SchemedataCollection schemedata) {

        String key=RKTDRef.push().getKey();
        schemedata.setId(key);
        RKTDRef.child(schemedata.getStrschName()).setValue(schemedata);
       schName.setText("");
       amount.setText("");
       criteria.setText("");
       below.setText("");
       above.setText("");
       prograss.setVisibility(View.GONE);

    }

    private void checktoInsert(SchemedataCollection schemedata) {
        RKTDRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SchemedataCollection collection = snapshot.getValue(SchemedataCollection.class);
                if (collection != (null)) {
                    if ((snapshot.child(schemedata.getStrschName()).exists())) {
                        Toast.makeText(getContext(), "Scheme already Inserted", LENGTH_SHORT).show();
                    } else {
                        InsertScheme(schemedata);
                        Toast.makeText(getContext(), "Succefully Insert", LENGTH_SHORT).show();
                    }
                }else {
                    InsertScheme(schemedata);
                    Toast.makeText(getContext(), "Succefully Insert", LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}