package model;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Banda {
	private int id;
	private String descricao;
	private String nomeBanda;
	private float cache;
	private String estilo;
	private LocalDateTime dataCriacao;	
	
	public Banda() {
		id = -1;
		descricao = "";
		cache = 0.00F;
		nomeBanda = "";
		estilo = "";
		dataCriacao = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public Banda(int id, String nome,String descricao, float cache,String estilo,LocalDateTime criacao) {
		setId(id);
		setNome(nome);
		setDescricao(descricao);
		setCache(cache);
		setEstilo(estilo);
		setDataCriacao(criacao);
	}		
	
	public void setDataCriacao (LocalDateTime dataCriacao) {
		LocalDateTime agora = LocalDateTime.now();
		if (agora.compareTo(dataCriacao) >= 0)
			this.dataCriacao = dataCriacao;
	}	
	
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public int getID() {
		return id;
	}
	public float getCache() {
		return cache;
	}


	public String getNome() {
		return nomeBanda;
	}
	public String getEstilo() {
		return estilo;
	}
	
	public void setCache(float cache) {
		this.cache = cache;
	}
	


	public void setNome(String name) {
		this.nomeBanda = name;
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
		return "Banda: " + nomeBanda + "   Cache: R$" + cache + "   Estilo: " + estilo + "   Descrição: " + descricao+ 
				 "   Data de Criação: "+ dataCriacao;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Banda) obj).getID());
	}	
}