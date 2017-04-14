package com.artes.alexbispo.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.artes.alexbispo.agenda.model.Aluno;

public class FormularioActivity extends AppCompatActivity {

    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Intent intent = getIntent();
        Long id_aluno = (Long) intent.getSerializableExtra("id_aluno");
        if(id_aluno != null){
            aluno = Aluno.find(this, id_aluno);
            if(aluno != null) {
                aluno.fillFormulario(this);
            }
        } else {
            aluno = new Aluno(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                aluno.fromFormulario(this);
                if (aluno.save()){
                    Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " salvo com sucesso!", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
