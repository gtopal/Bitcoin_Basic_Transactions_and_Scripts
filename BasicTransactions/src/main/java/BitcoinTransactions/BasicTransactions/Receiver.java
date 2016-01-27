package BitcoinTransactions.BasicTransactions;

import java.util.ArrayList;

import org.bitcoinj.core.Address;
import org.bitcoinj.params.MainNetParams;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a common receiver who after validation of his signature
// he can redeem the Bitcoins which sent to him by the sender.

public class Receiver {

	private ArrayList<Wallet> receiverWalletList;
	private Message myMessage = new Message();

	public Receiver() {
		receiverWalletList = new ArrayList<Wallet>();

	}

	public void addReceiverWallet(Wallet wallet) {
		receiverWalletList.add(wallet);
	}

	public ArrayList<Wallet> getReceiverWalletList() {
		return receiverWalletList;
	}

	public void setReceiverWalletList(ArrayList<Wallet> receiverWalletList) {
		this.receiverWalletList = receiverWalletList;
	}

	@Override
	public String toString() {
		return "Receiver [receiverWalletList=" + receiverWalletList + "]";
	}

	public ArrayList<byte[]> getOnlyHash160Addresses(
			ArrayList<Wallet> receiverWalletList) {
		ArrayList<byte[]> myPubKeyHash160List = new ArrayList<byte[]>();
		for (Wallet currentWallet : receiverWalletList) {
			myPubKeyHash160List.add(currentWallet.getClientKey().getPubKeyHash());    //giati na min steilo to currentWallet.getClientKey.getPubKeyHash()???
											//currentWallet.getHash160()
		}

		return myPubKeyHash160List;

	}

	public Message getMyMessage() {
		return myMessage;
	}

	public void setMyMessage(Message myMessage) {
		this.myMessage = myMessage;
	}

}
