package com.example.lab5_and103.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_and103.Model.Distributor;
import com.example.lab5_and103.R;
import com.example.lab5_and103.Service.Item_Distributor_Handle;

import java.util.ArrayList;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Distributor> listDistributor;
    private Item_Distributor_Handle item_distributor_handle;

    public DistributorAdapter(Context context, ArrayList<Distributor> listDistributor, Item_Distributor_Handle item_distributor_handle) {
        this.context = context;
        this.listDistributor = listDistributor;
        this.item_distributor_handle = item_distributor_handle;
    }



    @NonNull
    @Override
    public DistributorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distributor, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorAdapter.ViewHolder holder, int position) {
        Distributor distributor = listDistributor.get(position);
        holder.tvName.setText(distributor.getName());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item_distributor_handle.Update(distributor);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(distributor.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDistributor != null ? listDistributor.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageButton btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    private void showDeleteConfirmationDialog(final String distributorID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm deletion");
        builder.setMessage("Are you sure you want to delete it?");

        // Nút xác nhận
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Gọi hàm xóa khi người dùng xác nhận xóa
                item_distributor_handle.Delete(distributorID);
            }
        });

        // Nút hủy
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng dialog
            }
        });

        // Tạo và hiển thị dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
