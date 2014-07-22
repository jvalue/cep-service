package org.jvalue.ceps.utils;


/**
 * Components that have to restore some start after sever restart should implement this.
 */
public interface Restoreable {

	public void restoreState();

}
