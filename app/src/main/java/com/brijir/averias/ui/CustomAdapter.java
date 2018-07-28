package com.brijir.averias.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brijir.averias.R;
import com.brijir.averias.bd.Fault;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomVH> {
    List<Fault> listFaults;

    public CustomAdapter(List<Fault> listFaults) {
        if(listFaults != null) {
            this.listFaults = listFaults;
        }
        else {
            Log.d("Error Adapter","Lista viene vacía.");
        }
    }

    @Override
    public CustomAdapter.CustomVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fault_item,parent,false);
        return new CustomVH(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(CustomAdapter.CustomVH holder, int position) {
        Fault fault = listFaults.get(position);
        holder.tvNameFault.setText("Avería: " + fault.getNombre());
        holder.tvDescripcionFault.setText("Descripción: " + fault.getDescripcion());
        if(fault.getUsuario() != null) {
            holder.tvUserFault.setText("Reportado por: " + fault.getUsuario().nombre);
        }
        else {
            holder.tvUserFault.setText("");
        }
        holder.FaultId = fault.getId();
    }

    @Override
    public int getItemCount() {
        return listFaults.size();
    }

    class CustomVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_NameFault)
        TextView tvNameFault;

        @BindView(R.id.tv_DescripcionFault)
        TextView tvDescripcionFault;

        @BindView(R.id.tv_UserFault)
        TextView tvUserFault;

        @BindView(R.id.btn_EditFaultItem)
        Button btnEditFaultItem;

        String FaultId;

        private Context context;

        public CustomVH(View itemView, Context cont) {
            super(itemView);
            context = cont;
            ButterKnife.bind(this, itemView);
            btnEditFaultItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.equals(btnEditFaultItem)) {
                Intent intentEdit = new Intent(context, EditFaultActivity.class);
                intentEdit.putExtra("IdFaultEdit", FaultId);
                context.startActivity(intentEdit);
            }
        }
    }
}
