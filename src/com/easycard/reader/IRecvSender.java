package com.easycard.reader;

public interface IRecvSender {
	public byte[] sendRecv(byte[] sendBuffer);
	public boolean finish();
}
