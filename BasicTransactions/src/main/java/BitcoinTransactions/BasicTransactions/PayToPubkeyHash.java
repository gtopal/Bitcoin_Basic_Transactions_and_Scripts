package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.jcajce.provider.digest.RIPEMD160;

/**
 * @author Giorgos Topalidis
 */

// This class simulates the creation and the validation of a PayToPubKeyHah
// Transaction. This kind of transactions is the most famous and common
// Transaction in the Bitcoin environment.

public class PayToPubkeyHash extends Transaction {

	private String name;
	private String scriptDescription;
	private Receiver receiver;
	private Sender sender;
	private Miner miner;
	private final Double fee = 0.00000005; // 0.00000005 BT
	private ECDSASignature redeemerSignature;
	private Sha256Hash input;
	private Sha256Hash signed256Message; // this variable helps for the
											// verification process.
	
	
	
	private Double familyBitcoinSendingAmount; //this portion of bitcoins will be transfered to bitcoinTransaction() method of Multi-signature class to declare the amount of
											   //bitcoins which send the family to the receiver due to the PayToPubkeyHash transaction which have created after MultiSignature transaction
	
	
	private Double receiverBitcoinTakingAmount; //this portion of bitcoins will be transfered to bitcoinTransaction() method of Multi-signature class to declare the amount of
	                                            //bitcoins which take the receiver from the family due to the PayToPubkeyHash transaction which have created after MultiSignature transaction


