package camelinaction;

//Interface Iterator
public interface Iterator {
	public Bidder nextBidder();
	
	public boolean isLastBidder();
	
	public Bidder currentBidder();
}
