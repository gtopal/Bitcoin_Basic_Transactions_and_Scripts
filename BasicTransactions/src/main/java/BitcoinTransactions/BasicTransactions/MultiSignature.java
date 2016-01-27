package BitcoinTransactions.BasicTransactions;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

/**
 * @author Giorgos Topalidis
 */

// This class simulates the creation and the validation of a Multi-Signature
// Transaction.

public class MultiSignature extends Transaction {

	private String name;
	private String scriptDescription;
	private final Double fee = 0.00000005; // 0.00000005 BT
	private Sender sender;
	private Receiver receiver;
	private Miner miner;

	private Family family;
	ScriptBuilder scriptbuilder;
	private Map<ECKey, Sha256Hash> map = new HashMap<ECKey, Sha256Hash>();
	private Map<ECKey, Sha256Hash> map2 = new HashMap<ECKey, Sha256Hash>();


	private PayToPubkeyHash myPayToPubkeyHashTransaction; //after the unlocking the family is going to spend the funds to someone,this is the reason why the family creates a Pay-to-PubKey-Hash Transaction
	
	ArrayList<Sha256Hash> signedMessageList = new ArrayList<Sha256Hash>();

	public MultiSignature(Sender sender, Receiver receiver, Miner miner)
			throws AddressFormatException {
		super(sender, receiver, miner);

		name = "Multi-Signature";
		scriptDescription = "Multi-signature scripts set a condition"
				+ " where N public keys are recorded in the script"
				+ " and at least M of those" + " must provide signatures"
				+ " to release the encumbrance.";

		family = new Family();
		scriptbuilder = new ScriptBuilder();
	}

	public void createLockingScript() {

		int threshold = 2; // i am going to build a 2-of-3 multi signature
							// condition. In order to satisfy the pairs
							// (Father-Child,Mother-Child,Couple).
		ArrayList<ECKey> keyList = family.getAllKeys(family.getsList());
		// Creation of ScriptPub script
		Script myMultiSigOutputScript = ScriptBuilder
				.createMultiSigOutputScript(threshold, keyList);
		// System.out.println(myMultiSigOutputScript);

	}

