import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game 
 * and handle all user actions.
 * @author sudhakarshah
 *
 */
public class BigTwoTable implements CardGameTable {
	
	BigTwoClient game;
	//CardGame game;
	boolean[] selected = new boolean[13];
	//remove -1
	int activePlayer=-1 ;
	JFrame frame;
	JPanel bigTwoPanel;
	JButton playButton;
	JButton passButton;
	JTextArea msgArea;
	
	JTextArea chatArea;
	
	Image[][] cardImages;
	Image cardBackImage ;
	Image[] avatars;
	boolean clickActive=false;
	
	
	JTextArea incoming; // for showing messages 
	JTextField outgoing; // for user inputs 
	
	/**
	 * a constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * @param game Card game associated with this table
	 */
	public BigTwoTable(BigTwoClient game)
	{
		
		this.game=game;
		frame = new JFrame(); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		msgArea=new JTextArea(50,30);
		playButton= new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		passButton= new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		resetSelected();

		JPanel textPanel=new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		
		msgArea.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		
		game.setPlayerName(JOptionPane.showInputDialog(frame, "What's your name?"));


		// sets up a text area for showing incoming messages
		incoming = new JTextArea(15, 30); 
		incoming.setLineWrap(true); 
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming); 
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);  
		outgoing = new JTextField(30);
		outgoing.addActionListener(new ChatEnterListener());

		
		textPanel.add(scroller);
		textPanel.add(qScroller);
		textPanel.add(outgoing);
		frame.add(textPanel, BorderLayout.EAST);
		
		bigTwoPanel= new BigTwoPanel();
		bigTwoPanel.addMouseListener(new BigTwoPanel());


		// getting images of players
		avatars= new Image[4];
		avatars[0]=new ImageIcon("avatar/player1.png").getImage();
		avatars[1]=new ImageIcon("avatar/player2.png").getImage();
		avatars[2]=new ImageIcon("avatar/player3.png").getImage();
		avatars[3]=new ImageIcon("avatar/player4.png").getImage();
		
		// getting all the images of the cards
		char suits[]={'d','c','h','s'};
		char rank[]={'a','2','3','4','5','6','7','8','9','t','j','q','k'};
		cardImages=new Image[4][13];
		for (int i=0;i<4;i++)
		{
			for (int j=0;j<13;j++)
			{
				cardImages[i][j]=new ImageIcon("cards/"+rank[j]+suits[i]+".gif").getImage();
			}
		}
		
		cardBackImage =new ImageIcon("cards/b.gif").getImage();
		
		// creating the menu bar 
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game"); 
		JMenuItem connect = new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(connect);
		menu.add(quit);
		JMenu menu1 = new JMenu("Message"); 
		JMenuItem clear = new JMenuItem("Clear");
		clear.addActionListener(new ClearMenuItemListener());
		menu1.add(clear);
		menuBar.add(menu);
		menuBar.add(menu1);
		frame.setJMenuBar(menuBar);
		frame.add(buttonPanel,BorderLayout.SOUTH); 
		frame.add(bigTwoPanel,BorderLayout.CENTER); 
		frame.setSize(1000,730);
		//frame.pack(); 
		frame.setVisible(true);
		disable();
	}
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer=activePlayer;
	}

	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected()
	{
		int[] arr= new int[game.getPlayerList().get(activePlayer).getNumOfCards()];
		int index=0;
		for (int i=0;i<arr.length;i++)
		{
			if (selected[i])
			{
				arr[index]=i;
				index++;
			}		
		}
		
		int[] arr2=new int[index];
		for (int i=0;i<index;i++)
		{
			arr2[i]=arr[i];
		}
		return arr2;
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected()
	{
		for (int i=0;i<selected.length;i++)
		{
			selected[i]=false;
		}
	}

	/**
	 * Repaints the GUI.
	 */
	public void repaint()
	{
		frame.repaint();
	}

	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the message area of the card game
	 *            table
	 */
	public void printMsg(String msg)
	{
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
		msgArea.append(msg);
	}

	/**
	 * Clears the message area of the card game table.
	 */
	public void clearMsgArea()
	{
		msgArea.setText(null);
	}

	/**
	 * Resets the GUI.
	 */
	public void reset()
	{
		resetSelected();
		clearMsgArea();
		enable();
	}

	/**
	 * Enables user interactions.
	 */
	public void enable()
	{
		clickActive=true;
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}

	/**
	 * Disables user interactions.
	 */
	public void disable()
	{
		clickActive=false;
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	
	
	
	public void printChat(String msg)
	{
		incoming.setCaretPosition(incoming.getDocument().getLength());
		incoming.append(msg);
	}

	
	
	
	
	
	
	/**
	 * An inner class that extends the JPanel class and implements the MouseListener interface. Overrides the paintComponent()
	 *  method inherited from the JPanel class to draw the card game table. Implements the mouseClicked() method from the 
	 *  MouseListener interface to handle mouse click events.
	 *  
	 * @author sudhakarshah
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -2679386016864585827L;

		public void paintComponent(Graphics g) 
		{

			super.paintComponent(g);
			//Graphics2D g2d = (Graphics2D)g;
			setBackground(Color.pink);
			
			int num=0;
			for (int i=0;i<4;i++)
			{
				String name=game.getPlayerList().get(i).getName();
				if (name!=null)
					num=num+1;
			}

			//looping through players
			for (int i=0;i<4;i++)
			{
				String name=game.getPlayerList().get(i).getName();
				
				if (name!=null)
				{
				
					g.drawString(name,3,15+130*i);
					Image I = new ImageIcon(avatars[i].getScaledInstance(100,100, Image.SCALE_DEFAULT)).getImage();
					g.drawImage(I, 3,20+130*i, this);
					//Drawing winner batch for the player who wins the game
					
					
					/*
					if (game.getPlayerList().get(i).getNumOfCards()==0)
					{
						Image winner = new ImageIcon(new ImageIcon("avatar/winner.jpeg").getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)).getImage();
						g.drawImage(winner, 150,20+130*i-10, this);
					}
					
					*/
					if (num!=4)
						g.drawString("Waiting of "+(4-num)+" players to join",150,15+130*i);
					else
					{
						//looping through cards in hand
						for (int j=0;j<game.getPlayerList().get(i).getNumOfCards();j++)
						{
							int rank=game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
							int suit=game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
							
							if (i == activePlayer)
							{
								if (selected[j]==true)
									g.drawImage(cardImages[suit][rank], 150+(j*15),20+130*i-10, this);
								else
									g.drawImage(cardImages[suit][rank], 150+(j*15),20+130*i, this);
								
							}
							else
							{
								g.drawImage(cardBackImage, 150+(j*15),20+130*i, this);
							}
						}
					}
						
						

					g.drawLine(0,130*(i+1),1500,130*(i+1));
				}
				
				
				
				
			}

			
			if (game.getHandsOnTable().size()>0)
			{

				String lastHandPlayedBy=game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName();
				int sizeOfCards=game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size();
				g.drawString("Played by "+lastHandPlayedBy,3,15+130*4); 
				// printing the last hand played
				 for (int i=0;i<sizeOfCards;i++)
				 {
					int rank=game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getRank();
					int suit=game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getSuit();
					g.drawImage(cardImages[suit][rank],3+(i*15),20+130*4, this);
				 }
			}
			else
			{
				g.drawString("No Hands Played Yet",3,15+130*4);
			}

		}
		
		public void mouseClicked(MouseEvent e)
		{
			System.out.println("mouse clicked");
			// if jpanel is set to be not active
			if (clickActive==false)
				return;
			
			// getting x and y coordinates of clicking
			int x=e.getX();
		    int y=e.getY();
		    int sizeOfCards=game.getPlayerList().get(game.getCurrentIdx()).getCardsInHand().size();
		    //checking for coordinates
		    if (x>150 && x<150+(sizeOfCards-1)*15+74)
		    {
	    		int cardNo=((x-150)/15);
	    		if (cardNo>sizeOfCards-1)
	    		{
	    			cardNo=sizeOfCards-1;
	    		}
	    		
	    		if (y>20+130*activePlayer-10 && y<20+130*activePlayer) // checking raised part
	    		{
	    			int steps=0;
	    			if (cardNo==game.getPlayerList().get(activePlayer).getNumOfCards()-1)
		    		{
		    			steps=(x-(150+15*cardNo))/15;		    			
		    		}

	    			while (selected[cardNo]!=true && steps<4 && cardNo>0)
	    			{
	    				cardNo=cardNo-1;
	    				steps++;
	    			}
	    			selected[cardNo]=false;
	    		}
	    		
		    	// for deselecting the card
	    		else if (selected[cardNo]==true && y>20+130*activePlayer && y<20+130*activePlayer+86)
		    	{
	    			selected[cardNo]=false;
		    	}
	    		
		    	else if (y>20+130*activePlayer && y<20+130*activePlayer+96)
		    	{
		    		int steps=0;
		    		if (cardNo==game.getPlayerList().get(activePlayer).getNumOfCards()-1)
		    		{
		    			steps=(x-(150+15*cardNo))/15;		    			
		    		}
		    		while(selected[cardNo]==true && y>20+130*activePlayer+86 && cardNo!=0 && steps<4)
		    		{
		    			cardNo=cardNo-1;
		    			
		    			if (cardNo<0)
			    		{
			    			cardNo=0;
			    		}
		    			
		    			steps++;
		    		}
		    		selected[cardNo]=true;
		    	}
		    	frame.repaint();
		    }  
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() method 
	 * from the ActionListener interface to handle button-click events for the “Play” button.
	 * 
	 * @author sudhakarshah
	 *
	 */
	class PlayButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			if (clickActive==false)
				return;
			game.makeMove(activePlayer,getSelected());
		}
		
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() method 
	 * from the ActionListener interface to handle button-click events for the “Pass” button. 
	 *
	 *@author sudhakarshah
	 *
	 */
	class PassButtonListener implements ActionListener{	
		public void actionPerformed(ActionEvent event){
			// sending null as the user wants to pass
			if (clickActive==false)
				return;
			game.makeMove(activePlayer, null);
		}	
	}
	
	
	
	
	
	class ChatEnterListener implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			
			String text = outgoing.getText();
			CardGameMessage message=new CardGameMessage (CardGameMessage.MSG,-1,text);
			game.sendMessage(message);
			outgoing.setText("");
		}
		
	}
	
	/**
	 *  An inner class that implements the ActionListener interface. Implements the actionPerformed() method 
	 *  from the ActionListener interface to handle menu-item-click events for the “Restart” menu item.
	 *  
	 * @author sudhakarshah
	 *
	 */
	class ConnectMenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			if (game.getPlayerID()==-1)
			{
				game.makeConnection();
			}
		}
		
	}

	class ClearMenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			//setting chat message to null of the client
			incoming.setText(null);
		}
	}
	/**
	 * An inner class that implements the ActionListener interface. Implements the actionPerformed() method 
	 * from the ActionListener interface to handle menu-item-click events for the “Quit” menu item.
	 * @author sudhakarshah
	 *
	 */
	class QuitMenuItemListener implements ActionListener{
		public void actionPerformed(ActionEvent event){
			game.closeConnection();
			System.exit(2);  //exiting the code
			
		}
		
	}
}
