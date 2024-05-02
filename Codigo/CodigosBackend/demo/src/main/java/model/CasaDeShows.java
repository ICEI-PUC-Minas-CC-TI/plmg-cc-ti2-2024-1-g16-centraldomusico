package model;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CasaDeShows {
	private int id;
	private String nome;
	private LocalDateTime horario;
	private float valor;
	private String endereco;
	private String nomeDono;
	private String telefonedono;
	
	public CasaDeShows() {
		id = -1;
		nome = "";
		valor = 0.00F;
		endereco = "";
		nomeDono = "";
		telefonedono = "";
		horario = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	public CasaDeShows(int id, String nomeCasa, String nomeDono, float valor, String endereco, String telefone, LocalDateTime horarios) {
		setId(id);
		setNome(nomeCasa);
		setNomeDono(nomeDono);
		setValor(valor);
		setEndereco(endereco);
		setTelefone(telefone);
		setHorario(horarios);
	}		
	public String getTelefone() {
		return telefonedono;
	}
	public void setTelefone(String telefone) {
		this.telefonedono = telefone;
	}
	public String getNomeDono() {
		return nomeDono;
	}
	public void setNomeDono(String nomedono) {
		this.nomeDono = nomedono;
	}
	public void setHorario (LocalDateTime horario) {
		LocalDateTime agora = LocalDateTime.now();
		if (agora.compareTo(horario) >= 0)
			this.horario = horario;
	}	
	
	public LocalDateTime getHorarios() {
		return horario;
	}

	public int getID() {
		return id;
	}
	public float getValor() {
		return valor;
	}


	public String getNome() {
		return nome;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}
	


	public void setNome(String name) {
		this.nome = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}




	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Casa de Festas: " + nome + "   Valor: R$" + valor + "   Nome do Dono: " + nomeDono+ "   Telefone: " + telefonedono + "   Endereço: " + endereco+ 
				 "   Horario: "+ horario;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((Musico) obj).getID());
	}	
}