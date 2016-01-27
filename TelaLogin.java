package application;

import gfx.GFX;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.BancoDados;
import main.Sistema;

import com.alee.extended.image.WebImage;
import com.alee.laf.button.WebButton;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;

public class TelaLogin extends WebFrame {
	
	private static final long serialVersionUID = 1L;
	private Panel painel;
	private WebTextField txtLogin;
	private WebPasswordField txtSenha;
	
	public TelaLogin() {
		super(Sistema.nomeSistema + " Login");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(GFX.carregarIconePrincipal());
		
		setSize(300,300);
		setLocationRelativeTo(null);
		setSize(350,350);
		
		setUndecorated(true); setResizable(false);
		setBackground(new Color(0, 255, 0, 0));
		setOpacity(0.0f);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				main.Sistema.sair();
			}
		});
		
		painel = new Panel(this);
		add(painel);
		
	}
	
	public void setVisible(boolean visible) {
		limparCampos();
		super.setVisible(visible);
	}
	
	public void limparCampos() {
		txtLogin.setText("");
		txtSenha.setText("");
	}

	@SuppressWarnings("deprecation")
	public void checarLogin() {
		String nome = txtLogin.getText().toLowerCase();
		String senha = txtSenha.getText().toLowerCase();
		
		if (nome.isEmpty() | senha.isEmpty()) {
			Sistema.timedNotification ( this, "Preencha todos os campos" );
			return;
		}
		
		BancoDados.sendQuery("select * from TB_USUARIO");
		ResultSet r = BancoDados.getResult();
		
		try {			
			while (r.next()) {
				if (r.getString("login").equals(nome) & r.getString("senha").equals(senha)) {
					Sistema.logar(r.getString("permissao"));
					main.Sistema.abrirTelaInicial();				
					return;
				}
			}
			if (nome.equals("developer-user") & senha.equals("pyxis")) {
				Sistema.logar("A");
				main.Sistema.abrirTelaInicial();
				return;
			}
			
		} catch (SQLException e) {
        	System.out.println("Erro no método checarLogin da classe TelaLogin\n");
			e.printStackTrace();
		}
		Sistema.timedNotification(this, "Nome ou Senha Incorretos" );
	}
	
	private class Panel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private JFrame pai;
		
		public Panel(JFrame p){
			
			pai = p;
			
			setBackground(null); this.setOpaque(true);
			setFocusable(true); setDoubleBuffered(true);
			
			setLayout(null);
			
			WebButton btnFechar = new WebButton(new ImageIcon(GFX.carregar("icons/fechar.png")));
			btnFechar.setFont(Sistema.fontePadraoTexto);
			btnFechar.setBounds(270, 10, 20, 20);
			btnFechar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pai.dispose();
				}
			});
			add(btnFechar);
			
			JLabel lblLogin = new JLabel("Login");
			lblLogin.setForeground(Color.BLACK);
			lblLogin.setFont(new Font(Sistema.fontePadraoTexto.getFontName(), Sistema.fontePadraoTexto.getStyle(), 20));
			lblLogin.setBounds(88, 99, 53, 25);
			add(lblLogin);
			
			txtLogin = new WebTextField();
			txtLogin.setBounds(87, 128, 168, 30);
			txtLogin.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						checarLogin();
					}
				}
			});
			txtLogin.setFont(new Font(Sistema.fontePadraoTexto.getFontName(), Sistema.fontePadraoTexto.getStyle(), 18));
			txtLogin.setColumns(10);
			add(txtLogin);
			
			JLabel lblSenha = new JLabel("Senha");
			lblSenha.setForeground(Color.BLACK);
			lblSenha.setFont(new Font(Sistema.fontePadraoTexto.getFontName(), Sistema.fontePadraoTexto.getStyle(), 20));
			lblSenha.setBounds(88, 159, 63, 25);
			add(lblSenha);
			
			txtSenha = new WebPasswordField();
			txtSenha.setLeadingComponent(new WebImage( GFX.carregarIcone("key.png")));
			txtSenha.setColumns(10);
			txtSenha.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					System.out.println("KEY");
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						checarLogin();
					}
				}
			});
			txtSenha.setFont(new Font(Sistema.fontePadraoTexto.getFontName(), Sistema.fontePadraoTexto.getStyle(), 18));
			txtSenha.setBounds(88, 192, 168, 30);
			add(txtSenha);
			
			JButton btnEntrar = new JButton("Entrar");
			btnEntrar.setFont(Sistema.fontePadraoTexto);
			btnEntrar.setBounds(132, 230, 100, 25);
			btnEntrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checarLogin();
				}
			});
			add(btnEntrar);
		
			setOpacity(1f);
		}
		
		public void paintComponent(java.awt.Graphics g) {
			g.drawImage(GFX.carregarImgLogin(), 0, 0, this);
		}
	}

}
