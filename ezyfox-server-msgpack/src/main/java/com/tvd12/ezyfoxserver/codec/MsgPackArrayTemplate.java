package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;

import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public class MsgPackArrayTemplate extends AbstractTemplate<EzyArray> { 
	 
	@Override 
	public void write(Packer pk, EzyArray target, boolean required) 
			throws IOException {
		if(target != null)
			writeNotNull(pk, target);
		else  
			writeNull(pk, required);
		 
	} 
	 
	private void writeNull(Packer pk, boolean required) throws IOException {
        if (!required)
        	pk.writeNil();
        else 
            throw new NullPointerException("Attempted to write null"); 
	} 
	 
	private void writeNotNull(Packer pk, EzyArray target) throws IOException {
		pk.write(target.toList());
	} 
 
	@Override 
	public EzyArray read(Unpacker u, EzyArray to, boolean required) throws IOException {
		 throw new UnsupportedOperationException();
	} 
	 
} 
