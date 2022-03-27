package pe.edu.upc.wallpapeer;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegistrarActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etPassword;
    private EditText etEmail;

    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        AndroidNetworking.initialize(getApplicationContext());

        etUsuario = findViewById(R.id.etUsuarioReg);
        etPassword = findViewById(R.id.etPasswordReg);
        etEmail = findViewById(R.id.etEmailReg);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { registrarUsuario(); }
        });

    }
    private void registrarUsuario() {
        if (isValidarCampos()) {

            String usuario = etUsuario.getText().toString();
            String password = etPassword.getText().toString();
            String email = etEmail.getText().toString();

            Map<String,String> datos = new HashMap<>();
            datos.put("username", usuario);
            datos.put("email", email);
            datos.put("password", password);

            JSONObject jsondata = new JSONObject(datos);

            AndroidNetworking.post("https://infinite-tundra-77261.herokuapp.com/api/auth/signup")
                    .addJSONObjectBody(jsondata)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String messageSucces = response.getString("message");
                                Toast.makeText(RegistrarActivity.this, messageSucces, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(RegistrarActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(RegistrarActivity.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No se puede ingresar si existen campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidarCampos(){
        return !etUsuario.getText().toString().trim().isEmpty() &&
                !etPassword.getText().toString().trim().isEmpty() &&
                !etEmail.getText().toString().trim().isEmpty();
    }
}