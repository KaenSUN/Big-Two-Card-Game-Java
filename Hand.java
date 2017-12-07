
/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. 
 * It has a private instance variable for storing the player who plays this hand. It also has methods 
 * for getting the player of this hand, checking if it is a valid hand, getting the type of this hand, 
 * getting the top card of this hand, and checking if it beats a specified hand. It also has a sort method that
 * it overwrites to make sort the cards in the hand in ascending order. 
 * 
 * 
 * @author sudhakarshah
 *
 */
public abstract class Hand extends CardList {
	/**
	 * Player who played the hand
	 */
	private CardGamePlayer player ;
	
	/**
	 *  Constructor for Hand class that gives value to player and cards(instance variable of the CardList)
	 * 
	 * @param player Player who played the hand
	 * @param cards list of card that the player played
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player=player;
		for (int i=0;i<cards.size();i++){
			addCard(cards.getCard(i));
		}
		
	}
	/**
	 * Getter for player who played this hand
	 * @return player of the current hand object
	 */
	public CardGamePlayer getPlayer() {
		return player;
		
	}
	
	/**
	 * This method returns the top card of the hand
	 * @return returns the top card of the particular hand formed
	 */
	public Card getTopCard(){
		sort();
		return this.getCard(this.size()-1);
	}
	
	/**
	 * 
	 * This method compares two hands and checks whether this hand beats the hand send through the argument
	 * 
	 * @param hand The hand that needs to be compared with this
	 * @return	true if this hand beats the hand send as an argument, false otherwise
	 */
	public boolean beats(Hand hand){
		
		if (this.size()>hand.size())
		{
			return true;
		}
		
		else if (this.size()==hand.size())
		{
			if (this.getType()=="StraightFlush")
			{
				if (hand.getType()=="StraightFlush")
				{
					if (this.getTopCard().compareTo(hand.getTopCard())==1)
					{
						return true;
					}
					else
						return false;
				}
				else
				{
					return false;
				}
			}
			else if (this.getType()=="Quad")
			{
				if (hand.getType()=="StraightFlush")
				{
					return false;
				}
				else if (hand.getType()=="Quad")
				{
					//the suit will be same for both. So 
					//technically we can just compare the ranks
			
					if (this.getTopCard().compareTo(hand.getTopCard())==1)
					{
						return true;
					}
					else
						return false;
				}
				// for cases when hand is full house , flush or straight
				else
				{
					return true;
				}
			}
			else if (this.getType()=="FullHouse")
			{
				if (hand.getType()=="StraightFlush" || hand.getType()=="Quad")
				{
					return false;
				}
				else if (hand.getType()=="FullHouse")
				{
					// we cannot directly compare ranks
					if (this.getTopCard().compareTo(hand.getTopCard())==1)
						return true;
					else
						return false;
				}
				//for cases of flush and straight
				else
				{
					return true;
				}
			}
			else if (this.getType()=="Flush")
			{
				if (hand.getType()=="StraightFlush" || hand.getType()=="Quad" || hand.getType()=="FullHouse")
				{
					return false;
				}
				else if (hand.getType()=="Flush")
				{
					// We first compare the suits and then compare the ranks
					if (this.getTopCard().getSuit()>hand.getTopCard().getSuit())
						return true;
					else if (this.getTopCard().getSuit()==hand.getTopCard().getSuit())
					{
						// we cannot directly compare ranks
						if(this.getTopCard().compareTo(hand.getTopCard())==1)
							return true;
						else
							return false;
					}
					else
						return false;
								
				}
				// for case when hand is straight
				else
					return false;
			}
			else if (this.getType()=="Straight")
			{
				if (hand.getType()=="Straight")
				{
					if (this.getTopCard().compareTo(hand.getTopCard())==1)
						return true;
					else
						return false;
				}
				// if hand is straight flush or quad or full house or flush 
				else
					return false;
			}
			else if (this.getType()=="Triple")
			{
				// as we cannot just compare the rank(due to weird ordering)
				if (this.getTopCard().compareTo(hand.getTopCard())==1)
					return true;
				else
					return false;

			}
			else if (this.getType()=="Pair")
			{
				// hand is always  a pair due to the obove condition
				if (this.getTopCard().compareTo(hand.getTopCard())==1)
					return true;
				else
					return false;
			}
			else if (this.getType()=="Single")
			{
				
				if (this.getCard(0).compareTo(hand.getCard(0))==1)
				{
					return true;
				}
					
				else
					return false;
			}
			else
				return false;
		}
		else
		{
			return false;
		}
		
	}
	
	/**
	 * Returns true if the hand is a of a valid type and false otherwise
	 * 
	 * @return true if hand is valid, false otherwise
	 */
	abstract boolean  isValid();

	/**
	 * Returns the name of the type of hand
	 * 
	 * @returnR the name of the type of hand in string
	 */
	abstract String getType();
	
	
	/* (non-Javadoc)
	 * 
	 * Sorting the list of cards in ascending order by bubble sort method
	 * @see CardList#sort()
	 */
	public void sort(){
		
		//bubble sorting the cardList in ascending order
		
		for (int i=0;i<size();i++)
		{
			for (int j=0;j<size()-i-1;j++)
				if (getCard(j).compareTo(getCard(j+1))==1)
				{
					 setCard(j+1,setCard(j,getCard(j+1))); //Swapping the positions
				}
		}
	}
	
	/**
	 * A method to get set the order of the cards in correct rank according to big two game
	 * @param localRank int value of the rank of card passed
	 * @return rank of the card according to the big two game
	 */
	public int manageRank(int localRank){
		if (localRank==0)
			return 13;
		else if (localRank==1)
			return 14;
		else
			return localRank;
	}
	
	

}
