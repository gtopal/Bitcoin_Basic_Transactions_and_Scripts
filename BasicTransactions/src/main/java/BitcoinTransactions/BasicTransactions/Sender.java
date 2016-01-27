package BitcoinTransactions.BasicTransactions;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a common sender who sends his Bitcoins to receiver in
// exchange for a product or a service.

public class Sender {

	private ArrayList<Wallet> senderWalletList;
	ArrayList<byte[]> senderListFromReceiver; // this is in order to take the
												// Sender the public keys from
												// the receiver
	String hashOfRedeemScript;
	private Message myMessage = new Message();

	// the sender takes only
	// the Public
	// key(PubKeyHash) from
	// the receiver and not
	// the whole ECKey
	// because the whole
	// ECKey contains inside
	// the Private key of
	// the receiver of the bitcoins also!!!.

	public Sender() {
		senderWalletList = new ArrayList<Wallet>();
		senderListFromReceiver = new ArrayList<byte[]>();
	}

	public void addSenderWallet(Wallet wallet) {
		senderWalletList.add(wallet);
	}

	public ArrayList<Wallet> getSenderWalletList() {
		return senderWalletList;
	}

	public void setSenderWalletList(ArrayList<Wallet> senderWalletList) {
		this.senderWalletList = senderWalletList;
	}

	public ArrayList<byte[]> getSenderListFromReceiver() {
		return senderListFromReceiver;
	}

	public void setSenderListFromReceiver(
			ArrayList<byte[]> senderListFromReceiver) {
		this.senderListFromReceiver = senderListFromReceiver;
	}

	public String getHashOfRedeemScript() {
		return hashOfRedeemScript;
	}

	public String setHashOfRedeemScript(String hashOfRedeemScript) {
		this.hashOfRedeemScript = hashOfRedeemScript;
		return getHashOfRedeemScript();

	}

	public Message getMyMessage() {
		return myMessage;
	}

	public void setMyMessage(Message myMessage) {
		this.myMessage = myMessage;
	}

	@Override
	public String toString() {
		return "Sender [senderWalletList=" + senderWalletList + "]";
	}

}
