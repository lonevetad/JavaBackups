package srl.parsers;

public enum SRLTokenName {
	EOF,
	/** Accepts only "init". */
	InitRegister, Register,
	/** Accepts both "inc" and "incr". */
	Increment,
	/** Accepts both "dec" and "decr". */
	Decrement, For, OpenParenthesis, ClosedParenthesis, Swap, Comma, Number;
}