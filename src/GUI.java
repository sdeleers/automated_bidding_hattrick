import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.http.client.ClientProtocolException;


public class GUI extends JFrame
{
	JFrame frame = new JFrame("Simon's ubercool HT application");
	private JLabel item1;
	private static JComboBox mainSkillBox;
	private static JComboBox mainSkillMinimumValueBox;
	private static JComboBox mainSkillMaximumValueBox;
	private static JComboBox minimumAgeBox;
	private static JComboBox maximumAgeBox;
	private static JComboBox deadLineBox;
	private static JButton searchButton;
	private static JTextField minWage;
	private static JTextField maxPrice;
	private static String[] mainSkillOptions = {"Keepen","Conditie","Verdedigen","Spelmaken","Vleugelspel","Scoren","Spelhervatten","Passen","Ervaring","Leiderschap"};
	private static String[] mainSkillMinimumValueOptions = {"onbestaand","rampzalig","waardeloos","slecht","zwak","matig","redelijk","goed","uitstekend","formidabel","uitmuntend","briljant","wonderbaarlijk","wereldklasse","bovennatuurlijk","reusachtig","buitenaards","mythisch","magisch","utopisch","goddelijk"};
	private static String[] mainSkillMaximumValueOptions = {"onbestaand","rampzalig","waardeloos","slecht","zwak","matig","redelijk","goed","uitstekend","formidabel","uitmuntend","briljant","wonderbaarlijk","wereldklasse","bovennatuurlijk","reusachtig","buitenaards","mythisch","magisch","utopisch","goddelijk"};
	private static String[] minimumAgeOptions = {"17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
	private static String[] maximumAgeOptions = {"17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
	private static String[] deadLineOptions = {"Binnen de 3 dagen","Vandaag of morgen","Binnen de 12 uur"};
	
	public GUI()
	{
		frame.setSize(600,300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		makeTransferSearchPanel();
		
		
	}
	
	private void makeTransferSearchResultPanel(ArrayList<Player> players)
	{
		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().removeAll();
		frame.getContentPane().add(panel,BorderLayout.WEST);
		

	}

	private void makeTransferSearchPanel() 
	{
		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel,BorderLayout.WEST);
		GridBagConstraints c1 = new GridBagConstraints(); c1.gridx = 0; c1.gridy = 0; c1.insets = new Insets(5,5,5,5);
		JLabel label1 = new JLabel("Main Skill"); panel.add(label1,c1);
		GridBagConstraints c2 = new GridBagConstraints(); c2.gridx = 0; c2.gridy = -1; c2.insets = new Insets(5,5,5,5);
		JLabel label2 = new JLabel("Age"); panel.add(label2,c2);
		GridBagConstraints c3 = new GridBagConstraints(); c3.gridx = 0; c3.gridy = -2; c3.insets = new Insets(5,5,5,5);
		JLabel label3 = new JLabel("Deadline"); panel.add(label3,c3);
		GridBagConstraints c4 = new GridBagConstraints(); c4.gridx = 0; c4.gridy = -3; c4.insets = new Insets(5,5,5,5);
		JLabel label4 = new JLabel("Minimum Wage"); panel.add(label4,c4);
		GridBagConstraints c5 = new GridBagConstraints(); c5.gridx = 0; c5.gridy = -4; c5.insets = new Insets(5,5,5,5);
		JLabel label5 = new JLabel("Maximum Price"); panel.add(label5,c5);
		// Dropdown boxes
		mainSkillBox = new JComboBox(mainSkillOptions);
		mainSkillMinimumValueBox = new JComboBox(mainSkillMinimumValueOptions);
		mainSkillMaximumValueBox = new JComboBox(mainSkillMaximumValueOptions);
		minimumAgeBox = new JComboBox(minimumAgeOptions);
		maximumAgeBox = new JComboBox(maximumAgeOptions);
		deadLineBox = new JComboBox(deadLineOptions);
		mainSkillBox.addItemListener
		(new ItemListener(){
				public void itemStateChanged(ItemEvent event)
				{if(event.getStateChange() == ItemEvent.SELECTED)
					{Main.setMainSkill(Integer.toString(mainSkillBox.getSelectedIndex()+1));}}});
		mainSkillMinimumValueBox.addItemListener
		(new ItemListener(){
				public void itemStateChanged(ItemEvent event)
				{if(event.getStateChange() == ItemEvent.SELECTED)
					{Main.setmainSkillMinimumValue(Integer.toString((mainSkillMinimumValueBox.getSelectedIndex())));}}});
		mainSkillMaximumValueBox.addItemListener
		(new ItemListener(){
				public void itemStateChanged(ItemEvent event)
				{if(event.getStateChange() == ItemEvent.SELECTED)
					{Main.setmainSkillMaximumValue(Integer.toString((mainSkillMaximumValueBox.getSelectedIndex())));}}});
		minimumAgeBox.addItemListener
		(new ItemListener(){
				public void itemStateChanged(ItemEvent event)
				{if(event.getStateChange() == ItemEvent.SELECTED)
					{Main.setMinimumAge(Integer.toString(minimumAgeBox.getSelectedIndex()+17));}}});
		maximumAgeBox.addItemListener
		(new ItemListener(){
				public void itemStateChanged(ItemEvent event)
				{if(event.getStateChange() == ItemEvent.SELECTED)
					{Main.setMaximumAge(Integer.toString(maximumAgeBox.getSelectedIndex()+17));}}});
		deadLineBox.addItemListener
		(new ItemListener(){
				public void itemStateChanged(ItemEvent event)
				{if(event.getStateChange() == ItemEvent.SELECTED)
					{int index = deadLineBox.getSelectedIndex();
					if(index == 0) // binnen de drie dagen
						Main.setDeadline("-1");
					if(index == 1) // vandaag of morgen
						Main.setDeadline("3");
					if(index == 2) // Binnen de 12 uur
						Main.setDeadline("13");
					}}});
		
		c1.gridx = 1;
		panel.add(mainSkillBox,c1);
		c1.gridx = 2;
		panel.add(mainSkillMinimumValueBox,c1);
		c1.gridx = 3;
		panel.add(mainSkillMaximumValueBox,c1);
		c2.gridx = 1;
		panel.add(minimumAgeBox,c2);
		c2.gridx = 2;
		panel.add(maximumAgeBox,c2);
		c3.gridx = 1;
		panel.add(deadLineBox,c3);
		
		// Textfields
		minWage = new JTextField(8);
		maxPrice = new JTextField(8);
		c4.gridx = 1;
		panel.add(minWage,c4);
		c5.gridx = 1;
		panel.add(maxPrice,c5);
		
		// Search Button
		searchButton = new JButton("Search");
		searchButton.addActionListener
		(new ActionListener(){
			public void actionPerformed(ActionEvent event)
			{
				try {
					System.out.println("mainSkill: " + Main.getMainSkill() + "mainSkillMin: " + Main.getMainSkillMinimumValue() + "mainSkillMax: " + Main.getMainSkillMaximumValue());
					String sourceCode = Connection.searchTransferList(Main.getMainSkill(), Main.getMainSkillMinimumValue(), Main.getMainSkillMaximumValue(), Main.getMinimumAge(), Main.getMaximumAge(), Main.getDeadline());
					TransferSearch transferSearch = new TransferSearch(sourceCode,minWage.getText(),maxPrice.getText());
					ArrayList<Player> players = transferSearch.getPlayers();
					for(int i = 0; i < players.size(); i++)
					{
						System.out.println(players.get(i).getID());
					}
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints c6 = new GridBagConstraints(); c6.gridx = 0; c6.gridy = -5; c6.insets = new Insets(10,10,10,10);
		panel.add(searchButton,c6);
	}
	
	public String[] getMainSkillOptions()
	{
		return mainSkillOptions;
	}
	
}
