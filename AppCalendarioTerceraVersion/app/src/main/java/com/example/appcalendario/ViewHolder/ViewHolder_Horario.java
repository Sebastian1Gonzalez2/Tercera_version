package com.example.appcalendario.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcalendario.R;

import java.util.ConcurrentModificationException;

public class ViewHolder_Horario extends RecyclerView.ViewHolder {

    View view;

    private ViewHolder_Horario.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setmClickListener(ViewHolder_Horario.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Horario(@NonNull View itemView) {
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

        ImageView HorarioPendiente_item, HorarioTerminado_item;

        txtidHorarioList = view.findViewById(R.id.txtidHorarioList);
        txtUidUsuarioList = view.findViewById(R.id.txtUidUsuarioList);
        txtCorreoUsuarioList = view.findViewById(R.id.txtCorreoUsuarioList);
        txtFechaHoraActualList = view.findViewById(R.id.txtFechaHoraActualList);
        txtSesionList = view.findViewById(R.id.txtSesionList);
        txtPacienteList = view.findViewById(R.id.txtPacienteList);
        txtInformacionList = view.findViewById(R.id.txtInformacionList);
        txtFechaList = view.findViewById(R.id.txtFechaList);
        txtHoraList = view.findViewById(R.id.txtHoraList);
        txtEstadoList = view.findViewById(R.id.txtEstadoList);

        HorarioPendiente_item = view.findViewById(R.id.HorarioPendiente_item);
        HorarioTerminado_item = view.findViewById(R.id.HorarioTerminado_item);


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

        if (estado.equals("Finalizado")){
            HorarioTerminado_item.setVisibility(View.VISIBLE);
        }else {
            HorarioPendiente_item.setVisibility(View.VISIBLE);
        }

    }
}
