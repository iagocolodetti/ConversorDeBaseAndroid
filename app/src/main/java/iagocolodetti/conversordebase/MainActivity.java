package iagocolodetti.conversordebase;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.content.res.Configuration;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {
    boolean travar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        travar = false;

        //region Instanciar Variáveis
        final TextView tvR = (TextView) findViewById(R.id.textViewR);
        final Button btConverter = (Button) findViewById(R.id.buttonConverter);
        final Spinner sprDaBase = (Spinner) findViewById(R.id.spinnerDaBase);
        final Spinner sprParaBase = (Spinner) findViewById(R.id.spinnerParaBase);
        final EditText etBase = (EditText) findViewById(R.id.editTextBase);
        //endregion

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        tvR.setText(preferences.getString("tvR", ""));
        etBase.setText(preferences.getString("etBase", ""));

        //region Spinner Base
        if(Build.VERSION.SDK_INT < 21) {
            sprDaBase.setBackgroundColor(Color.parseColor("#DBDBDB"));
            sprParaBase.setBackgroundColor(Color.parseColor("#DBDBDB"));
        }
        else { //BackgroundTint só pode ser usado em APIs superiores a 21.
            sprDaBase.setBackgroundTintList(getResources().getColorStateList(R.color.colorSpinnerBackgroundTint));
            sprParaBase.setBackgroundTintList(getResources().getColorStateList(R.color.colorSpinnerBackgroundTint));
        }

        String[] spinnerBases = new String[] {
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32","33", "34", "35", "36"
        };
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_textstyle, spinnerBases);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprDaBase.setAdapter(dataAdapter);
        sprDaBase.setSelection(preferences.getInt("sprDaBase", 0));
        sprParaBase.setAdapter(dataAdapter);
        sprParaBase.setSelection(preferences.getInt("sprParaBase", 8));
        //endregion

        //region Botão Converter
        btConverter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            try {
                if (travar) {
                    mensagem("Aguarde o termino da conversão atual.");
                    return;
                }

                FuncoesBase fb = new FuncoesBase();
                travar = true;
                String algarismo = etBase.getText().toString();
                int daBase = Integer.parseInt(sprDaBase.getSelectedItem().toString());
                int paraBase = Integer.parseInt(sprParaBase.getSelectedItem().toString());
                tvR.setText(getString(R.string.tvBase) + " (" + Integer.toString(paraBase) + "): " + fb.converterAlgarismo(algarismo, daBase, paraBase));
            }
            catch (NumberFormatException e) {
                mensagem("Não foi possível realizar a conversão.");
            }
            catch (Exception e) {
                mensagem(e.getMessage());
            }
            finally {
                travar = false;
            }
            }
        });
        //endregion
    }

    private void mensagem(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final TextView tvR = (TextView) findViewById(R.id.textViewR);
        final Spinner sprDaBase = (Spinner) findViewById(R.id.spinnerDaBase);
        final Spinner sprParaBase = (Spinner) findViewById(R.id.spinnerParaBase);
        final EditText etBase = (EditText) findViewById(R.id.editTextBase);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tvR", tvR.getText().toString());
        editor.putInt("sprDaBase", sprDaBase.getSelectedItemPosition());
        editor.putInt("sprParaBase", sprParaBase.getSelectedItemPosition());
        editor.putString("etBase", etBase.getText().toString());
        editor.apply();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        final TextView tvR = (TextView) findViewById(R.id.textViewR);
        final Spinner sprDaBase = (Spinner) findViewById(R.id.spinnerDaBase);
        final Spinner sprParaBase = (Spinner) findViewById(R.id.spinnerParaBase);
        final EditText etBase = (EditText) findViewById(R.id.editTextBase);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        tvR.setText(preferences.getString("tvR", ""));
        sprDaBase.setSelection(preferences.getInt("sprDaBase", 0));
        sprParaBase.setSelection(preferences.getInt("sprParaBase", 8));
        etBase.setText(preferences.getString("etBase", ""));
    }

    //region Para os dados não se perderem ao girar a tela (Não entra mais no onCreate ao girar)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hideSoftKeyboard(this); // Ocultar teclado ao girar a tela
    }
    //endregion

    //region Função para ocultar teclado
    public static void hideSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //endregion
}