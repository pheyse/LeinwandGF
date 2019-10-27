package de.bright_side.lgf.base;

public class LErrorHandler {
	private LLogger logger;
	private boolean errorsOccured = false;
	
	public LErrorHandler(LLogger logger){
		this.logger = logger;
	}
	
	public void handleError(Throwable e){
		errorsOccured = true;
		if (e != null){
			e.printStackTrace();
		} else{
			System.err.println("Error: null");
		}
		logger.debug("Error:" + e);
	}

	public boolean isErrorsOccured() {
		return errorsOccured;
	}
	
	
	
	
}
