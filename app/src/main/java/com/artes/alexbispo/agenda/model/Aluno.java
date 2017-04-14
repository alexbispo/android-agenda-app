package com.artes.alexbispo.agenda.model;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.RatingBar;

import com.artes.alexbispo.agenda.FormularioActivity;
import com.artes.alexbispo.agenda.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 31/12/16.
 */

public class Aluno extends AbstractModel implements Serializable {

    private static final int DATA_BASE_VERSION = 1;

    private Context context;

    private Long id;
    private String nome;
    private  String endereco;
    private String telefone;
    private String site;
    private Double nota;

    public Aluno(Context context) {
        super(context, DATA_BASE_VERSION);
        this.context = context;
    }

    public void fromFormulario(Activity activity){
        EditText campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        EditText campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        EditText campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        EditText campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        RatingBar campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);

        setNome(campoNome.getText().toString());
        setEndereco(campoEndereco.getText().toString());
        setTelefone(campoTelefone.getText().toString());
        setSite(campoSite.getText().toString());
        setNota(Double.valueOf(campoNota.getProgress()));
    }

    public void fillFormulario(Activity activity){
        context = activity;
        EditText campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        EditText campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        EditText campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        EditText campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        RatingBar campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);

        campoNome.setText(getNome());
        campoEndereco.setText(getEndereco());
        campoSite.setText(getSite());
        campoTelefone.setText(getTelefone());
        campoNota.setProgress(getNota().intValue());
    }

    public boolean create(){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", getNome());
        contentValues.put("endereco", getEndereco());
        contentValues.put("telefone", getTelefone());
        contentValues.put("site", getSite());
        contentValues.put("nota", getNota());

        db.insert(getTableName(), null, contentValues);
        close();
        return true;
    }

    public boolean save(){
        if(getId() == null){
            return create();
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", getNome());
        contentValues.put("endereco", getEndereco());
        contentValues.put("telefone", getTelefone());
        contentValues.put("site", getSite());
        contentValues.put("nota", getNota());

        String[] params = {getId().toString()};
        db.update(getTableName(), contentValues, "id = ?", params);
        close();
        return true;
    }

    public static Aluno find(Context context, Long id_aluno) {
        Aluno aluno = new Aluno(context);
        SQLiteDatabase db = aluno.getReadableDatabase();

        String sql = "SELECT * FROM Alunos WHERE id = ?";
        String[] params = {id_aluno.toString()};

        Cursor cursor = db.rawQuery(sql, params);
        if (cursor.moveToNext()) {
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
        }
        cursor.close();
        aluno.close();
        return aluno.getId() != null ? aluno : null;
    }

    public List<Aluno> all(){
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Aluno> alunos = new ArrayList<>();

        while (cursor.moveToNext()){
            Aluno aluno = new Aluno(this.context);
            aluno.setId(cursor.getLong(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            alunos.add(aluno);
        }
        cursor.close();
        close();
        return alunos;
    }

    public void destroy() {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {getId().toString()};
        db.delete("Alunos", "id = ?", params);
        close();
    }

    public String toSql(){
        return getTableName() + " (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL)";
    }

    public String getTableName(){
        return "Alunos";
    }

    public Long getId() {
        return id;
    }

    public Aluno setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Aluno setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getEndereco() {
        return endereco;
    }

    public Aluno setEndereco(String endereco) {
        this.endereco = endereco;
        return this;

    }

    public String getTelefone() {
        return telefone;
    }

    public Aluno setTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public String getSite() {
        return site;
    }

    public Aluno setSite(String site) {
        this.site = site;
        return this;
    }

    public Double getNota() {
        return nota;
    }

    public Aluno setNota(Double nota) {
        this.nota = nota;
        return this;
    }

    @Override
    public String toString() {
        return getId() + " - " + getNome();
    }
}
