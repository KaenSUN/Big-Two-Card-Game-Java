/**
 * This class is a subclass of the Hand class, and are used to model a hand of Straight Flush. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author sudhakarshah
 *
 */
public class StraightFlush extends Hand {
	
	/**
	 * Constructor for StraightFlush type hand. Calls the constructor of Hand class.
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public StraightFlush(CardGamePlayer player, CardList cards){
		super(player,cards);
		
	}
	
	/* (non-Javadoc)
	 * Checks whether the hand is a Straight Flush
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		//his hand consists of five cards with consecutive ranks 
		//and the same suit. For the sake of simplicity, 2 and A 
		//can only form a straight flush with K but not with 3.

		// Straight Flush is both Straight and flush
		if (size()==5)
		{
			CardList tempList= new CardList();
			for (int i=0;i<size();i++)
			{
				tempList.addCard(getCard(i));
			}
			Hand tempStraight = new Straight(getPlayer(),tempList);
			Hand tempFlush = new Flush(getPlayer(),tempList);
			
			if (tempStraight.isValid() && tempFlush.isValid())
				return true;
		}
		return false;
		
	}
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "StraightFlush";
	}
	

}
