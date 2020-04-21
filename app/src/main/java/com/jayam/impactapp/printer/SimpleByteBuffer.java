package com.jayam.impactapp.printer;

public class SimpleByteBuffer {
	protected byte buf[];
    int count;
    
    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this byte array output stream.
     *
     * @param   b     the data.
     * @param   off   the start offset in the data.
     * @param   len   the number of bytes to write.
     */
    public SimpleByteBuffer(){
    	buf = new byte[32];

    }
    public synchronized void append(byte b[]) {
        int newcount = count + b.length;
        if (newcount > buf.length) {
            byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        System.arraycopy(b, 0, buf, count, b.length);
        count = newcount;
    }
    public synchronized void append(byte b) {
         append(new byte[]{b});
    }
    public synchronized byte toByteArray()[] {
            byte newbuf[] = new byte[count];
            System.arraycopy(buf, 0, newbuf, 0, count);
            return newbuf;
    }

}
