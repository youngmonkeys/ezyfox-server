package com.tvd12.ezyfoxserver.admintools.data;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyThreadsDetail {

	protected int threadsSize;
	protected long totalThreadsCpuTime;
	protected List<EzyThreadDetail> threads;

	public void sort() {
		Collections.sort(threads, new EzyThreadComparator());
	}
	
	public static class EzyThreadComparator implements Comparator<EzyThreadDetail> {
		@Override
		public int compare(EzyThreadDetail left, EzyThreadDetail right) {
			Long timeLeft = left.getCpuTime();
			Long timeRight = right.getCpuTime();
			return timeRight.compareTo(timeLeft);
		}
	}

}
