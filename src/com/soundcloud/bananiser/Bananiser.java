package com.soundcloud.bananiser;

public class Bananiser {

	private final String[] toRedact;

	public Bananiser(String... toRedact) {
		this.toRedact = toRedact;
	}

	public String process(String rawLine) {
		StringBuffer redactedLine = new StringBuffer("");

		String[] pieces = rawLine.split("&");

		boolean notFirst = false;
		for (String piece : pieces) {
			String keyTobeRedacted = containsAnyOfOurRedactedWords(piece);
			String toAppend = null;
			if (keyTobeRedacted != null) {
				toAppend = piece.replaceAll("=\\S+", "=BANANA");
			} else {
				toAppend = piece;
			}
			String prefix = notFirst ? "&" : "";
			redactedLine.append(prefix + toAppend);
			notFirst = true;
		}
		return redactedLine.toString();
	}

	private String containsAnyOfOurRedactedWords(String piece) {
		String found = null;
		for (String redact : toRedact) {
			if (piece.indexOf(redact) == 0) {
				found = redact;
				break;
			}
		}
		return found;
	}
}