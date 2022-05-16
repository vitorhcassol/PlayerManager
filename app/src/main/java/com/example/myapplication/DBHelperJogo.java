package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelperJogo extends SQLiteOpenHelper {
    //Atributos do banco de dados
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Jogos.db";

    //Atributos da classe time
    private static final String TABLE_TIME = "time";
    private static final String COL_IDTIME = "idTime";
    private static final String COL_DESCRICAO = "descricao";

    //Atributos da classe jogador
    private static final String TABLE_JOGADOR = "jogador";
    private static final String COL_ID = "id";
    private static final String COL_ID_TIME = "idTime";
    private static final String COL_NOME = "nome";
    private static final String COL_CPF = "cpf";
    private static final String COL_ANONASCIMENTO = "anoNascimento";


    //Atributo/Código de criação da tabela Time
    private static final String TABLE_CREATE_TIME="create table "+TABLE_TIME+
            "("+COL_IDTIME+" integer primary key autoincrement, "+
            COL_DESCRICAO+" text not null);";

    //Atributo/Código de criação da tabela Jogador
    private static final String TABLE_CREATE_JOGADOR="create table "+TABLE_JOGADOR+
            "("+COL_ID+" integer primary key autoincrement, "+
            COL_ID_TIME+" text, "+ COL_NOME+" text not null, "+
            COL_CPF+" text not null, "+ COL_ANONASCIMENTO+" integer not null);";

    SQLiteDatabase db;
    public DBHelperJogo(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Criação do BD juntamente com a criação da tabela existente nele
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_TIME);
        db.execSQL(TABLE_CREATE_JOGADOR);
        this.db = db;
    }

    //Drop tables if exists
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String queryUm = "DROP TABLE IF EXISTS "+ TABLE_TIME;
        db.execSQL(queryUm);
        String queryDois = "DROP TABLE IF EXISTS "+ TABLE_JOGADOR;
        db.execSQL(queryDois);
        this.onCreate(db);
    }

    //======== Métodos do Time =================================================

    //Método que insere um time
    public void insereTime(Time t){
        db=this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values=new ContentValues();
            values.put(COL_DESCRICAO, t.getDescricao());
            db.insertOrThrow(TABLE_TIME, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.d(TAG, "Erro ao inserir um time no banco de dados");
        } finally {
            db.endTransaction();
        }
    }

    //Método que busca e retorna o IdTime de um time
    public String buscarIdTime(String time) {
        db = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = ?",
                COL_IDTIME, TABLE_TIME, COL_IDTIME);
        String idTime = "idTime não encontrada";
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(time)});
            try {
                if (cursor.moveToFirst()) {
                    idTime = cursor.getString(0);
                    db.setTransactionSuccessful();
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Time não encontrado.");
        } finally {
            db.endTransaction();
        }
        return idTime;
    }

    //Método que lista todos os times
    public ArrayList<Time> buscarTimes(){
        String[] colunas={COL_IDTIME, COL_DESCRICAO};
        Cursor cursor = getReadableDatabase().query(TABLE_TIME,colunas,null,
                null,null,null,"upper(nome)",null);
        ArrayList<Time> list = new ArrayList<Time>();
        while (cursor.moveToNext()) {
            Time t = new Time();
            t.setIdTime(cursor.getInt(0));
            t.setDescricao(cursor.getString(1));
            list.add(t);
        }
        return list;
    }

    //Método para excluir um time
    public long excluirTime(Time time){
        long retornoDB;
        db=this.getWritableDatabase();
        String[] args = {String.valueOf(time.getIdTime())};
        retornoDB=db.delete(TABLE_TIME, COL_IDTIME+"=?", args);
        return retornoDB;
    }

    //Método de atualização de um time
    public long atualizarTime(Time time){
        long retornoDB;
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_DESCRICAO, time.getDescricao());
        String[] args = {String.valueOf(time.getIdTime())};
        retornoDB = db.update(TABLE_TIME, values, "id=?", args);
        db.close();
        return retornoDB;
    }



    //======== Métodos do Jogador ==============================================

    //Método que insere um jogador
    public void insereJogador(Jogador j) {
        db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_NOME, j.getNome());
            values.put(COL_CPF, j.getCpf());
            values.put(COL_ANONASCIMENTO, j.getAnoNascimento());
            db.insertOrThrow(TABLE_JOGADOR, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Erro ao tentar inserir um jogador no banco de dados");
        } finally {
            db.endTransaction();
        }
    }

    //Método que busca e retorna o id de um jogador
    public String buscarId(String jogador) {
        db = this.getReadableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE %s = ?",
                COL_ID, TABLE_JOGADOR, COL_ID);
        String id = "não encontrado";
        db.beginTransaction();;
        try {
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(jogador)});
            try {
                if (cursor.moveToFirst()) {
                    id = cursor.getString(0);
                    db.setTransactionSuccessful();
                }
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception e){
            Log.d(TAG, "Erro ao tentar buscar o id do jogador");
        } finally {
            db.endTransaction();
        }
        return id;
    }

    //Método que lista todos os jogadores
    public ArrayList<Jogador> buscarJogador(){
        String[] colunas = { COL_ID, COL_ID_TIME, COL_NOME, COL_CPF, COL_ANONASCIMENTO};
        Cursor cursor = getReadableDatabase().query(TABLE_JOGADOR, colunas, null,
                null, null, null, "upper(nome", null);
        ArrayList<Jogador> listaJogador = new ArrayList<Jogador>();
        while(cursor.moveToNext()){
            Jogador j = new Jogador();
            j.setId(cursor.getInt(0));
            j.setIdTime(cursor.getInt(1));
            j.setNome(cursor.getString(2));
            j.setCpf(cursor.getString(3));
            j.setAnoNascimento(cursor.getInt(4));
            listaJogador.add(j);
        }
        return listaJogador;
    }

    //Método para excluir um jogador
    public long excluirJogador(Jogador j) {
        long retornoBD;
        db = this.getWritableDatabase();
        String[] args = {String.valueOf(j.getId())};
        retornoBD = db.delete(TABLE_JOGADOR, COL_ID+"=?",args);
        return retornoBD;
    }

    //Método de atualização de um jogador
    public long atualizarContato(Jogador j){
        long retornoBD;
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID_TIME,j.getIdTime());
        values.put(COL_NOME,j.getNome());
        values.put(COL_CPF, j.getCpf());
        values.put(COL_ANONASCIMENTO,j.getAnoNascimento());
        String[] args = {String.valueOf(j.getId())};
        retornoBD=db.update(TABLE_JOGADOR,values,"id=?",args);
        db.close();
        return retornoBD;
    }
}
