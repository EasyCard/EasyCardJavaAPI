package Reader;

public interface IRecvSender {
	public byte[] sendRecv(byte[] sendBuffer);
	public boolean finish();
}
