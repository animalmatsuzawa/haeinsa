package kr.co.vcnc.haeinsa;

import java.io.IOException;
import java.util.Comparator;

import kr.co.vcnc.haeinsa.thrift.generated.TRowLock;

import com.google.common.collect.ComparisonChain;

/**
 * Scanner wrapper of HaeinsaKeyValue. 
 * Contains multiple HaeinsaKeyValue inside to allow iterator pattern.
 * <p>HaeinsaKeyValueScanner interface provides additional {@link HaeinsaKeyValueScanner#peek} 
 * method to peek element of scanner without moving iterator.
 * <p>Each HaeinsaKeyValueScanner have sequenceId which represent which scanner is newer one. 
 *  
 * @author Myungbo Kim
 *
 */
public interface HaeinsaKeyValueScanner {
	public static final Comparator<HaeinsaKeyValueScanner> COMPARATOR = new Comparator<HaeinsaKeyValueScanner>() {
		
		@Override
		public int compare(HaeinsaKeyValueScanner o1, HaeinsaKeyValueScanner o2) {
			return ComparisonChain.start()
					.compare(o1.peek(), o2.peek(), HaeinsaKeyValue.COMPARATOR)
					.compare(o1.getSequenceID(), o2.getSequenceID())
					.result();
		}
	}; 
	
	/**
	 * Look at the next KeyValue in this scanner, but do not iterate scanner.
	 * 
	 * @return the next KeyValue
	 */
	public HaeinsaKeyValue peek();

	/**
	 * Return the next KeyValue in this scanner, iterating the scanner
	 * 
	 * @return the next KeyValue
	 */
	public HaeinsaKeyValue next() throws IOException;
	
	/**
	 * 
	 * @return Return TRowLock if exist in HaeinsaKeyValue. Otherwise, return null
	 * @throws IOException
	 */
	public TRowLock peekLock() throws IOException;

	/**
	 * Get the sequence id associated with this KeyValueScanner. This is
	 * required for comparing multiple KeyValueScanners to find out which one has the
	 * latest data. The default implementation for this would be to return 0. 
	 * A KeyValueScanner having lower sequence id will be considered to be the newer one.
	 */
	public long getSequenceID();

	/**
	 * Close the KeyValue scanner.
	 */
	public void close();
}