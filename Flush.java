
/**
 * This class is a subclass of the Hand class, and are used to model a hand of Flush. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * @author sudhakarshah
 *
 */
public class Flush extends Hand {
	
	/**
	 * Constructor for Flush type hand
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public Flush(CardGamePlayer player, CardList cards){
		super(player,cards);
	}
	
	
	/* (non-Javadoc)
	 * Returns the top card of the hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		sort();
		return getCard(4);

	}
	
	/* (non-Javadoc)
	 * Checks whether the hand is a Flush
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		if (size()==5)
		{
			for (int i=0;i<4;i++)// index one less as i+1 being compared
			{
				if(getCard(i).getSuit() != getCard(i+1).getSuit())
					return false;
				
			}
			return true;
		}
		return false;
	}
	
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "Flush";
	}

}
