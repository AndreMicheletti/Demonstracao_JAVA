package main;

import java.awt.Component;
import java.awt.Font;
import java.util.Date;
import java.util.InputMismatchException;

import javax.swing.Icon;
import javax.swing.text.MaskFormatter;

import com.alee.extended.date.WebDateField;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.rootpane.WebFrame;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotificationPopup;

import application.TelaInicial;
import application.TelaLogin;

// Classe Principal do Sistema
public class Sistema {
	
	public static final String nomeSistema = "BolaDEX";		// Constante do Nome do Programa
	public static final boolean  needLogin = false;
	
	public static final String urlSistemaWebsite = "http://localhost:8080/Site/";
	
	public static Font fontePadraoTexto     	= new Font("SansSerif", Font.PLAIN, 12);
	public static Font fontePadraoNotificacao	= new Font("SansSerif", Font.PLAIN, 12);
	public static Font fontePadraoTitulo    	= new Font("SansSerif", Font.BOLD, 22);
	public static Font fontePadraoSubtitulo 	= new Font("SansSerif", Font.PLAIN, 18);
	
	public static String backupFolder = "C:/BackupBoladex/";
	
	public static String[] estadosArray = {
		"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",
		"MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
		"SP", "SE", "TO"
	};
	
	public static String[] mesesArray = {
		"Janeiro", "Fevereiro", "Março", "Abril",
		"Maio", "Junho", "Julho", "Agosto", "Setembro",
		"Outubro", "Novembro", "Dezembro"
	};
	
	public static String[] tamanhosArray = {
		"P", "M", "G", "GG"
	};
	
	public static String[] coresArray = {
		
	};
	
	public static String fonteTexto = "SansSerif";
	 
	private static TelaInicial telaInicial = null;  		// Variável de manipulação da tela inicial
	private static TelaLogin telaLogin = new TelaLogin();  	// Variável de manipulação da tela de login
	private static String usuarioLogado = null;				// Variável de usuário logado
	
	// Método Principal do Java
	public static void main(String[] args) {
		iniciar();
	}
	
	// Inicia o Sistema
	public static void iniciar() {
		iniciarUIManager();
		WebFrame f = new WebFrame() {
			private static final long serialVersionUID = 1L;
			public void setVisible(boolean visible) {
				super.setVisible(visible);
				if (visible == true) {
					setBackground(null);
					this.setSize(250, 50); setDefaultCloseOperation(EXIT_ON_CLOSE);
					this.setLocationRelativeTo(null);
					WebProgressBar progressBar = new WebProgressBar ();
			        progressBar.setIndeterminate ( true );
			        progressBar.setStringPainted ( true );
			        progressBar.setOpaque(true);
			        progressBar.setString ( "Conectando ao Banco..." );
					this.getContentPane().add(progressBar);	
					this.setOpacity(1f);
				}
			}
		};
		f.setUndecorated(true);
		f.setVisible(true);
		if (BancoDados.startConnection()) {
			if (needLogin) {
				abrirTelaLogin();
			} else {
				logar("A");
				abrirTelaInicial();
			}
		} else {
			WebOptionPane.showMessageDialog ( f, "Erro ao Fazer Conexão com o Banco de Dados", "Atenção", WebOptionPane.ERROR_MESSAGE );
		}
		f.dispose();
	}
	
	// Abre a tela inicial
	public static void abrirTelaInicial() {
		fecharTelaLogin();
		telaInicial = new TelaInicial();
		telaInicial.setVisible(true);
	}	
	
	// Fecha a tela inicial
	public static void fecharTelaInicial() {
		telaInicial.dispose();
		abrirTelaLogin();
	}

	// Sai do programa
	public static void sair() {
		System.out.println("Fechando Conexão com Banco");
		BancoDados.closeConnection();
	}
	
	// Abre a tela de Login
	public static void abrirTelaLogin() {
        telaLogin.setVisible(true);
	}
	
	// Fecha a tela de Login
	public static void fecharTelaLogin() {
    	telaLogin.setVisible(false);
	}
	
	// Instala e inicia o Look and Feel do programa
	private static void iniciarUIManager() {
		try {   
			javax.swing.UIManager.installLookAndFeel("Boladex", "com.alee.laf.WebLookAndFeel");
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Boladex".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        	System.out.println("Erro no método iniciarUIManager da classe Sistema\n");
        	ex.printStackTrace();
        }
	}
	
	// Mostra uma notificação temporizada
	public static void timedNotification(Component parent, String message) {
		WebNotificationPopup notificationPopup = new WebNotificationPopup ();
		notificationPopup.setIcon( NotificationIcon.information );
		notificationPopup.setFont(fontePadraoNotificacao);
        notificationPopup.setDisplayTime ( 4000 );
		notificationPopup.setContent(message);
        
        NotificationManager.showNotification (parent, notificationPopup );
	}
	
