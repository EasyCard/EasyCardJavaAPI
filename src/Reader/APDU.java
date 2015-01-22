package Reader;

//so Smart...
public abstract class APDU {
	
	protected boolean mReqDirty = true;
	
	
	protected static final int scReqMinLength_NoData = 9;
	protected static final int scReqMinLength = 10;
	protected static final int scReqInfoMinLength = 6;
	protected static final int scReqDataOffset = 8;
	
	/* Resquest Parameters and Methods */
	/* Prolog */
	protected byte Req_NAD = 0; 		// Node Address, 0x00
	protected byte Req_PCB = 0; 		// Protocol Control Byte, 0x00 
	protected byte Req_LEN = 0;			// Length of Information Section in bytes
	/* Information-Header */
	protected byte Req_CLA = 0;			// Classification code
	protected byte Req_INS = 0;			// Instruction code
	protected byte Req_P1 = 0;			// Parameter 1
	protected byte Req_P2 = 0;			// Parameter 2
	/* Information-Body */	
	protected byte Req_Lc = 0; 			// Length of command data field
	// protected byte[] Req_Data = null;	// Data contents
	protected byte Req_Le = 0; 			// Length of expected data responded
	/* Epilog */
	protected byte Req_EDC = 0;			// Error Detection Code
	
	public abstract byte[] GetRequest();
	public abstract boolean SetRequestData(byte[] bytes);
	public abstract int GetReqRespLength();
	
	protected static final int scRespMinLength = 6;
	protected static final int scRespDataOffset = 3;
	
	/* Respond Parameters and Methods */
	/* Prolog */ 
	protected byte Resp_NAD; 			// Node Address, 0x00
	protected byte Resp_PCB; 			// Protocol Control Byte, 0x00 
	protected byte Resp_LEN;			// Length of Information Section in bytes
	/* Information-Body */	
	protected byte[] Resp_Data;			// Data contents
	/* Information-Trailer */
	protected byte Resp_SW1;			// Status Code 1
	protected byte Resp_SW2;			// Status Code 2
	/* Epilog */
	protected byte Resp_EDC;			// Error Detection Code
	
	public abstract byte[] GetRespond();
	public abstract boolean SetRespond(byte[] bytes);
	
	protected byte getEDC(byte[] bytes, int length) {
		if (bytes == null || length < 2) {
			return 0x00;
		}
		
		byte sum = 0;
		for (int i = 0; i < length - 1; i ++) {
			sum ^= bytes[i]; 
		}
		
		return sum;
	}
	
	public byte ClearBit(byte b, int pos) {
		if (pos < 0 || pos > 7) {
			return b;
		}
		return (byte) (b & ~(1 << pos));
		
	}
	
	public byte SetBit(byte b, int pos) {
		if (pos < 0 || pos > 7) {
			return b;
		}
		return (byte) (b | (1 << pos));
	}
	
	public boolean IsBitSet(byte b, int pos) {
		if (pos < 0 || pos > 7) {
			return false;
		}
		return (b & (1 << pos)) > 0? true : false;
	}
	
	public boolean IsRespOk(byte SW1, byte SW2) {
		if (SW1 == 0x90 && SW2 == 0x00) {
			return true;
		}
		return false;
	}
	
