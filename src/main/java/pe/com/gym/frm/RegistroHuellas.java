package pe.com.gym.frm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pe.com.gym.delegate.Gym;
import pe.com.gym.dto.ClienteDTO;
import pe.com.gym.entidades.PlantillaHuella;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;

/**
 * Aplicativo para el registro huellas de los clientes
 * 
 * @author Omar Yarleque
 * 
 */
public class RegistroHuellas extends JFrame {

	private static final long serialVersionUID = 1L;
	private int cant_car;
	private JLabel lbl_datos_usu;
	private JLabel lbl_dni;
	private JLabel lbl_muestra1;
	private JLabel lbl_muestra2;
	private JLabel lbl_muestra3;
	private JLabel lbl_muestra4;
	private JTextField txt_dni;
	private JScrollPane scrl_area;
	private JButton btn_grabar;
	private JPanel png_datos;
	private JPanel png_dni;
	private JPanel png_muestras;
	private JPanel png_botones;
	private JPanel png_sur;
	
	private JTextArea txt_area;

	// Variables del lector
	// Varible que permite iniciar el dispositivo de lector de huella conectado
	// con sus distintos metodos.
	private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
	// Varible que permite establecer las capturas de la huellas, para determina
	// sus caracteristicas
	// y poder estimar la creacion de un template de la huella para luego poder
	// guardarla
	private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
	// Variable que para crear el template de la huella luego de que se hallan
	// creado las caracteriticas
	// necesarias de la huella si no ha ocurrido ningun problema
	private DPFPTemplate template;

	public static String TEMPLATE_PROPERTY = "template";
	private DPFPFeatureSet featuresinscripcion;

