/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.contact.domain;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public enum ContactAuthState {
	WAITING(0),
	REQUESTED(1),
	REJECTED(2),
	ACCEPTED(3),
	REMOVED(4);

	private int value;

	ContactAuthState(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ""+value;
	}

	public int getValue() {
		return value;
	}
}

