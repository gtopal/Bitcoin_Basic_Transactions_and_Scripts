package BitcoinTransactions.BasicTransactions;

import java.util.ArrayList;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;

/**
 * @author Giorgos Topalidis
 */

// This class simulates a company which in our example consisting of 4
// members
// who represent the Receivers in the PayToScriptHash Transaction.

public class Company {

	private Receiver r1; // company member
	private Receiver r2; // company member
	private Receiver r3; // company member
	private Receiver r4; // company member

	private ArrayList<Receiver> rList;
	private Double balance;

	public Company() throws AddressFormatException {
		super();
		r1 = new Receiver();
		r2 = new Receiver();
		r3 = new Receiver();
		r4 = new Receiver();

		rList = new ArrayList<Receiver>();
		rList.add(r1);
		rList.add(r2);
		rList.add(r3);
		rList.add(r4);
		// System.out.println("The size of the list is : " +rList.size());

		for (Receiver curentReceiver : getrList()) {
			Wallet wallet = new Wallet();
			curentReceiver.addReceiverWallet(wallet);
		}

	}

	public ArrayList<Receiver> getrList() {
		return rList;
	}

	public void setrList(ArrayList<Receiver> rList) {
		this.rList = rList;
	}

	public Receiver getR1() {
		return r1;
	}

	public void setR1(Receiver r1) {
		this.r1 = r1;
	}

	public Receiver getR2() {
		return r2;
	}

	public void setR2(Receiver r2) {
		this.r2 = r2;
	}

	public Receiver getR3() {
		return r3;
	}

	public void setR3(Receiver r3) {
		this.r3 = r3;
	}

	public Receiver getR4() {
		return r4;
	}

	public void setR4(Receiver r4) {
		this.r4 = r4;
	}

	public ArrayList<ECKey> getAllKeys(ArrayList<Receiver> rList) {
		ArrayList<ECKey> keyList = new ArrayList<ECKey>();

		for (Receiver currentReceiver : rList) {
			ECKey currentKey = currentReceiver.getReceiverWalletList().get(0)
					.getClientKey();
			keyList.add(currentKey);
		}

		return keyList;
	}

	// this function will change all the ECKeys of the members of the company by
	// adding new ECKeys for them.
	// i will do that in order to see that we need at least 2 valid keys in
	// order to check that the signatures are valid.

	public ArrayList<ECKey> changeAllKeys(ArrayList<Receiver> rList) {
		ArrayList<ECKey> keyList = new ArrayList<ECKey>();

		for (Receiver currentReceiver : rList) {
			ECKey myCurrentNewKey = new ECKey();
			currentReceiver.getReceiverWalletList().get(0)
					.setClientKey(myCurrentNewKey);
			ECKey currentKey = currentReceiver.getReceiverWalletList().get(0)
					.getClientKey();
			keyList.add(currentKey);
		}

		return keyList;
	}

	public double estimateTotalBalance(ArrayList<Receiver> rList) {
		double totalAmount = 0.0;
		for (Receiver currentReceiver : rList) {
			totalAmount = totalAmount
					+ currentReceiver.getReceiverWalletList().get(0)
							.getBalance();

		}

		return totalAmount;

	}

	@Override
	public String toString() {
		return "Company [r1=" + r1 + ", r2=" + r2 + ", r3=" + r3 + ", r4=" + r4
				+ ", rList=" + rList + "]";
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

}
