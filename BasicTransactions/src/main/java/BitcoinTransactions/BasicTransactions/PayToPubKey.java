package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.ECKey.ECDSASignature;

/**
 * @author Giorgos Topalidis
 */

// This class simulates the creation and the validation of a PayToPubKey
// Transaction. This kind of transactions is also known as Coinbase
// Transactions.

public class PayToPubKey extends Transaction {

	private String name;
	private String scriptDescription;
	private final Double award = 25.0; // 0.00000005 BT
	private Miner miner;
	private Receiver receiver;
	private Sender sender;

	public PayToPubKey(Sender sender, Receiver receiver, Miner miner) {
		super(sender, receiver, miner);
		name = " Pay to PubKey";
		scriptDescription = "The implementation of this script"
				+ "is the standard way of assigning newly mined "
				+ "Bitcoins and transaction fees to an address."; // in our example we return only the value of the mined block which is 25BT and not the fees from transactions
	}

	public boolean coinBaseFunction(Miner miner) {
		System.out
				.println("**************************************************************");
		System.out
				.println("This function implements a coinbase transaction from a miner. ");
		System.out
				.println("**************************************************************\n");

		miner = super.getMiner();
		ECKey mKey = miner.getWallet().getClientKey();

		Sha256Hash input = miner.getMyMessage().getSha256OfMessage();

		ECDSASignature minerSig = mKey.sign(input);

		if (mKey.verify(input, minerSig)) {
			System.out.println("*************************************");
			System.out.println("The transaction finished SUCCESSFULLY");
			System.out.println("*************************************\n");
			return true;
		}

		else {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out.println("The transaction finished UNSUCCESSFULLY");
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return false;
		}

	}

	public void transactionProcedure() throws IOException,
			NoSuchAlgorithmException {

		if (coinBaseFunction(miner)) {
			if (bitcoinTransaction(receiver, sender, miner)) {
				miner = super.getMiner();
				System.out.println("The new balance of the miner is : "
						+ miner.getBalance() + " BT");
			}

		}

	}

	public ECDSASignature scriptSig(Receiver receiver)
			throws NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean bitcoinTransaction(Receiver receiver, Sender sender,
			Miner miner) {

		miner = super.getMiner();
		System.out
				.println("*******************************************************************************");
		System.out
				.println("This function adds 25BT to miner's balance as an award for the Block creation!");
		System.out
				.println("Of course the miner gets the fee of every transaction which proved to be valid\n"
						+ " inside this block,but we estimate only the block award in our example because\n"
						+ " we do not know the transactions which took place for this block.");
		System.out
				.println("*******************************************************************************\n");

		miner.setBalance(award);

		// check the balance
		if (miner.getBalance() >= 25.0) {      // or we can say : miner.getWallet().getBalance()>=25.0
			return true;
		} else {
			return false;

		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScriptDescription() {
		return scriptDescription;
	}

	public void setScriptDescription(String scriptDescription) {
		this.scriptDescription = scriptDescription;
	}

	public boolean scriptPubKey(Receiver receiver, Sender sender, Miner miner)
			throws IOException {

		return false;
	}

}
