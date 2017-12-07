import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import javax.swing.JOptionPane;


public class BigTwoClient implements CardGame,NetworkGame{
	
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable=new ArrayList<Hand>();
	private int playerID=-1 ;
	private String playerName;
	private String serverIP;
	private int serverPort;
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx;
	private BigTwoTable table;
	
	ObjectInputStream ois;
	/**
	 * Integer storing the index value of the player who has played the last hand
	 */
	int lastHandPlayedBy; //new instance variable
	
	public BigTwoClient()
	{
		playerList= new ArrayList<CardGamePlayer>();
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		playerList.add(new CardGamePlayer());
		// setting value of ServerIp and port number
		serverIP="127.0.0.1";
		serverPort=2396;
		table= new BigTwoTable(this);
		makeConnection();
	}
	
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	public int getNumOfPlayers()
	{
		return 4;
	}

	/**
	 * Getter for Instance variable deck 
	 * @return Instance variable deck of the class Big Two
	 */
	public Deck getDeck(){
		return deck;
	}
	
	/**
	 * Getter for Instance variable playerList (The list of players)
	 * 
	 * @return Instance variable playerList of the class Big Two
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
		
	}
	
	/**
	 * Getter for the list of hands played on the table
	 * 
	 * @return Instance variable handsOnTable of the class Big Two
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return handsOnTable;
		
	}
	/**
	 * Getter for the index of the current player
	 * 
	 * @return Index of the current player
	 */
	public int getCurrentIdx() {
		return currentIdx;

	}
	
