package door.opposite.grupo2.dungeonscrolls.Telas;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;

import java.util.ArrayList;

import door.opposite.grupo2.dungeonscrolls.R;
import door.opposite.grupo2.dungeonscrolls.adapter.SalaAdapter;
import door.opposite.grupo2.dungeonscrolls.commands.Eventos;
import door.opposite.grupo2.dungeonscrolls.databinding.ActivityUserRoomsBinding;
import door.opposite.grupo2.dungeonscrolls.graficAssets.DialogFragmentCreator;
import door.opposite.grupo2.dungeonscrolls.model.Ficha;
import door.opposite.grupo2.dungeonscrolls.model.SQLite;
import door.opposite.grupo2.dungeonscrolls.model.Sala;
import door.opposite.grupo2.dungeonscrolls.model.Usuario;
import door.opposite.grupo2.dungeonscrolls.viewmodel.SalaModel;

public class UserRooms extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    // Atributos relativos à parte visual do programa:
    AlertDialog dialog;     // Um referenciador para Dialog Fragments, é possível fechar ou mostrar dialogs com ele
    DialogFragmentCreator geradorDialog = new DialogFragmentCreator();  // Cria um manipulador de Dialog Fragments, objeto da classe DialogFragmentCreator
    // Atributos relativos ao Data Binding
    ActivityUserRoomsBinding binding;
    SQLite sqLite;
    Intent extra;
    Usuario usuarioLogado;
    SalaModel salaModel;
    ArrayList<SalaModel> salaModelArrayList;
    SalaAdapter salaAdapter;
    int[] salasID;
    Sala salaUsada;
    int posicaoDelete = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cria uma View que referencia o layout dialogfragment_loadingcircle
        View loadingCircleDialog = getLayoutInflater().inflate(R.layout.dialogfragment_loadingcircle, null);
        // referência para um AlertDialog chamado dialog
        dialog = geradorDialog.criaDialogFragmentLoadingCircle(this, loadingCircleDialog);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_rooms);
        sqLite = new SQLite(this);

        extra = getIntent();
        usuarioLogado = (Usuario) extra.getSerializableExtra("usuarioLogado");

        salasID = usuarioLogado.getSalasID();
        salaModel = new SalaModel();
        salaModelArrayList = salaModel.getArrayListSala(usuarioLogado.getSalasID(), sqLite);
        salaAdapter = new SalaAdapter(this, salaModelArrayList);
        binding.lvUserRooms.setAdapter(salaAdapter);

        // Usa um dos métodos de DialogFragmentCreator para fechar o loading dialog, passando como parâmetro a referência para
        // o próprio dialog retornado na criação com criaDialogFragmentLoadingCircle
        geradorDialog.fechaDialogFragment(dialog);

        binding.lvUserRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int salaPosicao = position;
                for(int i = 0; i < usuarioLogado.getSalasID().length; i++){
                    if(i == salaPosicao){
                        if (salasID[i+1] == 0){
                         }else{
                           // System.out.println("=================Entrou aqui, eu achei a sala!");
                            // Cria uma View que referencia o layout dialogfragment_loadingcircle
                            View loadingCircleDialog = getLayoutInflater().inflate(R.layout.dialogfragment_loadingcircle, null);
                            // Usa um dos métodos de DialogFragmentCreator para criar um dialog fragment do loading dialog e ao mesmo tempo passar sua
                            // referência para um AlertDialog chamado dialog
                            dialog = geradorDialog.criaDialogFragmentLoadingCircle(UserRooms.this, loadingCircleDialog);

                            salaUsada = sqLite.selecionarSala(salasID[i+1]);
                            extra = new Intent(UserRooms.this, RoomActivity.class);
                            extra.putExtra("usuarioLogado", usuarioLogado);
                            extra.putExtra("salaUsada", salaUsada);
                            //System.out.println("=================Entrou aqui, eu achei a sala!" + salaUsada.getNotas());
                            startActivity(extra);
                        }
                    }
                }
            }
        });

        binding.lvUserRooms.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                PopupMenu menu = new PopupMenu(UserRooms.this ,view);
                menu.setOnMenuItemClickListener(UserRooms.this);
                menu.inflate(R.menu.menu_popup);
                posicaoDelete = position;

                menu.show();

                return true;
            }
        });

        binding.setCriaSala(new Eventos() {
            @Override
            public void onClickCad() {
                extra = new Intent(UserRooms.this, RoomCreationActivity.class);
                extra.putExtra("usuarioLogado", usuarioLogado);
                startActivity(extra);
            }

            @Override
            public void onClickLogin() {
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // dialog só é null antes de ser instanciado, apenas por garantia para não dar erros
        if(dialog != null){
            // Usado para fechar o Dialog Fragment do Loading Magic Circle, é chamado no onStop() pois ele apenas ocorre quando outra activity é chamada
            // e essa sai de visualização, logo após não estar mais visível.
            geradorDialog.fechaDialogFragment(dialog);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_vincular:
                return true;
            case R.id.item_deleta:
                salasID = usuarioLogado.getSalasID();
                for(int i = 0; i < usuarioLogado.getSalasID().length; i++){
                    if(i == posicaoDelete){
                        if (salasID[i+1] == 0){
                            System.out.println("==========================================================");
                        }else{
                            // System.out.println("=================Entrou aqui, eu achei a sala!");
                            Sala sala = sqLite.selecionarSala(salasID[i+1]);
                            //----------comeca
                            int array_auxiliar[] = usuarioLogado.getSalasID();
                            array_auxiliar = achaElemento(array_auxiliar, sala.getID());
                            usuarioLogado.setSalasID(array_auxiliar);
                            //-----------ternina
                            sqLite.updateDataUsuario(usuarioLogado);
                            sqLite.deleteDataSala(sala);
                            finish();
                            startActivity(getIntent());
                        }
                    }
                }
                return true;
            default:
                return false;

        }    }


    public static int[] achaElemento(int array_original[], int numero){
        int posicao = 0;
        boolean encontrou = false;
        for(int i = 0; i < array_original.length; i++){
            if(array_original[i] == numero){
                posicao = i;
                break;
            }
        }

        int array_novo[] = new int[array_original.length -1];
        for(int i = 0; i < array_novo.length; i++){
            if(i == posicao || encontrou == true) {
                array_novo[i] = array_original[i+1];
                encontrou = true;
            }
            else{
                array_novo[i] = array_original[i];
            }
        }

        return array_novo;
    }
}
