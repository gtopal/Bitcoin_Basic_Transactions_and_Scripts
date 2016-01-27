package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.bitcoinj.core.ECKey.ECDSASignature;

/**
 * @author Giorgos Topalidis
 */

// This class is an abstract class which initially created to supply its
// abstract methods to the PayToPubKey class, PayToPubKeyHash class,
// PayToScriptHash class and MultiSignature class
// but after some particular difficulties implementing some of the functions of
// the above classes, i decided to keep its functionality at low levels.

public abstract class Transaction {

	private Sender sender;
	private Receiver receiver;
	private Miner miner;

	public Transaction(Sender sender, Receiver receiver, Miner miner) {
		this.sender = sender;
		this.receiver = receiver;
		this.miner = miner;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public Miner getMiner() {
		return miner;
	}

	public void setMiner(Miner miner) {
		this.miner = miner;
	}

	// public abstract void transactionProcedure() throws IOException,
	// NoSuchAlgorithmException;
	//
	// public abstract boolean scriptPubKey(Receiver receiver,Sender
	// sender,Miner miner) throws IOException;
	//
	// public abstract ECDSASignature scriptSig(Receiver receiver)
	// throws NoSuchAlgorithmException, IOException;
	//
	// public abstract boolean bitcoinTransaction(Receiver receiver,
	// Sender sender, Miner miner);

}
