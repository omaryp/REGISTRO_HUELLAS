package pe.com.gym.frm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Aplicativo para el registro huellas de los clientes
 * 
 * @author Omar Yarleque
 * 
 */
public class frmTerminos extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel lbl_titulo;
	private JTextArea txt_terminos;
	private JScrollPane scrl_area;
	private JCheckBox chk_aceptar;
	private JButton btn_aceptar;
	private JButton btn_salir;
	private JPanel png_sur;
	private JPanel png_area;
	private JPanel png_botones;

	public frmTerminos(JFrame padre) {
		super(padre, true);
		this.setSize(new Dimension(700, 400));
		this.setTitle("TERMINOS Y CONDICIONES");
		this.setLayout(new BorderLayout());
		init_componentes();
		crea_pantalla();
		registra_componentes();
		setVisible(true);
	}
	public void init_componentes() {
		lbl_titulo = new JLabel("<html><span style='font-size : 12px'>Términos y condiciones </span></html>");
		txt_terminos = new JTextArea(13,80);
		scrl_area = new JScrollPane(txt_terminos);
		btn_aceptar = new JButton("Aceptar");
		btn_salir = new JButton("Salir");
		png_sur = new JPanel();
		png_botones = new JPanel();
		png_area = new JPanel();
		chk_aceptar = new JCheckBox("Acepta los terminos y condiciones.");
	}

	public void crea_pantalla() {
		png_area.setLayout(new BorderLayout());
		png_area.add(scrl_area,BorderLayout.CENTER);
		png_area.setBorder(BorderFactory.createTitledBorder(""));
		png_botones.setLayout(new FlowLayout());
		png_botones.add(btn_aceptar);
		png_botones.add(btn_salir);
		png_sur.setLayout(new BorderLayout());
		png_sur.add(chk_aceptar,BorderLayout.NORTH);
		png_sur.add(png_botones,BorderLayout.CENTER);	
		this.add(lbl_titulo,BorderLayout.NORTH);
		this.add(png_area,BorderLayout.CENTER);
		this.add(png_sur, BorderLayout.SOUTH);
	}

	public void registra_componentes() {
		btn_aceptar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				//guardarHuella();
			}
		});
	}

}