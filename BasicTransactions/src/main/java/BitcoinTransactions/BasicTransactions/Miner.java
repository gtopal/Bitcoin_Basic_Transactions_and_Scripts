package BitcoinTransactions.BasicTransactions;

import org.bitcoinj.core.AddressFormatException;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a common miner.His main activity is to mine blocks and
// win BT because of this effort.

public class Miner {

	private Double balance;
	private Wallet wallet;
	private Message myMessage = new Message();

	public Miner() throws AddressFormatException {
		wallet = new Wallet();
		balance = 0.0;

	}

	public Double getBalance() {
		return wallet.getBalance();
	}

	public void setBalance(Double balance) {
		wallet.setBalance(balance);
	}

	

	public Wallet getWallet() {
		return wallet;
	}

	@Override
	public String toString() {
		return "Miner [wallet=" + wallet + ", myMessage=" + myMessage + "]";
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public Message getMyMessage() {
		return myMessage;
	}

	public void setMyMessage(Message myMessage) {
		this.myMessage = myMessage;
	}

}
