package pe.edu.upc.wallpapeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etPassword;

    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(getApplicationContext());

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);

        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarUsuario();
            }
        });

    }

    public void irRegistrar(View view){
        Intent i = new Intent(this, RegistrarActivity.class);
        startActivity(i);
    }

    private void ingresarUsuario(){
        if(isValidarCampos()){

            String usuario = etUsuario.getText().toString();
            String password = etPassword.getText().toString();

            Map<String,String> datos = new HashMap<>();
            datos.put("username", usuario);
            datos.put("password", password);

            JSONObject jsondata = new JSONObject(datos);

            AndroidNetworking.post("https://infinite-tundra-77261.herokuapp.com/api/auth/signin")
                    .addJSONObjectBody(jsondata)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String username = response.getString("username");
                                Toast.makeText(MainActivity.this, "El usuario "+username+" si esta registrado", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(MainActivity.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else{
            Toast.makeText(this, "No se puede ingresar si existen campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    //devuelve verdadero si es que no hay campos vacios
    //devuelve falso si es que hay como minimo un campo vacio
    private boolean isValidarCampos(){
        return !etUsuario.getText().toString().trim().isEmpty() &&
                !etPassword.getText().toString().trim().isEmpty();
    }
}