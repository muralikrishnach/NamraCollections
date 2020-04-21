
package com.jayam.impactapp.printer;
public class SagemFP {
	static int inc = 0;
	public static final int AUTH_SUCCESS = 1;
	public static final byte ENROLL_ID = 0x21;
	public static final byte ILV_OK = 0x00;
	public static final byte ILVSTS_OK = 0x00;
	public static final byte ILVSTS_HIT = 0x01;
	public static final byte ILVSTS_NO_HIT = 0x02;
	public static final byte Sagem_PkComp = 0x02;
	public static byte[] ENROLL_IMG_TEMPL = {
		// Adding stx, rc and packet id b4r stuffing process.
				(byte) 0x21, // Command ID
				(byte) (0x08 + 0x07 + 0x09),// Length 1
				(byte) 0x00, // Length 0
				(byte) 0x00, // DB Indetifier
				(byte) 0x00, // Timeout 1
				(byte) 0x00, // Timeout 0
				(byte) 0x66, // Acquisition quality threshold [ 66= 40% ]
				(byte) 0x01, // Enrollment type
				(byte) 0x01, // Number of fingers
				(byte) 0x00, // Save record
				(byte) 0xff, // Export Minutiae Size
				/* Command for Async Message */
				(byte) 0x34, // Command ID for Async Message
				(byte) 0x04, // Length1
				(byte) 0x00, // Length0
				(byte) 0xc1, // c1---41
				(byte) 0x00, (byte) 0x00, (byte) 0x00,
				/* Command for Raw Image Request */
				(byte) 0x3d, // Command ID for Image Request
				(byte) 0x06, // Len 1
				(byte) 0x00, // Len 0
				(byte) 0x00, // Image type
				(byte) 0x3e, // ID_COMPRESSION - Used to define compression
				// parameters
				(byte) 0x02, // COMPRSESSION_VALUE_LENGTH_1
				(byte) 0x00, // COMPRSESSION_VALUE_LENGTH_0
				//(byte) 0x2C,// ID_COMPRESSION_NULL
				(byte) 0x2C, //ID_COMPRESSION_V1(Default Compression Algorithm) = 0x3C;
				// D_COMPRESSION_NULL=0x2C
				(byte) 0x00 // RFU
		// Adding crc , etx after stuffing process.
		};
	public static byte[]captureImageISO(){
		return ENROLL_IMG_TEMPL;
	}
	public static byte[] captureSagemDefault() {
		byte Enroll_Buff[] = new byte[80];
		int Count = 0;
		Enroll_Buff[Count++] = 0x02; // STX
		Enroll_Buff[Count++] = 0x61; // DATA PACKET
		Enroll_Buff[Count++] = (byte) (0x00 + inc); // Counter

		Enroll_Buff[Count++] = 0x21; // Enroll
		Enroll_Buff[Count++] = 0x15; // Length of the packet LSB
		Enroll_Buff[Count++] = 0x00; // Length of the packet MSB

		Enroll_Buff[Count++] = 0x00; // DB Identifier
		Enroll_Buff[Count++] = 0x00; // TIme out LSB
		Enroll_Buff[Count++] = 0x00; // Time out MSB

		Enroll_Buff[Count++] = 0x00; // Acquition Quality Threshold
		Enroll_Buff[Count++] = 0x00; // Enrollment Type (Default: PKC,

		Enroll_Buff[Count++] = 0x01; // Number of Fingers
		Enroll_Buff[Count++] = 0x00; // Save Record
		Enroll_Buff[Count++] = 0x01; // Export Minitue Size

		Enroll_Buff[Count++] = 0x04; // User ID
		Enroll_Buff[Count++] = 0x02;
		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x37;
		Enroll_Buff[Count++] = 0x00;

		Enroll_Buff[Count++] = 0x14;
		Enroll_Buff[Count++] = 0x05;
		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x61;
		Enroll_Buff[Count++] = 0x62;

		Enroll_Buff[Count++] = 0x63;
		Enroll_Buff[Count++] = 0x64;
		Enroll_Buff[Count++] = 0x00;

		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x1B;
		Enroll_Buff[Count++] = 0x03;

		byte[] out = new byte[Count];
		System.arraycopy(Enroll_Buff, 0, out, 0, Count);
		Util.setCRCSagem(out);
        inc++;
		return out;
	}

