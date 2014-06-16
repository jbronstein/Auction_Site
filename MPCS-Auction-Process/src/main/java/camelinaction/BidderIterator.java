package camelinaction;

import java.util.ArrayList;

//Bidder Iterator to go through ArrayList of Bidders
public class BidderIterator implements Iterator {

	public ArrayList<Bidder> bidderList;
	private int position;
	
	public BidderIterator(ArrayList<Bidder>bidderList){
		this.bidderList = bidderList;
		this.position = 0;
	}
	
	@Override
	public Bidder nextBidder() {
		Bidder bidder = null;
		if (position == (bidderList.size() - 1)){
			return null;
		}
		//else
		position++;
		bidder = bidderList.get(position);
		return bidder;
	}

	@Override
	public boolean isLastBidder() {
		if (position == (bidderList.size() - 1)){
				return true;
			}
		return false;
	}

	@Override
	public Bidder currentBidder() {
		return bidderList.get(position);
	}
}