	public RegistroHuellas() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setTitle("REGISTRO DE HUELLAS");
		this.setLayout(new BorderLayout());
		cant_car = 0;
		init_componentes();
		crea_pantalla();
		registra_componentes();
		setVisible(true);
	}

	public void init_componentes() {
		lbl_datos_usu = new JLabel(
				"Bienvenido : Omar Antonio Yarleque Peña - Asistente de caja");
		lbl_dni = new JLabel("DNI : ");
		lbl_muestra1 = new JLabel();
		lbl_muestra2 = new JLabel();
		lbl_muestra3 = new JLabel();
		lbl_muestra4 = new JLabel();
		txt_dni = new JTextField(20);
		txt_area = new JTextArea(13,80);
		scrl_area = new JScrollPane(txt_area);
		btn_grabar = new JButton("Grabar");
		png_datos = new JPanel();
		png_dni = new JPanel();
		png_muestras = new JPanel();
		png_botones = new JPanel();
		png_sur = new JPanel();
	}

	public void crea_pantalla() {
		png_datos.setLayout(new BorderLayout());
		png_datos.add(lbl_datos_usu, BorderLayout.NORTH);
		png_dni.setLayout(new FlowLayout());
		png_dni.add(lbl_dni);
		png_dni.add(txt_dni);
		png_datos.add(png_dni, BorderLayout.CENTER);
		this.add(png_datos, BorderLayout.NORTH);
		png_muestras.setLayout(new GridLayout(1, 4));
		png_muestras.add(lbl_muestra1);
		png_muestras.add(lbl_muestra2);
		png_muestras.add(lbl_muestra3);
		png_muestras.add(lbl_muestra4);
		png_muestras.setBorder(BorderFactory.createTitledBorder("Muestra de huelllas"));
		this.add(png_muestras, BorderLayout.CENTER);
		png_botones.setLayout(new FlowLayout());
		png_botones.add(btn_grabar);
		png_sur.setLayout(new BorderLayout());
		png_sur.add(scrl_area,BorderLayout.CENTER);
		png_sur.add(png_botones,BorderLayout.SOUTH);
		this.add(png_sur, BorderLayout.SOUTH);
	}

	public void registra_componentes() {
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				stop();
				System.exit(0);
			}

			public void windowOpened(java.awt.event.WindowEvent evt) {
				Iniciar();
		        start();
		        EstadoHuellas();
		        btn_grabar.setEnabled(false);
			}
		});
		txt_dni.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(cant_car < 8){
					char car = e.getKeyChar();
					if(Character.isDigit(car))
						cant_car ++;
					else
						e.consume();
				}
				else
					e.consume();
			}			
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) { 
				int code = e.getKeyCode();
				if(code == KeyEvent.VK_BACK_SPACE && cant_car>0)
					cant_car --;
			}
		});
		btn_grabar.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				guardarHuella();
			}
		});
	}
	
	public void guardarHuella(){
		try {
			String dni = txt_dni.getText().trim();
			PlantillaHuella huella = null;
			int res = 0;
			if(!dni.equals("")){
				ClienteDTO cliente = Gym.INSTANCE.obtenerClienteDNI(dni);
				if(cliente!=null){
					huella = new PlantillaHuella();
					huella.setPlantilla(template.serialize());
					res = Gym.INSTANCE.guardarHuella(cliente,huella);
					if(res == 0)
						JOptionPane.showMessageDialog(null, "Se guardó correctamente su huella Sr(a). "+cliente.getNombreCliente()+" "+cliente.getApellidoCliente());
				}else
					JOptionPane.showMessageDialog(null, "Cliente no registrado.");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
			ex.printStackTrace();
		}
	}

	protected void Iniciar() {
		Lector.addDataListener(new DPFPDataAdapter() {
			@Override
			public void dataAcquired(final DPFPDataEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EnviarTexto("La Huella Digital ha sido Capturada");
						ProcesarCaptura(e.getSample());
					}
				});
			}
		});

		Lector.addReaderStatusListener(new DPFPReaderStatusAdapter() {
			@Override
			public void readerConnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EnviarTexto("El sensor de huella digital esta activado o conectado");
					}
				});
			}

			@Override
			public void readerDisconnected(final DPFPReaderStatusEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EnviarTexto("El sensor de huella digital esta desactivado o no conectado");
					}
				});
			}
		});

		Lector.addSensorListener(new DPFPSensorAdapter() {
			@Override
			public void fingerTouched(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EnviarTexto("El dedo ha sido colocado sobre el Lector de Huella");
					}
				});
			}

			@Override
			public void fingerGone(final DPFPSensorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EnviarTexto("El dedo ha sido quitado del Lector de Huella");
					}
				});
			}
		});

		Lector.addErrorListener(new DPFPErrorAdapter() {
			@SuppressWarnings("unused")
			public void errorReader(final DPFPErrorEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EnviarTexto("Error: " + e.getError());
					}
				});
			}
		});
	}

	@SuppressWarnings("incomplete-switch")
	public void ProcesarCaptura(DPFPSample sample) {
		// Procesar la muestra de la huella y crear un conjunto de
		// características con el propósito de inscripción.
		featuresinscripcion = extraerCaracteristicas(sample,DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
		int estado = 0;

		// Comprobar la calidad de la muestra de la huella y lo añade a su
		// reclutador si es bueno
		if (featuresinscripcion != null) {
			try {
				Reclutador.addFeatures(featuresinscripcion);

				// Dibuja la huella dactilar capturada.
				Image image = CrearImagenHuella(sample);
				estado = EstadoHuellas();
				switch (estado) {
					case 3:
						DibujarHuella(image,lbl_muestra1);
						break;
					case 2:
						DibujarHuella(image,lbl_muestra2);
						break;
					case 1:
						DibujarHuella(image,lbl_muestra3);
						break;
					case 0:
						DibujarHuella(image,lbl_muestra4);
						break;
				}
			} catch (DPFPImageQualityException ex) {
				System.err.println("Error: " + ex.getMessage());
			} finally {
				// Comprueba si la plantilla se ha creado.
				switch (Reclutador.getTemplateStatus()) {
					case TEMPLATE_STATUS_READY: // informe de éxito y detiene la captura de huellas
						stop();
						setTemplate(Reclutador.getTemplate());
						EnviarTexto("La plantilla de la huella ha sido sreada, ya puede guardarla");
						btn_grabar.setEnabled(true);
						btn_grabar.grabFocus();
						break;
					case TEMPLATE_STATUS_FAILED: // informe de fallas y reiniciar la captura de huellas
						Reclutador.clear();
						stop();
						setTemplate(null);
						JOptionPane.showMessageDialog(
										RegistroHuellas.this,
										"La plantilla de la huella no pudo crearse, repita el proceso",
										"Inscripcion de huellas dactilares",
										JOptionPane.ERROR_MESSAGE);
						start();
						break;
					}
			}
		}
	}
	
	public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }
	
	public Image CrearImagenHuella(DPFPSample sample) {
        return DPFPGlobal.getSampleConversionFactory().createImage(sample);
    }

    public void DibujarHuella(Image image,JLabel lbl_image) {
    	lbl_image.setIcon(new ImageIcon(image.getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_DEFAULT)));
        repaint();
    }

    public int EstadoHuellas(){
    	int estado = 0;
    	estado = Reclutador.getFeaturesNeeded();
        EnviarTexto("Muestra de huellas necesarias para guardar template " + estado );
        return estado;
    }

    public void EnviarTexto(String string) {
        txt_area.append(string + "\n");
    }

    public void start() {
        Lector.startCapture();
        EnviarTexto("Utilizando el lector de huella dactilar ");
    }

    public void stop() {
        Lector.stopCapture();
        EnviarTexto("No se está usando el lector de huella dactilar ");
    }

    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate template) {
        DPFPTemplate old = this.template;
        this.template = template;
        firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }

}