	public static byte[] captureISO() {
		byte Enroll_Buff[] = new byte[80];

		int Count = 0;
		Enroll_Buff[Count++] = 0x02; // STX
		Enroll_Buff[Count++] = 0x61; // DATA PACKET
		Enroll_Buff[Count++] = (byte) (0x00 + inc); // Counter

		Enroll_Buff[Count++] = 0x21; // Enroll
		Enroll_Buff[Count++] = 0x0C; // Length of the packet LSB
		Enroll_Buff[Count++] = 0x00; // Length of the packet MSB

		Enroll_Buff[Count++] = 0x00; // DB Identifier
		Enroll_Buff[Count++] = 0x00; // TIme out LSB
		Enroll_Buff[Count++] = 0x00; // Time out MSB

		Enroll_Buff[Count++] = 0x00; // Acquition Quality Threshold
		Enroll_Buff[Count++] = 0x01; // Enrollment Type (Default: 00-PKC,01-ISO

		Enroll_Buff[Count++] = 0x01; // Number of Fingers
		Enroll_Buff[Count++] = 0x00; // Save Record
		Enroll_Buff[Count++] = 0x01; // Export Minitue Size

		// For ISO
		Enroll_Buff[Count++] = 0x38; // ISO P
		Enroll_Buff[Count++] = 0x00; //  
		Enroll_Buff[Count++] = 0x01; // 
		Enroll_Buff[Count++] = 0x6E; // ISO related

		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x00; //
		Enroll_Buff[Count++] = 0x1B;
		Enroll_Buff[Count++] = 0x03;

		byte[] out = new byte[Count];
		System.arraycopy(Enroll_Buff, 0, out, 0, Count);
		Util.setCRCSagem(out);
        inc++;
		return out;
	}
	public static byte[] captureImageTemplateComp(){
		byte Enroll_Buff[] = new byte[80];

		int Count = 0;
		Enroll_Buff[Count++] = 0x02; // STX
		Enroll_Buff[Count++] = 0x61; // DATA PACKET
		Enroll_Buff[Count++] = (byte) (0x00 + inc); // Counter

		Enroll_Buff[Count++] = 0x21; // Enroll
		Enroll_Buff[Count++] = 0x1B; // Length of the packet LSB
		Enroll_Buff[Count++] = 0x12; // Length of the packet MSB

		Enroll_Buff[Count++] = 0x00; // DB Identifier
		Enroll_Buff[Count++] = 0x00; // TIme out LSB
		Enroll_Buff[Count++] = 0x00; // Time out MSB
		Enroll_Buff[Count++] = 0x00; // Time out MSB

		Enroll_Buff[Count++] = 0x00; // Acquition Quality Threshold
		Enroll_Buff[Count++] = 0x00; // Enrollment Type (Default: 00-PKC,01-ISO

		Enroll_Buff[Count++] = 0x01; // Number of Fingers
		Enroll_Buff[Count++] = 0x00; // Save Record
		Enroll_Buff[Count++] = (byte) 0xFF; // Save Record

		Enroll_Buff[Count++] = 0x3D; // Export Minitue Size

		// For ISO
		Enroll_Buff[Count++] = 0x06; // ISO P
		Enroll_Buff[Count++] = 0x00; //  
		Enroll_Buff[Count++] = 0x00; // 
		Enroll_Buff[Count++] = 0x3E; // 
		Enroll_Buff[Count++] = 0x02; // ISO related

		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x3c; //
		Enroll_Buff[Count++] = 0x00;
		
		Enroll_Buff[Count++] = 0x69;
		Enroll_Buff[Count++] = 0x62;
		Enroll_Buff[Count++] = 0x1b;
		Enroll_Buff[Count++] = 0x03;

		byte[] out = new byte[Count];
		System.arraycopy(Enroll_Buff, 0, out, 0, Count);
		Util.setCRCSagem(out);
        inc++;
		return out;
	}
	public static byte[] captureImageTemplate(){
		byte Enroll_Buff[] = new byte[80];

		int Count = 0;
		Enroll_Buff[Count++] = 0x02; // STX
		Enroll_Buff[Count++] = 0x61; // DATA PACKET
		Enroll_Buff[Count++] = (byte) (0x00 + inc); // Counter

		Enroll_Buff[Count++] = 0x21; // Enroll
		Enroll_Buff[Count++] = 0x1b; // Length of the packet LSB
		Enroll_Buff[Count++] = 0x12; // Length of the packet MSB

		Enroll_Buff[Count++] = 0x00; // DB Identifier
		Enroll_Buff[Count++] = 0x00; // TIme out LSB
		Enroll_Buff[Count++] = 0x00; // Time out MSB
		Enroll_Buff[Count++] = 0x00; // Time out MSB

		Enroll_Buff[Count++] = 0x00; // Acquition Quality Threshold
		Enroll_Buff[Count++] = 0x01; // Enrollment Type (Default: 00-PKC,01-ISO

		Enroll_Buff[Count++] = 0x01; // Number of Fingers
		Enroll_Buff[Count++] = 0x00; // Save Record
		Enroll_Buff[Count++] = 0x00; // Save Record

		Enroll_Buff[Count++] = 0x3d; // Export Minitue Size

		// For ISO
		Enroll_Buff[Count++] = 0x06; // ISO P
		Enroll_Buff[Count++] = 0x00; //  
		Enroll_Buff[Count++] = 0x00; // 
		Enroll_Buff[Count++] = 0x3e; // 
		Enroll_Buff[Count++] = 0x02; // ISO related

		Enroll_Buff[Count++] = 0x00;
		Enroll_Buff[Count++] = 0x2c; //
		Enroll_Buff[Count++] = 0x00;
		
		Enroll_Buff[Count++] = 0x1e;
		Enroll_Buff[Count++] = 0x0c;
		Enroll_Buff[Count++] = 0x1b;
		Enroll_Buff[Count++] = 0x03;

		byte[] out = new byte[Count];
		System.arraycopy(Enroll_Buff, 0, out, 0, Count);
		//Util.setCRCSagem(out);
        inc++;
		return out;
	}
	public static byte[] verifySagemDefaultTemplate(byte[] template) {
		byte verify_buff[] = new byte[350];
		int LengthTotal = 11;

		verify_buff[0] = 0x02; // Packet ID
		verify_buff[1] = 0x61;
		verify_buff[2] = (byte) (0x00 + inc); // RC
		verify_buff[3] = 0x20; // data identifier - Verify
		verify_buff[4] = (byte) (template.length + 8); // setting the true
														// length
		verify_buff[5] = 0x00; // Length
		verify_buff[6] = 0x00; // Time Out
		verify_buff[7] = 0x00; // Time Out
		verify_buff[8] = 0x05; // Matching Threshold
		verify_buff[9] = 0x00; // Matching Threshold
		verify_buff[10] = 0x00; // Aquisition Quality Threshold
		verify_buff[11] = 0x02; // Reference Template 1 //iso-6E
		// ILV
		verify_buff[12] = (byte) template.length;
		verify_buff[13] = 0x00; // Length
		System.arraycopy(template, 0, verify_buff, 14, template.length); // Template
																			// data
		LengthTotal = 14 + template.length;

		verify_buff[LengthTotal] = 0x00;
		verify_buff[LengthTotal + 1] = 0x00;

		verify_buff[LengthTotal + 2] = 0x1b;
		verify_buff[LengthTotal + 3] = 0x03;
		byte[] out = new byte[LengthTotal + 3 + 1];
		System.arraycopy(verify_buff, 0, out, 0, out.length);
		Util.setCRCSagem(out);
        	inc++;
		return out;
	}

