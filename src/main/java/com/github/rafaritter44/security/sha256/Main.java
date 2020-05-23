package com.github.rafaritter44.security.sha256;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.xml.bind.DatatypeConverter;

public class Main {
	
	public static void main(final String[] args) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		final Deque<byte[]> blocks = new ArrayDeque<byte[]>();
		try (final InputStream inputStream = new FileInputStream(new File(args[0]))) {
			final byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				final byte[] block = new byte[length];
				System.arraycopy(buffer, 0, block, 0, length);
				blocks.offer(block);
			}
		}
		final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] block = blocks.pollLast();
		byte[] hash = messageDigest.digest(block);
		while ((block = blocks.pollLast()) != null) {
			final byte[] blockAndHash = new byte[block.length + hash.length];
			System.arraycopy(block, 0, blockAndHash, 0, block.length);
			System.arraycopy(hash, 0, blockAndHash, block.length, hash.length);
			hash = messageDigest.digest(blockAndHash);
		}
		System.out.println(DatatypeConverter.printHexBinary(hash));
	}
	
}
