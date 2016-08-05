package com.bg.model.fileclient;

import java.io.Serializable;

public class ResponseFile implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1425307876096494974L;
	
	
	public ResponseFile(){
		
	}

	private String serverName;
	/**
	 * start point
	 */
	private long start;
	/**
	 * file md5
	 */
	private String file_md5;
	/**
	 * download url
	 */
	private String file_url;
	/**
	 * is end?
	 */
	private boolean end;
	/**
	 * progress
	 */
	private int progress ;

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public String getFile_md5() {
		return file_md5;
	}

	public void setFile_md5(String file_md5) {
		this.file_md5 = file_md5;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("serverName:");
		sb.append(serverName);
		sb.append("\t\tprogress:");
		sb.append(progress);
		sb.append("\t\tstart:");
		sb.append(start);
		sb.append("\t\tfile_url:");
		sb.append(file_url);
		sb.append("\t\tfile_md5:");
		sb.append(file_md5);
		return sb.toString();
		
	}
	
}
