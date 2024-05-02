package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Musico {
	private int id;
	private String descricao;
	private String nomeCompleto;
	private String senha;
	private float cache;
	private String instrumento;
	private String objetivo;
	private String estilo;

	
	public Musico() {
		id = -1;
		descricao = "";
		cache = 0.00F;
		nomeCompleto = "";
		senha = "";
		instrumento = "";
		objetivo = "";
		estilo = "";
	}

	public Musico(int id, String nome,String senha,String objetivo,String instrumento,String descricao, float cache,String estilo) {
		setId(id);
		setNome(nome);
		setSenha(senha);
		setDescricao(descricao);
		setCache(cache);
		setInstrumento(instrumento);
		setObjetivo(objetivo);
		setEstilo(estilo);
	}		
	
	public int getID() {
		return id;
	}
	public float getCache() {
		return cache;
	}
	public String getInstrumento() {
		return instrumento;
	}
	public String getSenha() {
		return senha;
	}
	public String getObjetivo() {
		return objetivo;
	}
	public String getNome() {
		return nomeCompleto;
	}
	public String getEstilo() {
		return estilo;
	}
	
	public void setCache(float cache) {
		this.cache = cache;
	}
	
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void setNome(String name) {
		this.nomeCompleto = name;
	}
	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Musico: " + nomeCompleto + "   Cache: R$" + cache + "   Estilo: " + estilo + "   Instrumento: "
				+ instrumento  + "   Descrição: " + descricao+ "   Objetivo: "+ objetivo;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Musico) obj).getID());
	}	
}