package application.cadastro;

public class Aluno extends Pessoa {
	
	private int matricula;
	private String nomePai;
	private String nomeMae;
	private boolean ativo;
	private Turma turma;
	private boolean transporte;
	private double mensalidade;
	
	public Aluno() {
		this(0, 0, "", "", "", "", "", "", "", "", "", "", null, false, 0.0);
	}
	
	public Aluno(int matricula, int cpf, String nome, String sexo, String endereco, String telefone,
			String celular, String dataNasc, String rg, String observacao, String nomePai, String nomeMae,
			Turma turma, boolean transporte, double mensalidade) {
		
		this.setCpf(cpf);
		this.setNome(nome);
		this.setSexo(sexo);
		this.setCelular(celular);
		this.setEndereco(endereco);
		this.setTelefone(telefone);
		this.setDataNasc(dataNasc);
		this.setRg(rg);
		this.setObservacao(observacao);
		this.setMatricula(matricula);
		this.setNomePai(nomePai);
		this.setNomeMae(nomeMae);
		this.setTurma(turma);
		this.setTransporte(transporte);
		this.setMensalidade(mensalidade);
		this.setAtivo(true);
		
	}
	
	protected void alterarCadastro(int matricula, int cpf, String nome, String sexo, String endereco, String telefone,
			String celular, String dataNasc, String rg, String observacao, String nomePai, String nomeMae,
			Turma turma, boolean transporte, double mensalidade, boolean ativo) {
		
		this.setCpf(cpf);
		this.setNome(nome);
		this.setSexo(sexo);
		this.setCelular(celular);
		this.setEndereco(endereco);
		this.setTelefone(telefone);
		this.setDataNasc(dataNasc);
		this.setRg(rg);
		this.setObservacao(observacao);
		this.setMatricula(matricula);
		this.setNomePai(nomePai);
		this.setNomeMae(nomeMae);
		this.setTurma(turma);
		this.setTransporte(transporte);
		this.setMensalidade(mensalidade);
		this.setAtivo(ativo);
		
	}	
	
	public void ativar() {
		this.setAtivo(true);
	}
	
	public void inativar() {
		this.setAtivo(false);
	}
	
	// GETTERS AND SETTERS

	public int getMatricula() {
		return matricula;
	}

	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public boolean isTransporte() {
		return transporte;
	}

	public void setTransporte(boolean transporte) {
		this.transporte = transporte;
	}

	public double getMensalidade() {
		return mensalidade;
	}

	public void setMensalidade(double mensalidade) {
		this.mensalidade = mensalidade;
	}
	

}