	public static byte[] verifyISOTemplate(byte[] template) {
	
		byte Enroll_Buff[] = new byte[350];

		byte CRC[] = new byte[2];
		int LengthTotal = 11;

		Enroll_Buff[0] = 0x02; // Packet ID
		Enroll_Buff[1] = 0x61;
		Enroll_Buff[2] = (byte) (0x00 + inc); // RC
		Enroll_Buff[3] = 0x20; // data identifier - Verify
		byte[] l1 = Util.writeShort(template.length + 8 + 8); // Added 8 extra
																// for ISO_PK
																// Details
		Enroll_Buff[4] = (byte) l1[0];// (byte)(template.length+8); // ILV
										// 1+2+template
		Enroll_Buff[5] = (byte) l1[1]; // Length
		Enroll_Buff[6] = 0x00; // Time Out
		Enroll_Buff[7] = 0x00; // Time Out
		Enroll_Buff[8] = 0x05; // Matching Threshold
		Enroll_Buff[9] = 0x00; // Matching Threshold
		Enroll_Buff[10] = 0x00; // Aquisition Quality Threshold
		// ISO_PK DETAILS
		byte[] l3 = Util.writeShort(template.length + 8);
		Enroll_Buff[11] = 0x3F; // ID_ISO_PK
		Enroll_Buff[12] = l3[0]; // L1+L2
		Enroll_Buff[13] = l3[1]; // L1+L2
		// ISO_PK PARAM
		Enroll_Buff[14] = 0x40;
		Enroll_Buff[15] = 0x02;
		Enroll_Buff[16] = 0x00;
		Enroll_Buff[17] = 0x01;
		Enroll_Buff[18] = 0x01;

		// ISO_PK_DATA_ISO_FMR
		Enroll_Buff[19] = 0x6E; // iso-6E
		byte[] l2 = Util.writeShort(template.length);
		Enroll_Buff[20] = l2[0];
		Enroll_Buff[21] = l2[1];

		System.arraycopy(template, 0, Enroll_Buff, 14 + 8, template.length); // Template
																				// data
		LengthTotal = 14 + template.length + 8;
		CRC[0] = 0x00;
		CRC[1] = 0x00;
		// ComputeCrc16(Enroll_Buff+3,j+11,CRC+1,CRC);

		Enroll_Buff[LengthTotal] = CRC[0];
		Enroll_Buff[LengthTotal + 1] = CRC[1];

		Enroll_Buff[LengthTotal + 2] = 0x1b;
		Enroll_Buff[LengthTotal + 3] = 0x03;
		byte[] out = new byte[LengthTotal + 3 + 1];
		System.arraycopy(Enroll_Buff, 0, out, 0, out.length);
		Util.setCRCSagem(out);
        	inc++;
		return out;
	}

