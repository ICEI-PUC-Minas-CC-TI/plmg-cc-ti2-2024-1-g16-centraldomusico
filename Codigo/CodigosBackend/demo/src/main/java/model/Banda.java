package model;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.sql.Date;
import java.time.LocalDateTime;
//importar data
import java.time.format.DateTimeFormatter;

public class Banda {
	private int id;
	private String descricao;
	private String nomeBanda;
	private float cache;
	private String estilo;
	private Date dataCriacao;	
	private String senha;
	private String objetivo;
	
	
	public Banda() {
		id = -1;
		descricao = "";
		senha="";
		cache = 0.00F;
		nomeBanda = "";
		estilo = "";
		LocalDateTime dataTimestamp = LocalDateTime.now();
		Date data = Date.valueOf(dataTimestamp.toLocalDate());
		dataCriacao = data;
	}

	public Banda(int id, String nome,String descricao, String senha ,float cache,String estilo,String objetivo) {
		setId(id);
		setNome(nome);
		setDescricao(descricao);
		setSenha(senha);
		setCache(cache);
		setEstilo(estilo);
		LocalDateTime dataTimestamp = LocalDateTime.now();
		Date data = Date.valueOf(dataTimestamp.toLocalDate());
		setDataCriacao(data);
		setObjetivo(objetivo);
	}		
	
	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenha() {
		return senha;
	}

	public void setDataCriacao (Date dataCriacao) {
		LocalDateTime agoraTimestamp = LocalDateTime.now();
		Date agora = Date.valueOf(agoraTimestamp.toLocalDate());
		if (agora.compareTo(dataCriacao) >= 0)
			this.dataCriacao = dataCriacao;
	}	
	
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public LocalDateTime getDataCriacaoTimestamp() {
		return dataCriacao.toLocalDate().atStartOfDay();
	}

    public void setDataCriacaoTimestamp(LocalDateTime dataCriacaoTimestamp) {
        LocalDateTime agoraTimestamp = LocalDateTime.now();
        if (agoraTimestamp.compareTo(dataCriacaoTimestamp) >= 0)
            this.dataCriacao = Date.valueOf(dataCriacaoTimestamp.toLocalDate());
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