	/**
	 * Starts the card game.Removes all the cards from the players as well as from the table; 
	 * Distributes the cards to the players. Identifies the player who holds the 3 of Diamonds and sets 
	 * both the currentIdx of the BigTwo instance and the activePlayer of the BigTwoTable instance to the index of the 
	 * player who holds the 3 of Diamonds.
	 * 
	 * @param deck
	 *            the deck of cards to be used in this game
	 */
	public void start(Deck deck){
		
		
		
		//removing all the cards
		for (int i=0;i<4;i++)
		{
			playerList.get(i).getCardsInHand().removeAllCards();
		}
		
		//removing cards from hands on table
		handsOnTable=new ArrayList<Hand>();
		this.deck=deck;
		currentIdx=-1;
		
		// Distributing the cards to all four players sequentially
		int playerNo=0;
		for (int i=0;i<52;i++)
		{
			playerList.get(playerNo%4).addCard(this.deck.getCard(i));
			playerNo++;
		}
		
		// Finding the player having three of diamonds
		for (int i=0;i<playerList.size();i++)
		{
			if (playerList.get(i).getCardsInHand().contains(new BigTwoCard(0,2)))
			{
				currentIdx=i;
				break;
			}
				
		}
		// looping through the class and then sorting the cards in hand of each player
		for (int i=0;i<playerList.size();i++)
		{
			playerList.get(i).getCardsInHand().sort();// using sort provided in the card class	
		}

		lastHandPlayedBy=currentIdx;// the first active player will have to play a hand

		table.printMsg(playerList.get(currentIdx).getName()+"'s turn");
		
		table.setActivePlayer(playerID);
		
		// if the platerID is not the one playing current move then disable his clicking ability
		
		if (currentIdx!=getPlayerID())
		{
			table.disable();

		}
		else
		{
			table.enable();
		}
		table.repaint();
	}
	
	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerID, int[] cardIdx)
	{
		boolean notLegalMove= true;
		int[] input = cardIdx;//taking the user input
		
		//A player cannot pass his turn to the next player if he is the one who played the last hand of cards on the table. 
		 if (input==null && currentIdx==lastHandPlayedBy)
		 {	
			 table.printMsg("\n"+"{pass}"+" <== Not a legal move!!!");
			 table.resetSelected();
			 table.repaint();
		 }
		 
		 // if the user wants to pass the move
		 else if (input==null)   
		 {
			// change player
			table.printMsg("\n"+"{pass}");
			currentIdx=(currentIdx+1)%4;
			table.printMsg(playerList.get(currentIdx).getName()+"'s turn");
			table.resetSelected();
			table.repaint();
		}
		
		else
		{
			if (input.length==0)
			{
				table.printMsg("\n"+"No Cards Selected. Select cards to Play a move");
			}
			
			else
			{
				// creating a list of cards that the user wants to play
				CardList InputList= playerList.get(currentIdx).play(input);

				//checking whether the list of card forms a hand or not
				if (composeHand(playerList.get(currentIdx),InputList)==null)
				{
					String string="";
					for (int i=0;i<InputList.size();i++)
					{
						string = string + "[" + InputList.getCard(i) + "]";
					}
					table.printMsg("\n"+string+" <=== Not a legal move!!!");
					table.resetSelected();
					table.repaint();
				}
				
				else
				{
					Hand handFormed=composeHand(playerList.get(currentIdx),InputList);
					
					//check whether the hand can be played
					// if this is the first move of the game then check whether the move contains three of diamonds
					if (handsOnTable.size()==0)  
					{
						//for the first move check if the player play three of diamonds
						if (handFormed.contains(new BigTwoCard(0,2)))
						{
							notLegalMove= false;
						}
							
					}
					//if the player who played the last hand gets the chance again
					else if (currentIdx==lastHandPlayedBy)
					{
						notLegalMove= false;
					}
					// if the hand is of the same size and can beat the previous hand played
					else if (handFormed.size()==handsOnTable.get(handsOnTable.size()-1).size() && handFormed.beats(handsOnTable.get(handsOnTable.size()-1)))
					{
						notLegalMove= false;
					}	
					// if the player plays a legal move
					if(notLegalMove==false)
					{
						Hand handPlayed=handFormed;
						handsOnTable.add(handPlayed);
						lastHandPlayedBy=currentIdx;
						//printing the hand played by the current active player
						String string="";
						for (int i=0;i<handPlayed.size();i++)
						{
							string = string + "[" + handPlayed.getCard(i) + "]";
						}
						table.printMsg("\n" +"{" + handPlayed.getType() + "} "+ string);
						
						// removing the card played from the cards user has on his hand
						playerList.get(currentIdx).removeCards(handPlayed);
						if (endOfGame()==true)
						{
							table.disable();
							
						}
						else
						{
							currentIdx=(currentIdx+1)%4;
							table.printMsg("\n"+playerList.get(currentIdx).getName()+"'s turn");
							table.resetSelected();
							table.repaint();
						}
						
					}
					else
					{
						String string="";
						for (int i=0;i<handFormed.size();i++)
						{
							string = string + "[" + handFormed.getCard(i) + "]";
						}
						table.printMsg("\n" +"{" + handFormed.getType() + "} "+ string+" <=== Not a legal move!!!");
						//table.printMsg("Not a legal move!!!");
						table.resetSelected();
						table.repaint();
					}
				}
			}

		}
		if (currentIdx!=getPlayerID())
		{
			table.disable();
			System.out.println("disabled");
		}
		else
		{
			table.enable();
			System.out.println("enabled");
		}
		table.repaint();
		table.resetSelected(); // resetting any selected card
		
	}
	
	public void makeMove(int playerID, int[] cardIdx)
	{
		sendMessage(new CardGameMessage(CardGameMessage.MOVE,-1,cardIdx));
	}

	/**
	 * Checks for end of game.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame()
	{
		// after player plays legally checking whether he has zero number of cards
		if (playerList.get(currentIdx).getCardsInHand().size()==0)
		{
			table.repaint();
			table.setActivePlayer(-1); // setting Active Player to -1
			
			
			String string="Game ends";
			table.printMsg("\nGame ends");
			for (int i=0;i<playerList.size();i++)
			{
				if (i==currentIdx)
				{
					string=string+"Game ends"+"\nPlayer "+i+" wins the game.";
					table.printMsg("\nPlayer "+i+" wins the game.");
				}
				else
				{
					string=string+"\nPlayer "+i+" has "+playerList.get(i).getCardsInHand().size()+" cards in hand.";
					table.printMsg("\nPlayer "+i+" has "+playerList.get(i).getCardsInHand().size()+" cards in hand.");
				}
			}
			
			//dialog box
			JOptionPane.showMessageDialog(null,string);
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
			return true;
		}
		
		return false;
		
	}
	
	
	
	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID()
	{
		return playerID;
	}

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID)
	{
		this.playerID=playerID;
	}

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName)
	{
		this.playerName=playerName;
	}
	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP()
	{
		return serverIP;
	}

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public void setServerIP(String serverIP)
	{
		this.serverIP=serverIP;
	}
	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort()
	{
		return serverPort;
	}

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort)
	{
		this.serverPort=serverPort;
	}
	
	
	
	public void closeConnection()
	{
		try {
			//sock.shutdownInput(); 
			//sock.shutdownOutput(); 
			
			ois.close();
			oos.close();
			sock.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Makes a network connection to the server.
	 */
	public void makeConnection()
	{
		try {
			sock = new Socket(serverIP,serverPort);
			
			// oos a global vaiable
			oos = new ObjectOutputStream(sock.getOutputStream());
			Runnable threadJob = new ServerHandler(); 
			Thread myThread = new Thread(threadJob); 
			myThread.start();
			//message type join
			sendMessage(new CardGameMessage(CardGameMessage.JOIN,-1,playerName));
			//ready
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
			
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message)
	{
	
		// type PLAYER_LIST
		if (message.getType()==CardGameMessage.PLAYER_LIST)
		{
			setPlayerID(message.getPlayerID());
			for (int i=0;i<4;i++)
			{
				playerList.get(i).setName(((String[])message.getData())[i]);
			}
		
		}
		
		// JOIN
		else if (message.getType()==CardGameMessage.JOIN)
		{
			//dont do this. 
			//playerID=message.getPlayerID();
			
			// is this okay?
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			
		}
		//FULL
		else if (message.getType()==CardGameMessage.FULL)
		{
			table.printMsg("The Server is Full and cannot join the Game");
	
		}
		
		//QUIT
		else if (message.getType()==CardGameMessage.QUIT)
		{
			playerList.get(message.getPlayerID()).setName(null);
			//If a game is in progress, the client 
			//should stop the game
			table.disable();
			handsOnTable.clear();
			table.clearMsgArea();
			table.repaint();
			
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1,null));
			

		}
		
		//READY
		else if (message.getType()==CardGameMessage.READY)
		{
			table.printMsg(playerList.get(message.getPlayerID()).getName()+" is ready"+"\n");
			
		}
		//START
		else if (message.getType()==CardGameMessage.START)
		{
			// need to pass Deck type object
			start((Deck)message.getData());
		}
		
		//MOVE
		else if (message.getType()==CardGameMessage.MOVE)
		{
			checkMove(message.getPlayerID(),(int[])message.getData());
		}
		
		//MSG
		else if (message.getType()==CardGameMessage.MSG)
		{
			table.printChat((String)message.getData()+"\n");
			// print in chat window the data
		}
		
		table.repaint();
		
		
	}
	

	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message)
	{
		try {
			oos.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	/**
	 * a method for starting a Big Two card game. It creates a Big Two card game, 
	 * creates and shuffle a deck of cards, and start the game with the deck of cards.
	 * 
	 * @param args this parameter is not used
	 */
	public static void main(String[] args) {
		
		
		// creating instance of big two client
		BigTwoClient game = new BigTwoClient();

	}
	/**
	 * 
	 * a method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null is no valid hand can be composed from the specified list of cards.
	 * 
	 * @param player The active player who played the card list
	 * @param cards The list of card that the player intended to play
	 * @return If a hand is formed then return the hand or else return null
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards){
		Hand validHand;
		
		//one by one checking for each possible hand and returning the hand if it is formed
		
		validHand=new StraightFlush(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new Quad(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new FullHouse(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new Flush(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new Straight(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new Triple(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new Pair(player,cards);
		if (validHand.isValid())
			return validHand;
		validHand=new Single(player,cards);
		if (validHand.isValid())
			return validHand;
		
		return null;  //if a valid hand cannot be formed

	}
	
	class ServerHandler implements Runnable 
	{
		public void run()
		{
			
			CardGameMessage message;
			
			try {
				ois = new ObjectInputStream(sock.getInputStream());
				// waits for messages from the client
				while ((message = (CardGameMessage) ois.readObject()) != null) {
					parseMessage(message);
				} // close while
			} catch (Exception ex) {
				
				System.out.println("left while");
				ex.printStackTrace();
				// possible connection loss, removes the connection
			}
			
			
		}
	}
	

}
