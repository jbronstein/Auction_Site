package camelinaction;

import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SubProcess implements Processor{
	
	private ArrayList <Bidder> mBidders;
	private String mItemName;
	private String mInitialPrice;
	private int mInitial;
	private String mPriceLevel;
	private String mWinnerName;
	private int mWinnerAmount;
	private int mNewAmount;
	
	//Pass in all Bidders to constructor arraylist
	public SubProcess(ArrayList <Bidder> b){
		mBidders = b;
	}

	@Override
	public void process(Exchange e) throws Exception {
		
		// process each Auction item and output the winning bidder details
		String[] array = e.getIn().getBody(String.class)
				.replaceAll("\\[","").replaceAll("\\]", "").split("\t");	
		
		//Get objects from Auction Item
		mItemName = array[0];
		mInitialPrice = array[1];
		mPriceLevel = array[2];		
		mInitial = Integer.parseInt(mInitialPrice);
		
		//Create an inAuction arrayList to keep track of who is in which Auction
		ArrayList <Bidder> inAuction = new ArrayList <Bidder>();
		
		//Pass in inAuction arraylist to populate with correct bidders
		findEntryBidders(inAuction);
		
		//Get Winner by running each bidder's strategy
		getWinner(inAuction);	
		
		//Whoever wins the Auction needs to have their amount lowered by their official bid
		updateBidderAmount();
		
		//use string builder to generate output String with Winner
		StringBuilder message = new StringBuilder();
		message.append("ITEM: ").append(mItemName.trim());
		message.append("|INITIAL PRICE: ").append(mInitialPrice.trim());
		message.append("|LEVEL: ").append(mPriceLevel.trim());
		message.append("|WINNER: ").append(mWinnerName.trim()); //NAME
		message.append("|BID AMOUNT: ").append(mWinnerAmount); //How much bidder bidded
		message.append("|NEW AMOUNT: ").append(mNewAmount); //Remaining cash of the winning bidder
		
		System.out.println(message);
		e.getIn().setBody(message.toString());
	}	
	
	//Update the Amount of Cash for the Winning Bidder
	public void updateBidderAmount(){
		for(int i = 0; i <= mBidders.size() - 1; i++){
			if (mWinnerName.equals(mBidders.get(i).getName())){
				mBidders.get(i).updateAmount(mWinnerAmount);
				mNewAmount = mBidders.get(i).getAmount();
				break;
			}
		}
	}
	
	//Add Bidders who fit criteria to the inAuction arrayList
	public void findEntryBidders(ArrayList<Bidder>inGame){
		//I use iterator here
		int count = 0;
		BidderIterator bIterator = new BidderIterator(mBidders);
		Bidder check;
		
		while(!bIterator.isLastBidder()){
			if(count != 0){
				check = bIterator.nextBidder();
			}
			else {
				check = bIterator.currentBidder();
			}		
			if (mPriceLevel.equals("MID")){
				if (check.getRank() >=2 && check.getAmount() > mInitial){
					inGame.add(check);
				}
			}
			else if(mPriceLevel.equals("HIGH")){
				if (check.getRank() >= 3 && check.getAmount() > mInitial){
					inGame.add(check);
				}
			}			
			else if(mPriceLevel.equals("LOW")){
				if (check.getRank() == 1 && check.getAmount() > mInitial){
					inGame.add(check);
				}
			}	
			count++;
		}
	}
	
	//Get the winner of the Auction
	public void getWinner(ArrayList<Bidder> inGame){
		int winBid = 0;
		mWinnerAmount = 0;
		
		for (int i = 0; i <= inGame.size() - 1; i++){
			Bidder check = inGame.get(i);
			
			if (check.getBid(mInitial) > winBid){
				mWinnerName = check.getName();
				mWinnerAmount = check.getBid(mInitial);
				winBid = mWinnerAmount;
			}
		}
	}
}
