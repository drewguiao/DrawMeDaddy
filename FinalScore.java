
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class FinalScore extends JFrame{
		   
		public FinalScore(String scoreData){
		   JFrame frame = new JFrame();
		   frame.setSize(100,100);

		   Container conitainer = frame.getContentPane();
		   conitainer.setLayout(new BorderLayout());

		   JPanel scorePanel = new JPanel();
		   JTextArea scoreArea = new JTextArea();
		   scoreArea.setText(scoreData);

		   scorePanel.add(scoreArea);
		   conitainer.add(scorePanel,BorderLayout.CENTER);

		   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		   frame.setVisible(true);
		}
}