	public ArrayList<ECDSASignature> createUnlockingScript() {
		
		

		ArrayList<ECKey> keyList = family.getAllKeys(family.getsList());
		ArrayList<ECDSASignature> signatureList = new ArrayList<ECDSASignature>();

		for (Sender currentSender : family.getsList()) { // for every member of the family fetch its ECKey in order to produce afterwards the Signature 
			Wallet currentWallet = currentSender.getSenderWalletList().get(0);
			ECKey currentKey = currentWallet.getClientKey();

			Sha256Hash sha256OfCurrentSender = currentSender.getMyMessage()
					.getSha256OfMessage();
			
			signedMessageList.add(sha256OfCurrentSender); //here i add my signed messages in the list in order to compare afterwards these messages with the new ECKeys 

			// mapping the currentKey with the sha256OfCurrentSender
			map.put(currentKey, sha256OfCurrentSender);

			ECDSASignature senderSig = currentWallet.getClientKey().sign(
					sha256OfCurrentSender);
			signatureList.add(senderSig);

		}
		return signatureList;

	}
	//implementing essentially the CHECKMULTISIG OP-CODE
	public boolean createValidationScript() {

		System.out.println("--------------------------------------------");
		System.out.println("This function is going to implement\n"
				+ " the signature validation over the\n "
				+ "public keys of the senders.\n"
				+ "In our example we have to validate\n"
				+ " that 2 out of 3 public keys\n"
				+ " match with the 2 out of 3 signatures\n"
				+ "in order the family to unlock\n" + " the transaction and\n"
				+ " to send finally the bitcoins.");
		System.out.println("--------------------------------------------");

		ArrayList<ECKey> keyList = family.getAllKeys(family.getsList());
		ArrayList<ECDSASignature> signatureList = createUnlockingScript();
		int matches = 0;

		System.out.println("**********************************************");
		System.out.println("These 2 for loops compare\n "
				+ "the first signature against each public key\n"
				+ " until it finds an ECDSA match.\n "
				+ "Starting with the subsequent public key,\n"
				+ " it compares the second signature\n "
				+ "against each remaining public key\n"
				+ " until it finds an ECDSA match.\n"
				+ " The process is repeated until\n "
				+ "all signatures have been checked\n"
				+ " or not enough public keys remain\n"
				+ " to produce a successful result.");
		System.out.println("**********************************************\n");

		System.out.println("The size of the signature list is : "
				+ signatureList.size());
		System.out.println("The size of the EC keys is : " + keyList.size()
				+ "\n");

		// Iterator itr = keyList.iterator();
		ListIterator<ECKey> itr = keyList.listIterator();

		int counter = 1;
		int j = 0;

		for (ECDSASignature currentSignature : signatureList) {
			System.out.println("Now i am doing the : " + counter
					+ " check of signatures in my signature list.\n");

			// here i have to declare again my list, because after iteration the
			// list ends up empty!!!
			// keyList=getECKeys(family);

			if (counter > 1) {

				// GOOD SCENARIO
				ArrayList<ECKey> tempList = getECKeys(family);// refill the new
																// empty list
																// with the past
																// keys

				// BAD SCENARIO
				// ArrayList<ECKey> tempList =getAllChangedECKeys(family); 
				     //refill the new empty list with
				     // the new modified keys

				for (int k = 0; k < tempList.size(); k++) {
					keyList.add(tempList.get(k));
				}
				
				
//				for (int k = 0; k < tempList.size(); k++) {
//					map2.put(tempList.get(k), signedMessageList.get(k)); //map2 matches the previous messages from the first map to the new changed ECKeys
//					
//				}
				
				
				
				
				
				
				itr = keyList.listIterator(); // declare a "new" iterator for
												// the "new" list of keys...i refilled essentially the keyList which i had before(containing the ECKeys before the list going empty)

			}

			for (int i = keyList.size(); i > -1; i--) {

				ECKey currentKey = keyList.get(j);
				Sha256Hash input = null;
				 input = map.get(currentKey);

//				if(counter==1){
//				 input = map.get(currentKey);
//				}  else
//				{
//					 input = map2.get(currentKey);
//                                                          
//				}
														// this is the reason why i created the
														// map..in order in the
														// for loop of keyList to
														// have direct access to
														// the signed message
														// of the current
														// receiver
														// corresponding to this
														// ECKey.
				
				
				if (ECKey.verify(input.getBytes(), currentSignature,
						currentKey.getPubKey())) {
					System.out
							.println("************************************************************************************************************");
					System.out.println("The match was SUCCESSFUL!!!");
					matches = matches + 1;
					System.out
							.println("You can remove the current key from the list"
									+ " in order not to be matched again in the next iteration.");
					System.out.println("The matches now are : " + matches);
					System.out
							.println("************************************************************************************************************\n");

					if (itr.hasNext()) {
						itr.next();
						itr.remove(); // Use itr.remove() method
						System.out
								.println("--------------------------------------");
						System.out
								.println("The size of the ECKey list is NOW : "
										+ keyList.size());
						// System.out.println(keyList);
						System.out
								.println("--------------------------------------\n");

						// break;

					}

				} else {
					System.out
							.println("************************************************************************************************************");
					System.out.println("The match was UNSUCCESSFUL!!!");
					System.out
							.println("You can remove the current key from the list"
									+ " in order not to be matched again in the next iteration.");
					System.out
							.println("************************************************************************************************************\n");

					if (itr.hasNext()) {
						itr.next();
						itr.remove(); // Use itr.remove() method
						System.out
								.println("--------------------------------------");

						System.out
								.println("The size of the ECKey list is NOW : "
										+ keyList.size());
						// System.out.println(keyList);
						System.out
								.println("--------------------------------------\n");

						// break;

					}

				}

				// in order the i index to follow the current size of the list, because in every iteration the size of the list decreases by 1(for example at the first iteration was 3,at the second 2...and so on)
				// (3-->2-->1-->0)

				i = keyList.size();
				// always check the first key of the current list,keyList.get(0)

				// System.out.println(j+"\n");

			}

			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("it is obvious that the size of my key list after extracting all the keys will be : "
							+ keyList);
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

			counter++;

		}

		if (matches >= 2) {
			System.out
					.println("*******************************************************FINALLY*********************************************************************");
			System.out.println("At least 2 out of 3 members(senders) "
					+ "of this family have in their possession"
					+ " valid signatures according to their public keys!!!");
			System.out
					.println("***********************************************************************************************************************************\n");

			return true;
		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!UNFORTUNATELY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("The number of keys which validate the signatures for this transaction is not suficient");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
			return false;
		}

	}

	public ArrayList<ECKey> getECKeys(Family family) {

		// i have now all the keys from the senders(members) of the family!
		ArrayList<ECKey> myList = family.getAllKeys(family.getsList());
		return myList;

	}

	public ArrayList<ECKey> getAllChangedECKeys(Family family) {

		// i have now all the new keys from the senders(members) of the
		// family in order to check the validity of the signatures!
		ArrayList<ECKey> myList = family.changeAllKeys(family.getsList());
		return myList;

	}

