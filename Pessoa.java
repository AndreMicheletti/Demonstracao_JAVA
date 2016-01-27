package application.cadastro;

public class Pessoa {

	private int    cpf;
	protected String nome;
	protected String sexo;
	protected String endereco;
	protected String telefone;
	protected String celular;
	protected String dataNasc;
	protected String rg;
	protected String observacao;

	public Pessoa() {
		this(0, "", "", "", "", "", "", "", "");
	}
	

	public Pessoa(int cpf, String nome, String sexo, String endereco, String telefone,
			String celular, String dataNasc, String rg, String observacao) {
		
		this.setCpf(cpf);
		this.setNome(nome);
		this.setSexo(sexo);
		this.setCelular(celular);
		this.setEndereco(endereco);
		this.setTelefone(telefone);
		this.setDataNasc(dataNasc);
		this.setRg(rg);
		this.setObservacao(observacao);
		
	}	
	
	protected void alterarCadastro(int cpf, String nome, String sexo, String endereco, String telefone,
			String celular, String dataNasc, String rg, String observacao) {
		
		this.setCpf(cpf);
		this.setNome(nome);
		this.setSexo(sexo);
		this.setCelular(celular);
		this.setEndereco(endereco);
		this.setTelefone(telefone);
		this.setDataNasc(dataNasc);
		this.setRg(rg);
		this.setObservacao(observacao);
		
	}
	
	protected void alterarObservacao(String observacao) {
		this.setObservacao(observacao);
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public String getRg() {
		return rg;
	}


	public void setRg(String rg) {
		this.rg = rg;
	}


	public String getDataNasc() {
		return dataNasc;
	}


	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}


	public String getCelular() {
		return celular;
	}


	public void setCelular(String celular) {
		this.celular = celular;
	}


	public String getTelefone() {
		return telefone;
	}


	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getSexo() {
		return sexo;
	}


	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public int getCpf() {
		return cpf;
	}


	public void setCpf(int cpf) {
		this.cpf = cpf;
	}
}
