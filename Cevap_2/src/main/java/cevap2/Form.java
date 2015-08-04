package cevap2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.EventQueue;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import javax.swing.JComboBox;

import net.sf.ehcache.*;

import com.google.code.jspot.Results;
import com.google.code.jspot.Spotify;
import com.google.code.jspot.Track;

public class Form extends JFrame {

	private JPanel contentPane;
	private final static Logger logger = LoggerFactory.getLogger(Form.class);

	/**
	 * Launch the application.
	 */
	public static JComboBox comboBox = new JComboBox();
	Label lbl = new Label();
	static int count = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					
					Form frame = new Form();
					//Apiyi  kullanarak spotify sinifi yaratmak.
					Spotify spotify = new Spotify();
					//Apiden bu sarkicinin tracklarini getirmek
					Results<Track> results = spotify
							.searchTrack("artist:weezer");
					for (Track track : results.getItems()) {
                  //Tracklari listeliyor
						comboBox.addItem(track.getName());

					}
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Form() {
		//Cache manager instance getirme
		CacheManager cm = CacheManager.getInstance();
		logger.debug("Starting Ehcache...");
     //Cache getirmek
		final Cache cache = cm.getCache("trackCache");

		if (cache == null) {
			cm.addCache("trackCache");
		}
		final JTextField jTextField = new JTextField();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
//Track buttonu track aramak icin yaratik
		JButton btnArama = new JButton("Arama");

		lbl.setBounds(35, 150, 300, 20);
		lbl.setVisible(true);
		lbl.setText("Track name");
		contentPane.add(lbl);
		btnArama.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try {
                //Cacheten veri cekmek
					Element ele = cache.get(jTextField.getText());

					lbl.setText(ele.getObjectValue().toString());
					logger.debug("element : {}", ele);					

				} catch (Exception e) {

					lbl.setText("Aranan taga uygun track bulunamadi  " + e);
				}

			}
		});
		btnArama.setBounds(35, 91, 89, 23);
		contentPane.add(btnArama);

		comboBox.setBounds(35, 36, 200, 20);
		contentPane.add(comboBox);

		jTextField.setBounds(250, 36, 150, 20);
		contentPane.add(jTextField);

		JButton btnAddTag = new JButton("Add Tag");
		btnAddTag.setBounds(250, 91, 150, 20);
		contentPane.add(btnAddTag);

		btnAddTag.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
           //Cache tag eklemek
				cache.put(new Element(jTextField.getText(), comboBox
						.getSelectedItem().toString()));
				logger.debug("cache : {}", cache);

			}
		});

	}
}