	// Mostra uma notificação temporizada
	public static void timedNotification(Component parent, String message, int timeInFrames) {
		WebNotificationPopup notificationPopup = new WebNotificationPopup ();
		notificationPopup.setIcon( NotificationIcon.information );
		notificationPopup.setFont(fontePadraoNotificacao);
        notificationPopup.setDisplayTime ( timeInFrames );
		notificationPopup.setContent(message);
        
        NotificationManager.showNotification (parent, notificationPopup );
	}

	// Mostra uma notificação temporizada
	public static void timedNotification(Component parent, String message, int timeInFrames, Icon icon) {
		WebNotificationPopup notificationPopup = new WebNotificationPopup ();
		notificationPopup.setFont(fontePadraoNotificacao);
        notificationPopup.setDisplayTime ( timeInFrames );
		notificationPopup.setContent(message);
        notificationPopup.setIcon( icon );
        
        NotificationManager.showNotification (parent, notificationPopup );
	}

	// Retorna o usuário logado
	public static String getUsuarioLogado() {
		return usuarioLogado;
	}
	
	// Loga com um determinado usuario
	public static void logar(String usuario) {
		System.out.println("Logando como :" + usuario);
		usuarioLogado = usuario;
	}
	
	// Desloga o Sistema
	public static void deslogar() {
		timedNotification(telaLogin,  (Sistema.getUsuarioLogado().equals("A") ? "Administrador" : "Funcionário") +" deslogado.");
		System.out.println("Deslogando"); usuarioLogado = null;
		fecharTelaInicial();
		abrirTelaLogin();
	}
	
	// Retorna a permissão adm
	public static boolean permissaoADM() {
		return usuarioLogado.equals("A");
	}
	
	public static TelaInicial getTelaInicial() throws NullPointerException {
		return telaInicial;		
	}
	
	public static MaskFormatter fazerMascara(String mascara) {
		
		MaskFormatter f_mascara = new MaskFormatter();
		
		try {
			
			f_mascara.setMask(mascara);
			f_mascara.setPlaceholderCharacter(' ');
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return f_mascara;
	}public static boolean isCPF(String CPF) {
		// considera-se erro CPF's formados por uma sequencia de numeros iguais
	    if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
	        CPF.equals("22222222222") || CPF.equals("33333333333") ||
	        CPF.equals("44444444444") || CPF.equals("55555555555") ||
	        CPF.equals("66666666666") || CPF.equals("77777777777") ||
	        CPF.equals("88888888888") || CPF.equals("99999999999") ||
	       (CPF.length() != 11))
	       return(false);

	    char dig10, dig11;
	    int sm, i, r, num, peso;

	// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
	    try {
	// Calculo do 1o. Digito Verificador
	      sm = 0;
	      peso = 10;
	      for (i=0; i < 9; i++) {              
	// converte o i-esimo caractere do CPF em um numero:
	// por exemplo, transforma o caractere '0' no inteiro 0         
	// (48 eh a posicao de '0' na tabela ASCII)         
	        num = (int)(CPF.charAt(i) - 48); 
	        sm = sm + (num * peso);
	        peso = peso - 1;
	      }

	      r = 11 - (sm % 11);
	      if ((r == 10) || (r == 11))
	         dig10 = '0';
	      else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

	// Calculo do 2o. Digito Verificador
	      sm = 0;
	      peso = 11;
	      for(i=0; i < 10; i++) {
	        num = (int)(CPF.charAt(i) - 48);
	        sm = sm + (num * peso);
	        peso = peso - 1;
	      }

	      r = 11 - (sm % 11);
	      if ((r == 10) || (r == 11))
	         dig11 = '0';
	      else dig11 = (char)(r + 48);

	// Verifica se os digitos calculados conferem com os digitos informados.
	      if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
	         return(true);
	      else return(false);
	    } catch (InputMismatchException erro) {
	        return(false);
	    }
	  }
	
	@SuppressWarnings("deprecation")
	public static int getDiaDaSemana() {
		return new Date().getDay() + 1;
	}

	@SuppressWarnings("deprecation")
	public static int getDiaDaSemana(Date date) {
		return date.getDay() + 1;
	}
	
	public static String bancoToData(String data) {
		String r = data.replace("00:00:00.0", "");		
		return r.substring(8, 10) + "." +  data.substring(5, 7) + "." +  data.substring(0, 4);	
	}
	
	public static String getDateHoje() {
		
		WebDateField field = new WebDateField();
		field.setDate(new Date());
		
		return field.getText();
	}
	
}
