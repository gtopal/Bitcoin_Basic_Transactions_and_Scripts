package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.ScriptBuilder;

/**
 * @author Giorgos Topalidis
 */

// This is my main class which instantiates different objects
// in order to simulate different scenarios regarding my Transactions

public class Main {

	public static void main(String[] args) throws IOException,
			AddressFormatException, NoSuchAlgorithmException {

		// Creation of  identities which participate in transactions

		Sender sender = new Sender();
		Receiver receiver = new Receiver();
		Miner miner = new Miner();
		Company company = new Company();
	//	Family family =new Family();

		Wallet receiverWallet = new Wallet();
		// Wallet receiverWallet2 = new Wallet();

		Wallet sendererWallet1 = new Wallet();
		// Wallet sendererWallet2 = new Wallet();

		receiver.getReceiverWalletList().clear();
		receiver.addReceiverWallet(receiverWallet);
		// receiver.addReceiverWallet(receiverWallet2);

		sender.getSenderWalletList().clear();
		sender.addSenderWallet(sendererWallet1);
		// sender.addSenderWallet(sendererWallet2);

		sender.setSenderListFromReceiver(receiver.getOnlyHash160Addresses(receiver.getReceiverWalletList())); // the exchange of the Receiver's PublicKeyHash list.

																				 
		
		
		
		// SCENARIO 1- WALLET
		//System.out.println("This is for example the Bitcoin Address of the Receiver : " +receiverWallet.getBitcoinAddress()+ " and as we can see is valid, because it starts with 1");
		//System.out.println("This is the result of encoding the public key hash with Base58 encoding without using checksum : " +receiverWallet.getBitcoinAddressWithoutChecksum());
		
		//SCENARIO 2-PAY-TO-PUBKEY-HASH
		//PayToPubkeyHash paytopubkeyhashTransaction=new
		//PayToPubkeyHash(sender, receiver, miner);
		//paytopubkeyhashTransaction.transactionProcedure();
		
		//SCENARIO 3-PAY-TO-PUBKEY
		// PayToPubKey paytopubkeyTransaction=new PayToPubKey(sender, receiver,miner);
		// paytopubkeyTransaction.transactionProcedure();
		
		//SCENARIO 4-MULTI-SIGNATURE
		//MultiSignature multiSignature = new MultiSignature(sender, receiver,miner);
	   // multiSignature.implementPayToPubkeyHashTransaction(receiver, sender,miner);
	    
		//SCENARIO 5-PAY-TO-SCRIPT-HASH
		// PayToScriptHash paytoscripthashTransaction=new PayToScriptHash(sender, receiver, miner);
        // paytoscripthashTransaction.transactionProcedure();

 		//SCENARIO 6-DATA OUTPUT TRANSACTION
        // DataOutput dataOutputTransaction = new DataOutput();
 		// dataOutputTransaction.createOpReturnTransaction(dataOutputTransaction.getTransaction(),dataOutputTransaction.getScriptbuilder());
		
		

	
		

	}
	
}
