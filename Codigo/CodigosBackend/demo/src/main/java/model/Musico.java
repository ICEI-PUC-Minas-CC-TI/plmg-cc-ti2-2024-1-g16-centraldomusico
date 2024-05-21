package model;

public class Musico {
    private int id;
    private String nome;
    private String descricao;
    private String senha;
    private float cache;
    private String instrumento1;
    private String instrumento2;
    private String instrumento3;
    private String objetivo;
    private String estilo;

    public Musico(int id, String nome, String descricao, String senha, float cache, String instrumento1, String instrumento2, String instrumento3, String objetivo, String estilo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.senha = senha;
        this.cache = cache;
        this.instrumento1 = instrumento1;
        this.instrumento2 = instrumento2;
        this.instrumento3 = instrumento3;
        this.objetivo = objetivo;
        this.estilo = estilo;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public float getCache() {
        return cache;
    }

    public void setCache(float cache) {
        this.cache = cache;
    }

    public String getInstrumento1() {
        return instrumento1;
    }

    public void setInstrumento1(String instrumento1) {
        this.instrumento1 = instrumento1;
    }

    public String getInstrumento2() {
        return instrumento2;
    }

    public void setInstrumento2(String instrumento2) {
        this.instrumento2 = instrumento2;
    }

    public String getInstrumento3() {
        return instrumento3;
    }

    public void setInstrumento3(String instrumento3) {
        this.instrumento3 = instrumento3;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }
}
