package Recent_Transactions.WalletAppKit;

import java.awt.List;
import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;

/**
 * @author Giorgos Topalidis
 */

// This class fetches the last Block of the Block Chain and gives some
// information about the transactions which are included in this Block.

public class DumpLastBlock {

	public static void main(String[] args) throws InterruptedException,
			ExecutionException, BlockStoreException {

		WalletAppKit kit = new WalletAppKit(MainNetParams.get(),
				new java.io.File("."), "test");
		kit.startAndWait();
		BlockChain chain = kit.chain();
		BlockStore bs = chain.getBlockStore();
		Peer peer = kit.peerGroup().getDownloadPeer();
		Block b = peer.getBlock(bs.getChainHead().getHeader().getHash()).get();

		DumpLastBlock dumplastblock = new DumpLastBlock();
		dumplastblock.printInfoAboutBlock(b, bs);
		dumplastblock.printInfoAboutTheFirstTransaction(b, chain);

	}

	public void printInfoAboutBlock(Block b, BlockStore bs)
			throws BlockStoreException {
		System.out
				.println("--------------------------------------------------------------------------");
		System.out.println("#"
				+ bs.get(bs.getChainHead().getHeader().getHash()).getHeight());
		System.out.println("BlockHash :" + b.getHashAsString());
		System.out.println("Difficulty : " + b.getDifficultyTarget());
		System.out.println("Nonce : " + b.getNonce());
		System.out.println("Time : " + b.getTime());
		System.out.println("Previous :" + b.getPrevBlockHash());
		System.out.println("Number of transactions :"
				+ b.getTransactions().size());
		System.out
				.println("--------------------------------------------------------------------------");
	}

	public void printInfoAboutTheFirstTransaction(Block b, BlockChain chain) {
		java.util.List<Transaction> tList = b.getTransactions();
		System.out
				.println("*******************************************************************************************************************************************");
		System.out
				.println("The hash of the first transaction of this block is : "
						+ tList.get(0).getHashAsString());
		System.out.println("Is this transaction a coinbase transaction : "
				+ tList.get(0).isCoinBase());// because it is the first of the block!!!
		
		System.out.println("The hash of the second transaction of this block is : " +tList.get(1).getHashAsString());
		System.out.println("Is this transaction a coinbase transaction : "
				+ tList.get(1).isCoinBase());

														
		System.out
				.println("*******************************************************************************************************************************************");
	}

}
