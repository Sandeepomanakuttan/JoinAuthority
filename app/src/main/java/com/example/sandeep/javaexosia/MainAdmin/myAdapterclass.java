
package com.example.sandeep.javaexosia.MainAdmin;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.sandeep.javaexosia.LoginDataClass;
import com.example.sandeep.javaexosia.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import com.example.sandeep.javaexosia.ProfileData;

public class myAdapterclass extends FirebaseRecyclerAdapter <ProfileData,myAdapterclass.myViewholder>{
String Authority,id,name;
LoginDataClass data;
    DatabaseReference references= FirebaseDatabase.getInstance().getReference("LoginTable");
    public myAdapterclass(@NonNull FirebaseRecyclerOptions<ProfileData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, int position, @NonNull ProfileData model) {
        DatabaseReference Profilereference= FirebaseDatabase.getInstance().getReference("Profile_Table");
        String id= model.getId();
        name=model.getUserName();
        Query query=Profilereference.orderByChild("id").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ProfileData data = childSnapshot.getValue(ProfileData.class);
                    String link1 =data.getImageUri();

                    Picasso.get().load(link1).into(holder.img1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.name.setText(model.getName());
            holder.username.setText(model.getUserName());
            holder.password.setText(model.getPassword());
            holder.authority.setText(model.getAuthority_Place());

            holder. update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Authority=holder.editAuthority.getText().toString();
                    if (TextUtils.isEmpty(Authority)){
                        holder.editAuthority.setVisibility(View.VISIBLE);
                        holder.authority.setVisibility(View.GONE);
                        holder.editAuthority.requestFocus();

                    }
                    else{

                            UpdateAuthorityPlace(Authority,v);
                            holder.editAuthority.setVisibility(View.GONE);
                            holder.authority.setVisibility(View.VISIBLE);

                    }
                }
            });
    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.authorityview,parent,false);
        return new myViewholder(view);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class myViewholder extends ViewHolder{

        ImageView img1;
        EditText editAuthority;
        TextView name,username,password,authority;
        Button update;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            img1= itemView.findViewById(R.id.img1);
            name= itemView.findViewById(R.id.txtName);
            username= itemView.findViewById(R.id.txtusername);
            password= itemView.findViewById(R.id.txtPassword);
            authority= itemView.findViewById(R.id.txtAuthorityName);
            update= itemView.findViewById(R.id.btnupdate);
            editAuthority= itemView.findViewById(R.id.AuthorityName);


        }

    }

    private void UpdateAuthorityPlace(String authority, View v) {
        Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();
        HashMap< String, Object > update=new HashMap< String, Object >();
        update.put("authority_Place",authority);
        references.child(name).updateChildren(update).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}