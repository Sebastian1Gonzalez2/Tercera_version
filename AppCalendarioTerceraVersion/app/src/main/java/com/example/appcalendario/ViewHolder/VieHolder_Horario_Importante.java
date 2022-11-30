package com.example.appcalendario.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcalendario.R;

public class VieHolder_Horario_Importante extends RecyclerView.ViewHolder {


    View view;

    private VieHolder_Horario_Importante.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setmClickListener( VieHolder_Horario_Importante.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public  VieHolder_Horario_Importante(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });

    }
    public void SetearDatos(Context context, String id_horario, String uid_usuario, String correo_usuario, String fecha_hora_actual, String Sesion, String Paciente,
                            String Informacion, String fecha_horario, String hora_horario, String estado){

        TextView txtidHorarioList, txtUidUsuarioList, txtCorreoUsuarioList, txtFechaHoraActualList,
                txtSesionList, txtPacienteList, txtInformacionList, txtFechaList, txtHoraList, txtEstadoList;

        txtidHorarioList = view.findViewById(R.id.txtidHorarioList_I);
        txtUidUsuarioList = view.findViewById(R.id.txtUidUsuarioList_I);
        txtCorreoUsuarioList = view.findViewById(R.id.txtCorreoUsuarioList_I);
        txtFechaHoraActualList = view.findViewById(R.id.txtFechaHoraActualList_I);
        txtSesionList = view.findViewById(R.id.txtSesionList_I);
        txtPacienteList = view.findViewById(R.id.txtPacienteList_I);
        txtInformacionList = view.findViewById(R.id.txtInformacionList_I);
        txtFechaList = view.findViewById(R.id.txtFechaList_I);
        txtHoraList = view.findViewById(R.id.txtHoraList_I);
        txtEstadoList = view.findViewById(R.id.txtEstadoList_I);


        txtidHorarioList.setText(id_horario);
        txtUidUsuarioList.setText(uid_usuario);
        txtCorreoUsuarioList.setText(correo_usuario);
        txtFechaHoraActualList.setText(fecha_hora_actual);
        txtSesionList.setText(Sesion);
        txtPacienteList.setText(Paciente);
        txtInformacionList.setText(Informacion);
        txtFechaList.setText(fecha_horario);
        txtHoraList.setText(hora_horario);
        txtEstadoList.setText(estado);

    }
}