     public static byte[] iMyEnrollForInternal()
	{
         	byte Enroll_Buff[] = new byte[150];
		byte CRC[] = new byte[2];
		int LengthTotal = 11;
        int Count = 0;
	
	Enroll_Buff[Count++]=0x02;
	Enroll_Buff[Count++]=0x61;
	Enroll_Buff[Count++]=(byte) (0x00 + inc);

	Enroll_Buff[Count++]=0x21;
	Enroll_Buff[Count++]=0x1a;
	Enroll_Buff[Count++]=0x00;

	Enroll_Buff[Count++]=0x00;
	Enroll_Buff[Count++]=0x00;
	Enroll_Buff[Count++]=0x00;

	Enroll_Buff[Count++]=0x00;
	Enroll_Buff[Count++]=0x00;

	Enroll_Buff[Count++]=0x01;
	Enroll_Buff[Count++]=0x01;
	Enroll_Buff[Count++]=0x01;
//***********************************************************************************
// For USER ID
	Enroll_Buff[Count++]=0x04;	
	Enroll_Buff[Count++]=0x02; //Length of Packet
	Enroll_Buff[Count++]=0x00; //Length of Packet

    Enroll_Buff[Count++]=0x32; // USER ID
    Enroll_Buff[Count++]=0x00;
//***********************************************************************************
//For USER FIRST NAME
	Enroll_Buff[Count++]=0x14;
	Enroll_Buff[Count++]=0x06; //Length of Packet
    Enroll_Buff[Count++]=0x00; //Length of Packet
/**** User Name  is given as Sagem*********/
    Enroll_Buff[Count++]=0x53;
    Enroll_Buff[Count++]=0x61;
    Enroll_Buff[Count++]=0x67;
    Enroll_Buff[Count++]=0x65;
    Enroll_Buff[Count++]=0x6d ;
	Enroll_Buff[Count++]=0x00;


//***********************************************************************************

	Enroll_Buff[Count++]=0x14;
	Enroll_Buff[Count++]=0x01; //Length of Packet
	Enroll_Buff[Count++]=0x00; //Length of Packet
	Enroll_Buff[Count++]=0x00;
	
	CRC[0]=0x00;
	CRC[1]=0x00;

    Enroll_Buff[Count++]=0x00;
	Enroll_Buff[Count++]=0x00;
	Enroll_Buff[Count++]=0x1B;
	Enroll_Buff[Count++]=0x03;

    byte[] out = new byte[Count];
       System.arraycopy(Enroll_Buff, 0, out, 0, Count);
		Util.setCRCSagem(out);
    inc++;
    return out;
	
	}

 public static byte[] VerifyInternal()
	{         	
         byte   verify_buff[] = new byte[150];
		byte CRC[] = new byte[2];
		int LengthTotal = 11;
        int Count = 0;

	verify_buff[Count++]=0x02;
	verify_buff[Count++]=0x61;
	verify_buff[Count++]=(byte) (0x00 + inc);

	verify_buff[Count++]=0x22;
	verify_buff[Count++]=0x06;
	verify_buff[Count++]=0x00;

	verify_buff[Count++]=0x00; // Data base
	verify_buff[Count++]=0x00; // time out
	verify_buff[Count++]=0x00; // time out

	verify_buff[Count++]=0x05; // Matching threshold
	verify_buff[Count++]=0x00; // Matching threshold
	verify_buff[Count++]=0x00; // RFU

	CRC[0]=0x00;
	CRC[1]=0x00;


    verify_buff[Count++]=0x00; // CRC
	verify_buff[Count++]=0x00; // CRC
	verify_buff[Count++]=0x1B;
	verify_buff[Count++]=0x03;

    byte[] out = new byte[Count];
       System.arraycopy(verify_buff, 0, out, 0, Count);
		Util.setCRCSagem(out); // CRC calculation
    inc++;
    return out;

	}
}
