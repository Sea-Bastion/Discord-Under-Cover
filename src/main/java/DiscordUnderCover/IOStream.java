package DiscordUnderCover;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

class IOStream implements ReadableByteChannel {

	private ByteBuffer buf;

	public void write(String i){
		buf = Charset.forName("UTF-8").encode(i);
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		dst = buf;
		return dst.position();
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	public void close() throws IOException {}
}
