package BitcoinTransactions.BasicTransactions;

import java.util.ArrayList;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a family which in our example consisting of 3
// members, a couple and a child,
// who represent the Senders in the MultiSignature Transaction.

public class Family {

	private Sender s1; // father
	private Sender s2; // mother
	private Sender s3; // a child(under 18 years old, in order
						// to ask for permission from its parents to spend its
						// BT.
						// We do this in order to create a realistic example.

	private ArrayList<Sender> sList;
	private Double balance;

	public Family() throws AddressFormatException {
		super();
		s1 = new Sender();
		s2 = new Sender();
		s3 = new Sender();

		sList = new ArrayList<Sender>();
		sList.add(s1);
		sList.add(s2);
		sList.add(s3);
		// System.out.println("The size of the list is : " +sList.size());

		for (Sender curentSender : getsList()) {
			Wallet wallet = new Wallet();
			curentSender.addSenderWallet(wallet);
		}

	}

	public ArrayList<Sender> getsList() {
		return sList;
	}

	public void setsList(ArrayList<Sender> sList) {
		this.sList = sList;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Sender getS1() {
		return s1;
	}

	public void setS1(Sender s1) {
		this.s1 = s1;
	}

	public Sender getS2() {
		return s2;
	}

	public void setS2(Sender s2) {
		this.s2 = s2;
	}

	public Sender getS3() {
		return s3;
	}

	public void setS3(Sender s3) {
		this.s3 = s3;
	}

	public ArrayList<ECKey> getAllKeys(ArrayList<Sender> sList) {
		ArrayList<ECKey> keyList = new ArrayList<ECKey>();

		for (Sender currentSender : sList) {
			ECKey currentKey = currentSender.getSenderWalletList().get(0)
					.getClientKey();
			keyList.add(currentKey);
		}

		return keyList;
	}

	// this function will change all the ECKeys of the members of the company by
	// adding new ECKeys for them.
	// i will do that in order to see that we need at least 2 valid keys in
	// order to check that the signatures are valid.

	public ArrayList<ECKey> changeAllKeys(ArrayList<Sender> sList) {
		ArrayList<ECKey> keyList = new ArrayList<ECKey>();

		for (Sender currentSender : sList) {
			ECKey myCurrentNewKey = new ECKey();
			currentSender.getSenderWalletList().get(0)
					.setClientKey(myCurrentNewKey);
			ECKey currentKey = currentSender.getSenderWalletList().get(0)
					.getClientKey();
			keyList.add(currentKey);
		}

		return keyList;
	}

	public double estimateTotalBalance(ArrayList<Sender> sList) {
		double totalAmount = 0.0;
		for (Sender currentSender : sList) {
			totalAmount = totalAmount
					+ currentSender.getSenderWalletList().get(0).getBalance();

		}

		return totalAmount;
 
	}

	@Override
	public String toString() {
		return "Family [s1=" + s1 + ", s2=" + s2 + ", s3=" + s3 + ", sList="
				+ sList + ", balance=" + balance + "]";
	}

}
