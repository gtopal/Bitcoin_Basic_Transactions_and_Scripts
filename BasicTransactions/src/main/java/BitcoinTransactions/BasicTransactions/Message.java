package BitcoinTransactions.BasicTransactions;

import java.util.Scanner;

import org.bitcoinj.core.Sha256Hash;

/**
 * @author Giorgos Topalidis
 */

/*
 * This class will create a message (in a String variable)\n"
 * " which is going to be signed by the private key of the owner of ECKey.\n"
 * "Afterwards in order to check the validity of the ECDSASignature\n"
 * " of the owner of ECKey,we have to provide :\n "
 * "1st : the sha256= SHA256(message) wraped in Sha256Hash mySha256=new Sha256Hash(sha256)\n"
 * "2nd : the ECDSASignature\n" "3rd : the public key of the owner of ECKey.")
 */

public class Message {

	private String myMessageToBeSigned;

	public Message() {

	}

	public String getMyMessageToBeSigned() {
		return myMessageToBeSigned;
	}

	public void setMyMessageToBeSigned(String myMessageToBeSigned) {
		this.myMessageToBeSigned = myMessageToBeSigned;
	}

	// This function defines the message, estimates initially the
	// SHA256(message),
	// afterwards wraps this outcome and
	// finally returns it.

	public Sha256Hash getSha256OfMessage() {
		System.out.println("Type the message which is going to be signed...");
		Scanner console = new Scanner(System.in);
		myMessageToBeSigned = console.nextLine();

		byte[] sha256 = org.apache.commons.codec.digest.DigestUtils
				.sha256(myMessageToBeSigned);
		Sha256Hash mySha256 = new Sha256Hash(sha256);
		return mySha256;

	}

	@Override
	public String toString() {
		return "Message [myMessageToBeSigned=" + myMessageToBeSigned + "]";
	}

}
