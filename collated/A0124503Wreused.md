# A0124503Wreused
###### src\com\tg\parser\DateTimeHandler.java
``` java
	public static void checkTaskValidity(String input) throws ParseException {
		int inputLength = input.length();
		int inputLengthCheck = input.replaceAll(Constants.DATE_DETECTION, Constants.NULL).length();

		if(inputLength - inputLengthCheck != 0)
			throw new ParseException(input, inputLengthCheck);

		inputLengthCheck = input.replaceAll(Constants.TIME_DETECTION, Constants.NULL).length();

		if(inputLength - inputLengthCheck != 0)
			throw new ParseException(input, inputLengthCheck);

	}
```
###### src\com\tg\parser\Parser.java
``` java
	private static String removeFirstWord(String input) {
		return input.replaceFirst(getFirstWord(input), Constants.NULL).trim();
	}

	private static String getFirstWord(String input) {
		String inputString = input.trim().split(Constants.WORD_SPLIT)[0];

		if (inputString.equals(Constants.UPDATE) || inputString.equals(Constants.SORT)) {
			inputString += Constants.SPACE + input.trim().split(Constants.WORD_SPLIT)[1];
		}
		return inputString;
	}
}
```