	public void implementPayToPubkeyHashTransaction(Receiver receiver,
			Sender sender, Miner miner) throws IOException,
			NoSuchAlgorithmException {
		if (createValidationScript() == true) {

			sender = super.getSender();
			receiver = super.getReceiver();
			miner = super.getMiner();

			System.out
					.println("**********************************************");
			System.out.println("The validation procedure of Multi-Signature\n"
					+ " transaction for the family\n"
					+ "ended up SUCCESSFULLY, and now we can continue\n"
					+ " with implementing a PayToPubKey Transaction\n"
					+ "in order the family to send its BT to a receiver.");
			System.out
					.println("**********************************************\n");

			// myPayToPubkeyHashTransaction.scriptPubKey(receiver, sender,
			// miner);
			// myPayToPubkeyHashTransaction.scriptSig(receiver);
			myPayToPubkeyHashTransaction = new PayToPubkeyHash(sender,
					receiver, miner);
			myPayToPubkeyHashTransaction.transactionProcedure();

			bitcoinTransaction(family, receiver, miner);

		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("The validation procedure of Multi-Signature went wrong./TRY AGAIN");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		}
	}

	public boolean bitcoinTransaction(Family family, Receiver receiver,
			Miner miner) {

		System.out
				.println("////////////////////////////////////////////////////////////////////////////////");
		System.out
				.println("/This procedure will estimate the balance of the Receiver and Family(containing of 3 senders) Wallet/\n"
						+ " /as long as the fee which is going to be given to the miner/\n "
						+ "/due to his effort mining the Block "
						+ "which contains this specific transaction!!!/");
		System.out
				.println("////////////////////////////////////////////////////////////////////////////////\n");

		receiver = super.getReceiver();
		miner = super.getMiner();
		this.family = family;

		// Initialize the wallet of the Family with an amount of Bitcoins
		System.out
				.println("********The family now acts as Sender and initializes its wallet with the amount which was defined from the PayToPubkeyHash class ********\n");
//		Scanner console = new Scanner(System.in);
//		String familyAmountOfBT = console.nextLine();
//		Double familyAmount = Double.parseDouble(familyAmountOfBT);
		
		Double familyAmount = myPayToPubkeyHashTransaction.getFamilyBitcoinSendingAmount();
		

		Double myInitialBalance = family
				.estimateTotalBalance(family.getsList()); 
		Double myFinalBalance = myInitialBalance + familyAmount;
		family.setBalance(myFinalBalance);

		System.out
				.println("Your answer for initializing the Family's wallet was : "
						+ familyAmount + " BT\n");
		System.out.println("So now the family has : " + family.getBalance()
				+ " BT");

		System.out.println("********The Receiver takes the amount of bitcoins which was defined from the PayToPubkeyHash class ********\n");

//		Scanner console2 = new Scanner(System.in);
//		String receiverAmountOfBT = console2.nextLine();
//		Double rAmount = Double.parseDouble(receiverAmountOfBT);
		
		Double rAmount = myPayToPubkeyHashTransaction.getReceiverBitcoinTakingAmount();
		
		
		receiver.getReceiverWalletList()
				.get(0)
				.setBalance(
						rAmount
								+ receiver.getReceiverWalletList().get(0)
										.getBalance());

		System.out
				.println("Your answer for sending an amount of BT to receiver was : "
						+ rAmount + " BT\n");

		if (family.getBalance() - rAmount >= fee) {
			System.out
					.println("***********************************************");
			System.out
					.println("********The transfer of BT is valid!!!********");
			System.out
					.println("***********************************************\n");

			// MINER
			 miner.getWallet().setBalance(fee); //the balance of the miner's wallet returns to the value of fee

			System.out
					.println("--------The fee of the transaction is fixed for every transaction to  : "
							+ fee + " BT--------\n");
			miner.setBalance(fee);
			System.out
					.println("----------------------------------------------------");
			System.out.println("-----------The miner now has : "
					+ miner.getBalance() + " BT-----------");
			System.out
					.println("----------------------------------------------------\n");

			// FAMILY
			Double familyOutcome = family.getBalance() - (rAmount + fee);
			family.setBalance(familyOutcome);
			System.out
					.println("-----------------------------------------------------");
			System.out.println("--------The family now has : "
					+ family.getBalance() + " BT--------");
			System.out
					.println("-----------------------------------------------------\n");

			// RECEIVER
			 receiver.getReceiverWalletList().get(0).setBalance(rAmount); //the balance of the receiver's wallet returns to the value of rAmount
			 
			System.out
					.println("-----------------------------------------------------");
			System.out.println("------------The receiver now has : "
					+ receiver.getReceiverWalletList().get(0).getBalance()
					+ " BT------------");
			System.out
					.println("-----------------------------------------------------\n");

			return true;

		} else {
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			System.out
					.println("!!!!!!!!The transfer of BT is not valid!!!!!!!!");
			System.out
					.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

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

}
