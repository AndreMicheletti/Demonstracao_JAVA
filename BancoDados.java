package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class BancoDados {
	
	private static String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private static String user = "BOLADEX";
	private static String password = "123";
	private static boolean connected = false;
	
	// Variavel de Conexão
	private static Connection conn = null;
	private static ResultSet  QueryResult = null;
	
	public static boolean startConnection() {
		try {
			// Procura pela classe Driver
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();  
			// Abre a conexão
	        conn = DriverManager.getConnection(url,user,password); 
			// imprime na tela
			System.out.println("Conexão obtida com sucesso.");
			// Retorna sucesso
			connected = true;
			// Muda o DATE FORMAT
			sendQuery("alter session set NLS_DATE_FORMAT = 'DD-MM-YYYY'");
			return true;
		} catch (SQLException ex) {
			// se ocorrem erros de conexão
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			// se ocorrerem outros erros
			System.out.println("Problemas ao tentar conectar com o banco de dados: \nMétodo startConnection \n" + e);
		}
		// Retorna falha
		return false;
	}
	
	public static Connection getConnection() throws Exception {
		if (connected) {
			return conn;
		} else {
			throw new Exception("Não conectado ao banco de dados");
		}
	}
	
	public static boolean sendQuery(String query) {
		if (connected) {
			try {
				Statement st = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				QueryResult = st.executeQuery(query);
				return true;
			} catch (SQLException ex) {
				// se ocorrem erros de sql
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
				System.out.println("StackTrace: \n" ); ex.printStackTrace();
			} catch (Exception e) {
				// se ocorrerem outros erros
				System.out.println("Problemas ao tentar conectar com o banco de dados:\nMétodo sendQuery \n" + e);
			}
		} else {
			System.out.println("Banco de Dados não foi conectado \n");	
		}
		return false;
	}	
	
	public static boolean sendNonQuery(String query) {
		if (connected) {
			try {
				Statement st = conn.createStatement();
				st.execute(query);
				return true;
			} catch (SQLException ex) {
				// se ocorrem erros de sql
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
				System.out.println("StackTrace: \n" ); ex.printStackTrace();
			} catch (Exception e) {
				// se ocorrerem outros erros
				System.out.println("Problemas ao tentar conectar com o banco de dados:\nMétodo sendQuery \n" + e);
			}
		} else {
			System.out.println("Banco de Dados não foi conectado \n");	
		}
		return false;
	}	
	
	public static ResultSet getResult() {
		if (connected) {
			return QueryResult;
		} else {
			System.out.println("Banco de Dados não foi conectado \n");
		}	
		
		return null;
	}
	
	public static ArrayList<String> getColumn(String columnLabel) {
		if (connected) {
			ArrayList<String> returnResult = new ArrayList<String>();
			
			if (QueryResult == null) 
				return returnResult;
			
			try {			
				while (QueryResult.next()) {
					returnResult.add(QueryResult.getString(columnLabel));
				}
			} catch (SQLException e) {
	        	System.out.println("Erro no método getColumn da classe BancoDados\n");
				e.printStackTrace();
			}
			
			return returnResult;
			
		} else {
			System.out.println("Banco de Dados não foi conectado \n");
		}	
		
		return null;	
	}
	
	public static void criarArquivoBackup() {

		Date data = new Date();
	 
		SimpleDateFormat dfData;
		SimpleDateFormat dfHora;

		dfData = new SimpleDateFormat("dd.MM.yyyy"); 
		dfHora = new SimpleDateFormat("HHmmss"); 

		String dataHoje = dfData.format(data); 
		String horaHoje = dfHora.format(data); 

		String nomeArquivo = Sistema.backupFolder + dataHoje + "_" + horaHoje + ".bol";	
		
		if (!FileManager.existe(Sistema.backupFolder)) {
			System.out.println("Criando pasta padrão de Backup\n");
			FileManager.criarPasta(Sistema.backupFolder);
		}
		
		System.out.println("Gravando arquivo chamado '" + nomeArquivo + "'\n");
		FileManager.gravarArq(nomeArquivo, criarScriptBackup());
	}
	
	public static void restoreBackup(String filename) {
		
		try {
			
			// Truncate nas Tabelas
			sendNonQuery("delete from tb_locacao_quadra");
			sendNonQuery("delete from tb_chamada");
			sendNonQuery("delete from tb_aluno");
			sendNonQuery("delete from tb_uniformes");
			sendNonQuery("delete from tb_turma");
			sendNonQuery("delete from tb_funcionario");
			sendNonQuery("delete from tb_almoxarifado");
			sendNonQuery("delete from tb_professor");
			sendNonQuery("delete from tb_usuario");
			
			String conteudo = FileManager.lerArq(filename);
			
			for (String insertQuery : conteudo.split(";")) {
				//System.out.println("EXECUTANDO :\n" + insertQuery);
				sendNonQuery(insertQuery);
			}
			
		} catch (Exception ex) {
			
			System.out.println("Erro na restauração do backup");
			ex.printStackTrace();
			
		}
	}
	
	public static void closeConnection() {
		if (connected) {
			try {
				conn.close();
				connected = false;
			} catch (Exception e) {
	        	System.out.println("Erro no método closeConnection da classe BancoDados\n");
				e.printStackTrace();
			}
		} else {
			System.out.println("Banco de Dados não foi conectado \n");
		}
	}
	
	public static String criarScriptBackup() {
		String script = "";
		
		try {
			
			ResultSet r;				
			
			// TABELA PROFESSOR
			BancoDados.sendQuery("select * from tb_professor");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_professor values (";
				script += r.getInt(1) + ", ";
				script += "'" + r.getString(2) + "', ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5) + "', ";
				script += "'" + r.getString(6) + "', ";
				script += "'" + r.getString(7) + "', ";
				script += "'" + Sistema.bancoToData(r.getString(8)) + "', ";
				script += "'" + r.getString(9) + "', ";
				script += "'" + r.getString(10) + "', ";
				script += "'" + r.getString(11) + "', ";
				script += "'" + r.getString(12) + "', ";
				script += r.getInt(13) + ", ";
				script += "'" + r.getString(14) + "', ";
				script += "'" + r.getString(15) + "', ";
				script += "'" + r.getString(16) + "', ";
				script += "'" + r.getString(17) + "', ";
				script += "'" + r.getString(18) + "', ";
				script += "'" + r.getString(19);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA	
			
			// TABELA ALMOXARIFADO
			BancoDados.sendQuery("select * from tb_almoxarifado");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_almoxarifado values (";
				script += r.getInt(1) + ", ";
				script += "'" + r.getString(2) + "', ";
				script += "'" + r.getString(3) + "', ";
				script += r.getInt(4) ;
				script += ");\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA	
			
			// TABELA FUNCIONARIO
			BancoDados.sendQuery("select * from tb_funcionario");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_funcionario values (";
				script += r.getInt(1) + ", ";
				script += "'" + r.getString(2) + "', ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5) + "', ";
				script += "'" + r.getString(6) + "', ";
				script += "'" + r.getString(7) + "', ";
				script += "'" + r.getString(8) + "', ";
				script += "'" + Sistema.bancoToData(r.getString(9)) + "', ";
				script += "'" + r.getString(10) + "', ";
				script += "'" + r.getString(11) + "', ";
				script += r.getInt(12) + ", ";
				script += "'" + r.getString(13) + "', ";
				script += "'" + r.getString(14) + "', ";
				script += "'" + r.getString(15) + "', ";
				script += "'" + r.getString(16) + "', ";
				script += "'" + r.getString(17) + "', ";
				script += "'" + r.getString(18) + "', ";
				script += "'" + r.getString(19) + "', ";
				script += "'" + r.getString(20);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA	
			
			// TABELA TURMA
			BancoDados.sendQuery("select * from tb_turma");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_turma values (";
				script += r.getInt(1) + ", ";
				script += r.getInt(2) + ", ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5) + "', ";
				script += "'" + r.getString(6) + "', ";
				script += "'" + r.getString(7) + "', ";
				script += "'" + r.getString(8);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA			
			
			// TABELA UNIFORMES
			BancoDados.sendQuery("select * from tb_uniformes");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_uniformes values (";
				script += r.getInt(1) + ", ";
				script += "'" + r.getString(2) + "', ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += r.getInt(5) + ", ";
				script += r.getInt(6) + ", ";
				script += "'" + r.getString(7);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA				
			
			// TABELA ALUNO
			BancoDados.sendQuery("select * from tb_aluno");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_aluno values (";
				script += r.getInt(1) + ", ";
				script += r.getInt(2) + ", ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5) + "', ";
				script += "'" + Sistema.bancoToData(r.getString(6)) + "', ";
				script += "'" + r.getString(7) + "', ";
				script += "'" + r.getString(8) + "', ";
				script += "'" + r.getString(9) + "', ";
				script += "'" + r.getString(10) + "', ";
				script += "'" + r.getString(11) + "', ";
				script += "'" + r.getString(12) + "', ";
				script += "'" + r.getString(13) + "', ";
				script += "'" + r.getString(14) + "', ";
				script += "'" + r.getString(15) + "', ";
				script += "'" + r.getString(16) + "', ";
				script += "'" + r.getString(17) + "', ";
				script += "'" + r.getString(18) + "', ";
				script += r.getInt(19) + ", ";
				script += "'" + r.getString(20) + "', ";
				script += "'" + r.getString(21) + "', ";
				script += "'" + r.getString(22) + "', ";
				script += "'" + Sistema.bancoToData(r.getString(23)) + "', ";
				script += "'" + r.getString(24);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA					
			
			// TABELA CHAMADA
			BancoDados.sendQuery("select * from tb_chamada");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_chamada values (";
				script += r.getInt(1) + ", ";
				script += r.getInt(2) + ", ";
				script += "'" + Sistema.bancoToData(r.getString(3)) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA
			
			r = null;
			// FIM TABELA			
			
			// TABELA LOCACAO QUADRA
			BancoDados.sendQuery("select * from tb_locacao_quadra");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_locacao_quadra values (";
				script += r.getInt(1) + ", ";
				script += r.getInt(2) + ", ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5) + "', ";
				script += "'" + Sistema.bancoToData(r.getString(6)) + "', ";
				script += "'" + r.getString(7);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA			
			
			// TABELA USUARIO
			BancoDados.sendQuery("select * from tb_usuario");
			r = BancoDados.getResult();			
			while (r.next()) {
				script += "insert into tb_usuario values (";
				script += r.getInt(1) + ", ";
				script += r.getInt(2) + ", ";
				script += "'" + r.getString(3) + "', ";
				script += "'" + r.getString(4) + "', ";
				script += "'" + r.getString(5);
				script += "');\n";
			}
			script += "\n\n";
			
			r = null;
			// FIM TABELA	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return script.replaceAll("null", "");
	}
}