	public PayToPubkeyHash(Sender sender, Receiver receiver, Miner miner) {
		super(sender, receiver, miner);
		// this.receiver = receiver;
		// this.sender = sender;
		// this.miner = miner;
		name = "Pay to PubKey Hash";
		scriptDescription = "The OP_DUP OP_HASH160 OP_DATA_20 OP_EQUALVERIFY OP_CHECKSIG script"
				+ " is the standard way to send Bitcoins to a single address;"
				+ " it is known as a Pay to PubKey Hash transaction. "
				+ "Predictably, it is the most popular transaction type in the block chain.";

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

	public void transactionProcedure() throws IOException,
			NoSuchAlgorithmException {
		
		boolean flag=false; // this variable indicates if it is needed after validation to proceed to the payment or not(if the validation script is invalid)!!!

		if (scriptPubKey(receiver, sender, miner)) {
			System.out
					.println("The sender sent the amount of Bitcoins to the receiver(********ScriptPubKey is complete********)");

			System.out
					.println("Now...it is the time for the receiver to claim that he owns"
							+ " the public key which corresponds to the address"
							+ " in which the sender sent the Bitcoins...in order to redeem the Bitcoins...\n");

			receiver = super.getReceiver();

			ECKey rKey = receiver.getReceiverWalletList().get(0).getClientKey();

			redeemerSignature = scriptSig(receiver);

			// 1st way of verification.
		//	verification that this message was signed by this signature which created 
          //  from the private key of receiver which corresponds to the public key hash that was sent to the Sender 
			
//			if (rKey.verify(getSigned256Message(), redeemerSignature))  {
//			 System.out
//			 .println("The receiver prove with his signature that this address belongs to him(********Validation Script completed********)\n");
//				System.out.println("The transaction finished SUCCESSFULLY");
			//flag=true;
//			}
//
//			else
//			{
//			 System.out.println("The receiver can not prove with his signature that this address belongs to him");
//				System.out.println("The transaction finished UNSUCCESSFULLY");
//			}
		

			// 2nd way of verification.
			 if (ECKey.verify(getSigned256Message().getBytes(),
			 redeemerSignature,
			 rKey.getPubKey())) {
			 System.out
			 .println("The receiver prove with his signature that this address belongs to him(********Validation Script completed********)\n");
			 System.out.println("The transaction finished SUCCESSFULLY");
			 flag=true;
			 }
			
			 else {
			
			 System.out
			 .println("The receiver can not prove with his signature that this address belongs to him");
			 System.out
			 .println("The transaction finished UNSUCCESSFULLY");
			 }

		}
		
		if(flag==true){
		  bitcoinTransaction(receiver, sender, miner); 
		 
		 
		
		 }
	}


	public boolean scriptPubKey(Receiver receiver, Sender sender, Miner miner)
			throws IOException {

		System.out
				.println("***************************************************************");
		System.out
				.println("This function creates the Locking Script from the sender of BT");
		System.out
				.println("***************************************************************\n");

		sender = super.getSender();
		receiver = super.getReceiver();

		Wallet rWallet = receiver.getReceiverWalletList().get(0);
		byte[] pubKeyHashFromReceiver = rWallet.getClientKey().getPubKeyHash();

		byte[] pubKeyHashFromSender = sender.getSenderListFromReceiver().get(0); // it
																					// is
																					// the
																					// hash
																					// value
																					// which
																					// was
																					// sent
																					// to
																					// the
																					// S
																					// from
																					// R

		if (Arrays.equals(pubKeyHashFromReceiver, pubKeyHashFromSender)) {
			System.out.println("********EQUALVERIFY********\n");
			return true;
		} else {

			System.out.println("!!!!!!!!NOT EQUALVERIFY!!!!!!!!\n");
			return false;
		}

	}

	public ECDSASignature scriptSig(Receiver receiver)
			throws NoSuchAlgorithmException, IOException {

		System.out
				.println("*********************************************************************************************************");
		System.out
				.println("This function creates the Unlocking Script from the receiver of BT"
						+ " in order to be able to redeem the BT!!!");
		System.out
				.println("*********************************************************************************************************\n");

		receiver = super.getReceiver();
		Wallet rwallet = receiver.getReceiverWalletList().get(0);

		Sha256Hash input = receiver.getMyMessage().getSha256OfMessage(); // the receiver creates the message which is going to be signed, the message simulates the transaction which locks the bitcoins.
		setSigned256Message(input);

		ECDSASignature receiverSig = rwallet.getClientKey().sign(input); // creation of the ECDSA Signature of Receiver
		
																			// SOS
																			// this
																			// input
																			// must
																			// be
																			// the
																			// message
																			// which
																			// is
																			// going
																			// to
																			// be
																			// signed
																			// by
																			// private
																			// key!!!

		return receiverSig;

	}

	public void bitcoinTransaction(Receiver r, Sender s, Miner m) {

		System.out
				.println("////////////////////////////////////////////////////////////////////////////////");
		System.out
				.println("/This procedure will estimate the balance of the Sender's and Receiver's Wallet/\n"
						+ " /as long as the fee which is going to be given to the miner/\n "
						+ "/due to his effort mining the Block "
						+ "which contains this specific transaction!!!/");
		System.out
				.println("////////////////////////////////////////////////////////////////////////////////\n");

		r = super.getReceiver();
		s = super.getSender();
		m = super.getMiner();

		// Initialize the wallet of the Sender with an amount of Bitcoins
		System.out
				.println("********Initialize the wallet of the Sender with an amount of Bitcoins********\n");
		Scanner console = new Scanner(System.in);
		String senderAmountOfBT = console.nextLine();
		Double sAmount = Double.parseDouble(senderAmountOfBT); familyBitcoinSendingAmount=Double.parseDouble(senderAmountOfBT); 
		s.getSenderWalletList().get(0).setBalance(sAmount);
		System.out
				.println("Your answer for initializing the Sender's wallet was : "
						+ sAmount + " BT\n");

		System.out
				.println("********Decide an ammount of Bitcoins to send to the receiver********\n");
		Scanner console2 = new Scanner(System.in);
		String receiverAmountOfBT = console2.nextLine();
		Double rAmount = Double.parseDouble(receiverAmountOfBT); receiverBitcoinTakingAmount=Double.parseDouble(receiverAmountOfBT);
		r.getReceiverWalletList().get(0).setBalance(rAmount);
		System.out
				.println("Your answer for sending an amount of BT to receiver was : "
						+ rAmount + " BT\n");

		if (sAmount - rAmount >= fee) {
			System.out
					.println("***********************************************");
			System.out
					.println("********The transfer of BT is valid!!!********");
			System.out
					.println("***********************************************\n");

			// MINER
			System.out
					.println("--------The fee of the transaction is fixed for every transaction to  : "
							+ fee + " BT--------\n");
			m.setBalance(fee);
			System.out
					.println("----------------------------------------------------");
			
			Double totalBalanceOfMiner=m.getBalance();
			m.getWallet().setBalance(totalBalanceOfMiner);
			System.out.println("-----------The miner now has : "
					+ m.getWallet().getBalance()+ " BT-----------");   //System.out.println("or the miner now has : " +m.getBalance()+ " BT");
																	// as a second way through the balance variable

			System.out
					.println("----------------------------------------------------\n");

			// SENDER
			Double senderOutcome = sAmount - (rAmount + fee);
			s.getSenderWalletList().get(0).setBalance(senderOutcome);
			System.out
					.println("-----------------------------------------------------");
			System.out.println("--------The sender now has : "
					+ s.getSenderWalletList().get(0).getBalance()
					+ " BT--------");
			System.out
					.println("-----------------------------------------------------\n");

			// RECEIVER
			// Double receiverOutcome = sAmount - rAmount;
			// r.getReceiverWalletList().get(0).setBalance(receiverOutcome);
			System.out
					.println("-----------------------------------------------------");
			System.out.println("------------The receiver now has : "
					+ r.getReceiverWalletList().get(0).getBalance()
					+ " BT------------");
			System.out
					.println("-----------------------------------------------------\n");

			

		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("!!!!!!!!The transfer of BT is not valid!!!!!!!!");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

			
		}

	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Miner getMiner() {
		return miner;
	}

	public void setMiner(Miner miner) {
		this.miner = miner;
	}

	public ECDSASignature getRedeemerSignature() {
		return redeemerSignature;
	}

	public void setRedeemerSignature(ECDSASignature redeemerSignature) {
		this.redeemerSignature = redeemerSignature;
	}

	public Sha256Hash getInput() {
		return input;
	}

	public void setInput(Sha256Hash input) {
		this.input = input;
	}

	public Double getFee() {
		return fee;
	}

	public Sha256Hash getSigned256Message() {
		return signed256Message;
	}

	public void setSigned256Message(Sha256Hash signed256Message) {
		this.signed256Message = signed256Message;
	}

	public Double getFamilyBitcoinSendingAmount() {
		return familyBitcoinSendingAmount;
	}

	public void setFamilyBitcoinSendingAmount(Double familyBitcoinSendingAmount) {
		this.familyBitcoinSendingAmount = familyBitcoinSendingAmount;
	}

	public Double getReceiverBitcoinTakingAmount() {
		return receiverBitcoinTakingAmount;
	}

	public void setReceiverBitcoinTakingAmount(Double receiverBitcoinTakingAmount) {
		this.receiverBitcoinTakingAmount = receiverBitcoinTakingAmount;
	}
	
	

}
