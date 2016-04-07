package com.bestsonic.search.utils;

import java.util.BitSet;

public class BitSetUtils {

	public static BitSet toBitSet(String s, int length) {
		int len = s.length();
		BitSet bs = new BitSet(length);
		for (int i = 0; i < len; i++) {
			if (s.charAt(i) == '1')
				bs.set(i);
		}
		return bs;
	}

	public static String toString(BitSet bs) {
		int len = bs.length();
		StringBuffer buf = new StringBuffer(len);
		for (int i = 0; i < len; i++)
			buf.append(bs.get(i) ? '1' : '0');
		return buf.toString();
	}
}