	public int GetRespCode() {
		return ((Resp_SW1 << 8) + Resp_SW2) & 0x0000FFFF;
	}
	

	
	public String GetRespDescription() {
		int status = GetRespCode();
		
		switch (status) {
		case 0x9000:
			return "���榨�\";
		// �H�U��APDU�榡���~-->����פ�, �Э��s�ާ@, ���s�}���γ���	
		case 0x6001:
			return "CLA, INS ERROR";
		case 0x6002:
			return "p1, P2 ERROR";
		case 0x6003:
			return "LC, LE ERROR";
		case 0x6004:
			return "CHECK SUM ERROR";
		case 0x6005:
			return "DATA ERROR";
		case 0x6088:
			return "�u�����}/Time Out";
		// �H�U�����d���~-->�����`�d, �ڵ����
		case 0x6101:
			return "���d���A��(AID Error)";
		case 0x6102:
			return "Issuer Code Error";
		case 0x6103:
			return "CPD Error";
		case 0x6104:
			return "���}�d�䲼�d/���d���A����(Mifare�d)";
		case 0x6105:
			return "�D�n���Ȯ榡���~";
		case 0x6106:
			return "�ƥ����Ȯ榡���~";
		case 0x6107:
			return "�q�l���Ȯ榡���~";
		case 0x6108:
			return "���d�L��";	
		case 0x6109:
			return "���d�w��d";
		case 0x610A:
			return "���d���ˮֽX���~/�λP0x640E�P�q";
		case 0x610B:
			return "Two FAT Error";
		case 0x610C:
			return "FAT Content Error";
		case 0x610D:
			return "�d�����w��(���d�_�l�������)";
		case 0x610E:
			return "CTC��TSQN�W�X����";
		case 0x610F:
			return "Level1 Mifare Locked(6999)";
		case 0x6111:
			return "�������T����CPU�d�W�@�B�J��CPU�d���ҥ���";
		// RC531���~-->����פ�, �Э��s�ާ@
		case 0x6201:
			return "�䤣��d��";
		case 0x6202:
			return "Ū�d����";
		case 0x6203:
			return "�g�d����";
		case 0x6204:
			return "�h�i�d";
		case 0x6205:
			return "RC531 load key����"; 
		case 0x6206:
			return "RC531 Auth����";
		case 0x6207:
			return "CPU�dRecord not found�ζW�X�d��";
		case 0x6208:
			return "CPU�dRecord���䴩MAC��MAC���~";
		// SAM�d���~-->����פ�, �Э��s�}��
		case 0x6301:
			return "SAM�{�ҥ���";
		case 0x6302:
			return "�������T����SAM�W�@�B�J";
		case 0x6303:
			return "�LSAM�d";
		case 0x6304:
			return "�����UPR_Reset/PPR_Reset";
		case 0x6305:
			return "�����UPR_SignOn/PPR_SignOn";
		case 0x6306:
			return "SAM�d�[���B��(CA)����";
		case 0x6307:
			return "��SAM�d���`";
		case 0x6308:
			return "�ݦA���楽�����ҥH��sSAM�Ѽ�";
		case 0x6309:
			return "�sSAM�d���`";
		case 0x630A:
			return "�s��SAM�d�Ҳ��`";
		case 0x630B:
			return "STC�W�X����";
		case 0x630C:
			return "SAM�d���䴩�ӥ\��";
		// ������~-->�ڵ����
		case 0x6401:
			return "��������P�W���������";
		case 0x6402:
			return "������B�W�L�B��";
		case 0x6403:
			return "�l�B����";
		case 0x6404:
			return "�d�����~";
		case 0x6405:
			return "H_TAC���e�{�ҿ��~";
		case 0x6406:
			return "API�^���T�ΦW��/�i�����ʦW�沼�d";
		case 0x6407:
			return "�D���֥d�����i��";
		case 0x6408:
			return "HOST�P�_�����i��";
		case 0x6409:
			return "�۰ʥ[�ȺX�Х��ҥ�";
		case 0x640A:
			return "���d�۰ʥ[�Ȫ��B���s";
		case 0x640B:
			return "�Ҭd�ߪ��ӵ�����Log��Ƥ��s�b";
		case 0x640C:
			return "�ֿn�p�B���O(�ʳf)���B�W�X�魭�B";
		case 0x640D:
			return "�榸�p�B���O(�ʳf)���B�W�X�����B";
		case 0x640E:
			return "����e�l�B�W�X�̫�@���[�ȫ�l�B";
		case 0x640F:
			return "Reader�֭p�[�Ȫ��B�W�X�B�׺ޱ�����";
		case 0x6410:
			return "���d���A��(�D���q�d)";
		case 0x6411:
			return "���d�������(����D100)";
		case 0x6412:
			return "�W�L�q���d����B��(�l�B>1000)";
		case 0x6413:
			return "�s�d�饼���T�Ӥ�";
		case 0x6414:
			return "�d���ϥΦ��ƥ�������";
		case 0x6415:
			return "�ݰ������X�k����";
		case 0x6416:
			return "�۰ʥ[�ȺX�ХH�ҥ�";
		case 0x6417:
			return "���d�w�}�d";
		case 0x6418:
			return "���d�󦹳q������ϥ�";
		case 0x6419:
			return "CPU�d���d���A���A�Φ����";
		case 0x641A:
			return "PPR_SignOn�ˮֽX(EDC)���~";
		case 0x641B:
			return "�������Ƥ��A�Φ����(CPU�d)";
		case 0x641C:
			return "�������Ƥ��A�Φ����(SAM�d)";
		case 0x641D:
			return "�W�X���u�۰ʥ[�Ȥ�u��";
		case 0x641E:
			return "VAR-��X�ˬd����";
		// �w�����~-->�г���
		case 0x6501:
			return "Key Locked";
		case 0x6502:
			return "Key No Initial";
		case 0x6503:
			return "RAM Error";
		case 0x6504:
			return "Flash Failed";
		case 0x6505:
			return "SAM�ϥΦ��Ƥw�F�W��";
		case 0x6506:
			return "SAM�ϥΦ��Ʋ��`/���~";
		case 0x6507:
			return "CPU�dTXN Key Selection Error";
		case 0x6508:
			return "CPU�dSignature Key Selection Error";
		case 0x6509:
			return "SAM�dKey Selection Error";
		case 0x65FF:
			return "�u���D������}�a";
		}
		
		return "���������~";
	}
}
