package com.artes.alexbispo.agenda;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.artes.alexbispo.agenda.model.Aluno;

import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class ListaAlunosActivity extends AppCompatActivity {

    private static final int CALL_PHONE_REQUEST_CODE = 10;
    private ListView listaAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        Button btnNovoAluno = (Button) findViewById(R.id.novo_aluno);
        btnNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);
            }
        });
        listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);
                Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intent.putExtra("id_aluno", aluno.getId());
                startActivity(intent);
            }
        });
        registerForContextMenu(listaAlunos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadAlunosTask().execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(adapterContextMenuInfo.position);

        MenuItem callItem =  menu.add("Ligar");
        callItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, new String[]{CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
                }
                return false;
            }
        });

        MenuItem smsItem = menu.add("Enviar SMS");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + aluno.getTelefone()));
        smsItem.setIntent(smsIntent);

        MenuItem siteItem = menu.add("Ir para o site");
        Intent siteIntent = new Intent(Intent.ACTION_VIEW);
        String siteUri = aluno.getSite();
        if(!aluno.getSite().startsWith("http://")){
            siteUri = "http://" + siteUri;
        }
        siteIntent.setData(Uri.parse(siteUri));
        siteItem.setIntent(siteIntent);

        MenuItem mapItem = menu.add("Mapa");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("geo:0,0?z=14&q=" + aluno.getEndereco()));
        mapItem.setIntent(mapIntent);

        MenuItem removeMenuItem = menu.add("Remover");
        removeMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                aluno.destroy(ListaAlunosActivity.this);
                new LoadAlunosTask().execute();
                return false;
            }
        });
    }

    private class LoadAlunosTask extends AsyncTask<Void, Void, List<Aluno>> {

        @Override
        protected List<Aluno> doInBackground(Void... voids) {
            Aluno aluno = new Aluno();
            List<Aluno> alunos = aluno.all(ListaAlunosActivity.this);
            return alunos;
        }

        @Override
        protected void onPostExecute(List<Aluno> alunos) {
            ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(ListaAlunosActivity.this, android.R.layout.simple_list_item_1, alunos);
            listaAlunos.setAdapter(adapter);
        }
    }
}
