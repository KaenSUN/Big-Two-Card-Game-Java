/**
 * This class is a subclass of the Hand class, and are used to model a hand of Full House. 
 * It overrides getTopCard,isValid and getType method that it inherits from Hand class.
 * 
 * @author sudhakarshah
 *
 */
public class FullHouse extends Hand{
	
	
	/**
	 * Constructor for Full house type hand
	 * 
	 * @param player Player who plays the hand
	 * @param cards  List of card played by the player
	 */
	public FullHouse(CardGamePlayer player, CardList cards){
		super(player,cards);
		
	}
	
	/* (non-Javadoc)
	 * Returns the top card of the hand
	 * @see Hand#getTopCard()
	 */
	public Card getTopCard(){
		
		//The card in the triplet with the highest suit 
		//in a full house is referred to as the top card of this full house
		sort();
		//if  triplet has higher rank than duplet
		if (getCard(1).getRank() != getCard(2).getRank())
			return getCard(4);
		// if triplet has lower rank than duplet

		else 
			return getCard(2);

	}
	
	/* (non-Javadoc)
	 * Checks whether the hand is a Flush
	 * 
	 * @see Hand#isValid()
	 */
	public boolean isValid(){
		// This hand consists of five cards, 
		//with two having the same rank and three having another same rank.
		if (size()==5)
		{
			sort();
			if (getCard(1).getRank() != getCard(2).getRank())
			{
				if (getCard(0).getRank()==getCard(1).getRank())
				{
					if (getCard(2).getRank()==getCard(3).getRank() && getCard(3).getRank()==getCard(4).getRank())
						return true;
				}

			}
			else if (getCard(2).getRank() != getCard(3).getRank())
			{
				if (getCard(3).getRank()==getCard(4).getRank())
				{
					if (getCard(0).getRank()==getCard(1).getRank() && getCard(1).getRank()==getCard(2).getRank())
						return true;
				}
			}
		}
		return false;
	}
	
	
	/* (non-Javadoc)
	 * returns type of string
	 * @see Hand#getType()
	 */
	public String getType(){
		return "FullHouse";
	}

}
