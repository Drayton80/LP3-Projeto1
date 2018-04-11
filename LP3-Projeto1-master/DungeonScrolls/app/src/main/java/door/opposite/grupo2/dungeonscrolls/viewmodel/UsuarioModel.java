package door.opposite.grupo2.dungeonscrolls.viewmodel;

import android.databinding.BaseObservable;

import door.opposite.grupo2.dungeonscrolls.R;
import door.opposite.grupo2.dungeonscrolls.model.Usuario;

/**
 * Created by ci on 02/04/18.
 */

public class UsuarioModel extends BaseObservable{

    public int id;
    public String nick;
    public String senha;
    public String email;


    public UsuarioModel() {
    }

    public UsuarioModel(Usuario usuario){
        this.id = usuario.getID();
        this.nick = usuario.getNick();
        this.senha = usuario.getSenha();
        this.email = usuario.getEmail();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
        notifyPropertyChanged(R.id.login_username_plainText);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
        notifyPropertyChanged(R.id.login_password_password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(R.id.signup_email_textView);

    }




}