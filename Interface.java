/*
AUTHOR: JESUS ZARAGOZA
CLASS CREATES A SIMPLE INTERFACE THAT REPRESENTS A TERMINAL THAT CAN BE USED TO IMPLEMENT JSQL
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Interface extends JPanel implements ActionListener{
	private JTextField inputText;
	private JTextArea area;
	private Parser parser;

	public Interface(){
		super(new BorderLayout(0,10));
		parser = new Parser();
		inputText = new JTextField(50);

		inputText.addActionListener(this);

		area = new JTextArea(20,50);
		area.setEditable(false);
		JScrollPane scrollBar = new JScrollPane(area);

		add(inputText, BorderLayout.NORTH);
		add(scrollBar,BorderLayout.SOUTH);

		
		
	}

	public void actionPerformed(ActionEvent evt){
		String text = inputText.getText();
		try{
			parser.performAction(text, area);
		}catch(JSQLException e){
			text = e.getMessage();
			area.append(text + "\n");
		}
		
		inputText.selectAll();

		area.setCaretPosition(area.getDocument().getLength());
	}

	private static void createAndShowGUI(){
		JFrame frame = new JFrame("JSQL");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new Interface());

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				createAndShowGUI();
			}
		});
	}

	
}