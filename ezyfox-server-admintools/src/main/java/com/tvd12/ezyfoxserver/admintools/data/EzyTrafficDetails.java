package com.tvd12.ezyfoxserver.admintools.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyTrafficDetails implements Serializable {
	private static final long serialVersionUID = 3710448434804149207L;

	private int maxUser;
	private int totalUser;

	private int socketMaxSession;
	private int socketTotalSession;
	private long socketTransferredInputData;
	private long socketTransferredOutputData;
	private long socketCurrentInputDataTransferRate;
	private long socketCurrentOutputDataTransferRate;

	private int webSocketMaxSession;
	private int webSocketTotalSession;
	private long webSocketTransferredInputData;
	private long webSocketTransferredOutputData;
	private long webSocketCurrentInputDataTransferRate;
	private long webSocketCurrentOutputDataTransferRate;

	private long maxSession;
	private long totalSession;
	private long transferredInputData;
	private long transferredOutputData;
	
	private long currentInputDataTransferRate;
	private long currentOutputDataTransferRate;

}
